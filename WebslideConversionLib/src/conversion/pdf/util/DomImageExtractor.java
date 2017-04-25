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
import org.apache.pdfbox.contentstream.PDFStreamEngine;

/**
 * This class gets makes image objects from the DOM
 * @author Gertjan
 */
public class DomImageExtractor extends PDFStreamEngine {

    public DomImageExtractor() throws IOException {
       /* super(org.apache.pdfbox.util.ResourceLoader.loadProperties(
                "org/apache/pdfbox/resources/PDFTextStripper.properties", true));*/
    }

    /**
     * This method extracts all images from a pdf file and creates jpg files on the location given by s.
     *
     * @param document
     * @param location
     * @throws IOException
     */
    public void extractImage(PDDocument document, String location) throws IOException {
        
    }

    /**
     * Extracts images from document and gives them to the ZOS (creating what represents a jpg as well as making a subfolder images)
     * @param document
     * @param ZOS 
     */
    public void extractImage(PDDocument document, ZipOutputStream ZOS, String saveLocation) throws IOException {
        
    }
}
