/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.pdf.util;


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
            for (int i = 0; i < slide.getPptObjects().size(); i++) {
                try{
                if (slide.getPptObjects().get(i) != null && slide.getPptObjects().get(i) instanceof Textpart) {
                    Text text = new Text();
                    Textpart tp = (Textpart) slide.getPptObjects().get(i);
                    //System.out.println("Tekstpart behandeld: " + tp.getXPosition()+","+tp.getYPosition());
                    text.addTextpart(tp);
                    slide.getPptObjects().set(i, text);
                }}
                catch(Error e){
                    
                }
            }
        }

    }
}
