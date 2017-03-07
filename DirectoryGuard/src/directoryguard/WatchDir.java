import directoryguard.ConversionThread;
import directoryguard.LogThread;
import java.nio.file.*;
import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;
import java.nio.file.attribute.*;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import openwebslideslogger.Logger;


/**
 * Example to watch a directory (or tree) for changes to files.
 */

public class WatchDir {

    private final WatchService watcher;
    private final Map<WatchKey,Path> keys;
    private final boolean recursive;
    private boolean trace = false;
    private final Logger logger;
    private final Logger threadLogger;
    private long lastid;
    
    private final Queue<Queue<String>> conversionLogQueue;

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }

    /**
     * Register the given directory with the WatchService
     */
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (trace) {
            Path prev = keys.get(key);
            if (prev == null) {
                System.out.format("register: %s\n", dir);
                logger.println(Logger.log("Register folder", "Register folder: " + dir));
            } else {
                if (!dir.equals(prev)) {
                    System.out.format("update: %s -> %s\n", prev, dir);
                    logger.println(Logger.log("Update folder", "Update folder: " + dir));
                }
            }
        }
        keys.put(key, dir);
    }

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                throws IOException
            {
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Creates a WatchService and registers the given directory
     */
    WatchDir(Path dir, boolean recursive, Logger logger, Logger threadLogger) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey,Path>();
        this.recursive = recursive;
        this.logger = logger;
        this.threadLogger = threadLogger;
        this.conversionLogQueue = new ConcurrentLinkedDeque<>();
        this.lastid = 0;

        if (recursive) {
            System.out.format("Scanning %s ...\n", dir);
            registerAll(dir);
            System.out.println("Done.");
        } else {
            register(dir);
        }

        // enable trace after initial registration
        this.trace = true;
    }

    /**
     * Process all events for keys queued to the watcher
     */
    void processEvents() {
        logger.println(Logger.log("Init", "start LogThread"));
        new LogThread(conversionLogQueue, threadLogger).start();
        
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.pptx");
         // when looking through different directories use glob:**.pptx
        for (;;) {

            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                logger.println(Logger.log("Error", "WatchKey not recognized!"));
                continue;
            }

            for (WatchEvent<?> event: key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW) {
                    continue;
                }

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);

                // print out event
                System.out.format("%s: %s\n", event.kind().name(), child);   
                logger.println(Logger.log("Event called", event.kind().name() + ": " + child));                    
               
                // the current set of keys
                for(Map.Entry<WatchKey,Path> e:keys.entrySet()){
                    System.out.println("Watchkey: "+e.getKey()+"\tPath:"+e.getValue());
                }

                if(event.kind().name().equals("ENTRY_CREATE")){
                    System.out.println("This file will be converted "+child);
                    logger.println(Logger.log("File to convert", "This file will be converted: "+child));
                    //pass to parser and let parser create new Thread for the parsing process OR start new process for parser 
                    if(matcher.matches(child))
                        convertFile(child.toString());
                }
                
               // testing of extension will happen upon creation, so in case the file will be deleted don't rerun these
               //operations as the child will already be deleted from the directory
               if(kind != java.nio.file.StandardWatchEventKinds.ENTRY_DELETE){
                try{
                    //System.out.println("Does it match? "+matcher.matches(child));
                	/*if(matcher.matches(child)){
                            //do something with modified file
                	}
                	else{*/
                        if(!matcher.matches(child)){
                		try{
                                        System.out.println("I deleted the child: "+child);
                                        logger.println(Logger.log("File deleted", "Deleted the file: "+child));
                                        if(Files.exists(child))
                                            Files.delete(child);
                		}
                		catch(NoSuchFileException x) {
                			System.err.format("%s: no such" + " file or directory%n", child);
                                        logger.println(Logger.error("There is no such file or directory", x));
                		}
                		catch(DirectoryNotEmptyException x) {
                			System.err.format("%s not empty%n", child);
                                        logger.println(Logger.error(child + " is not empty", x));
                		}
                		catch(IOException x){
                			System.err.println(x);
                                        logger.println(Logger.error("Can't read the file", x));
                		}
                	}

            	}
            	catch(InvalidPathException e){
            		System.err.println(e.getMessage());
                        logger.println(Logger.error("Incorrect path", e));
            	}

                // if directory is created, and watching recursively, then
                // register it and its sub-directories
                if (recursive && (kind == ENTRY_CREATE)) {
                    try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }
                    } catch (IOException x) {
                        // ignore to keep sample readbale
                    }
                }
            }
        }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }
    
    private void convertFile(String file){
        String targetDir = "convertedFiles\\"+file.substring(0, file.lastIndexOf(".pptx"));
        
        //arguments to be passed to the converter
        String[] args = new String[] {"-i",file,"-o",targetDir};
        
        //use the timestamp as id for the thread
        long id = new Timestamp(System.currentTimeMillis()).getTime();
        while(id == lastid)
            id = new Timestamp(System.currentTimeMillis()).getTime();
            
        lastid = id;
        
        //log start of conversion thread
        System.out.println("start conversion thread with id " + id);
        logger.println("start conversion thread with id " + id);
        
        //start converter in new thread
        (new ConversionThread(args, conversionLogQueue, id)).start();
        
        System.out.println(new Timestamp(new Date().getTime()) + " " + id + " thread started");
        logger.println(new Timestamp(new Date().getTime()) + " " + id + " thread started");
    }

    static void usage() {
        System.err.println("usage: java WatchDir [-r] dir");
        System.exit(-1);
    }

    public static void main(String[] args) throws IOException {
        // parse arguments
        if (args.length == 0 || args.length > 2)
            usage();
        boolean recursive = false;
        int dirArg = 0;
        if (args[0].equals("-r")) {
            if (args.length < 2)
                usage();
            recursive = true;
            dirArg++;
        }
        
        // register directory and process its events
        Path dir = Paths.get(args[dirArg]);
        new WatchDir(dir, recursive,new Logger("C:\\temp\\","Serverlog","Directory Guard"),new Logger("C:\\temp\\","Converterlog","Converter threads")).processEvents();
    }
}
