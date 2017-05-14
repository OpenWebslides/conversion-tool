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
    
   //A Video can have a link when it refers to a youtube video.
   private String link;

   /**
    * Get the link behind the video (embedded youtube link)
    * @return String
    */
    public String getLink() {
        return link;
    }

    /**
     * Set the link of the video
     * @param link String
     */
    public void setLink(String link) {
        this.link = link;
    }
   
   
    /**
     * Return the data behind the video: dimension, location, link
     * @return String
     */
   @Override
    public String toString(){
        return getFilename() + "("+ link + ") " + getDimension().getWidth() + "," + getDimension().getHeight() + " " + getLocation().getWidth() + "," + getLocation().getHeight();
    }
}
