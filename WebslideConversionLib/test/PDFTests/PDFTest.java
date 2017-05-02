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
import java.util.logging.Level;
import java.util.zip.ZipOutputStream;
import logger.Logger;
import objects.PPT;
import org.junit.Test;
import output.StdLogOutput;

/**
 *
 * @author Gertjan
 */
public class PDFTest {
    /*
     public static void main(String[] args) {
     // TODO code application logic here
     File file = new File("C:\\temp\\slides.pdf");
     IConverter converter = ConverterFactory.getConverter(file);
     PPT ppt = new PPT();
        
     converter.parse(ppt,"C:\\temp\\output");
        
        
     FileWriter fw = null;
     //uitschrijfstuk nog niet van toepassing...
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
     }*/

    @Test
    public void main1() {
        File file = new File("C:\\temp\\slidesJonas.pdf");
        IConverter converter;
        try {
            converter = ConverterFactory.getConverter(file);
            converter.setOutput(new StdLogOutput(new Logger("c:\\temp\\log", "logging", "Gertjan")));
            PPT ppt = new PPT();

            converter.parse(ppt, "C:\\temp\\output");
            PDFConverter pdfc = (PDFConverter) converter; //even casten, deze methode is enkel voor testing...

        } catch (PDFException ex) {
            java.util.logging.Logger.getLogger(PDFTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(PDFTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        FileWriter fw = null;
        //uitschrijfstuk nog niet van toepassing...
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
        File file = new File("C:\\temp\\slidesJoann.pdf");
        IConverter converter;
        try {
            converter = ConverterFactory.getConverter(file);
            converter.setOutput(new StdLogOutput(new Logger("c:\\temp\\log", "logging", "Gertjan")));
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream("C:\\temp\\output.zip"));
            PPT ppt = new PPT();
            converter.parse(ppt, zos, "images");
            zos.close();
        } catch (IllegalArgumentException ex) {
            java.util.logging.Logger.getLogger(PDFTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PDFException ex) {
            java.util.logging.Logger.getLogger(PDFTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(PDFTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

}
