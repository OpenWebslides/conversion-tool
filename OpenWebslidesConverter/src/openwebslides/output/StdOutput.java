/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslides.output;

/**
 * Output channel that writes to the screen.
 * @author Jonas
 */
public class StdOutput implements Output {

    @Override
    public void print(String s) {
        System.out.print(s);
    }

    @Override
    public void println(String s) {
        System.out.println(s);
    }

    @Override
    public void error(String message, String trace) {
        System.err.println(message);
    }
    
}
