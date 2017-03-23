/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.powerpoint;

import java.util.List;
import objects.Image;
import objects.PPTObject;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import logger.Logger;
import output.Output;

/**
 *
 * @author Karel
 */
class MediaHandler {
    
    /**
     * Handle media, extract images, videos, chartdata from ppt
     * @param slide
     * @param pptObjects
     * @param saveLocation
     * @param file
     * @param output 
     */
    public static void handle(XSLFSlide slide, List<PPTObject> pptObjects, String saveLocation, File file, Output output){
        copyImages(slide, pptObjects, saveLocation, file, output);
    }

    private static void copyImages(XSLFSlide slide, List<PPTObject> pptObjects, String saveLocation, File file, Output output) {
        try{
            for(XSLFShape sh : slide.getShapes()){
                if(sh.getClass().equals(XSLFPictureShape.class)){
                    for(PPTObject po : pptObjects){
                        if(po.getClass().equals(Image.class)){
                            if(((Image)po).getId().equals(""+sh.getShapeId())){
                                ((Image)po).setFilename(saveLocation + "\\" + ((XSLFPictureShape) sh).getPictureData().getFileName());
                                copyImage(((Image)po),((XSLFPictureShape) sh).getPictureData().getFileName(), file,output);
                            }
                        }
                    }
                }
            }
        }
        catch(Exception e){
            output.println(Logger.error("Error while extracting images from powerpoint zip", e));
        }
    }
   private static void copyImage(Image img, String name, File file, Output output) {
        try{
            File f = new File(img.getFilename());
            f.getParentFile().mkdirs();
            OutputStream out = new FileOutputStream(f);
            FileInputStream fin = new FileInputStream(file.getAbsoluteFile());
            BufferedInputStream bin = new BufferedInputStream(fin);
            ZipInputStream zin = new ZipInputStream(bin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.getName().equals("ppt/media/"+name)) {
                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = zin.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    out.close();
                    break;
                }
            }
        }catch(Exception e){
            output.println(Logger.error("Error while extracting images from powerpoint zip, image " + img.toString(), e));
        }
    }
}
