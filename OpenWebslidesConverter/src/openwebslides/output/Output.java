/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslides.output;

/**
 * Used as output channel.
 * @author Jonas
 */
public interface Output {
    /**
     * Print the string s to the channel.
     * @param s 
     */
    public void print(String s);
    
    /**
     * Print the string s to the channel, followed by a newline.
     * @param s 
     */
    public void println(String s);
    
    /**
     * Print an error the the channel.
     * @param message The message to be printed.
     * @param trace The stacktrace of the error.
     */
    public void error(String message, String trace);
}
