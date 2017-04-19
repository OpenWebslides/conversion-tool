/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.pdf.util;

import java.util.ArrayList;
import objects.PPT;
import objects.PPTObject;
import objects.Slide;
import objects.Text;
import objects.Textpart;

/**
 *
 * @author Gertjan
 */
public class TextIntelligence {
    public void makeText(PPT ppt){
        ArrayList<Slide> slides = (ArrayList<Slide>) ppt.getSlides();
        /*
            voorlopig
        */
        for(Slide slide : slides){
            ArrayList<PPTObject> objects = (ArrayList<PPTObject>) slide.getPptObjects();
            for(PPTObject object : objects){
                if(object.getClass().getName().equals("Textpart")){
                    Text text = new Text();
                    text.addTextpart((Textpart) object);
                    objects.remove(object);
                    objects.add(text);
                }
            }
        }
    }
}
