/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.awt.Dimension;

/**
 *
 * @author Joann
 */
public class Image implements PPTObject{
    
    private String filename;
    private final Dimension dimension;
    private final Dimension location;
    private String id;
    
    /**
     * Create an Image object
     * An empty dimension object will be created for the dimension and location of the Image
     */
    public Image(){
        this.dimension = new Dimension();
        this.location = new Dimension();
    }

    /**
     * Return the filename of the file where the image is saved
     * @return 
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Set the filename of the file where the image is saved
     * @param filename 
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
        return filename + " " + dimension.getWidth() + "," + dimension.getHeight() + " " + location.getWidth() + "," + location.getHeight();
    }

    /**
     * Return the dimension (size) of the Image, this will be an editable dimension object
     * Any changes made will be changed in the Image object too
     * @return 
     */
    public Dimension getDimension() {
        return dimension;
    }
    
    /**
     * Return the location of the Image, this will be an editable dimension object
     * Any changes made will be changed in the Image object too
     * @return 
     */
    public Dimension getLocation() {
        return location;
    }

    /**
     * Set the image id
     * @param id 
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Return the image id
     * @return 
     */
    public String getId() {
        return id;
    }
    
}
