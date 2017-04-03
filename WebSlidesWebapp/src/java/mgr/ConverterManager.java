/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgr;

import conversion.ConversionCallable;
import conversion.LogThread;
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
import java.util.logging.Level;

/**
 *
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

    private ConverterManager() {
        this.sessionFiles = new HashMap<>();
        this.logger = new Logger(System.getProperty("user.home") + "\\tiwi\\java_app_logs\\", "threadcreation_log", "log of the conversionthread lifecycle");
        this.threadLogger = new Logger(System.getProperty("user.home") + "\\tiwi\\java_app_logs\\", "conversionprogress_log", "log of the progress of the individual loggers");
        this.lastid = 0;
        this.conversionLogQueue = new ConcurrentLinkedDeque<>();
        this.logthread = new LogThread(conversionLogQueue, threadLogger);

    }

    //open voor Singleton pattern later
    public static ConverterManager getConverterManager() {
        return new ConverterManager();
    }

    public void startLogThread() {
        logthread.start();
    }

    public void stopLogThread() {
        try {
            logthread.join(1500);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(ConverterManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
        convertFile(System.getProperty("user.home") + "\\tiwi\\upload\\" + value.getName());

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

    public void convertFile(String file) {
        System.out.println("file to convert (should be FULL PATH) " + file);
        String targetDir = System.getProperty("user.home") + "\\tiwi\\download\\";
//+file.substring(0, file.lastIndexOf(".pptx"))
        System.out.println("targetDir = " + targetDir);
        // arguments to be passed to the converter
        String[] args = new String[]{"-i", file, "-o", targetDir};

        ++lastid;
        logger.println(Logger.log(lastMessage.getName()));
        logger.println(new Timestamp(new Date().getTime()) + " " + lastid);
        System.out.println("Starting conversion thread with id: " + lastid);
        ConversionCallable t = new ConversionCallable(args, conversionLogQueue, lastid, this);
        executor.submit(t);
    }

    @Override
    public void callableComplete(long id) {
        System.out.println("*-*-*-*The conversion Callable with id " + id + " has finished its work!!!");
    }

}
