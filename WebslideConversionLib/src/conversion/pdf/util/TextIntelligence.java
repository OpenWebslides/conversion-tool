/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.pdf.util;

import java.util.ArrayList;
import java.util.List;
import objects.PPT;
import objects.PPTObject;
import objects.Slide;
import objects.Text;
import objects.Textpart;
import objects.Title;

/**
 *
 * @author Gertjan
 */
public class TextIntelligence {

    public void makeText(PPT ppt) {
        /*
            voorlopig
         */

        for (Slide slide : ppt.getSlides()) {
            for (int i = 0; i < slide.getPptObjects().size(); i++) {
                try{
                if (slide.getPptObjects().get(i) != null && slide.getPptObjects().get(i) instanceof Textpart) {
                    Text text = new Text();
                    Textpart tp = (Textpart) slide.getPptObjects().get(i);
                    text.addTextpart(tp);
                    slide.getPptObjects().set(i, text);
                }}
                catch(Error e){
                    
                }
            }
        }

    }
}
