/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import objects.Chart;
import objects.PPTList;
import objects.PPTObject;
import objects.Slide;
import objects.Text;
import objects.Textpart;
import objects.Title;

/**
 *
 * @author Joann
 */
public class HelpWriter {

    /*
        §Text§
        $Title$
        -Textpart-
        #Ordered list#
        °Unordered list°
        ~Bullet~
    
        [CHART]
     */
    
    public static String writeSlide(Slide slide) {
        String res = "";
        for (PPTObject obj : slide.getPptObjects()) {
            res += writePPTObject(obj);
        }
        return res;
    }
    
    private static String writePPTObject(PPTObject obj) {
        String res = "";
        if (obj instanceof Text) {
            res += writeText((Text) obj);
        } else if (obj instanceof PPTList) {
            res += writePPTList((PPTList) obj);
        } else if (obj instanceof Title) {
            res += writeTitle((Title) obj);
        } else if (obj instanceof Chart){
            res += "[CHART]";
        }
        res += "\n";
        return res;
    }

    private static String writePPTList(PPTList list) {
        String res = "";
        String li = "°";
        if (list.isOrdered()) {
            li = "#";
        }
        for (PPTObject obj : list.getBullets()) {
            res += "~";
            if (obj instanceof Text) {
                res += writeText((Text) obj);
            } else if (obj instanceof PPTList) {
                res += writePPTList((PPTList) obj);
            }
            res += "~";
        }
        return li + res + li;
    }

    private static String writeText(Text text) {
        String res = "";
        for (Textpart tp : text.getTextparts()) {
            res += writeTextpart(tp);
        }
        return "§" + res + "§";
    }

    private static String writeTitle(Title title) {
        String res = "";
        for (Textpart tp : title.getTextparts()) {
            res += writeTextpart(tp);
        }
        return "$" + res + "$";
    }

    private static String writeTextpart(Textpart tp) {
        return "-" + tp.getContent() + "-";
    }
}
