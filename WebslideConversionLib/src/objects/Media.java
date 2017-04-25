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
    
    private String filename;
    private final Dimension dimension;
    private final Dimension location;
    private String id;
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
     * @return Dimension dimension
     */
    public Dimension getDimension() {
        return dimension;
    }
    
    /**
     * Return the location of the Image, this will be an editable dimension object
     * Any changes made will be changed in the Image object too
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
    public String getContent(){
        return filename;
    }

    public boolean isCopied() {
        return copied;
    }

    public void setCopied(boolean copied) {
        this.copied = copied;
    }
}
