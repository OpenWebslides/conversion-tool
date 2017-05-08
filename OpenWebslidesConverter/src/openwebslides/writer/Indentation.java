/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslides.writer;

import objects.Slide;

/**
 * A writer that prints the slides with indentation in the output.
 * @author Jonas
 */
public interface Indentation {
    /**
     * Write the content of the slides with indentation.
     * @param slide The Slide that should be written out.
     * @param indentation The amound of tabs used as indentation.
     * @return 
     */
    public String writeSlide(Slide slide, int indentation);
}
