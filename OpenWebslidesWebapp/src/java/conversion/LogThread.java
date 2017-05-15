/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion;

import java.util.Queue;
import java.util.Timer;
import openwebslideslogger.Logger;

/**
 *
 * @author Jonas / dhoogla
 */
public class LogThread extends Thread {

    private final Queue<Queue<String>> logQueue;
    private final Logger logger;
    private final Timer timer;

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
        this.timer = new Timer();
    }

    /**
     * This method will do a LogOperation on the logQueue with the provided logger.
     * It will start doing this after 5s and then repeat every second
     */
    @Override
    public void run() {        
        timer.scheduleAtFixedRate(new LogOperation(logQueue, logger), 5000,1000);             
    }

}
