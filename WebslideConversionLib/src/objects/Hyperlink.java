/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author Karel
 */
public class Hyperlink extends Textpart{
    
    private ArrayList<Textpart> parts;
    private String url;


    public ArrayList<Textpart> getParts() {
        return parts;
    }

    public void setParts(ArrayList<Textpart> parts) {
        this.parts = parts;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    @Override
    public String getContent(){
        String toret = "";
        for(Textpart par : parts){
            toret += par.getContent();
        }
        return toret;
    } 

    
    
}
