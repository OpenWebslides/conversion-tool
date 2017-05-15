/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import objects.PPT;
import objects.PPTObject;
import objects.Slide;
import objects.Text;
import objects.Textpart;
import objects.Title;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Joann
 */
public class TitleTest {

    public TitleTest() {
    }

    @Test
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
        int nr_titles = 0;

        for (PPTObject obj : ppt.getSlides().get(0).getPptObjects()) {
            if (!(obj instanceof Title)) {
                otherObjects = true;
            } else {
                nr_titles++;
                if (otherObjects) {
                    titleOK = false;
                }
            }
        }
        assertEquals(2, nr_titles);
        assertTrue(titleOK);
    }
}
