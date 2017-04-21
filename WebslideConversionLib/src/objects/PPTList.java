/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Karel
 */
public class PPTList implements PPTObject {

    private final java.util.List<PPTObject> bullets;
    private boolean ordered;

    /**
     * Create a PPTList object An empty ArrayList will be created for the
     * bullets The ordered boolean will be set to false, meaning default
     * implementation is an unordered list
     */
    public PPTList() {
        bullets = new ArrayList<>();
        ordered = false;
    }

    /**
     * Return the bullets of the PPTList This will be an editable list, any
     * changes made will be saved in the PPTList object
     *
     * @return List bullets
     */
    public List<PPTObject> getBullets() {
        return bullets;
    }

    /**
     * Add a PPTObject to the bullet list
     *
     * @param obj PPTObject
     */
    public void addPPTObject(PPTObject obj) {
        bullets.add(obj);
    }

    @Override
    /**
     * Return a string containing:
     * <ul>
     * <li>The toString() method of all of the bullets</li>
     * <li>The class of each bullet</li>
     * <li>If the list is ordered or not</li>
     */
    public String toString() {
        String toret = "\n";
        for (PPTObject po : bullets) {
            toret += "\t" + po.getClass().toString().replace("class objects.", "") + " : " + po.toString() + "\n";
        }
        return toret;
    }
    
    public String getContent(){
        String toret = "\n";
        for (PPTObject po : bullets) {
            toret += "\t" + po.getContent()+ "\n";
        }
        return toret;
    }

    /**
     * Return if the list is ordered or not
     *
     * @return boolean ordered
     */
    public boolean isOrdered() {
        return ordered;
    }

    /**
     * Set the list to ordered or not
     *
     * @param ordered boolean
     */
    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    public List<PPTObject> getAllObjects() {
        List<PPTObject> list = new ArrayList<>();
        for (PPTObject po : bullets) {
            if (po instanceof PPTList) {
                list.addAll(((PPTList) po).getAllObjects());
            } else {
                list.add(po);
            }
        }
        return list;
    }

    public void replace(PPTObject pp, PPTObject obj) {
        if(bullets.contains(obj)){
            bullets.set(bullets.indexOf(pp), obj);
        }
        
    }

}
