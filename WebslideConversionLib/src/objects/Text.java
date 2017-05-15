/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.util.ArrayList;

/**
 *
 * @author Karel
 * A text is a paragraph
 */
public class Text implements PPTObject {
    
    //Text is built from multiple textparts
    private final ArrayList<Textpart> textparts;
    
    private String level;
    
    /**
     * Create a Text object. An empty array list of its textparts will be created
     */
    public Text(){
        textparts = new ArrayList<>();
    }
    
    /**
     * Method to add a text part 
     * @param td  Textpart
     */
    public void addTextpart(Textpart td){
        textparts.add(td);
    }
    
    @Override
    /**
     * Used for testing purposes. This will print out
     * <ul>
     *      <li>The toString() method of all of the textparts<li>
     *      <li>The level<li>
     * <ul>
     */
    public String toString(){
        String toret = "";
        if(textparts.size()>0){
            for(Textpart td : textparts){
                toret += td.toString();
            }
        }
        if(level != null) return toret + " lvl:" + level;
        return toret;
    }

    @Override
    /**
     * Return a String with the content of the text
     */
    public String getContent(){ 
        String toret = "";
        if(textparts.size()>0){
            for(Textpart td : textparts){
                toret += td.getContent();
            }
        }
        return toret;
    }
    /**
     * A Text object has a level if it is part of a list.
     * If the object is not part of a list, the level will be null
     * @return String
     */
    public String getLevel() {
        return level;
    }

    /**
     * A Text object has a level if it is part of a list.
     * If the object is not part of a list, the level will be null
     * @param level  String
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * An Text object contains multiple textparts. 
     * Each textpart object has unique features containing:
     * <ul>
            <li>Content</li>
            <li>Type</li>
            <li>Font</li>
            <li>Size</li>
            <li>...</li>
        </ul>
     * @return  ArrayList textparts
     */
    public ArrayList<Textpart> getTextparts() {
        return textparts;
    }  
   
}
