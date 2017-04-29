/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.pdf.util;

import java.util.zip.ZipOutputStream;
import objects.PPT;

/**
 *this class can be used to resolve image issues, synchronizing the model with the extracted imaged.
 * @author Gertjan
 */
public class ImageIntelligence {
    /**
     * Checks if the imagelocation from the images in the ppt object can be found in the ZOS
     * if not, the images are replaces by placeholders
     * this is correct on Page level. (the exact order of placeholder and image is lost).
     * @param ppt 
     * @param zos 
     */
        public void checkImages(PPT ppt, ZipOutputStream zos){
        
        }
    /**
     * Checks if the imagelocation from the images in the ppt object can be found on the given location
     * if not, the images are replaces by placeholders
     * this is correct on Page level. (the exact order of placeholder and image is lost).
     * @param ppt
     * @param location 
     */    
        public void checkImages(PPT ppt, String location){
            
        }
        
        
       
}
