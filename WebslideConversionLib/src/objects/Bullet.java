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

    private final String content;
    
    /**
     * Create a Bullet object
     * @param content 
     */
    public Bullet(String content){
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
     * For testing
     */
    public String toString(){
        return content;
    }
    
}
