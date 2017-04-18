/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslides.writer;

import objects.Slide;

/**
 *
 * @author Jonas
 */
public interface Indentation {
    public String writeSlide(Slide slide, int indentation);
}
