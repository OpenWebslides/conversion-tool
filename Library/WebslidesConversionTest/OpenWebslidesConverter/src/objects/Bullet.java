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
public class Bullet implements PPTObject{

    private String content;
    
    public Bullet(String content){
        this.content = content;
    }
    
    @Override
    public String toHtml(int indentation) {
        
        String temp = "";
        for(int i = 0; i < indentation ; i++)
            temp += "\t";
        temp+="<li style=\"\">\n";
        for(int i = 0; i < indentation+1 ; i++)
            temp += "\t";
        temp+=content+"\n";
        for(int i = 0; i < indentation ; i++)
            temp += "\t";
        temp+="</li>\n";
        return temp;
    }

    public String getContent() {
        return content;
    }
    
}
