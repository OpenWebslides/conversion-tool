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
    private String rid;
    
    public Hyperlink(Textpart textpart) {
        super.setFont(textpart.getFont());
        super.setType(textpart.getType());
        super.setSize(textpart.getSize());
        super.setContent(textpart.getContent());
        super.setCharachterSpacing(textpart.getCharachterSpacing());
        super.setColor(textpart.getColor());
        parts = new ArrayList<>();
    }

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
        return super.getContent();
    } 
    
    @Override
    public String toString(){
        String toret = this.getClass().toString()+ ": ";
        for(Textpart par : parts){
            toret += par.toString();
;
        }
        return toret + "("+ url +")";
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    
    
}
