/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.pdf.util;

import java.util.ArrayList;
import java.util.List;
import objects.PPT;
import objects.Slide;
import objects.Text;
import objects.Textpart;

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
            List<Integer> toRemove = new ArrayList<>();
            int i = 0;
            while (i < slide.getPptObjects().size()) {
                if (slide.getPptObjects().get(i) != null && slide.getPptObjects().get(i) instanceof Textpart) {
                    Text text = new Text();
                    Textpart tp = new Textpart((Textpart) slide.getPptObjects().get(i));
                    tp.setContent(tp.getContent());
                    text.addTextpart(tp);
                    double posY = tp.getYPosition();
                    //System.out.println("Tekstpart behandeld: " + tp.getXPosition()+","+tp.getYPosition());
                    int ii = i;
                    while (ii + 1 < slide.getPptObjects().size() && slide.getPptObjects().get(ii + 1) instanceof Textpart && posY == ((Textpart) slide.getPptObjects().get(ii + 1)).getYPosition()) {
                        tp = new Textpart((Textpart) slide.getPptObjects().get(ii + 1));
                        tp.setContent(tp.getContent());
                        text.addTextpart(tp);
                        toRemove.add(ii + 1);
                        ii++;
                    }
                    slide.getPptObjects().set(i, text);
                    i = ii + 1;
                } else {
                    i++;
                }
            }
            Object[] keys = toRemove.toArray();
            for (int j = keys.length - 1; j >= 0; j--) {
                slide.getPptObjects().remove((int) keys[j]);
            }
        }

    }
}
