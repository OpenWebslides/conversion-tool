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
import java.util.logging.Level;
import java.util.logging.Logger;
import objects.PPT;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Karel
 */
public class WebslideConvertorTest {
    
    public WebslideConvertorTest() {
    }
    

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    
    
    @Test
    public void testRun() {
        File file = new File("C:\\temp\\slides.pptx");
        
        IConverter converter = ConverterFactory.getConverter(file);
        
        PPT ppt = new PPT();
        
        converter.parse(ppt);
        
	FileWriter fw = null;

        try {
                fw = new FileWriter("C:\\temp\\test.html");
                fw.write(ppt.toHTML());
        } catch (Exception e) {
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
            }
        }

        
    }
}
