/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;


public class ConversionThread extends Thread{
    private static final String CONVERTER_JAR = /* Windows:*/ System.getProperty("user.home")+"\\tiwi\\java_app_pack\\OpenWebslidesConverter.jar";
            //"/home/tiwi/java_app_pack/OpenWebslidesConverter.jar"; 
    
    private static final String CONVERTER_MAIN_CLASS = "openwebslidesconverter.OpenWebslidesConverter";
    
    private final String[] args;
    private final long id;
    private final Queue<Queue<String>> logQueue;
    private Queue<String> queue;  
        
    /**
     * Creates an instance of the class ConversionThread.
     * @param args the command line arguments
     * @param logQueue A queue of queues of strings. The inner queue of strings will be filled with the log output from the logger.
     * @param id A unique identification for the thread. Used in the log to keep the different converters apart.
     */
    public ConversionThread(String[] args, Queue<Queue<String>> logQueue, long id){
        this.args = args;
        this.logQueue = logQueue;
        this.id = id;     
    }
    
    /**
     * The run method of the thread. Contains all the logic of the class. It passes the arguments from the constructor to the converter via reflection.
     * At the end the queue of strings with the logs from the converter will be pushed into logQueue.
     */
    @Override
    public void run(){
        try {
            this.queue = new ConcurrentLinkedDeque<>();
            
            // log start of thread
            logToQueue("thread started");
            
            // Getting the jar URL which contains target class
            File jar = new File(CONVERTER_JAR);
            URL[] classLoaderUrls = new URL[]{jar.toURI().toURL()};

            // Create a new URLClassLoader
            URLClassLoader urlClassLoader = new URLClassLoader(classLoaderUrls);

            // Load the target class
            Class<?> OpenWebslidesConverter = urlClassLoader.loadClass(CONVERTER_MAIN_CLASS);

            // Getting a method from the loaded class and invoke it
            Method method = OpenWebslidesConverter.getMethod("queueEntry", String[].class, Queue.class, long.class);
            
            final Object[] param = new Object[3];
            param[0] = args;
            param[1] = queue;
            param[2] = id;
            
            logToQueue("invoke converter via queueEntry");
            
            method.invoke(null, param);
            
            logToQueue("end of thread");
        } catch (Exception ex) {
            logToQueue("error:" + ex.getMessage());
        } finally{
            logQueue.offer(queue);
        }
    }
    
    /**
     * Private help method to write a message to the log queue. The id of the thread and a timestamp will be added before the message.
     * @param msg 
     */
    private void logToQueue(String msg){
        queue.offer(id + " " + new Timestamp(new Date().getTime()) + " " + msg);
    }
}