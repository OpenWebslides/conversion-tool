/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.powerpoint.util;

import java.util.List;
import objects.PPTList;
import objects.PPTObject;
import objects.Text;
import objects.Textpart;

/**
 *
 * @author Karel
 */
public class TextHandler {

    /**
     * Handle the text of the powerpoint, create spaces etc
     * @param pptobjects 
     */
    public static void handle(List<PPTObject> pptobjects) {
        for (PPTObject po : pptobjects) {
            if (po instanceof Text) {
                handleText((Text) po);
            }

            if (po instanceof PPTList) {
              //  handleList((PPTList) po);
                for (PPTObject pp : ((PPTList) po).getAllObjects()) {
                    if (pp instanceof Text) {
                        handleText((Text) pp);
                    }
                }
            }
        }
    }

    private static boolean tpEquals(Textpart tp1, Textpart tp2) {
        if (tp1.isDirty() && tp2.isDirty()) {
            return tp1.isErr() && tp2.isErr();
        }
        if (tp1.isErr() && tp2.isErr()) {
            return tp1.isDirty() && tp2.isDirty();
        }
        return false;
    }

    private static void handleText(Text text) {
        Text t = text;
        if (t.getTextparts().size() == 1) {
            t.getTextparts().get(0).setContent(t.getTextparts().get(0).getContent().trim() + " ");
        }
        else{
            //Add the correct spaces to the textparts
            //Punctuations will always have a space after and no space before
            String punctutations = ".,:;?!><*-/\\";
            for (int i = 0; i < t.getTextparts().size() - 1; i++) {
                if (punctutations.contains(t.getTextparts().get(i).getContent())) {
                    if (i > 0) {
                        t.getTextparts().get(i - 1).setContent(t.getTextparts().get(i - 1).getContent().trim());
                    }
                    t.getTextparts().get(i).setContent(t.getTextparts().get(i).getContent().trim() + " ");
                } else if (tpEquals(t.getTextparts().get(i), t.getTextparts().get(i + 1))) {
                    t.getTextparts().get(i).setContent(t.getTextparts().get(i).getContent().trim());
                } else {
                    t.getTextparts().get(i).setContent(t.getTextparts().get(i).getContent().trim() + " ");
                }
            }
            
            if (t.getTextparts().size() - 1 >= 0) {
                if (punctutations.contains(t.getTextparts().get(t.getTextparts().size() - 1).getContent())) {
                    t.getTextparts().get(t.getTextparts().size() - 1).setContent(t.getTextparts().get(t.getTextparts().size() - 1).getContent().trim());
                    t.getTextparts().get(t.getTextparts().size() - 2).setContent(t.getTextparts().get(t.getTextparts().size() - 2).getContent().trim());

                }
            }
        }/*
        for (int i = 0; i < t.getTextparts().size() - 1; i++) {
            if (t.getTextparts().get(i).getContent().trim().length() == 0) {
                t.getTextparts().remove(t.getTextparts().get(i));
            }
        }*/

    }
}
