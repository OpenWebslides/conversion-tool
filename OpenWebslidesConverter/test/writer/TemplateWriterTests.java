/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import objects.PPT;
import objects.Slide;
import openwebslides.writer.HTMLWriter;
import openwebslides.writer.TemplateWriter;
import openwebslides.writer.Writer;
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
public class TemplateWriterTests {
    
    public TemplateWriterTests() {
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
    //1 empty slide
    public void test1() throws IOException{
        String result;
        try(StringWriter sw = new StringWriter(); BufferedWriter out = new BufferedWriter(sw)){
            PPT ppt = new PPT();
            Slide slide = new Slide();
            ppt.getSlides().add(slide);
            
            TemplateWriter tw = new TemplateWriter(new HTMLWriter(), null, null);
            tw.write(out, ppt);
            
            out.flush();
            result = sw.toString();
        }
        String expected = "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n\t<title>Introduction</title>\n\t<meta charset=\"utf-8\">\n\t<meta http-equiv=\"x-ua-compatible\" content=\"ie=edge\">\n\t<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,maximum-scale=1\">\n\t<link rel=\"stylesheet\" href=\"_shared/styles/screen-16x10.css\">\n</head>\n<body class=\"shower list\">\n\t<header class=\"caption\">\n\t\t<h1>\n\t\t\t<a class=\"series\" href=\"#title\">Opleidingsonderdeel</a><br>\n\t\t\t<a class=\"module\" href=\"#title\">Hoofdstuk</a>\n\t\t</h1>\n\t\t<p>\n\t\t</p>\n\t</header>\n\n\r\n\t<div class=\"title slide\" id=\"slide0\">\n\t</div>\n\n\t<footer>\n\t</footer>\n\t<script src=\"_shared/scripts/shower.min.js\"></script>\n\t<script src=\"_shared/scripts/enhancements.js\"></script>\n\t<script>ga=function(){ga.q.push(arguments)};ga.q=[['create','UA-6142365-12','auto'],['require','autotrack'],['send','pageview']];ga.l=1*new Date</script>\n\t<script async src=\"_shared/scripts/autotrack.js\"></script>\n</body>\n</html>";
        
        //System.out.println("**" + org.apache.commons.lang3.StringEscapeUtils.escapeJava(result) + "**");
        //System.out.println(result);
        
        Assert.assertEquals(result, expected);
    }
    
    @Test
    //course and chapter
    public void test2() throws IOException{
        String result;
        try(StringWriter sw = new StringWriter(); BufferedWriter out = new BufferedWriter(sw)){
            PPT ppt = new PPT();
            
            TemplateWriter tw = new TemplateWriter(new HTMLWriter(), "vak 0123456789", "deel 987");
            tw.write(out, ppt);
            
            out.flush();
            result = sw.toString();
        }
        String expected = "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n\t<title>Introduction</title>\n\t<meta charset=\"utf-8\">\n\t<meta http-equiv=\"x-ua-compatible\" content=\"ie=edge\">\n\t<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,maximum-scale=1\">\n\t<link rel=\"stylesheet\" href=\"_shared/styles/screen-16x10.css\">\n</head>\n<body class=\"shower list\">\n\t<header class=\"caption\">\n\t\t<h1>\n\t\t\t<a class=\"series\" href=\"#title\">vak 0123456789</a><br>\n\t\t\t<a class=\"module\" href=\"#title\">deel 987</a>\n\t\t</h1>\n\t\t<p>\n\t\t</p>\n\t</header>\n\n\n\n\t<footer>\n\t</footer>\n\t<script src=\"_shared/scripts/shower.min.js\"></script>\n\t<script src=\"_shared/scripts/enhancements.js\"></script>\n\t<script>ga=function(){ga.q.push(arguments)};ga.q=[['create','UA-6142365-12','auto'],['require','autotrack'],['send','pageview']];ga.l=1*new Date</script>\n\t<script async src=\"_shared/scripts/autotrack.js\"></script>\n</body>\n</html>";
        
        //System.out.println("**" + org.apache.commons.lang3.StringEscapeUtils.escapeJava(result) + "**");
        //System.out.println(result);
        
        Assert.assertEquals(result, expected);
    }
}
