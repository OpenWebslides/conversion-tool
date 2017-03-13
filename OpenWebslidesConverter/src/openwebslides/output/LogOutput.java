/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslides.output;

import java.sql.Timestamp;
import java.util.Date;
import openwebslideslogger.Logger;

/**
 * Output channel to a openwebslides.Logger object.
 * @author Jonas
 */
public class LogOutput implements Output {
    private final Logger logger;
    
    /**
     * Creates an instance of the class LogOutput.
     * @param logger The logger where the messages are printed to.
     */
    public LogOutput(Logger logger){
        this.logger = logger;
    }

    @Override
    public void print(String s) {
        logger.print(getTimestamp() + s);
    }

    @Override
    public void println(String s) {
        logger.println(getTimestamp() + s);
    }

    @Override
    public void error(String message, String trace) {
        logger.println(Logger.error(getTimestamp() + message + "\n" + trace, null));
    }
    
    private String getTimestamp(){
        return new Timestamp(new Date().getTime()).toString() + " ";
    }
}
