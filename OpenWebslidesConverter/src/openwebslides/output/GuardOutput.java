/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslides.output;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Queue;

/**
 *
 * @author Jonas
 */
public class GuardOutput implements Output {
    private final Queue<String> queue;
    private final long id;
    
    public GuardOutput(Queue<String> queue, long converterid){
        this.queue = queue;
        this.id = converterid;
    }

    @Override
    public void print(String s) {
        queue.offer(id + getTimestamp() + s);
    }

    @Override
    public void println(String s) {
        queue.offer(id + getTimestamp() + s);
    }

    @Override
    public void error(String message, String trace) {
        String m = "Error:" + message + "\n" + trace;
        queue.offer(id + getTimestamp() + m);
    }
    
    private String getTimestamp(){
        return " " + new Timestamp(new Date().getTime()).toString() + " ";
    }
    
}
