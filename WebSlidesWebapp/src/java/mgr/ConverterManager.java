/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgr;

import conversion.ConversionCallable;
import conversion.LogThread;
import datastructures.Pair;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import websocket.InboundMsgDefinition;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import openwebslideslogger.Logger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import websocket.ConversionCompleteCallback;

/**
 * The pure java part of the back-end
 * Receives InboundMsgDefinitions from the ServerEndpoint and signal the ServerEndpoint when the thread doing the conversion has finished.
 * This class never directly addresses clients nor receives direct messages from them. 
 * @author dhoogla
 */
public class ConverterManager implements CallableCallback {

    private final HashMap<String, ArrayList<InboundMsgDefinition>> sessionFiles;    
    private final Logger logger;
    private final Logger threadLogger;
    private long lastid;
    private InboundMsgDefinition lastMessage;
    private final Queue<Queue<String>> conversionLogQueue;
    private final ExecutorService executor = Executors.newFixedThreadPool(3);
    private final LogThread logthread;
    private final HashMap<Long,Pair<String,String>> threadSessionFile;
    private final ConversionCompleteCallback serverEndpoint;
    private final HashMap<Long,Future<Integer>> threadFinishedStatus;

    /**
     * The ConverterManager is at the heart of the business logic
     * It keeps track of the sessionIDs and maps the files that need conversion to them
     * It is responsible for concurrent logging capabilities, both of the inner conversion threads and the outer management of those threads by this class
     * Will notify the ServerEndpoint upon completion of a conversion (in a separate thread) to enable a message-based 2-way communication between the client and the server.
     * @param ccc an object to call back to when the conversion is completed
     */
    private ConverterManager(ConversionCompleteCallback ccc) {
        this.sessionFiles = new HashMap<>();
        this.logger = new Logger(System.getProperty("user.home") + File.separator+"tiwi"+File.separator+"java_app_logs"+File.separator, "threadcreation_log", "log of the conversionthread lifecycle");
        this.threadLogger = new Logger(System.getProperty("user.home") + File.separator+"tiwi"+File.separator+"java_app_logs"+File.separator, "conversionprogress_log", "log of the progress of the individual loggers");
        this.lastid = 0;
        this.conversionLogQueue = new ConcurrentLinkedDeque<>();
        this.logthread = new LogThread(conversionLogQueue, threadLogger);
        this.threadSessionFile = new HashMap<>();
        this.serverEndpoint =ccc;
        this.threadFinishedStatus = new HashMap<>();
    }

    //open voor Singleton pattern later
    public static ConverterManager getConverterManager(ConversionCompleteCallback ccc) {
        return new ConverterManager(ccc);
    }

    /**
     * A method to start the logging thread
     */
    public void startLogThread() {
        logthread.start();
        logger.println("The LogThread has been activated, it is used to log the internal messages generated during conversion by the converter");
    }

    public void stopLogThread() {
        try {
            logthread.join(1500);
            logger.println("The LogThread has been deactivated");
        } catch (InterruptedException ex) {
            logger.println("The LogThread didn't shut down correctly"+ex.getMessage());
        }
    }

    /**
     * This is a dual purpose method, first it takes care of the bookkeeping and then calls convertFile which launches the conversion
     * @param key A Websocket session token to identify the owner of the original file and addressee for the converted file
     * @param value An InboundMsgDefinition which contains the necessary info for administration & consistency
     */
    public void addEntry(String key, InboundMsgDefinition value) {
        lastMessage = value;
        if (sessionFiles.containsKey(key)) {
            sessionFiles.get(key).add(value);
        } else {
            sessionFiles.put(key, new ArrayList<>());
            sessionFiles.get(key).add(value);
        }

        System.out.println("***I added to sessionFiles***");
        ArrayList<InboundMsgDefinition> p = sessionFiles.get(key);
        System.out.println("Websocket session key:" + key);
        for (InboundMsgDefinition t : p) {
            System.out.println(t);
        }
        convertFile(System.getProperty("user.home") +File.separator+"tiwi"+File.separator+"upload" +File.separator+ value.getFileName(),key,value.getOutputType());
    }
    
    public void removeEntry(String key){
        sessionFiles.remove(key);
    }

    public HashMap<String, ArrayList<InboundMsgDefinition>> getSessionFiles() {
        return sessionFiles;
    }

    public void printSessionFiles() {
        for (String k : sessionFiles.keySet()) {
            System.out.println("Websocket session key:" + k);

            for (InboundMsgDefinition t : sessionFiles.get(k)) {
                System.out.println(t);
            }
        }
    }
    
    /**
     * This method interprets the file parameter to know which file to convert.
     * It subsequently sets a location for the output after conversion
     * The bookkeeping is logged by logger, the conversion threads receive the concurrent data structure to store their messages
     * The actual conversion is done in a java Callable, starting them done with an ExecutorService.
     * Finally an internal data structure is used to save which converter is handling which file
     * @param file the absolute path of the file that requires conversion
     * @param sessionKey the Websocket session token associated with the user who submitted the file
     * @param outputType the desired format for the output, either raw html5 or shower: bundled output with css and the Shower javascript presentation engine
     */   
    
    public void convertFile(String file,String sessionKey,String outputType) {
        System.out.println("file to convert (should be FULL PATH) " + file);
        String targetDir = System.getProperty("user.home") +File.separator+"tiwi"+File.separator+"download"+File.separator+sessionKey+File.separator+lastMessage.getFileName();        
        System.out.println("targetDir = " + targetDir);
        File directory = new File(String.valueOf(targetDir));
        if(! directory.exists())directory.mkdirs();
        // arguments to be passed to the converter
        String[] args = new String[]{"-i", file, "-o", targetDir, "-t", outputType};
        ++lastid;
        logger.println(Logger.log(lastMessage.getFileName()));
        logger.println(new Timestamp(new Date().getTime()) + "*** " + lastid);
        System.out.println("Starting conversion thread with id: " + lastid);
        
        ConversionCallable t = new ConversionCallable(args, conversionLogQueue, lastid, this);        
        threadFinishedStatus.put(lastid, executor.submit(t));        
        threadSessionFile.put(lastid,new Pair<>(sessionKey,file.substring(file.lastIndexOf(File.separator)+1,file.length())));  
    }

    /**
     * This callback is used to signal the ServerEndpoint which file from which session was converted.
     * That info is used by the ServerEndpoint to construct appropriate Websocket messages for the client.
     * @param id The id of the Callable that was used to convert the file
     * @param status The status parameter is used to convey if the file was converted successfully or not
     */
    @Override
    public void callableComplete(long id, int status) {
        try {
            System.out.println("*-*-*-*The conversion Callable with id " + id + " has finished !!!");            
                System.out.println("Signaling ServerEndpoint");
                Pair<String,String> tmp = threadSessionFile.get(id);
            if(status == 0){
                serverEndpoint.conversionComplete(tmp.getLeft(),tmp.getRight(), "SUCCESS");
            }
            else {
                serverEndpoint.conversionComplete(tmp.getLeft(), tmp.getRight(), "FAIL");
            }
        }
        finally {
            System.out.println("Removing entry from threadSessionFile");
            threadSessionFile.remove(id);
            System.out.println("Removing entry from threadFinishedStatus");
            threadFinishedStatus.remove(id);
        }
        
    }

}
