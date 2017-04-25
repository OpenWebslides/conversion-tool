/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import objects.FontDecoration;
import objects.Image;
import objects.PPT;
import objects.Slide;
import objects.Textpart;
import objects.Title;
import openwebslides.writer.HTMLWriter;
import openwebslides.writer.Writer;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import output.StdOutput;

/**
 *
 * @author Jonas
 */
public class ImagesTests {
    
    public ImagesTests() {
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

    @Test
    public void gg() throws IOException{
        PPT ppt = createPPT(1.2, 1.2);
        String result = result(ppt);
        
        //System.out.println("**" + org.apache.commons.lang3.StringEscapeUtils.escapeJava(result) + "**");
        //System.out.println(result);
        
        String expected = "\r\n\t<div class=\"title slide\" id=\"slide0\">\n\t\t<figure class=\"cover width\">\n\t\t\t<img src=\"images/image1.jpg\">\n\t\t</figure>\n\t</div>";
        Assert.assertEquals(result, expected);
    }
    
    @Test
    public void gm() throws IOException{
        PPT ppt = createPPT(1.2, 1.0);
        String result = result(ppt);
        
        //System.out.println("**" + org.apache.commons.lang3.StringEscapeUtils.escapeJava(result) + "**");
        //System.out.println(result);
        
        String expected = "\r\n\t<div class=\"title slide\" id=\"slide0\">\n\t\t<figure class=\"cover height\">\n\t\t\t<img src=\"images/image1.jpg\">\n\t\t</figure>\n\t</div>";
        Assert.assertEquals(result, expected);
    }
    
    @Test
    public void gk() throws IOException{
        PPT ppt = createPPT(1.2, 0.8);
        String result = result(ppt);
        
        //System.out.println("*****" + org.apache.commons.lang3.StringEscapeUtils.escapeJava(result) + "**");
        //System.out.println(result);
        
        String expected = "\r\n\t<div class=\"title slide\" id=\"slide0\">\n\t\t<figure class=\"cover width\">\n\t\t\t<img src=\"images/image1.jpg\">\n\t\t</figure>\n\t</div>";
        Assert.assertEquals(result, expected);
    }
    
    @Test
    public void mg() throws IOException{
        PPT ppt = createPPT(1.0, 1.2);
        String result = result(ppt);
        
        //System.out.println("**" + org.apache.commons.lang3.StringEscapeUtils.escapeJava(result) + "**");
        //System.out.println(result);
        
        String expected = "\r\n\t<div class=\"title slide\" id=\"slide0\">\n\t\t<figure class=\"cover width\">\n\t\t\t<img src=\"images/image1.jpg\">\n\t\t</figure>\n\t</div>";
        Assert.assertEquals(result, expected);
    }
    
    @Test
    public void mm() throws IOException{
        PPT ppt = createPPT(1.0, 1.0);
        String result = result(ppt);
        
        //System.out.println("**" + org.apache.commons.lang3.StringEscapeUtils.escapeJava(result) + "**");
        //System.out.println(result);
        
        String expected = "\r\n\t<div class=\"title slide\" id=\"slide0\">\n\t\t<figure class=\"cover height\">\n\t\t\t<img src=\"images/image1.jpg\">\n\t\t</figure>\n\t</div>";
        Assert.assertEquals(result, expected);
    }
    
    @Test
    public void mk() throws IOException{
        PPT ppt = createPPT(1.0, 0.8);
        String result = result(ppt);
        
        //System.out.println("**" + org.apache.commons.lang3.StringEscapeUtils.escapeJava(result) + "**");
        //System.out.println(result);
        
        String expected = "\r\n\t<div class=\"title slide\" id=\"slide0\">\n\t\t<figure class=\"cover width\">\n\t\t\t<img src=\"images/image1.jpg\">\n\t\t</figure>\n\t</div>";
        Assert.assertEquals(result, expected);
    }
    
    @Test
    public void kg() throws IOException{
        PPT ppt = createPPT(0.8, 1.2);
        String result = result(ppt);
        
        //System.out.println("**" + org.apache.commons.lang3.StringEscapeUtils.escapeJava(result) + "**");
        //System.out.println(result);
        
        String expected = "\r\n\t<div class=\"title slide\" id=\"slide0\">\n\t\t<figure class=\"cover height\">\n\t\t\t<img src=\"images/image1.jpg\">\n\t\t</figure>\n\t</div>";
        Assert.assertEquals(result, expected);
    }
    
    @Test
    public void km() throws IOException{
        PPT ppt = createPPT(0.8, 1.0);
        String result = result(ppt);
        
        //System.out.println("**" + org.apache.commons.lang3.StringEscapeUtils.escapeJava(result) + "**");
        //System.out.println(result);
        
        String expected = "\r\n\t<div class=\"title slide\" id=\"slide0\">\n\t\t<figure class=\"cover height\">\n\t\t\t<img src=\"images/image1.jpg\">\n\t\t</figure>\n\t</div>";
        Assert.assertEquals(result, expected);
    }
    
    @Test
    public void kk() throws IOException{
        PPT ppt = createPPT(0.8, 0.8);
        String result = result(ppt);
        
        //System.out.println("**" + org.apache.commons.lang3.StringEscapeUtils.escapeJava(result) + "**");
        //System.out.println(result);
        
        String expected = "\r\n\t<div class=\"title slide\" id=\"slide0\">\n\t\t<figure>\n\t\t\t<img src=\"images/image1.jpg\" width=\"81%\">\n\t\t</figure>\n\t</div>";
        Assert.assertEquals(result, expected);
    }
    
    @Test
    public void zeroByZero() throws IOException{
        PPT ppt = createPPT(0, 0);
        String result = result(ppt);
        
        //System.out.println("**" + org.apache.commons.lang3.StringEscapeUtils.escapeJava(result) + "**");
        //System.out.println(result);
        
        String expected = "\r\n\t<div class=\"title slide\" id=\"slide0\">\n\t\t<figure>\n\t\t\t<img src=\"images/image1.jpg\">\n\t\t</figure>\n\t</div>";
        Assert.assertEquals(result, expected);
    }
    
    @Test
    public void noSize() throws IOException{
        Image image = new Image();
        image.setFilename("image1.jpg");
        
        Slide slide = new Slide();
        slide.getPptObjects().add(image);
        
        PPT ppt = new PPT();
        ppt.getSlides().add(slide);
        
        String result = result(ppt);
        
        //System.out.println("**" + org.apache.commons.lang3.StringEscapeUtils.escapeJava(result) + "**");
        //System.out.println(result);
        
        String expected = "\r\n\t<div class=\"title slide\" id=\"slide0\">\n\t\t<figure>\n\t\t\t<img src=\"images/image1.jpg\">\n\t\t</figure>\n\t</div>";
        Assert.assertEquals(result, expected);
    }
    
    @Test
    public void noFilename() throws IOException{
        Image image = new Image();
        
        Slide slide = new Slide();
        slide.getPptObjects().add(image);
        
        PPT ppt = new PPT();
        ppt.getSlides().add(slide);
        
        String result = result(ppt);
        
        //System.out.println("**" + org.apache.commons.lang3.StringEscapeUtils.escapeJava(result) + "**");
        //System.out.println(result);
        
        String expected = "\r\n\t<div class=\"title slide\" id=\"slide0\">\n\t\t<div class=\"placeholder\">image</div>\n\t</div>";
        Assert.assertEquals(result, expected);
    }
    
    
    
    PPT createPPT(double w, double h){
        Image image = new Image();
        image.setFilename("image1.jpg");
        image.getDimension().setSize(w*33, h*19);
        
        Slide slide = new Slide();
        slide.getPptObjects().add(image);
        
        PPT ppt = new PPT();
        ppt.getSlides().add(slide);
        return ppt;
    }
    
    String result(PPT ppt) throws IOException{
        String result;
        try(StringWriter sw = new StringWriter(); BufferedWriter out = new BufferedWriter(sw)){
            
            Writer writer = new HTMLWriter(new StdOutput());
            writer.write(out, ppt);
            
            out.flush();
            result = sw.toString();
        }
        return result;
    }
}
