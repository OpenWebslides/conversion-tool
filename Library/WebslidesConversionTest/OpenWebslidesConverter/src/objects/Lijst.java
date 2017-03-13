/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Karel
 */
public class Lijst implements PPTObject{

    private final List<PPTObject> bullets;

    public List<PPTObject> getBullets() {
        return bullets;
    }
    
    /**
     * Create a Lijst object
     */
    public Lijst(){
        bullets = new ArrayList<>();
    }
    /**
     * Return the html code from this element
     * @param indentation
     * @return 
     */    
    @Override
    public String toHtml(int indentation) {
        if(!bullets.isEmpty()){
            String temp = "";            
            for(int i = 0; i < indentation ; i++)
                temp += "\t";
            temp+="<ul>\n";
            for(PPTObject obj : bullets){
                 temp += obj.toHtml(indentation+1);
            }
            for(int i = 0; i < indentation ; i++)
                temp += "\t";
            return temp + "</ul>\n";
        }
        return "";
    }
    
    /**
     * Return th PPTObjects
     * @param obj 
     */
    public void addPPTObject(PPTObject obj){
        bullets.add(obj);
    }
}
