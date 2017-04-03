/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion;

import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import openwebslideslogger.Logger;

/**
 *
 * @author Jonas
 */
public class LogThread extends Thread {

    private final Queue<Queue<String>> logQueue;
    private final Logger logger;

    /**
     * Creates an instance of the class LogThread.
     *
     * @param logQueue A queue of log queues that will be filled in with the
     * messages that should be logged from within this class.
     * @param logger The logger that writes the messages to the file.
     */
    public LogThread(Queue<Queue<String>> logQueue, Logger logger) {
        this.logQueue = logQueue;
        this.logger = logger;
    }

    /**
     * The run method of the threadToLog. Contains all the logic of the class. It
     * runs an infinite loop where is checked if there are any messages in the
     * queue. If a message is added, it will be written to the file with the
     * logger.
     */
    @Override
    public void run() {
        Timer t = new Timer();
        t.scheduleAtFixedRate(new LogOperation(logQueue, logger), 5000,1000);             
    }

}
