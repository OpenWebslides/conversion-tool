/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion;

import java.util.Queue;
import java.util.TimerTask;
import openwebslideslogger.Logger;

/**
 *
 * @author Laurens
 */
public class LogOperation extends TimerTask {

    private final Queue<Queue<String>> logQueue;
    private final Logger logger;

    /**
     * Creates an instance of the class LogOperation.
     *
     * @param logQueue A queue of log queues that will be filled in with the
     * messages that should be logged.
     * @param logger The logger that writes the messages to the file.
     */
    public LogOperation(Queue<Queue<String>> logQueue, Logger logger) {
        this.logQueue = logQueue;
        this.logger = logger;
    }

    /**
     * Logs the head of the logQueue which itself is a queue of messages.
     * Those messages are written to the 
     */
    @Override
    public void run() {
        if (!logQueue.isEmpty()) {
            System.out.println("Logoperation ongoing");
            Queue<String> threadToLog = logQueue.poll();
            if(threadToLog != null)logger.writeQueue(threadToLog);
        }
    }
}
