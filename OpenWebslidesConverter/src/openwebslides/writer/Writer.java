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
 * A writer prints the content of the PPT object to a BufferedWriter. The format of that output depends on the implementation.
 * @author Jonas
 */
public abstract class Writer {
    /**
     * Prints the content of the PPT object to the BufferedWriter. The default implementation prints every slide one by one by using the writeSlide(Slide) method.
     * @param out The BufferedWriter that writes the output to a stream or file.
     * @param ppt A PPT object that represents the presentation and holds the content.
     * @throws IOException If the content cannot be printed to the BufferedWriter.
     */
    public void write(BufferedWriter out, PPT ppt) throws IOException{
        for(Slide slide : ppt.getSlides()){
            out.newLine();
            out.write(writeSlide(slide));
        }
    }
    
    /**
     * Returns the content of the Slide as implemented.
     * @param slide The Slide that holds the content.
     * @return The content of the Slide as implemented.
     */
    public abstract String writeSlide(Slide slide);
}
