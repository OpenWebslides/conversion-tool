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
    
    ArrayList<Textpart> tekstdelen;
        private int level;
    
    public Text(){
        tekstdelen = new ArrayList<>();
    }
    
    public void addTekstdeel(Textpart td){
        tekstdelen.add(td);
    }
    
    public String toString(){
        String toret = "";
        if(tekstdelen.size()>0){
        for(Textpart td : tekstdelen){
            toret += td.toString() + "\t";
        
        }}
        return toret + " lvl:" + level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
