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
public class Titel implements PPTObject{

    private String content;
    
    public Titel(String content){
        this.content = content;
    }
    
    @Override
    public String toHtml(int indentation) {
        if(!content.equals("")){
            String temp = "";
            for(int i = 0; i < indentation ; i++)
                temp += "\t";
            temp+="<h2>\n";
            for(int i = 0; i < indentation + 1 ; i++)
                temp += "\t";
            temp+=content+"\n";
            for(int i = 0; i < indentation ; i++)
                temp += "\t";
            temp+="</h2>\n";
            return temp;
        }
        return "";
    }

    public String getContent() {
        return content;
    }
    
}
