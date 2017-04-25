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
    public String writeSlide(Slide slide, int indentation);
}
