/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.powerpoint.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import objects.Chart;
import objects.Image;
import objects.PPTList;
import objects.PPTObject;
import objects.Text;
import output.Output;

/**
 *
 * @author Karel
 */
public class GarbageHandler {

    /**
     * Remove the Null values, empty elements in lists, empty lists, empty charts, from pptobject
     * @param pptObjects List
     * @param output Output
     */
    public static void handle(List<PPTObject> pptObjects, Output output) {
        removeNullValues(pptObjects);
    }

    /**
     * Remove the Null values, empty elements in lists, empty lists, empty charts,...
     * @param pptObjects 
     */
    private static void removeNullValues(List<PPTObject> pptObjects) {
        pptObjects.removeAll(Collections.singleton(null));
        ArrayList<PPTObject> toRemove = new ArrayList<>();
        for (PPTObject po : pptObjects) {
            int remove = -1;
            if (po.getClass().equals(Chart.class)) {
                if (((Chart) po).getChartType() == null) {
                    remove = 0;
                }
            }
            if (po.getClass().equals(PPTList.class)) {
                remove = 0;
                int j = 0;
                while (remove == 0 && j < ((PPTList) po).getBullets().size()) {
                    if (((PPTList) po).getBullets().get(j) instanceof Image) {
                        remove++;
                    }
                    if (((PPTList) po).getBullets().get(j) instanceof Text) {
                        if (!textEmpty((Text) ((PPTList) po).getBullets().get(j))) {
                            remove++;
                        } else {
                            ((PPTList) po).getBullets().remove(((PPTList) po).getBullets().get(j));
                        }
                    }
                    j++;
                }
            }
            if (po.getClass().equals(Text.class)) {
                remove = 0;
                int i = 0;
                while (remove == 0 && i < ((Text) po).getTextparts().size()) {
                    if (!((Text) po).getTextparts().get(i).getContent().equals("") || ((Text) po).getTextparts().get(i).getContent() != null) {
                        remove++;
                        i++;
                    }
                }
            }
            if (remove == 0) {
                toRemove.add(po);
            }

        }
        pptObjects.removeAll(toRemove);
    }

    private static boolean textEmpty(Text text) {
        int i = 0;
        int remove = 0;
        while (remove == 0 && i < text.getTextparts().size()) {
            if (!text.getTextparts().get(i).getContent().equals("") || text.getTextparts().get(i).getContent() != null) {
                remove++;
                i++;
            }
        }
        return remove == 0;
    }
}
