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
public class Title extends Text{

    private final String content;
    
    /**
     * Create a Titel object
     * @param content 
     */
    public Title(String content){
        this.content = content;
    }

    /**
     * Return the content
     * @return 
     */
    public String getContent() {
        return content;
    }
    @Override
    /**
     * Return a string with the content of the title
     */
    public String toString(){
        return content;
    }
}
