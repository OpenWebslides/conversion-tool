/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

/**
 *
 * @author Karel
 * This object will be used when an object can not be converted/is not implemented yet from the dataset to HTML/XML/...
 */
public class Placeholder implements PPTObject{
    
    private String content;

    public Placeholder() {
    }

    @Override
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    
}
