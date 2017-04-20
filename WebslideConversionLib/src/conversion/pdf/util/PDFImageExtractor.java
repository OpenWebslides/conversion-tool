/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.pdf.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.apache.pdfbox.util.PDFStreamEngine;

/**
 *
 * @author Gertjan
 */
public class PDFImageExtractor extends PDFStreamEngine {

    public PDFImageExtractor() throws IOException {
        super(org.apache.pdfbox.util.ResourceLoader.loadProperties(
                "org/apache/pdfbox/resources/PDFTextStripper.properties", true));
    }

    /**
     * This method extracts all images from a pdf file and creates jpg files on the location given by s.
     *
     * @param document
     * @param location
     * @throws IOException
     */
    public void extractImage(PDDocument document, String location) throws IOException {
        List<PDPage> list = document.getDocumentCatalog().getAllPages();

        int totalImages = 1;
        for (PDPage page : list) {
            PDResources pdResources = page.getResources();

            Map pageImages = pdResources.getImages();
            if (pageImages != null) {

                Iterator imageIter = pageImages.keySet().iterator();
                while (imageIter.hasNext()) {
                    String key = (String) imageIter.next();
                    PDXObjectImage pdxObjectImage = (PDXObjectImage) pageImages.get(key);
                    //System.out.println("saving: "+ location + File.separator +  "img" + totalImages + ".jpg");
                    FileOutputStream output = new FileOutputStream(new File(location + File.separator +  "img" + totalImages + ".jpg"));
                    pdxObjectImage.write2OutputStream(output);
                    totalImages++;
                }
            }
        }

    }

    /**
     * Extracts images from document and gives them to the ZOS (creating what represents a jpg as well as making a subfolder images)
     * @param document
     * @param ZOS 
     */
    public void extractImage(PDDocument document, ZipOutputStream ZOS, String saveLocation) throws IOException {
        List<PDPage> list = document.getDocumentCatalog().getAllPages();

        int totalImages = 1;
        
        for (PDPage page : list) {
            PDResources pdResources = page.getResources();

            Map pageImages = pdResources.getImages();
            if (pageImages != null) {
                
                Iterator imageIter = pageImages.keySet().iterator();
                while (imageIter.hasNext()) {
                    String key = (String) imageIter.next();
                    PDXObjectImage pdxObjectImage = (PDXObjectImage) pageImages.get(key);
                    ZOS.putNextEntry(new ZipEntry(saveLocation + File.separator + "img" + totalImages + ".jpg"));
                    pdxObjectImage.write2OutputStream(ZOS);
                    ZOS.closeEntry();
                    totalImages++;
                }
            }
            
        }
        
    }
}
