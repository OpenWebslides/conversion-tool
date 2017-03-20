/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import objects.*;
import openwebslides.writer.*;
import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jonas
 */
public class HTMLWriterTests {
    
    public HTMLWriterTests() {
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
    public void writePPTTest0() throws IOException{
        String result;
        try(StringWriter sw = new StringWriter(); BufferedWriter out = new BufferedWriter(sw)){
            PPT ppt = new PPT();
            Slide[] slides = new Slide[3];
            
            for(int i=0; i<3; i++){
                slides[i] = new Slide();
                ppt.getSlides().add(slides[i]);
            }
            
            Writer writer = new HTMLWriter();
            writer.write(out, ppt);
            
            out.flush();
            result = sw.toString();
        }
        String expected_result = "\r\n"
                + "<div class=\"title slide\" id=\"slide0\">\n"
                + "</div>\r\n"
                + "<div class=\"slide\" id=\"slide1\">\n"
                + "</div>\r\n"
                + "<div class=\"slide\" id=\"slide2\">\n"
                + "</div>";
        
        //System.out.println("**" + org.apache.commons.lang3.StringEscapeUtils.escapeJava(result) + "**");
        
        Assert.assertEquals(result, expected_result);
    }
    
    @Test
    public void writePPTTest1() throws IOException{
        String result;
        try(StringWriter sw = new StringWriter(); BufferedWriter out = new BufferedWriter(sw)){
            PPT ppt = new PPT();
            Slide[] slides = new Slide[1];
            slides[0] = new Slide();
            slides[0].addPPTObject(new Titel("Lorem ipsum dolor"));
            
            for(Slide s : slides){
                ppt.getSlides().add(s);
            }
            
            Writer writer = new HTMLWriter();
            writer.write(out, ppt);
            
            out.flush();
            result = sw.toString();
        }
        //System.out.println(result);
    }
}
