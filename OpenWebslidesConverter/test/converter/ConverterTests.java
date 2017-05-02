/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.zip.ZipOutputStream;
import openwebslidesconverter.Converter;
import logger.Logger;
import objects.PPT;
import objects.PPTObject;
import objects.Slide;
import openwebslidesconverter.WebslidesConverterException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import output.*;

/**
 *
 * @author Jonas
 */
public class ConverterTests {
    
    public ConverterTests() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    /*
    @Test
    public void imagesToZip() throws IOException, WebslidesConverterException{
        Converter converter = new Converter(new LogOutput(new Logger("C:\\temp\\ziptest\\imagesToZip\\", "testlog", "1")));
        
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream("C:\\temp\\ziptest\\imagesToZip\\output.zip"));
        
        converter.convert(new File("C:\\temp\\ziptest\\imagesToZip\\input.pptx"), zos, "images");
        
        zos.close();
    }
    
    @Test
    public void saveToStream1() throws FileNotFoundException, WebslidesConverterException{
        Converter converter = new Converter(new StdOutput());
        
        PPT ppt = new PPT();
        Slide[] slides = new Slide[3];
        for(int i=0; i<3; i++){
            slides[i] = new Slide();
            ppt.getSlides().add(slides[i]);
        }
        converter.setPPT(ppt);
        
        FileOutputStream os = new FileOutputStream("C:\\temp\\tests\\saveToStream1\\index_raw.html");
        converter.saveToStream(os, Converter.outputType.RAW, Converter.outputFormat.HTML);
        FileOutputStream os2 = new FileOutputStream("C:\\temp\\tests\\saveToStream1\\index_shower.html");
        converter.saveToStream(os2, Converter.outputType.SHOWER, Converter.outputFormat.HTML);
    }
    
    @Test
    public void saveToDirectory1() throws Exception{
        Converter converter = new Converter(new StdOutput());
        
        PPT ppt = new PPT();
        Slide[] slides = new Slide[3];
        for(int i=0; i<3; i++){
            slides[i] = new Slide();
            ppt.getSlides().add(slides[i]);
        }
        converter.setPPT(ppt);
        
        File dir = new File("C:\\temp\\tests\\saveToDirectory1");
        converter.saveToDirectory(dir, Converter.outputType.SHOWER, Converter.outputFormat.HTML);
    }
    
    @Test
    public void saveToDirectory2() throws Exception{
        Converter converter = new Converter(new StdOutput());
        
        PPT ppt = new PPT();
        Slide[] slides = new Slide[3];
        for(int i=0; i<3; i++){
            slides[i] = new Slide();
            ppt.getSlides().add(slides[i]);
        }
        converter.setPPT(ppt);
        
        File dir = new File("C:\\temp\\tests\\saveToDirectory2");
        converter.saveToDirectory(dir, Converter.outputType.RAW, Converter.outputFormat.HTML);
    }
    
    @Test
    public void saveToDirectory3() throws Exception{
        Converter converter = new Converter(new StdOutput());
        
        PPT ppt = new PPT();
        Slide[] slides = new Slide[3];
        for(int i=0; i<3; i++){
            slides[i] = new Slide();
            ppt.getSlides().add(slides[i]);
        }
        converter.setPPT(ppt);
        
        converter.setCourse("Bachelorproef");
        converter.setChapter("save to directory with custom info");
        
        File dir = new File("C:\\temp\\tests\\saveToDirectory3");
        converter.saveToDirectory(dir, Converter.outputType.SHOWER, Converter.outputFormat.HTML);
    }
    
    @Test
    public void saveToZip1() throws FileNotFoundException, IOException, WebslidesConverterException{
        Converter converter = new Converter(new StdOutput());
        
        PPT ppt = new PPT();
        Slide[] slides = new Slide[3];
        for(int i=0; i<3; i++){
            slides[i] = new Slide();
            ppt.getSlides().add(slides[i]);
        }
        converter.setPPT(ppt);
        
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream("C:\\temp\\tests\\saveToZip1\\output.zip"));
        converter.saveToZip(zos, Converter.outputType.SHOWER, Converter.outputFormat.HTML);
        zos.close();
    }
    
    @Test
    public void saveToZip2() throws FileNotFoundException, IOException, WebslidesConverterException{
        Converter converter = new Converter(new StdOutput());
        
        PPT ppt = new PPT();
        Slide[] slides = new Slide[3];
        for(int i=0; i<3; i++){
            slides[i] = new Slide();
            ppt.getSlides().add(slides[i]);
        }
        converter.setPPT(ppt);
        
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream("C:\\temp\\tests\\saveToZip2\\output.zip"));
        converter.saveToZip(zos, Converter.outputType.RAW, Converter.outputFormat.HTML);
        zos.close();
    }
    
    @Test
    public void converterTest1() throws FileNotFoundException, Exception{
        Converter converter = new Converter(new StdOutput());
        File inputFile = new File("C:\\temp\\tests\\converterTest1\\input.pptx");
        converter.convert(inputFile, "C:\\temp\\tests\\converterTest1\\images");
        File outputDir = new File("C:\\temp\\tests\\converterTest1");
        converter.saveToDirectory(outputDir, Converter.outputType.SHOWER, Converter.outputFormat.HTML);
    }
    */
    @Test
    public void link() throws WebslidesConverterException{
        Converter converter = new Converter(new StdOutput());
        File inputFile = new File("C:\\temp\\tests\\link\\input.pptx");
        converter.convert(inputFile, "C:\\temp\\tests\\link\\images");
        File outputDir = new File("C:\\temp\\tests\\link");
        converter.saveToDirectory(outputDir, Converter.outputType.SHOWER, Converter.outputFormat.HTML);
        
        Slide slide = converter.getPPT().getSlides().get(0);
        PPTObject obj = slide.getPptObjects().get(0);
    }
    
}
