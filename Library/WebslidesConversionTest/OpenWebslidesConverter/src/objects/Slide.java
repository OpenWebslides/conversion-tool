/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.util.ArrayList;
import java.util.List;


public class Slide implements PPTObject {

    private List<PPTObject> pptObjects;
    private int slideNr;

    public int getSlideNr() {
        return slideNr;
    }

    public void setSlideNr(int slideNr) {
        this.slideNr = slideNr;
    }

    public Slide() {
        pptObjects = new ArrayList<>();
    }

    @Override
    public String toHtml(int indentation) {
        if(!testOutput()){
            String temp = "";            
            for(int i = 0; i < indentation ; i++)
                temp += "\t";
            temp += "<div class=\"slide\" id=\"slide" + slideNr + "\">\n";
            for(PPTObject obj : pptObjects){
                 temp += obj.toHtml(indentation+1);
            }
            for(int i = 0; i < indentation ; i++)
                temp += "\t";
            return adjustHeight(temp + "</div>\n");
            }
        return "";
    }
    
    public void addPPTObject(PPTObject obj){
        pptObjects.add(obj);
    }

    public List<PPTObject> getPptObjects() {
        return pptObjects;
    }

    private boolean testOutput() { 
        String temp = "";
        for(PPTObject obj : pptObjects){
                 temp += obj.toHtml(0);
        }
        return temp.equals("");
    }

    private String adjustHeight(String string) {
        /*int count = string.length() - string.replace("\n", "").length();
        if(count > 50){            
            //System.out.println(count);
            int height = 750 / count * 2;
            return string.replace("style=\"\"", "style=\"height: " + height + "px; padding-bottom: " + Math.floor(height*1.1) + "px; margin-bottom: " + Math.floor(height*1.1) + "px\"");
        }*/
        return string;
    }

}
