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

    //@Test
    public void testOrderedList() {
        testNumberedList();
        testAlpList();
    }

    private void testAlpList() {
        Slide slide = new Slide();
        String[] tekst = {"Hieronder staat een opsomming.", "A. Een", "B. Twee", "C. Drie", "Nog altijd 3", "A. Drie.Een", "A. Drie.Een.Een", "D. Vier", "Nog altijd 4"};
        int[] level = {0, 4, 4, 4, 4, 8, 13, 4, 4};
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
                text += writePPTObject(obj);
            }
        }

        String result = "*-Hieronder staat een opsomming.-*\n"
                + "°§*-Een-*§"
                + "§*-Twee-*§"
                + "§*-DrieNog altijd 3-*§"
                + "§°§*-Drie.Een-*§"
                + "§°§*-Drie.Een.Een-*§°§°§"
                + "§*-VierNog altijd 4-*§°\n";

        assertEquals(result, text);
        assertEquals(2, ppt.getSlides().get(0).getPptObjects().size());
    }

    private void testNumberedList() {
        Slide slide = new Slide();
        String[] tekst = {"Hieronder staat een opsomming.", "1) Een", "2) Twee", "3) Drie", "Nog altijd 3", "1) Drie.Een", "1) Drie.Een.Een", "4) Vier", "Nog altijd 4"};
        int[] level = {0, 4, 4, 4, 4, 8, 13, 4, 4};
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
                text += writePPTObject(obj);
            }
        }

        String result = "*-Hieronder staat een opsomming.-*\n"
                + "°§*-Een-*§"
                + "§*-Twee-*§"
                + "§*-DrieNog altijd 3-*§"
                + "§°§*-Drie.Een-*§"
                + "§°§*-Drie.Een.Een-*§°§°§"
                + "§*-VierNog altijd 4-*§°\n";

        assertEquals(result, text);
        assertEquals(2, ppt.getSlides().get(0).getPptObjects().size());
    }

    //@Test
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
                text += writePPTObject(obj);
            }
        }

        String result = "*-Hieronder staat een opsomming.-*\n"
                + "%§*-Een-*§"
                + "§*-Twee-*§"
                + "§*-DrieNog altijd 3-*§"
                + "§%§*-Drie.Een-*§"
                + "§%§*-Drie.Een.Een-*§%§%§"
                + "§*-VierNog altijd 4-*§%\n";
        assertEquals(result, text);
        assertEquals(2, ppt.getSlides().get(0).getPptObjects().size());
    }

    //@Test
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

    @Test
    public void testNestedFontDecoration() {
        Slide slide = new Slide();
        String[] tekst = {"Hieronder staat een opsomming.", "• A", "• B", "• C", "...", "○ CA", "◌ CAA", "• D", "..."};
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
                text += writePPTObject(obj);
            }
        }

        String result = "*-Hieronder staat een opsomming.-*\n"
                + "%§*-A-*§"
                + "§*-B-*§"
                + "§*-C...-*§"
                + "§%§*-CA-*§"
                + "§%§*-CAA-*§%§%§"
                + "§*-D...-*§%\n";
        assertEquals(result, text);
        assertEquals(2, ppt.getSlides().get(0).getPptObjects().size());
    }

    //@Test
    public void testTitleFirst() {
        Slide slide = new Slide();
        String[] tekst = {"Paragraaf 1", "Paragraaf 2", "Titel 1", "Paragraaf 3", "Titel 2", "Paragraaf 4"};

        for (String t : tekst) {
            Textpart tp = new Textpart();
            tp.setContent(t);
            if (t.contains("Titel")) {
                tp.setSize(4400);
            } else {
                tp.setSize(1800);
            }
            Text text = new Text();
            text.addTextpart(tp);
            slide.getPptObjects().add(text);
        }

        PPT ppt = new PPT();
        ppt.getSlides().add(slide);

        Logic logic = new Logic();
        logic.format(ppt);

        boolean titleOK = true;
        boolean otherObjects = false;

        for (PPTObject obj : ppt.getSlides().get(0).getPptObjects()) {
            if (!(obj instanceof Title)) {
                otherObjects = true;
            } else if (otherObjects) {
                titleOK = false;
            }
        }
        assertTrue(titleOK);
    }

    /*
        ** tekst
        $$ title
        -- textpart
        °° ol
        %% ul
        §§ bullet
     */
    private String writePPTObject(PPTObject obj) {
        String res = "";
        if (obj instanceof Text) {
            res += writeText((Text) obj);
        } else if (obj instanceof PPTList) {
            res += writePPTList((PPTList) obj);
        } else if (obj instanceof Title) {
            res += writeTitle((Title) obj);
        }
        res += "\n";
        return res;
    }

    private String writePPTList(PPTList list) {
        String res = "";
        String li = "%";
        if (list.isOrdered()) {
            li = "°";
        }
        for (PPTObject obj : list.getBullets()) {
            res += "§";
            if (obj instanceof Text) {
                res += writeText((Text) obj);
            } else if (obj instanceof PPTList) {
                res += writePPTList((PPTList) obj);
            }
            res += "§";
        }
        return li + res + li;
    }

    private String writeText(Text text) {
        String res = "";
        for (Textpart tp : text.getTextparts()) {
            res += writeTextpart(tp);
        }
        return "*" + res + "*";
    }

    private String writeTitle(Title title) {
        String res = "";
        for (Textpart tp : title.getTextparts()) {
            res += writeTextpart(tp);
        }
        return "$" + res + "$";
    }

    private String writeTextpart(Textpart tp) {
        return "-" + tp.getContent() + "-";
    }
    
    @Test
    private void testTable(){
        PPT ppt = new PPT();
        Slide slide = new Slide();
        
        Table t = new Table();
        Text te = new Text();
        
        
        
        
    }
}
