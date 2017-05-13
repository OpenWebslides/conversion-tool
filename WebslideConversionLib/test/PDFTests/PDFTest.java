package PDFTests;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import conversion.ConverterFactory;
import conversion.IConverter;
import conversion.pdf.PDFConverter;
import conversion.pdf.util.PDFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.zip.ZipOutputStream;
import logger.Logger;
import objects.PPT;
import objects.Slide;
import org.junit.Test;
import output.StdLogOutput;

/**
 *
 * @author Gertjan
 */
public class PDFTest {

    @Test
    public void main1() {
        URL path = PDFTest.class.getResource("slides.pdf");
        File file = new File(path.getFile());
        
        //System.out.println("Testing: tabelJoann");
        IConverter converter;
        try {
            converter = ConverterFactory.getConverter(file);
            converter.setOutput(new StdLogOutput(new Logger("c:\\temp\\log", "logging", "Gertjan")));
            PPT ppt = new PPT();

            converter.parse(ppt, "output");
            //SchrijfUit(ppt);
            //PDFConverter pdfc = (PDFConverter) converter; //even casten, deze methode is enkel voor testing...

        } catch (PDFException ex) {
            java.util.logging.Logger.getLogger(PDFTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(PDFTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        FileWriter fw = null;
        //uitschrijfstuk nog niet van toepassing op dit niveau...
        try {
            fw = new FileWriter("C:\\temp\\test.html");
            // fw.write(ppt.toHTML());
        } catch (Exception e) {
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
            }
        }
    }

    @Test
    public void imagesToZip() throws IOException {
        URL path = PDFTest.class.getResource("slides.pdf");
        File file = new File(path.getFile());
        
        //System.out.println("Testing: slides.pdf");
        IConverter converter;
        try {
            converter = ConverterFactory.getConverter(file);
            converter.setOutput(new StdLogOutput(new Logger("c:\\temp\\log", "logging", "Gertjan")));
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream("output.zip"));
            PPT ppt = new PPT();
            converter.parse(ppt, zos, "images");
            //System.out.println("parse completed");
            zos.close();
            // SchrijfUit(ppt);
        } catch (IllegalArgumentException ex) {
            java.util.logging.Logger.getLogger(PDFTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PDFException ex) {
            java.util.logging.Logger.getLogger(PDFTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println("exception found");
            java.util.logging.Logger.getLogger(PDFTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Can be used to see what's inside the ppt structure
     *
     * @param ppt
     */
    public void SchrijfUit(PPT ppt) {
        for (Slide slide : ppt.getSlides()) {
            System.out.println(slide.getContent());
        }
    }

}
