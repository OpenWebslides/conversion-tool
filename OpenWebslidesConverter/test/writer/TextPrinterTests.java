/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package writer;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import objects.FontDecoration;
import objects.Text;
import objects.Textpart;
import openwebslides.writer.TextPrinter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Jonas
 */
public class TextPrinterTests {
    
    public TextPrinterTests() {
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
    public void bold(){
        Text text = new Text();
        
        Textpart[] part = new Textpart[1];
        
        part[0] = new Textpart();
        part[0].setContent("eerste");
        part[0].getType().add(FontDecoration.BOLD);
        
        for(Textpart p : part){
            text.addTextpart(p);
        }
        
        String res = TextPrinter.printText(text);
        String expected = "<strong>eerste</strong>";
        
        Assert.assertEquals(expected, res);
    }
    
    @Test
    public void italic(){
        Text text = new Text();
        
        Textpart[] part = new Textpart[1];
        
        part[0] = new Textpart();
        part[0].setContent("eerste");
        part[0].getType().add(FontDecoration.ITALIC);
        
        for(Textpart p : part){
            text.addTextpart(p);
        }
        
        String res = TextPrinter.printText(text);
        String expected = "<em>eerste</em>";
        
        Assert.assertEquals(expected, res);
    }
    
    @Test
    public void underline(){
        Text text = new Text();
        
        Textpart[] part = new Textpart[1];
        
        part[0] = new Textpart();
        part[0].setContent("eerste");
        part[0].getType().add(FontDecoration.UNDERLINE);
        
        for(Textpart p : part){
            text.addTextpart(p);
        }
        
        String res = TextPrinter.printText(text);
        String expected = "<span class=\"underline\">eerste</span>";
        
        Assert.assertEquals(expected, res);
    }
    
    @Test
    public void strike(){
        Text text = new Text();
        
        Textpart[] part = new Textpart[1];
        
        part[0] = new Textpart();
        part[0].setContent("eerste");
        part[0].getType().add(FontDecoration.STRIKETHROUH);
        
        for(Textpart p : part){
            text.addTextpart(p);
        }
        
        String res = TextPrinter.printText(text);
        String expected = "<del>eerste</del>";
        
        Assert.assertEquals(expected, res);
    }
    
    @Test
    public void test2(){
        Text text = new Text();
        
        Textpart[] part = new Textpart[2];
        
        part[0] = new Textpart();
        part[0].setContent("eerste");
        part[0].getType().add(FontDecoration.BOLD);
        part[1] = new Textpart();
        part[1].setContent("tweede");
        part[1].getType().add(FontDecoration.ITALIC);
        part[1].getType().add(FontDecoration.BOLD);
        
        for(Textpart p : part){
            text.addTextpart(p);
        }
        
        String res = TextPrinter.printText(text);
        String expected = "<strong>eerste<em>tweede</em></strong>";
        
        Assert.assertEquals(expected, res);
    }
    
    @Test
    public void test3(){
        Text text = new Text();
        
        Textpart[] part = new Textpart[3];
        
        part[0] = new Textpart();
        part[0].setContent("eerste");
        part[0].getType().add(FontDecoration.BOLD);
        part[1] = new Textpart();
        part[1].setContent("tweede");
        part[1].getType().add(FontDecoration.ITALIC);
        part[1].getType().add(FontDecoration.BOLD);
        part[2] = new Textpart();
        part[2].setContent("derde");
        part[2].getType().add(FontDecoration.ITALIC);
        part[2].getType().add(FontDecoration.BOLD);
        
        for(Textpart p : part){
            text.addTextpart(p);
        }
        
        String res = TextPrinter.printText(text);
        String expected = "<strong>eerste<em>tweedederde</em></strong>";
        
        Assert.assertEquals(expected, res);
    }
    
    @Test
    public void test4(){
        Text text = new Text();
        
        Textpart[] part = new Textpart[3];
        
        part[0] = new Textpart();
        part[0].setContent("eerste");
        part[0].getType().add(FontDecoration.BOLD);
        part[1] = new Textpart();
        part[1].setContent("tweede");
        part[1].getType().add(FontDecoration.ITALIC);
        part[1].getType().add(FontDecoration.BOLD);
        part[2] = new Textpart();
        part[2].setContent("derde");
        part[2].getType().add(FontDecoration.ITALIC);
        
        for(Textpart p : part){
            text.addTextpart(p);
        }
        
        String res = TextPrinter.printText(text);
        String expected = "<strong>eerste<em>tweede</em></strong><em>derde</em>";
        
        Assert.assertEquals(expected, res);
    }
    
    @Test
    public void test5(){
        Text text = new Text();
        
        Textpart[] part = new Textpart[4];
        
        part[0] = new Textpart();
        part[0].setContent("eerste");
        part[0].getType().add(FontDecoration.BOLD);
        part[1] = new Textpart();
        part[1].setContent("tweede");
        part[1].getType().add(FontDecoration.ITALIC);
        part[1].getType().add(FontDecoration.BOLD);
        part[2] = new Textpart();
        part[2].setContent("derde");
        part[2].getType().add(FontDecoration.ITALIC);
        part[2].getType().add(FontDecoration.ITALIC);
        part[3] = new Textpart();
        part[3].setContent("derde");
        part[3].getType().add(FontDecoration.ITALIC);
        part[3].getType().add(FontDecoration.ITALIC);
        
        for(Textpart p : part){
            text.addTextpart(p);
        }
        
        String res = TextPrinter.printText(text);
        String expected = "<strong>eerste<em>tweede</em></strong><em>derde</em>";
        
        Assert.assertEquals(expected, res);
    }
    
}
