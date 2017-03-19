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
    private Dimension afmetingen;
    
    public Image(String filename, Dimension d){
        this.filename = filename;
        this.afmetingen = d;
        System.out.println(d.toString());
    }

    
}
