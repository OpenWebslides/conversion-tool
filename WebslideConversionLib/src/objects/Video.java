/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;


/**
 *
 * @author Karel
 */
public class Video extends Media{
    
   private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
   
   
    public String toString(){
        return getFilename() + "("+ link + ") " + getDimension().getWidth() + "," + getDimension().getHeight() + " " + getLocation().getWidth() + "," + getLocation().getHeight();
    }
}
