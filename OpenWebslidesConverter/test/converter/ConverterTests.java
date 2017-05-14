/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.zip.ZipOutputStream;
import openwebslidesconverter.Converter;
import logger.Logger;
import objects.PPT;
import objects.PPTObject;
import objects.Placeholder;
import objects.Slide;
import objects.Textpart;
import objects.Title;
import openwebslides.writer.HTMLWriter;
import openwebslides.writer.Writer;
import openwebslidesconverter.WebslidesConverterException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
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
    
    
    @Test
    public void imagesToZip() throws IOException, WebslidesConverterException{
        Converter converter = new Converter(new LogOutput(new Logger("test\\files\\ziptest\\imagesToZip\\", "testlog", "1")));
        
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream("test\\files\\ziptest\\imagesToZip\\output.zip"));
        
        converter.convert(new File("test\\files\\ziptest\\imagesToZip\\input.pptx"), zos, "images");
        
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
        
        FileOutputStream os = new FileOutputStream("test\\files\\saveToStream1\\index_raw.html");
        converter.saveToStream(os, Converter.outputType.RAW, Converter.outputFormat.HTML);
        FileOutputStream os2 = new FileOutputStream("test\\files\\saveToStream1\\index_shower.html");
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
        
        File dir = new File("test\\files\\saveToDirectory1");
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
        
        File dir = new File("test\\files\\saveToDirectory2");
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
        
        File dir = new File("test\\files\\saveToDirectory3");
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
        
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream("test\\files\\saveToZip1\\output.zip"));
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
        
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream("test\\files\\saveToZip2\\output.zip"));
        converter.saveToZip(zos, Converter.outputType.RAW, Converter.outputFormat.HTML);
        zos.close();
    }
    
    @Test
    public void converterTest1() throws FileNotFoundException, Exception{
        Converter converter = new Converter(new StdOutput());
        File inputFile = new File("test\\files\\converterTest1\\input.pptx");
        converter.convert(inputFile, "test\\files\\converterTest1\\images");
        File outputDir = new File("test\\files\\converterTest1");
        converter.saveToDirectory(outputDir, Converter.outputType.SHOWER, Converter.outputFormat.HTML);
    }
    
    @Test
    public void link() throws WebslidesConverterException{
        Converter converter = new Converter(new StdOutput());
        File inputFile = new File("test\\files\\link\\input.pptx");
        converter.convert(inputFile, "test\\files\\link\\images");
        File outputDir = new File("test\\files\\link");
        converter.saveToDirectory(outputDir, Converter.outputType.SHOWER, Converter.outputFormat.HTML);
        
        Slide slide = converter.getPPT().getSlides().get(0);
        PPTObject obj = slide.getPptObjects().get(0);
    }
    
    @Test
    //1 slide with a placeholder
    public void placeHolder() throws Exception{
        
        Converter converter = new Converter(new StdOutput());
        
        PPT ppt = new PPT();
        Slide front = new Slide();
        Slide slide = new Slide();
        Title title = new Title();
        Textpart tp = new Textpart();
        tp.setContent("titel van de slide");
        title.addTextpart(tp);
        Placeholder ph = new Placeholder();
        ph.setContent("This graph could not be converted.");
        slide.getPptObjects().add(title);
        slide.getPptObjects().add(ph);
        ppt.getSlides().add(front);
        ppt.getSlides().add(slide);
        converter.setPPT(ppt);
        
        File dir = new File("test\\files\\placeholder");
        converter.saveToDirectory(dir, Converter.outputType.SHOWER, Converter.outputFormat.HTML);
        
    }
    
}
