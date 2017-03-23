/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.awt.Dimension;
import javafx.util.Pair;

/**
 *
 * @author Joann
 */
public class Image implements PPTObject{
    
    private String filename;
    private Dimension dimension;
    private Dimension location;
    private String id;
    
    public Image(){
        this.dimension = new Dimension();
        this.location = new Dimension();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String toString(){
        return filename + " " + dimension.getWidth() + "," + dimension.getHeight();
    }

    public Dimension getDimension() {
        return dimension;
    }
    public Dimension getLocation() {
        return location;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
    
}
