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
    
    /**
     * Creates an instance of the class LogThread.
     * @param logQueue A queue of log queues that will be filled in with the messages that should be logged from within this class.
     * @param logger The logger that writes the messages to the file.
     */
    public LogThread(Queue<Queue<String>> logQueue, Logger logger){
        this.logQueue = logQueue;
        this.logger = logger;
    }
    
    /**
     * The run method of the thread. Contains all the logic of the class. It runs an infinite loop where is checked if there are any messages in the queue.
     * If a message is added, it will be written to the file with the logger.
     */
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
