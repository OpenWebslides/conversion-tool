/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.powerpoint;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import objects.FontDecoration;
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
                Text t = ((Text) po);
                if (t.getTextparts().size() == 1) {
                    t.getTextparts().get(0).setContent(t.getTextparts().get(0).getContent().trim() + " ");
                }
                for (int i = 0; i < t.getTextparts().size() - 1; i++) {
                    if (t.getTextparts().get(i + 1).isDirty()) {
                        t.getTextparts().get(i).setContent(t.getTextparts().get(i).getContent().trim());
                    } else {
                        t.getTextparts().get(i).setContent(t.getTextparts().get(i).getContent().trim() + " ");
                    }
                }
            }
        }
    }


}
