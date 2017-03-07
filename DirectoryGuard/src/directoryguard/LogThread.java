/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package directoryguard;

import java.util.Queue;
import openwebslideslogger.Logger;

/**
 *
 * @author Jonas
 */
public class LogThread extends Thread{
    private final Queue<Queue<String>> logQueue;
    private final Logger logger;
    
    public LogThread(Queue<Queue<String>> logQueue, Logger logger){
        this.logQueue = logQueue;
        this.logger = logger;
    }
    
    @Override
    public void run(){
        for(;;){
            // print out the queue with messages from the conversion threads
            if(!logQueue.isEmpty()){
                Queue<String> thread = logQueue.poll();
                while(thread != null){

                    //print out the messages from that thread
                    logger.writeQueue(thread);

                    // poll for the next queue from a thread
                    thread = logQueue.poll();
                }
            }
        }
            
    }
    
}
