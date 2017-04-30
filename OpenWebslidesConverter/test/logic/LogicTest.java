/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import objects.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Joann
 */
public class LogicTest {

    public LogicTest() {
    }

    //@Test
    public void testOrderedList1() {
        Slide slide = new Slide();
        String[] tekst = {"Hieronder staat een opsomming.", "1. Een", "2. Twee", "3. Drie", "Nog altijd 3", "3.1. Drie.Een", "3.1.1. Drie.Een.Een", "4. Vier", "Nog altijd 4"};

        for (int i = 0; i < tekst.length; i++) {
            Text text = new Text();
            Textpart textpart = new Textpart();
            textpart.setContent(tekst[i]);
            text.addTextpart(textpart);
            slide.getPptObjects().add(text);
        }
        String text = "";

        PPT ppt = new PPT();
        ppt.getSlides().add(slide);

        Logic logic = new Logic();
        logic.format(ppt);

        for (Slide s : ppt.getSlides()) {
            for (PPTObject obj : s.getPptObjects()) {
                if (obj instanceof PPTList) {
                    text += writePPTList((PPTList) obj);
                }
            }
        }

        String result = "Een\nTwee\nDrieNog altijd 3\nDrie.Een\nDrie.Een.Een\nVierNog altijd 4\n";
        assertEquals(result, text);
        assertEquals(2, ppt.getSlides().get(0).getPptObjects().size());
    }

    @Test
    public void testOrderedList2() {
        Slide slide = new Slide();
        String[] tekst = {"Hieronder staat een opsomming.", "1) Een", "2) Twee", "3) Drie", "Nog altijd 3", "1) Drie.Een", "1) Drie.Een.Een", "4) Vier", "Nog altijd 4"};
        int[] level = {0, 0, 0, 0, 0, 1, 5, 0, 0};
        for (int i = 0; i < tekst.length; i++) {
            Text text = new Text();
            Textpart textpart = new Textpart();
            textpart.setContent(tekst[i]);
            textpart.setXPosition(level[i]);
            text.addTextpart(textpart);
            slide.getPptObjects().add(text);
        }
        String text = "";

        PPT ppt = new PPT();
        ppt.getSlides().add(slide);

        Logic logic = new Logic();
        logic.format(ppt);

        for (Slide s : ppt.getSlides()) {
            for (PPTObject obj : s.getPptObjects()) {
                if (obj instanceof PPTList) {
                    text += writePPTList((PPTList) obj);
                }
            }
        }

        String result = "Een\nTwee\nDrieNog altijd 3\nDrie.Een\nDrie.Een.Een\nVierNog altijd 4\n";
        assertEquals(result, text);
        assertEquals(2, ppt.getSlides().get(0).getPptObjects().size());
    }

    @Test
    public void testUnorderedList() {
        Slide slide = new Slide();
        String[] tekst = {"Hieronder staat een opsomming.", "• Een", "• Twee", "• Drie", "Nog altijd 3", "○ Drie.Een", "◌ Drie.Een.Een", "• Vier", "Nog altijd 4"};
        int[] level = {0, 0, 0, 0, 0, 1, 4, 0, 0};
        for (int i = 0; i < tekst.length; i++) {
            Text text = new Text();
            Textpart textpart = new Textpart();
            textpart.setXPosition(level[i]);
            textpart.setContent(tekst[i]);
            text.addTextpart(textpart);
            slide.getPptObjects().add(text);
        }
        String text = "";

        PPT ppt = new PPT();
        ppt.getSlides().add(slide);

        Logic logic = new Logic();
        logic.format(ppt);

        for (Slide s : ppt.getSlides()) {
            for (PPTObject obj : s.getPptObjects()) {
                if (obj instanceof PPTList) {
                    text += writePPTList((PPTList) obj);
                }
            }
        }

        String result = "Een\nTwee\nDrieNog altijd 3\nDrie.Een\nDrie.Een.Een\nVierNog altijd 4\n";
        assertEquals(result, text);
        assertEquals(2, ppt.getSlides().get(0).getPptObjects().size());
    }

    @Test
    public void testFontDecoration() {
        Slide slide = new Slide();
        String[] tekst = {"Dit ", "is ", "een ", "zin", ".", ".", "."};
        boolean[] bold = {true, true, false, false, true, true, true};
        boolean[] underline = {false, true, false, false, true, true, true};
        boolean[] italic = {true, false, false, false, false, false, false};

        Text text = new Text();
        for (int i = 0; i < tekst.length; i++) {
            Textpart textpart = new Textpart();
            if (bold[i]) {
                textpart.addType(FontDecoration.BOLD);
            }
            if (underline[i]) {
                textpart.addType(FontDecoration.UNDERLINE);
            }
            if (italic[i]) {
                textpart.addType(FontDecoration.ITALIC);
            }
            textpart.setContent(tekst[i]);
            text.addTextpart(textpart);
        }
        slide.getPptObjects().add(text);

        PPT ppt = new PPT();
        ppt.getSlides().add(slide);
        assertEquals(7, ((Text) ppt.getSlides().get(0).getPptObjects().get(0)).getTextparts().size());

        Logic logic = new Logic();
        logic.format(ppt);

        assertEquals(4, ((Text) ppt.getSlides().get(0).getPptObjects().get(0)).getTextparts().size());
        String str = "";
        for (Textpart tp : ((Text) ppt.getSlides().get(0).getPptObjects().get(0)).getTextparts()) {
            str += tp.getContent() + "\n";
        }
        assertEquals("Dit \nis \neen zin\n...\n", str);
    }

    private String writePPTList(PPTList list) {
        String res = "";

        for (PPTObject obj2 : list.getBullets()) {
            if (obj2 instanceof Text) {
                for (Textpart p : ((Text) obj2).getTextparts()) {
                    res += p.getContent();
                }
                res += "\n";
            } else if (obj2 instanceof PPTList) {
                res += writePPTList((PPTList) obj2);
            }
        }

        return res;
    }
}
