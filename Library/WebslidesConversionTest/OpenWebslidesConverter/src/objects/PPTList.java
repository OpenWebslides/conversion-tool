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

    public List<PPTObject> getBullets() {
        return bullets;
    }
    
    /**
     * Create a Lijst object
     */
    public PPTList(){
        bullets = new ArrayList<>();
    }

    
    /**
     * Return th PPTObjects
     * @param obj 
     */
    public void addPPTObject(PPTObject obj){
        bullets.add(obj);
    }

    public void addPPTObject(Image afbeelding, Dimension imageDimensionInPixels) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
