/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import objects.*;
import openwebslides.writer.*;
import openwebslidesconverter.Converter;
import openwebslidesconverter.WebslidesConverterException;
import org.apache.commons.lang3.StringEscapeUtils;
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
    //empty slides
    public void writePPTTest0() throws IOException{
        String result;
        try(StringWriter sw = new StringWriter(); BufferedWriter out = new BufferedWriter(sw)){
            PPT ppt = new PPT();
            Slide[] slides = new Slide[3];
            
            for(int i=0; i<3; i++){
                slides[i] = new Slide();
                ppt.getSlides().add(slides[i]);
            }
            
            Writer writer = new HTMLWriter(new StdOutput());
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
    //1 slide with a title
    public void writePPTTest1() throws IOException{
        String result;
        try(StringWriter sw = new StringWriter(); BufferedWriter out = new BufferedWriter(sw)){
            PPT ppt = new PPT();
            Slide[] slides = new Slide[1];
            slides[0] = new Slide();
            
            Title title1 = new Title();
            Textpart part1 = new Textpart();
            part1.setContent("Lorem ipsum dolor");
            title1.getTextparts().add(part1);
            
            slides[0].getPptObjects().add(title1);
            
            for(Slide s : slides){
                ppt.getSlides().add(s);
            }
            
            Writer writer = new HTMLWriter(new StdOutput());
            writer.write(out, ppt);
            
            out.flush();
            result = sw.toString();
        }
        //System.out.println("**" + org.apache.commons.lang3.StringEscapeUtils.escapeJava(result) + "**");
        
        String expected = "\r\n<div class=\"title slide\" id=\"slide0\">\n\t<h2>Lorem ipsum dolor</h2>\n</div>";
        
        Assert.assertEquals(result, expected);
    }
    
    @Test
    //2 slides with text and a title
    public void writePPTTest2() throws IOException{
        String result;
        try(StringWriter sw = new StringWriter(); BufferedWriter out = new BufferedWriter(sw)){
            PPT ppt = new PPT();
            Slide[] slides = new Slide[2];
            
            Title title1 = new Title();
            Textpart part1 = new Textpart();
            part1.setContent("Lorem ipsum dolor sit amet");
            title1.getTextparts().add(part1);
            Title title2 = new Title();
            Textpart part2 = new Textpart();
            part2.setContent("Vivamus posuere neque");
            title2.getTextparts().add(part2);
            
            slides[0] = new Slide();
            slides[0].getPptObjects().add(title1);
            slides[1] = new Slide();
            slides[1].getPptObjects().add(title2);
            
            Textpart[] parts = new Textpart[4];
            parts[0] = new Textpart();
            parts[0].setContent("Consectetur adipiscing elit. Nullam tincidunt ligula quis ligula bibendum pharetra. ");
            parts[1] = new Textpart();
            parts[1].setContent("Donec eget lectus ut nisi laoreet aliquam. Curabitur eget posuere purus.");
            parts[2] = new Textpart();
            parts[2].setContent("Sit amet ante porta, vitae ornare nunc luctus. Interdum et malesuada fames ac ante ipsum primis in faucibus. ");
            parts[3] = new Textpart();
            parts[3].setContent("Nam volutpat dictum felis quis semper. Phasellus vitae nisi consectetur, semper lorem vel, tempor nunc.");
            
            Text[] text = new Text[2];
            text[0] = new Text();
            text[0].addTextpart(parts[0]);
            text[0].addTextpart(parts[1]);
            text[1] = new Text();
            text[1].addTextpart(parts[2]);
            text[1].addTextpart(parts[3]);
            
            slides[0].getPptObjects().add(text[0]);
            slides[1].getPptObjects().add(text[1]);
            
            for(Slide s : slides){
                ppt.getSlides().add(s);
            }
            
            Writer writer = new HTMLWriter(new StdOutput());
            writer.write(out, ppt);
            
            out.flush();
            result = sw.toString();
        }
        //System.out.println("**" + org.apache.commons.lang3.StringEscapeUtils.escapeJava(result) + "**");
        //System.out.println(result);
        
        String expected = "\r\n<div class=\"title slide\" id=\"slide0\">\n\t<h2>Lorem ipsum dolor sit amet</h2>\n\t<p>Consectetur adipiscing elit. Nullam tincidunt ligula quis ligula bibendum pharetra. Donec eget lectus ut nisi laoreet aliquam. Curabitur eget posuere purus.</p>\n</div>\r\n<div class=\"slide\" id=\"slide1\">\n\t<h2>Vivamus posuere neque</h2>\n\t<p>Sit amet ante porta, vitae ornare nunc luctus. Interdum et malesuada fames ac ante ipsum primis in faucibus. Nam volutpat dictum felis quis semper. Phasellus vitae nisi consectetur, semper lorem vel, tempor nunc.</p>\n</div>";
        
        Assert.assertEquals(result, expected);
    }
    
    @Test
    //1 slide with bold, underlined and italic textparts
    public void writePPTTest3() throws IOException{
        String result;
        try(StringWriter sw = new StringWriter(); BufferedWriter out = new BufferedWriter(sw)){
            PPT ppt = new PPT();
            Slide[] slides = new Slide[1];
            
            slides[0] = new Slide();
            
            Textpart[] parts = new Textpart[7];
            parts[0] = new Textpart();
            parts[0].setContent("This is text with a ");
            parts[1] = new Textpart();
            parts[1].setContent("important");
            parts[1].getType().add(FontDecoration.BOLD);
            parts[2] = new Textpart();
            parts[2].setContent(" word, ");
            parts[3] = new Textpart();
            parts[3].setContent("underlined");
            parts[3].getType().add(FontDecoration.UNDERLINE);
            parts[4] = new Textpart();
            parts[4].setContent(" word and ");
            parts[5] = new Textpart();
            parts[5].setContent("italic");
            parts[5].getType().add(FontDecoration.ITALIC);
            parts[6] = new Textpart();
            parts[6].setContent(" word.");
            
            Text[] text = new Text[3];
            text[0] = new Text();
            text[1] = new Text();
            text[2] = new Text();
            
            for(Textpart p : parts){
                text[0].addTextpart(p);
            }
            
            Textpart[] parts2 = new Textpart[3];
            parts2[0] = new Textpart();
            parts2[0].setContent("You could be using ");
            parts2[1] = new Textpart();
            parts2[1].getType().add(FontDecoration.STRIKETHROUH);
            parts2[1].setContent("deprecated");
            parts2[2] = new Textpart();
            parts2[2].setContent(" code.");
            
            for(Textpart p : parts2){
                text[1].addTextpart(p);
            }
            
            Textpart[] parts3 = new Textpart[3];
            parts3[0] = new Textpart();
            parts3[0].setContent("Some ");
            parts3[1] = new Textpart();
            parts3[1].getType().add(FontDecoration.BOLD);
            parts3[1].getType().add(FontDecoration.ITALIC);
            parts3[1].getType().add(FontDecoration.UNDERLINE);
            parts3[1].getType().add(FontDecoration.STRIKETHROUH);
            parts3[1].setContent("nested");
            parts3[2] = new Textpart();
            parts3[2].setContent(" tags.");
            
            for(Textpart p : parts3){
                text[2].addTextpart(p);
            }
            
            slides[0].getPptObjects().add(text[0]);
            slides[0].getPptObjects().add(text[1]);
            slides[0].getPptObjects().add(text[2]);
            
            for(Slide s : slides){
                ppt.getSlides().add(s);
            }
            
            Writer writer = new HTMLWriter(new StdOutput());
            writer.write(out, ppt);
            
            out.flush();
            result = sw.toString();
        }
        //System.out.println("**" + org.apache.commons.lang3.StringEscapeUtils.escapeJava(result) + "**");
        //System.out.println(result);
        
        String expected = "\r\n<div class=\"title slide\" id=\"slide0\">\n\t<p>This is text with a <strong>important</strong> word, <span style=\"text-decoration: underline;\">underlined</span> word and <em>italic</em> word.</p>\n\t<p>You could be using <strike>deprecated</strike> code.</p>\n\t<p>Some <strike><em><span style=\"text-decoration: underline;\"><strong>nested</strong></span></em></strike> tags.</p>\n</div>";
        
        Assert.assertEquals(result, expected);
    }
    
    @Test
    //1 slide with an ordered list
    public void writePPTTest4() throws IOException{
        String result;
        try(StringWriter sw = new StringWriter(); BufferedWriter out = new BufferedWriter(sw)){
            PPT ppt = new PPT();
            Slide slide = new Slide();
            
            PPTList list = new PPTList();
            list.setOrdered(true);
            
            Text[] objects = new Text[3];
            
            for(int i=0; i<3; i++){
                objects[i] = new Text();
                Textpart tp = new Textpart();
                tp.setContent("Line "+i);
                objects[i].addTextpart(tp);
                list.addPPTObject(objects[i]);
            }
            
            slide.getPptObjects().add(list);
            ppt.getSlides().add(slide);
            
            Writer writer = new HTMLWriter(new StdOutput());
            writer.write(out, ppt);
            
            out.flush();
            result = sw.toString();
        }
        String expected_result = "\r\n<div class=\"title slide\" id=\"slide0\">\n\t<ol>\n\t\t<li><p>Line 0</p></li>\n\t\t<li><p>Line 1</p></li>\n\t\t<li><p>Line 2</p></li>\n\t</ol>\n</div>";
        
        //System.out.println("**" + org.apache.commons.lang3.StringEscapeUtils.escapeJava(result) + "**");
        //System.out.println(result);
        
        Assert.assertEquals(result, expected_result);
    }
    
    @Test
    //1 slide with an unordered list
    public void writePPTTest5() throws IOException{
        String result;
        try(StringWriter sw = new StringWriter(); BufferedWriter out = new BufferedWriter(sw)){
            PPT ppt = new PPT();
            Slide slide = new Slide();
            
            PPTList list = new PPTList();
            list.setOrdered(false);
            
            Text[] objects = new Text[3];
            
            for(int i=0; i<3; i++){
                objects[i] = new Text();
                Textpart tp = new Textpart();
                tp.setContent("Line "+i);
                objects[i].addTextpart(tp);
                list.addPPTObject(objects[i]);
            }
            
            slide.getPptObjects().add(list);
            ppt.getSlides().add(slide);
            
            Writer writer = new HTMLWriter(new StdOutput());
            writer.write(out, ppt);
            
            out.flush();
            result = sw.toString();
        }
        String expected_result = "\r\n<div class=\"title slide\" id=\"slide0\">\n\t<ul>\n\t\t<li><p>Line 0</p></li>\n\t\t<li><p>Line 1</p></li>\n\t\t<li><p>Line 2</p></li>\n\t</ul>\n</div>";
        
        //System.out.println("**" + org.apache.commons.lang3.StringEscapeUtils.escapeJava(result) + "**");
        //System.out.println(result);
        
        Assert.assertEquals(result, expected_result);
    }
    
    @Test
    //1 slide with nested lists
    public void writePPTTest6() throws IOException{
        String result;
        try(StringWriter sw = new StringWriter(); BufferedWriter out = new BufferedWriter(sw)){
            PPT ppt = new PPT();
            Slide slide = new Slide();
            
            PPTList list = new PPTList();
            list.setOrdered(false);
            
            Text[] objects = new Text[3];
            
            for(int i=0; i<3; i++){
                objects[i] = new Text();
                Textpart tp = new Textpart();
                tp.setContent("Line "+i);
                objects[i].addTextpart(tp);
                list.addPPTObject(objects[i]);
            }
            
            PPTList outerList = new PPTList();
            outerList.setOrdered(true);
            outerList.addPPTObject(list);
            
            slide.getPptObjects().add(outerList);
            ppt.getSlides().add(slide);
            
            Writer writer = new HTMLWriter(new StdOutput());
            writer.write(out, ppt);
            
            out.flush();
            result = sw.toString();
        }
        String expected_result = "\r\n<div class=\"title slide\" id=\"slide0\">\n\t<ol>\n\t\t<li><ul>\n\t\t\t<li><p>Line 0</p></li>\n\t\t\t<li><p>Line 1</p></li>\n\t\t\t<li><p>Line 2</p></li>\n\t\t</ul></li>\n\t</ol>\n</div>";
        
        //System.out.println("**" + org.apache.commons.lang3.StringEscapeUtils.escapeJava(result) + "**");
        //System.out.println(result);
        
        Assert.assertEquals(result, expected_result);
    }
    
    @Test
    //1 slide with 1 image
    public void writePPTTest7() throws IOException{
        String result;
        try(StringWriter sw = new StringWriter(); BufferedWriter out = new BufferedWriter(sw)){
            PPT ppt = new PPT();
            Slide slide = new Slide();
            
            Image img = new Image();
            img.setFilename("image1.jpg");
            
            slide.getPptObjects().add(img);
            ppt.getSlides().add(slide);
            
            Writer writer = new HTMLWriter(new StdOutput());
            writer.write(out, ppt);
            
            out.flush();
            result = sw.toString();
        }
        String expected_result = "\r\n<div class=\"title slide\" id=\"slide0\">\n\t<figure>\n\t\t<img src=\"image1.jpg\">\n\t</figure>\n</div>";
        
        //System.out.println("**" + org.apache.commons.lang3.StringEscapeUtils.escapeJava(result) + "**");
        //System.out.println(result);
        
        Assert.assertEquals(result, expected_result);
    }
    
    @Test
    //1 slide with a title with decoration
    public void writePPTTest8() throws IOException{
        String result;
        try(StringWriter sw = new StringWriter(); BufferedWriter out = new BufferedWriter(sw)){
            PPT ppt = new PPT();
            Slide[] slides = new Slide[1];
            slides[0] = new Slide();
            
            Title title = new Title();
            Textpart part1 = new Textpart();
            part1.setContent("Lorem ");
            title.getTextparts().add(part1);
            Textpart part2 = new Textpart();
            part2.setContent("ipsum");
            part2.getType().add(FontDecoration.BOLD);
            title.getTextparts().add(part2);
            Textpart part3 = new Textpart();
            part3.setContent(" dolor");
            title.getTextparts().add(part3);
            
            slides[0].getPptObjects().add(title);
            
            for(Slide s : slides){
                ppt.getSlides().add(s);
            }
            
            Writer writer = new HTMLWriter(new StdOutput());
            writer.write(out, ppt);
            
            out.flush();
            result = sw.toString();
        }
        //System.out.println("**" + org.apache.commons.lang3.StringEscapeUtils.escapeJava(result) + "**");
        //System.out.println(result);
        
        String expected = "\r\n<div class=\"title slide\" id=\"slide0\">\n\t<h2>Lorem <strong>ipsum</strong> dolor</h2>\n</div>";
        
        Assert.assertEquals(result, expected);
    }
    
    
    
}
