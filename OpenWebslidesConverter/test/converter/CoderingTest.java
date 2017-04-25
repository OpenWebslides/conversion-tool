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
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipOutputStream;
import logger.Logger;
import objects.*;
import openwebslidesconverter.Converter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import output.LogOutput;
import output.StdOutput;

/**
 *
 * @author Jonas
 */
public class CoderingTest {
    
    public CoderingTest() {
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
    public void main() throws Exception{
        Converter converter = new Converter(new StdOutput());
        converter.convert(new File("C:\\temp\\tests\\Codering\\input.pptx"), "images");
        
        Slide slide = converter.getPPT().getSlides().get(0);
        PPTObject obj = slide.getPptObjects().get(0);
        
        PPTList list = null;
        if(obj instanceof PPTList)
            list = (PPTList)obj;
        
        obj = list.getBullets().get(0);
        Text text = null;
        if(obj instanceof Text){
            text = (Text)obj;
            System.out.println(text.getTextparts().size());
            String res = "";
            
            for(Textpart part : text.getTextparts()){
                res += part.getContent();
                
            }
            
            System.out.println(res + " vóór");
            print(res + " vóór");
        }
    }
    
    
    public void print(String s) throws FileNotFoundException, UnsupportedEncodingException, IOException{
        String text = "é";
        
        FileOutputStream os = new FileOutputStream("c:\\temp\\tests\\Codering\\utf.html");
        //BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF8"));
        
        bufferedWriter.write(s);
        
        bufferedWriter.flush();
    }
}
