/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package output;

import logger.Logger;


/**
 * A combination of the StdOutput class and LogOutput class. The messages are printed both to the screen and the logger.
 * @author Jonas
 */
public class StdLogOutput implements Output {
    private final Output stdOutput;
    private final Output logOutput;

    /**
     * Creates an instance of the class StdLogOutput.
     * @param logger The logger where the messages are printed to.
     */
    public StdLogOutput(Logger logger){
        this.stdOutput = new StdOutput();
        this.logOutput = new LogOutput(logger);
    }
    
    @Override
    public void print(String s) {
        stdOutput.print(s);
        logOutput.print(s);
    }

    @Override
    public void println(String s) {
        stdOutput.println(s);
        logOutput.println(s);
    }

    @Override
    public void error(String message, String trace) {
        stdOutput.error(message,trace);
        logOutput.error(message,trace);
    }
    
}
