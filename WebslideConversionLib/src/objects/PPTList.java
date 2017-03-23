/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Karel
 */
public class PPTList implements PPTObject{

    private final java.util.List<PPTObject> bullets;

    
    /**
     * Create a Lijst object
     */
    public PPTList(){
        bullets = new ArrayList<>();
    }
    
    public List<PPTObject> getBullets() {
        return bullets;
    }
    
    /**
     * Return th PPTObjects
     * @param obj 
     */
    public void addPPTObject(PPTObject obj){
        bullets.add(obj);
    }
    
    public String toString(){
        String toret = "\n";
        for(PPTObject po : bullets){
            toret += "\t" + po.getClass() + " : " +po.toString() + "\n";
        }
        return toret;
    }
}
