/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.powerpoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import objects.Chart;
import objects.PPTObject;
import output.Output;

/**
 *
 * @author Karel
 */
public class GarbageHandler {

    public static void handle(List<PPTObject> pptObjects, Output output) {
        removeNullValues(pptObjects);
    }

    private static void removeNullValues(List<PPTObject> pptObjects) {
        pptObjects.removeAll(Collections.singleton(null));
        ArrayList<PPTObject> toRemove = new ArrayList<>();
        for(PPTObject po : pptObjects){
            if(po.getClass().equals(Chart.class)){
                if(((Chart)po).getChartType()==null){
                    toRemove.add(po);
                }
            }
        }
        pptObjects.removeAll(toRemove);
    }
}
