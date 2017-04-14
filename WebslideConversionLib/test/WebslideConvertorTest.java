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
public class WebslideConvertorTest {

    public WebslideConvertorTest() {
    }

    @Test
    public void testRun() {
        File file = new File("C:\\temp\\temp.pptx");

        IConverter converter = ConverterFactory.getConverter(file);
        converter.setOutput(new StdLogOutput(new Logger("c:\\temp\\log", "logging", "karel")));

        PPT ppt = new PPT();

        converter.parse(ppt, "C:\\temp\\images");

        FileWriter fw = null;

        try {
            fw = new FileWriter("C:\\temp\\test.html");
        } catch (Exception e) {
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
            }
        }

    }
}
