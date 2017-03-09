/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

/**
 *
 * @author Joann
 */
public class Afbeelding implements PPTObject{
    
    private String filename;
    
    public Afbeelding(String filename){
        this.filename = filename;
    }
    /**
     * Return the html code from this element
     * @param indentation
     * @return 
     */
    @Override
    public String toHtml(int indentation) {
        if(!filename.equals("")){
            String temp = "";
            for(int i = 0; i < indentation ; i++)
                temp += "\t";
            temp+="<li style=\"\">\n";
            for(int i = 0; i < indentation+1 ; i++)
                temp += "\t";
            temp+="<img src=\"" + filename + "\"/>\n";
            for(int i = 0; i < indentation ; i++)
                temp += "\t";
            temp+="</li>\n";
            return temp;
            }
        return "";
    }
    
}
