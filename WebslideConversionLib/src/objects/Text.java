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
 */
public class Text implements PPTObject {
    
    ArrayList<Textpart> textdelen;
    
    private String level;
    
    public Text(){
        textdelen = new ArrayList<>();
    }
    
    public void addTextdeel(Textpart td){
        textdelen.add(td);
    }
    
    public String toString(){
        String toret = "";
        if(textdelen.size()>0){
        for(Textpart td : textdelen){
            toret += td.toString() + "\t";
        
        }}
        return toret + " lvl:" + level;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
