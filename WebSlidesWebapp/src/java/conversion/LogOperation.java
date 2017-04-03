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

    public LogOperation(Queue<Queue<String>> logQueue, Logger logger) {
        this.logQueue = logQueue;
        this.logger = logger;
    }

    @Override
    public void run() {
        if (!logQueue.isEmpty()) {
            System.out.println("Logoperation ongoing");
            Queue<String> threadToLog = logQueue.poll();
            if(threadToLog != null)logger.writeQueue(threadToLog);
        }
    }
}
