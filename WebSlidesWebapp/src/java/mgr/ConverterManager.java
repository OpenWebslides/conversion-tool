/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgr;

import conversion.ConversionThread;
import directoryguard.LogThread;
import java.util.ArrayList;
import java.util.HashMap;
import websocket.InboundMsgDefinition;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import openwebslideslogger.Logger;

/**
 *
 * @author dhoogla
 */
public class ConverterManager {

    private final HashMap<String, ArrayList<InboundMsgDefinition>> sessionFiles;
    private final Logger logger;
    private final Logger threadLogger;
    private long lastid;
    private InboundMsgDefinition lastMessage;
    private final Queue<Queue<String>> conversionLogQueue;    
    

    public ConverterManager() {
        this.sessionFiles = new HashMap<>();        
        this.logger = new Logger(System.getProperty("user.home")+"\\tiwi\\java_app_logs\\","threadcreation_log","log of the conversionthread lifecycle");
        this.threadLogger = new Logger(System.getProperty("user.home")+"\\tiwi\\java_app_logs\\","conversionprogress_log","log of the progress of the individual loggers");
        this.lastid=0;
        this.conversionLogQueue = new ConcurrentLinkedDeque<>();
    }

    public void addEntry(String key, InboundMsgDefinition value) {
        lastMessage=value;
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
        convertFile(System.getProperty("user.home")+"\\tiwi\\upload\\"+value.getName());
        
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

    public void convertFile(String file){
        System.out.println("file to convert (should be FULL PATH) "+file);
        String targetDir = System.getProperty("user.home")+"\\tiwi\\download\\"; 
//+file.substring(0, file.lastIndexOf(".pptx"))
        System.out.println("targetDir = "+targetDir);
        // arguments to be passed to the converter
        String[] args = new String[] {"-i",file,"-o",targetDir};
        
        ++lastid;
        logger.println(Logger.log(lastMessage.getName(), "start LogThread"));
        new LogThread(conversionLogQueue, threadLogger).start();
        logger.println(new Timestamp(new Date().getTime())+" "+lastid);
        System.out.println("Starting conversion thread with id: "+lastid);
        ConversionThread t = new ConversionThread(args, conversionLogQueue, lastid);     
        t.start();      
        // morgen oplossing met callables & futuretasks
     
               
    }
    
    
}
