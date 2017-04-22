/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.powerpoint;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import objects.FontDecoration;
import objects.PPTList;
import objects.PPTObject;
import objects.Text;
import objects.Textpart;

/**
 *
 * @author Karel
 */
class TextHandler {

    public static void handle(List<PPTObject> pptobjects) {
        for (PPTObject po : pptobjects) {
            if (po instanceof Text) {
                handleText((Text) po);
            }

            if (po instanceof PPTList) {
                handleList((PPTList) po);
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
            if (tp1.isErr() && tp2.isErr()) {
                return true;
            } else {
                return false;
            }
        }
        if (tp1.isErr() && tp2.isErr()) {
            if (tp1.isDirty() && tp2.isDirty()) {
                return true;
            } else {
                return false;
            }
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

    private static void handleList(PPTList pptList) {
        List<PPTObject> objs = pptList.getBullets();
        for (int i = 0; i < objs.size() - 1; i++) {
            if (objs.get(i + 1) instanceof PPTList) {
                ((PPTList) objs.get(i + 1)).getBullets().add(0, objs.get(i));
                remove(pptList, objs.get(i));
                handleList((PPTList) objs.get(i));
            }
        }
    }

    private static void remove(PPTList pptList, PPTObject objs) {
        for (PPTObject po : pptList.getBullets()) {
            if (po.equals(objs)) {
                pptList.getBullets().remove(po);
                break;
            } else if (po instanceof PPTList) {
                remove((PPTList) po, objs);
            }
        }
    }

}
