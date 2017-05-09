/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.awt.Dimension;

/**
 *
 * @author Karel
 */
public abstract class Media implements PPTObject{
    
    //Where is the Media saved?
    private String filename;
    
    //The size of the media, this will be saved in percentages 
    //eg. an Image thats the upper half of the slide will be 100,50 (full width, upper halve height)
    private final Dimension dimension;
    
    //The location of the media, this will also be saved in percentages (location of the upper left corner)
    //eg an Image thats in the lower right corner of the slide will have location 100,100
    private final Dimension location;
    
    //The id of the slide
    private String id;
    
    //Whether there will be a file behind filename or not
    //If the converter failed to copy the media from the ppt this will be false
    private boolean copied;

    
    /**
     * Create an Image object
     * An empty dimension object will be created for the dimension and location of the Image
     */
    public Media(){
        this.dimension = new Dimension();
        this.location = new Dimension();
        copied = false;
    }

    /**
     * Return the filename of the file where the image is saved
     * @return String filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Set the filename of the file where the image is saved
     * @param filename  String
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    /**
     * Return a string containing
     * <ul>
     *      <li>Filename</li>
     *      <li>Dimension width and height</li>
     *      <li>Location X and Y</li>
     * </ul>
     */
    public String toString(){
        return filename + "("+ id + ") " + dimension.getWidth() + "," + dimension.getHeight() + " " + location.getWidth() + "," + location.getHeight();
    }

    /**
     * Return the dimension (size) of the Image, this will be an editable dimension object
     * Any changes made will be changed in the Image object too
     * The size of the media will be saved in percentages 
     * eg. an Image thats the upper half of the slide will be 100,50 (full width, upper halve height)
     * @return Dimension dimension
     */
    public Dimension getDimension() {
        return dimension;
    }
    
    /**
     * Return the location of the Image, this will be an editable dimension object
     * Any changes made will be changed in the Image object too
     * The location of the media, this will also be saved in percentages (location of the upper left corner)
     * eg an Image thats in the lower right corner of the slide will have location 100,100
     * @return Dimension location
     */
    public Dimension getLocation() {
        return location;
    }

    /**
     * Set the image id
     * @param id String 
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Return the image id
     * @return String id
     */
    public String getId() {
        return id;
    }
    
    @Override
    /**
     * Returns the filename of the Media
     */
    public String getContent(){
        return filename;
    }

    /**
     * Returns whether  there will be a file behind filename or not
     * If the converter failed to copy the media from the ppt this will be false
     * @return boolean
     */
    public boolean isCopied() {
        return copied;
    }

    /**
     * Set the result of the copy action. If the media has succesfully been copied out of the ppt this should be true
     * @param copied boolean
     */
    public void setCopied(boolean copied) {
        this.copied = copied;
    }
}
