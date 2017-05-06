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
public class ListTest {

    public ListTest() {

    }

    @Test
    public void testAlpList() {
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

        Chart chart = new Chart("");
        slide.getPptObjects().add(chart);

        Text text = new Text();
        Textpart textpart = new Textpart();
        textpart.setContent("Tekst na chart");
        textpart.setXPosition(0);
        text.addTextpart(textpart);
        slide.getPptObjects().add(text);

        PPT ppt = new PPT();
        ppt.getSlides().add(slide);

        Logic logic = new Logic();
        logic.format(ppt);

        String text2 = HelpWriter.writeSlide(ppt.getSlides().get(0));

        String result = "§-Hieronder staat een opsomming.-§\n"
                + "#~§-Een-§~"
                + "~§-Twee-§~"
                + "~§-DrieNog altijd 3-§~"
                + "~#~§-Drie.Een-§~"
                + "~#~§-Drie.Een.Een-§~#~#~"
                + "~§-VierNog altijd 4-§~#\n"
                + "[CHART]\n"
                + "§-Tekst na chart-§\n";

        assertEquals(result, text2);
        assertEquals(4, ppt.getSlides().get(0).getPptObjects().size());
    }

    @Test
    public void testNumberedList() {
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

        PPT ppt = new PPT();
        ppt.getSlides().add(slide);

        Logic logic = new Logic();
        logic.format(ppt);

        String text = HelpWriter.writeSlide(ppt.getSlides().get(0));

        String result = "§-Hieronder staat een opsomming.-§\n"
                + "#~§-Een-§~"
                + "~§-Twee-§~"
                + "~§-DrieNog altijd 3-§~"
                + "~#~§-Drie.Een-§~"
                + "~#~§-Drie.Een.Een-§~#~#~"
                + "~§-VierNog altijd 4-§~#\n";

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

        PPT ppt = new PPT();
        ppt.getSlides().add(slide);

        Logic logic = new Logic();
        logic.format(ppt);

        String text = HelpWriter.writeSlide(ppt.getSlides().get(0));

        String result = "§-Hieronder staat een opsomming.-§\n"
                + "°~§-Een-§~"
                + "~§-Twee-§~"
                + "~§-DrieNog altijd 3-§~"
                + "~°~§-Drie.Een-§~"
                + "~°~§-Drie.Een.Een-§~°~°~"
                + "~§-VierNog altijd 4-§~°\n";
        assertEquals(result, text);
        assertEquals(2, ppt.getSlides().get(0).getPptObjects().size());
    }

}
