/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedDeque;
import mgr.CallableCallback;

public class ConversionCallable implements Callable<Integer> {

    private static final String CONVERTER_JAR = /* Windows & Linux*/ System.getProperty("user.home") + File.separator + "tiwi" + File.separator + "java_app_pack" + File.separator + "OpenWebslidesConverter.jar";

    private static final String CONVERTER_MAIN_CLASS = "openwebslidesconverter.OpenWebslidesConverter";

    private final String[] args;
    private final long id;
    private final Queue<Queue<String>> logQueue;
    private Queue<String> queue;
    private boolean normalfinish = false;
    private final CallableCallback callback;

    /**
     * Creates an instance of the class ConversionThread.
     *
     * @param args the command line arguments
     * @param logQueue A queue of queues of strings. The inner queue of strings
     * will be filled with the log output from the logger.
     * @param id A unique identification for the thread. Used in the log to keep
     * the different converters apart.
     * @param cb an object to call back to when the conversion is finished
     */
    public ConversionCallable(String[] args, Queue<Queue<String>> logQueue, long id, CallableCallback cb) {
        this.args = args;
        this.logQueue = logQueue;
        this.id = id;
        this.callback = cb;
    }

    /**
     * Private help method to write a message to the log queue. The id of the
     * thread and a timestamp will be added before the message.
     *
     * @param msg the message to log
     */
    private void logToQueue(String msg) {
        queue.offer(id + " " + new Timestamp(new Date().getTime()) + " " + msg);
    }

    /**
     * The call method of the callable. Contains all the logic of the class. It
     * passes the arguments from the constructor to the converter via
     * reflection. At the end the queue of strings with the logs from the
     * converter will be pushed into logQueue.
     */
    @Override
    public Integer call() {
        try {
            this.queue = new ConcurrentLinkedDeque<>();

            // log start of thread
            logToQueue("[WEB] ConversionCallable with id "+id+" called");

            // Getting the jar URL which contains target class
            File jar = new File(CONVERTER_JAR);
            URL[] classLoaderUrls = new URL[]{jar.toURI().toURL()};

            // Create a new URLClassLoader
            URLClassLoader urlClassLoader = new URLClassLoader(classLoaderUrls);

            // Load the target class
            Class<?> OpenWebslidesConverter = urlClassLoader.loadClass(CONVERTER_MAIN_CLASS);

            // Getting a method from the loaded class and invoke it
            Method method = OpenWebslidesConverter.getMethod("queueEntry", String[].class, OutputStream.class, Queue.class, long.class);
                       
            try{
            String s = args[3].substring(args[3].lastIndexOf(File.separator)+1,args[3].length());
            s = s.substring(0, s.lastIndexOf('.'));            
            OutputStream fos = new FileOutputStream(new File(args[3])+File.separator+s+".zip");           
            
            
            final Object[] param = new Object[4];
            param[0] = args;
            param[1] = fos;          
            param[2] = queue;
            param[3] = id;
            
            logToQueue("[WEB] invoke converter via OpenWebslidesConverter.queueEntry");
            
            method.invoke(null, param);
            fos.close();
            }
            catch(FileNotFoundException exe){ 
                System.err.println("[WEB] I COULD NOT OPEN the FileOutputStream for converter zip-output");
            }
            
            logToQueue("[WEB] end of ConversionCallble with id "+id);
            normalfinish = true;

        } catch (MalformedURLException | ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | IllegalStateException ex) {
            System.err.println("[WEB] CALLABLE_INSIDE error!" + ex.getClass());
            System.err.println("[WEB] CALLABLE_INSIDE stacktrace: ");
            logToQueue("[WEB] CALLABLE_INSIDE error:" + ex.getMessage());
            logToQueue("[WEB] CALLABLE_INSIDE stacktrace: "+ex.getStackTrace());
            ex.printStackTrace();
           
            normalfinish = false;
        } catch (Exception ex) {
            System.err.println("[WEB] CALLABLE_INSIDE error! " + ex.getClass());
            logToQueue("[WEB] CALLABLE_INSIDE error:" + ex.getMessage());
            normalfinish = false;
        } finally {
            logQueue.offer(queue);            
            callback.callableComplete(this.id, normalfinish ? 0 : -1);
        }
        if (normalfinish) {
            return 0;
        } else {
            return -1;
        }
    }
}
