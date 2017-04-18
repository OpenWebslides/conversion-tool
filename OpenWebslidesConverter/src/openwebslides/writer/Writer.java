/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslides.writer;

import java.io.BufferedWriter;
import java.io.IOException;
import objects.*;

/**
 *
 * @author Jonas
 */
public abstract class Writer {
    public void write(BufferedWriter out, PPT ppt) throws IOException{
        for(Slide slide : ppt.getSlides()){
            out.newLine();
            out.write(writeSlide(slide));
        }
    }
    
    public abstract String writeSlide(Slide slide);
}
