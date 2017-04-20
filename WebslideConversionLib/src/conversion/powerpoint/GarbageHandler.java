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
import objects.Image;
import objects.PPTList;
import objects.PPTObject;
import objects.Text;
import objects.Textpart;
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
            int remove = -1;
            if(po.getClass().equals(Chart.class)){
                if(((Chart)po).getChartType()==null){
                    toRemove.add(po);
                }
            }
            if(po.getClass().equals(PPTList.class)){
                remove = 0;
                int j = 0;
                while(remove==0 && j < ((PPTList)po).getBullets().size()){
                    if(((PPTList)po).getBullets().get(j) instanceof Image) {
                        remove++;
                    }
                    if(((PPTList)po).getBullets().get(j) instanceof Text){
                        int i = 0;
                        while(remove==0&& i < ((Text) ((PPTList)po).getBullets().get(j)).getTextparts().size()){
                            if(!((Text) ((PPTList)po).getBullets().get(j)).getTextparts().get(i).getContent().equals("")||((Text) ((PPTList)po).getBullets().get(j)).getTextparts().get(i).getContent()!=null){
                                remove++;
                                i++;
                            }
                        }
                    }
                    j++;
                }
            }
            if(remove==0){
                toRemove.add(po);
            }
        }
        pptObjects.removeAll(toRemove);
    }
}
