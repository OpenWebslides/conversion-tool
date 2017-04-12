/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import openwebslidesconverter.OpenWebslidesConverter;
import openwebslidesconverter.WebslidesConverterException;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jonas
 */
public class OpenWebslidesConverterTests {
    
    public OpenWebslidesConverterTests() {
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
    public void test1() {
        String[] args = new String[] {"-i","C:\\temp\\tests\\OpenWebslidesConverterTests\\test1\\input.pptx",
                                      "-o","C:\\temp\\tests\\OpenWebslidesConverterTests\\test1"};
        OpenWebslidesConverter.main(args);
    }
    
    @Test
    public void test2() {
        String[] args = new String[] {"-i","C:\\temp\\tests\\OpenWebslidesConverterTests\\test2\\input.pptx",
                                      "-o","C:\\temp\\tests\\OpenWebslidesConverterTests\\test2",
                                      "-zip"};
        OpenWebslidesConverter.main(args);
    }
    
    @Test
    public void test3() {
        String[] args = new String[] {"-i","C:\\temp\\tests\\OpenWebslidesConverterTests\\test3\\input.pptx",
                                      "-o","C:\\temp\\tests\\OpenWebslidesConverterTests\\test3",
                                      "-t","shower"};
        OpenWebslidesConverter.main(args);
    }
    
    @Test
    public void test4() {
        String[] args = new String[] {"-i","C:\\temp\\tests\\OpenWebslidesConverterTests\\test4\\input.pptx",
                                      "-o","C:\\temp\\tests\\OpenWebslidesConverterTests\\test4",
                                      "-t","shower",
                                      "-zip"};
        OpenWebslidesConverter.main(args);
    }
    
    @Test
    public void test5() throws IOException, WebslidesConverterException {
        String zipLocation = "C:\\temp\\tests\\OpenWebslidesConverterTests\\test5";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        
        String[] args = new String[] {"-i","C:\\temp\\tests\\OpenWebslidesConverterTests\\test5\\input.pptx"};
        Queue<String> queue = new ConcurrentLinkedDeque<>();
        long id = 1;
        
        OpenWebslidesConverter.queueEntry(args, os, queue, id);
        os.close();
        
        DAOSimulation(new ByteArrayInputStream(os.toByteArray()), zipLocation);
    }
    
    @Test
    public void test6() throws IOException, WebslidesConverterException {
        String zipLocation = "C:\\temp\\tests\\OpenWebslidesConverterTests\\test6";
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        
        String[] args = new String[] {"-i","C:\\temp\\tests\\OpenWebslidesConverterTests\\test6\\input.pptx",
                                      "-t","shower"};
        Queue<String> queue = new ConcurrentLinkedDeque<>();
        long id = 1;
        
        OpenWebslidesConverter.queueEntry(args, os, queue, id);
        os.close();
        
        DAOSimulation(new ByteArrayInputStream(os.toByteArray()), zipLocation);
    }
    
    private void DAOSimulation(InputStream is, String zipLocation) throws FileNotFoundException, IOException {
        try (OutputStream os = new FileOutputStream(zipLocation+File.separator+"webslides.zip")) {
            IOUtils.copy(is, os);
        }
    }
    
    
    
    @Test
    public void allElementsTest(){
        String[] args = new String[] {"-i","C:\\temp\\tests\\OpenWebslidesConverterTests\\allElementsTest\\input.pptx",
                                      "-o","C:\\temp\\tests\\OpenWebslidesConverterTests\\allElementsTest",
                                      "-t","shower"};
        OpenWebslidesConverter.main(args);
    }
    
    @Test
    public void imagesSizes() throws WebslidesConverterException{
        String dir = "C:\\temp\\tests\\OpenWebslidesConverterTests\\imagesSizes";
        String input = "C:\\temp\\tests\\OpenWebslidesConverterTests\\imagesSizes\\input.pptx";
        
        String[] args = new String[] {"-i",input,
                                      "-o",dir,
                                      "-t","shower"};
        OpenWebslidesConverter.main(args);
    }
    
}
