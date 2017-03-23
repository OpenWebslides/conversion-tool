/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.util.HashSet;


/**
 *
 * @author Karel
 */
public class Textpart implements PPTObject{
    
    private String font;
    private HashSet<FontDecoration> type;
    private int size;
    private String content;
    private int charachterSpacing;
    private String color;

    public Textpart(){
        type = new HashSet<>();
    }    
        
    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public void addType(FontDecoration f){
        type.add(f);
    }
    
    @Override
    public String toString(){
        if(!content.equals("")){
        String toret = "["+content+"]" + " (";
        for(FontDecoration fd : type){
            toret += fd.name() + " ";
        }
        if(size!=0) toret += " sz " + size;
        if(charachterSpacing!=0) toret += " spc " + charachterSpacing;
        if(color!=null&&!color.equals("")) toret += " color " + color;
        return toret + ")";}return "";
    }

    public HashSet<FontDecoration> getType() {
        return type;
    }

    public void setType(HashSet<FontDecoration> type) {
        this.type = type;
    }

    public int getCharachterSpacing() {
        return charachterSpacing;
    }

    public void setCharachterSpacing(int charachterSpacing) {
        this.charachterSpacing = charachterSpacing;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}


