/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.util.ArrayList;
import java.util.List;


public class Slide implements PPTObject {

    private final List<PPTObject> pptObjects;

    public Slide() {
        pptObjects = new ArrayList<>();
    }

    public void addPPTObject(PPTObject obj){
        pptObjects.add(obj);
    }

    public List<PPTObject> getPptObjects() {
        return pptObjects;
    }

    public void addPPTObjects(ArrayList<Text> content) {
        pptObjects.addAll(content);
    }

}
