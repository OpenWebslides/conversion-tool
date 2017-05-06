/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import objects.FontDecoration;
import objects.PPT;
import objects.Slide;
import objects.Text;
import objects.Textpart;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Joann
 */
public class FontDecorationTest {

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

    @Test
    public void testNestedFontDecoration() {
        Slide slide = new Slide();
        String[] tekst = {"Hieronder staat een opsomming.", "• A", "• B", "• C", "...", "○ CA", "○ CB", "◌ CBA", "◌ CBB", "• D", "..."};
        int[] level = {0, 0, 0, 0, 0, 1, 1, 4, 4, 0, 0};
        for (int i = 0; i < tekst.length; i++) {
            Text text = new Text();
            Textpart textpart = new Textpart();
            textpart.setXPosition(level[i]);
            textpart.setContent(tekst[i]);
            text.addTextpart(textpart);
            slide.getPptObjects().add(text);
        }

        PPT ppt = new PPT();
        ppt.getSlides().add(slide);

        Logic logic = new Logic();
        logic.format(ppt);

        String text = HelpWriter.writeSlide(ppt.getSlides().get(0));

        String result = "§-Hieronder staat een opsomming.-§\n"
                + "°~§-A-§~"
                + "~§-B-§~"
                + "~§-C...-§~"
                + "~°~§-CA-§~"
                + "~§-CB-§~"
                + "~°~§-CBA-§~"
                + "~§-CBB-§~°~°~"
                + "~§-D...-§~°\n";

        assertEquals(result, text);
        assertEquals(2, ppt.getSlides().get(0).getPptObjects().size());
    }

}
