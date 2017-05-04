/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.pdf.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

import objects.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.PageIterator;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;
import technology.tabula.extractors.BasicExtractionAlgorithm;
import technology.tabula.extractors.ExtractionAlgorithm;

/**
 * This class extracts tables from a pdf.
 *
 * @author Gertjan
 */
public class TableIntelligence {

    /**
     * returns an arraylist with pairs of int and table objects. The int
     * represents the pagenumber. The array is filled with info from a
     * PDDocument.
     *
     * @param d
     * @return
     */
    public ArrayList<Pair<Integer, objects.Table>> extractTables(PDDocument d) {
        ObjectExtractor oe;
        int pagenr = -1;
        ArrayList<Pair<Integer, objects.Table>> tabellen = new ArrayList();
        try {
            oe = new ObjectExtractor(d);

            ExtractionAlgorithm extractor = new BasicExtractionAlgorithm();

            PageIterator it = oe.extract();
            boolean growing = false;
            while (it.hasNext()) {
                Page page = it.next();
                pagenr++;
                for (Table table : extractor.extract(page)) {
                    objects.Table tabel = new objects.Table();
                    for (List<RectangularTextContainer> row : table.getRows()) {
                        int teller = 0;
                        //System.out.print("|");
                        for (RectangularTextContainer cell : row) {
                            //System.out.print(cell.getText() + "|");

                            if (cell.getText() != "") {
                                teller++;
                            }
                        }
                        //System.out.print(teller);
                        if (teller > 1) {
                            //maak tabel aan
                            growing = true;
                            //System.out.print(" =tabel!!!");
                            objects.Row rij = new objects.Row();
                            for (RectangularTextContainer cell : row) {
                                rij.getCells().add(new objects.Cell(cell.getText(), 0, 0));
                            }
                            tabel.getRows().add(rij);
                        } else if (growing = true) {
                            //hij was aan het groeien maar nu heb je dingen te kort -> tabel afsluiten
                            tabellen.add(new Pair(pagenr, tabel));
                            tabel = new objects.Table();
                        }
                        //System.out.println("");
                    }
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(TableIntelligence.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tabellen;
    }

    /**
     * adds all tables on the right place
     *
     * @param ppt
     * @param tab
     */
    public void placeTables(PPT ppt, ArrayList<Pair<Integer, objects.Table>> tab) {
        int pagenr = 0;
        /*  first place the slides on the page where they belong
         next remove te double text per page.  */
        for (Slide slide : ppt.getSlides()) {
           
            for (Pair<Integer, objects.Table> t : tab) {
                if (t.getKey() == pagenr) {
                    slide.getPptObjects().add(t.getValue());
                    //tab.remove(t); //verwijderen bespaard zoekwerk voor toekomst! maar gaat blijkbaar niet in foreach
                }
            }
            pagenr++;
        }
        RemoveDouble(ppt);
    }   

    /**
     * removes text that is found in a table on the same page. (once, because double extracted).
     * @param ppt 
     */
    private void RemoveDouble(PPT ppt) {

    }

}
