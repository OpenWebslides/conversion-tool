/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.pdf.util;

import java.util.ArrayList;
import objects.PPTObject;
import objects.Textpart;
import objects.Title;

/**
 *
 * @author Gertjan
 */
public class Info2Object {

    //OPM dit moet textdeel worden!!! --> merge ergens nodig met karel... 
    public PPTObject convert(ArrayList<TekenPlus> t) {
        
        StringBuilder sb = new StringBuilder();
        for (TekenPlus tp : t) {
            sb.append(tp.getTeken());
        }

        String woord = sb.toString();
        //System.out.println("woord: " + woord);
        Textpart obj = new Textpart();
        if(!t.isEmpty()){   //extra controle want gaf blijkbaar wel nog een fout...
        TekenPlus eersteLetter = t.get(0);
        
        obj.setContent(woord);
        obj.setSize((int) eersteLetter.getFontsize());
        obj.setCharachterSpacing((int) eersteLetter.getSpace());
        obj.setSize((int) eersteLetter.getHeigt());
        }
        
        return obj;
    }
}
