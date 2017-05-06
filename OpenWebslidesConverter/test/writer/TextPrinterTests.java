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
import objects.Hyperlink;
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
        
        String res = TextPrinter.printText(text.getTextparts());
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
        
        String res = TextPrinter.printText(text.getTextparts());
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
        
        String res = TextPrinter.printText(text.getTextparts());
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
        
        String res = TextPrinter.printText(text.getTextparts());
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
        
        String res = TextPrinter.printText(text.getTextparts());
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
        
        String res = TextPrinter.printText(text.getTextparts());
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
        
        String res = TextPrinter.printText(text.getTextparts());
        String expected = "<strong>eerste<em>tweede</em></strong><em>derde</em>";
        
        Assert.assertEquals(expected, res);
    }
    
    /* could give other result resulting from the changing order of the FontDecoration in the list
    @Test
    public void test5(){
        Text text = new Text();
        
        Textpart[] part = new Textpart[14];
        
        part[0] = new Textpart();
        part[0].setContent("E");
        part[1] = new Textpart();
        part[1].setContent("e");
        part[1].getType().add(FontDecoration.UNDERLINE);
        part[2] = new Textpart();
        part[2].setContent("n m");
        part[3] = new Textpart();
        part[3].setContent("oei");
        part[3].getType().add(FontDecoration.ITALIC);
        part[3].getType().add(FontDecoration.UNDERLINE);
        part[3].getType().add(FontDecoration.BOLD);
        part[4] = new Textpart();
        part[4].setContent("li");
        part[4].getType().add(FontDecoration.BOLD);
        part[4].getType().add(FontDecoration.UNDERLINE);
        part[5] = new Textpart();
        part[5].setContent("j");
        part[6] = new Textpart();
        part[6].setContent("ke");
        part[6].getType().add(FontDecoration.ITALIC);
        part[6].getType().add(FontDecoration.UNDERLINE);
        part[6].getType().add(FontDecoration.BOLD);
        part[7] = new Textpart();
        part[7].setContent(" z");
        part[8] = new Textpart();
        part[8].setContent("i");
        part[8].getType().add(FontDecoration.STRIKETHROUH);
        part[8].getType().add(FontDecoration.UNDERLINE);
        part[9] = new Textpart();
        part[9].setContent("n");
        part[9].getType().add(FontDecoration.ITALIC);
        part[9].getType().add(FontDecoration.UNDERLINE);
        part[9].getType().add(FontDecoration.STRIKETHROUH);
        part[10] = new Textpart();
        part[10].setContent(" ");
        part[11] = new Textpart();
        part[11].setContent("of ");
        part[11].getType().add(FontDecoration.ITALIC);
        part[11].getType().add(FontDecoration.UNDERLINE);
        part[11].getType().add(FontDecoration.BOLD);
        part[12] = new Textpart();
        part[12].setContent("n");
        part[12].getType().add(FontDecoration.BOLD);
        part[13] = new Textpart();
        part[13].setContent("ieT?");
        part[13].getType().add(FontDecoration.ITALIC);
        part[13].getType().add(FontDecoration.BOLD);
        
        for(Textpart p : part){
            text.addTextpart(p);
        }
        
        String res = TextPrinter.printText(text.getTextparts());
        System.out.println(res);
        String expected = "E<span class=\"underline\">e</span>n m<span class=\"underline\"><em><strong>oei</strong></em><strong>li</strong></span>j<span class=\"underline\"><em><strong>ke</strong></em></span> z<del><span class=\"underline\">i<em>n</em></span></del> <span class=\"underline\"><em><strong>of </strong></em></span><strong>n<em>ieT?</em></strong>";
        Assert.assertEquals(expected, res);
    }*/
    
    @Test
    public void test6(){
        Text text = new Text();
        
        Textpart[] part = new Textpart[4];
        
        part[0] = new Textpart();
        part[0].setContent("eerste");
        part[0].getType().add(FontDecoration.BOLD);
        part[1] = new Textpart();
        part[1].setContent("tweede");
        part[1].getType().add(FontDecoration.ITALIC);
        part[1].getType().add(FontDecoration.BOLD);
        
        Hyperlink link = new Hyperlink(new Textpart());
        link.setUrl("http://webslide.ugent.be/");
        Textpart tp = new Textpart();
        tp.setContent("klik");
        link.getParts().add(tp);
        part[2] = link;
        
        part[3] = new Textpart();
        part[3].setContent("derde");
        part[3].getType().add(FontDecoration.ITALIC);
        
        for(Textpart p : part){
            text.addTextpart(p);
        }
        
        String res = TextPrinter.printText(text.getTextparts());
        String expected = "<strong>eerste<em>tweede</em></strong> <a href=\"http://webslide.ugent.be/\" target=\"_blank\">klik</a> <em>derde</em>";
        
        Assert.assertEquals(expected, res);
    }
    
}
