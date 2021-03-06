/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.pdf.util;

import java.io.File;
import java.util.ArrayList;
import objects.Image;
import objects.PPT;
import objects.Placeholder;
import objects.Slide;
import output.Output;

/**
 * this class can be used to resolve image issues, synchronizing the model with
 * the extracted imaged.
 *
 * @author Gertjan
 */
public class ImageIntelligence {

    private Output output;
    /**
     * Checks if the imagelocation from the images in the ppt object can be
     * found in the array of arraylists. if not, the images are replaces by placeholders this is
     * correct on Page level. (the exact order of placeholder and image is
     * lost).
     *
     * @param ppt PPT
     * @param afbeeldingen ArrayList
     *
     */
    public void checkImages(PPT ppt, ArrayList<String> afbeeldingen) {
        //System.out.println("checking from ZIP...");
        for (Slide slide : ppt.getSlides()) {
            int i = 0;
            while (i < slide.getPptObjects().size()) {
                if (slide.getPptObjects().get(i) != null && slide.getPptObjects().get(i) instanceof Image) {
                    Image im = (Image) slide.getPptObjects().get(i);

                   
                    if (!afbeeldingen.contains(im.getFilename())) {
                        output.println("Unable to find image: " +File.separator+ im.getFilename() + " - placeholder inserted");
                        Placeholder p = new Placeholder();
                        p.setContent("Image");
                        slide.getPptObjects().set(i, p);
                    }
                }
                i++;
            }

        }
        
    }
    public void setOutput(Output output) {
        this.output = output;
    }

    /**
     * Checks if the imagelocation from the images in the ppt object can be
     * found on the given location if not, the images are replaces by
     * placeholders this is correct on Page level. (the exact order of
     * placeholder and image is lost).
     *
     * @param ppt PPT
     * @param location String
     */
    public void checkImages(PPT ppt, String location) {
        //System.out.println("checking from location...");
        for (Slide slide : ppt.getSlides()) {
            int i = 0;
            while (i < slide.getPptObjects().size()) {
                if (slide.getPptObjects().get(i) != null && slide.getPptObjects().get(i) instanceof Image) {
                    Image im = (Image) slide.getPptObjects().get(i);

                    File f = new File(location +File.separator +  im.getFilename());
                    if (!f.exists() && !f.isDirectory()) {
                        output.println("Unable to find image: " + location +File.separator+ im.getFilename() + " - placeholder inserted");
                        Placeholder p = new Placeholder();
                        p.setContent("Image");
                        slide.getPptObjects().set(i, p);
                    }
                }
                i++;
            }

        }
    }
}
