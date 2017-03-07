/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslides.output;

/**
 *
 * @author Jonas
 */
public interface Output {
    public void print(String s);
    
    public void println(String s);
    
    public void error(String message, String trace);
}
