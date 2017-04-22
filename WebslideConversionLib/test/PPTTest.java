
import conversion.ConverterFactory;
import conversion.IConverter;
import conversion.pdf.util.PDFException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import logger.Logger;
import objects.PPT;
import static org.junit.Assert.*;
import org.junit.Test;
import output.StdLogOutput;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Karel
 */
public class PPTTest {

    @Test
    public void main() {
        File file = new File("C:\\temp\\testPpts\\13.pptx");

        IConverter converter;
        try {
            converter = ConverterFactory.getConverter(file);
        
        converter.setOutput(new StdLogOutput(new Logger("c:\\temp\\log", "logging", "karel")));

        PPT ppt = new PPT();

        converter.parse(ppt, "C:\\temp\\images");
        } catch (PDFException ex) {
            java.util.logging.Logger.getLogger(PPTTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   // @Test
    public void testALotOfPPT() {
        File file = new File("C:\\temp\\testPpts");
        for (File f : file.listFiles()) {
            if(f.isFile()){
                System.out.println("READING " + f.getName());
                try {
                    IConverter converter = ConverterFactory.getConverter(f);
                    converter.setOutput(new StdLogOutput(new Logger("c:\\temp\\log", "logging", "karel")));
                    PPT ppt = new PPT();
                    String folder = f.getName().substring(0,f.getName().lastIndexOf("."));
                    converter.parse(ppt, "C:\\temp\\testPpts\\" + folder + "\\images");
                    if (f.getName().equals("1.pptx")||f.getName().equals("sample.ppt")) {
                        //1.pptx is a corupt pptx and should not be read
                        assertFalse(true);
                    }
                } catch (Exception e) {
                    if (!f.getName().equals("1.pptx") && !f.getName().equals("sample.ppt")) {
                        //1.pptx is a corrupt pptx ad shouold not be read
                        assertFalse(true);
                    }
                }
            }
        }
        assertTrue(true);        
    }
}
