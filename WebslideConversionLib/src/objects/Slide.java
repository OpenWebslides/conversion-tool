/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.util.ArrayList;
import java.util.List;


public class Slide implements PPTObject {

    //A Slide can contains multiple pptObjects
    private final List<PPTObject> pptObjects;

    /**
     * Create a new Slide object
     */
    public Slide() {
        pptObjects = new ArrayList<>();
    }
    /**
     * Return the list with powerpoint objects this slide contains
     * @return List pptobjects
     */
    public List<PPTObject> getPptObjects() {
        return pptObjects;
    }
    
    @Override
    /**
     * Return a string with the data of this slide, for testing purposes
     */
    public String toString(){
        String toret = "";
        toret = pptObjects.stream().filter((po) -> (po!=null)).map((po) -> po.getClass().toString().replace("class objects.", "") + " : " + po.toString() + "\n").reduce(toret, String::concat);
        return toret;
    }

    @Override
    /**
     * Return a String with the content of all of its elements
     */
    public String getContent() {
        String toret = "";
        for(PPTObject po : pptObjects){
            toret += po.getContent();
        }
        return toret;
    }

    /**
     * Return all of the objects in this slide, without containing lists
     * eg. if a Slide contains multiple lists 3 list items this method will return all of the items in his lists, but not the lists themselves
     * @return List
     */
    public List<PPTObject> getAllPptObjects() {
        List<PPTObject> list = new ArrayList<>();
        for (PPTObject po : pptObjects) {
            if (po instanceof PPTList) {
                list.addAll(((PPTList) po).getAllObjects());
            } else {
                list.add(po);
            }
        }
        return list;
    }
}
