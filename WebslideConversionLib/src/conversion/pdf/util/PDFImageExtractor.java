/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.pdf.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
 * Extracts fysical images from PDFStream
 * @author Gertjan
 */
public class PDFImageExtractor extends PDFStreamEngine {
    private int pagenr = 0;
    public PDFImageExtractor() throws IOException {
        super(org.apache.pdfbox.util.ResourceLoader.loadProperties(
                "org/apache/pdfbox/resources/PDFTextStripper.properties", true));
    }

    /**
     * Extract per page to a given location
     * @param page
     * @param location
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void extractImage(PDPage page, String location) throws FileNotFoundException, IOException{
        int totalImagesOnPage = 0;
            PDResources pdResources = page.getResources();

            Map pageImages = pdResources.getImages();
      
            if (pageImages != null) {

                Iterator imageIter = pageImages.keySet().iterator();
                while (imageIter.hasNext()) {
                    String key = (String) imageIter.next();
                    PDXObjectImage pdxObjectImage = (PDXObjectImage) pageImages.get(key);
                    //System.out.println("saving: "+ location + File.separator +  "img" + pagenr + "-" + totalImagesOnPage + ".jpg");
                    FileOutputStream output = new FileOutputStream(new File(location + File.separator +  "img" + pagenr + "-" + totalImagesOnPage + ".jpg"));
                    totalImagesOnPage++;
                    pdxObjectImage.write2OutputStream(output);
                    
                }
            }
            
    }
    
    
    /**
     * This method extracts all images from a pdf file and creates jpg files on the location given by s.
     *
     * @param document PDDocument
     * @param location String
     * @throws IOException exception
     */
    public void extractImage(PDDocument document, String location) throws IOException {
        List<PDPage> list = document.getDocumentCatalog().getAllPages();
        //List<PDPage> list = (List<PDPage>) document.getDocumentCatalog().getPages();
        int totalImages = 1;
        
        for (PDPage page : list) {
            int totalImagesOnPage = 0;
            PDResources pdResources = page.getResources();

            Map pageImages = pdResources.getImages();
      
            if (pageImages != null) {

                Iterator imageIter = pageImages.keySet().iterator();
                while (imageIter.hasNext()) {
                    String key = (String) imageIter.next();
                    PDXObjectImage pdxObjectImage = (PDXObjectImage) pageImages.get(key);
                   // System.out.println("saving: "+ location + File.separator +  "img" + pagenr + "-" + totalImagesOnPage + ".jpg");
                    FileOutputStream output = new FileOutputStream(new File(location + File.separator +  "img" + pagenr + "-" + totalImagesOnPage + ".jpg"));
                    totalImagesOnPage++;
                    pdxObjectImage.write2OutputStream(output);
                    totalImages++;
                }
            }
            pagenr++;
        }

    }

    /**
     * Extracts images from document and gives them to the ZOS (creating what represents a jpg as well as making a subfolder images)
     * the returned arraylist serves as a controle mechanism to get syncronisation between images and imagelocations
     * the image locations are extracted with text extraction...
     * @param document Document
     * @param ZOS ZipOutputStream
     * @param saveLocation String
     * @return  ArrayList arraylist
     * @throws java.io.IOException exception
     */
    public ArrayList<String> extractImage(PDDocument document, ZipOutputStream ZOS, String saveLocation) throws IOException {
        
        int totalImages = 1;
        List<PDPage> list = document.getDocumentCatalog().getAllPages();

        ArrayList<String> afbeeldingen = new ArrayList();
        
        for (PDPage page : list) {
            int totalImagesOnPage = 0;
            PDResources pdResources = page.getResources();
            ArrayList<String> afbPerPage = new ArrayList();
            Map pageImages = pdResources.getImages();
            if (pageImages != null) {
                
                Iterator imageIter = pageImages.keySet().iterator();
                
                while (imageIter.hasNext()) {
                    
                    String key = (String) imageIter.next();
                    PDXObjectImage pdxObjectImage = (PDXObjectImage) pageImages.get(key);
                    ZOS.putNextEntry(new ZipEntry(saveLocation + File.separator +  "img" + pagenr + "-" + totalImagesOnPage + ".jpg"));
                    pdxObjectImage.write2OutputStream(ZOS);
                    ZOS.closeEntry();
                    afbPerPage.add(saveLocation + File.separator +  "img" + pagenr + "-" + totalImagesOnPage + ".jpg");
                    afbeeldingen.add("img" + pagenr + "-" + totalImagesOnPage + ".jpg");
                    //System.out.println("controle array: " + "img" + pagenr + "-" + totalImagesOnPage + ".jpg");
                    totalImages++;
                    totalImagesOnPage++;
                }
                
            }
            pagenr++;
        }
        return afbeeldingen;
    }
}
