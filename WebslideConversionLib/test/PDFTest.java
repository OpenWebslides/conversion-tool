/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import conversion.ConverterFactory;
import conversion.IConverter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import logger.Logger;
import objects.PPT;
import org.junit.Test;
import output.StdLogOutput;

/**
 *
 * @author Karel
 */
public class PDFTest {
    
    /* public static void main(String[] args) {
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
        
    }
}
