/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.pdf.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

import objects.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.RectangularTextContainer;
import technology.tabula.extractors.BasicExtractionAlgorithm;
import technology.tabula.extractors.ExtractionAlgorithm;


/**
 * This class extracts tables from a pdf.
 *
 * @author Gertjan
 */
public class TableIntelligence {

    private int totalTables = 0;

    /**
     * returns an arraylist with pairs of int and table objects. The int
     * represents the pagenumber. The array is filled with info from a
     * PDDocument.
     *
     * @param d Document
     * @return ArrayList
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Pair<Integer, objects.Table>> extractTables(PDDocument d) {
       // System.err.close();
        // PrintStream original = System.out;
        // System.setErr(new processOperatorBug());

        //System.out.println("extracting into array of pairs...");
        ObjectExtractor oe;
        int pagenr = -1;
        ArrayList<Pair<Integer, objects.Table>> tabellen = new ArrayList();
        try {
            oe = new ObjectExtractor(d);

            ExtractionAlgorithm extractor = new BasicExtractionAlgorithm();

            myPageIterator it = (myPageIterator) oe.extract();
            boolean growing = false;
            try {
                while (it != null && it.hasNext()) {
                    try {
                        Page page = it.next();
                        pagenr++;
                        for (technology.tabula.Table table : extractor.extract(page)) {
                           // System.out.println("extracted a page...");
                            objects.Table tabel = new objects.Table();
                            // System.out.println("tabel gevonden:");
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
                                    if (tabel.toString().trim().length() == 0) {
                                        // System.out.println("lege tabel!!!");
                                        tabel = new objects.Table();
                                    } else {
                                        // System.out.println("adding: " + tabel.toString().trim().length());
                                        tabellen.add(new Pair(pagenr, tabel));
                                        totalTables++;
                                        tabel = new objects.Table();
                                    }
                                }
                        //System.out.println("");
                                // System.out.println(tabel.toString());
                            }

                        }
                    } catch (Exception e) {
                        System.out.println("gevangen");
                    }
                }
            } catch (ArrayIndexOutOfBoundsException exeption) {
                System.out.println("catching AIOBex");
            }

        } catch (IOException ex) {
            System.out.println("exceptie gevangen");
            //Logger.getLogger(TableIntelligence.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception e) {
            System.out.println("table extraction failed");
        }
        //removeEmptyTables(tabellen);
        //finally{
        //System.setErr(original);
        //}
        return tabellen;
    }

    /**
     * adds all tables on the right place
     *
     * @param ppt PPT
     * @param tab ArrayList
     */
    @SuppressWarnings("unchecked")
    public void placeTables(PPT ppt, ArrayList<Pair<Integer, objects.Table>> tab) {
        System.out.println("placing tables in ppt...");
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
        removeFalseTables(ppt);
    }

    /**
     * removes text that is found in a table on the same page. (once, because
     * double extracted).
     *
     * @param ppt
     */
    @SuppressWarnings("unchecked")
    private void RemoveDouble(PPT ppt) {

        for (Slide slide : ppt.getSlides()) {
            ArrayList<objects.Text> removeFromSlide = new ArrayList();
            for (PPTObject obj : slide.getPptObjects()) {
                if (obj instanceof objects.Table) {
                    objects.Table tab = (objects.Table) obj;
                    for (objects.Row rij : tab.getRows()) {
                        //System.out.print("rij gevonden: ");
                        //System.out.println(rij.getContent());
                        ArrayList<String> cellen = new ArrayList();
                        for (objects.Cell cel : rij.getCells()) {
                            cellen.add(cel.getContent());
                        }
                        removeFromSlide.addAll(removeTextFromSlide(slide, cellen));
                    }
                }
            }

            for (Text txt : removeFromSlide) {
                slide.getPptObjects().remove(txt);
            }

        }
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Text> removeTextFromSlide(objects.Slide slide, ArrayList<String> cellen) {
        //System.out.println("cellen:");
        ArrayList<String> removeFromCellen = new ArrayList();
        for (String s : cellen) {
            if (s.isEmpty()) {
                removeFromCellen.add(s);
            } else {
                // System.out.println(s);
            }
        }
        //System.out.println("===============");
        for (String s : removeFromCellen) {
            cellen.remove(s);
        }

        ArrayList<objects.Text> toRemove = new ArrayList();
        for (PPTObject obj : slide.getPptObjects()) {
            if (obj instanceof objects.Text) {
                objects.Text t = (objects.Text) obj;
                int i = 0;
                for (Textpart tp : t.getTextparts()) {
                    while (i < cellen.size() && tp.getContent().trim().equals(cellen.get(i).trim())) {
                        // System.out.println("vergeleken: " + tp.getContent() + cellen.get(i));
                        i++;
                    }
                }
                //System.out.println("size: " + cellen.size() + " i: " + i);
                if (i == cellen.size()) {
                    toRemove.add(t);
                }
            }
        }
        return toRemove;
    }

    public int getTableNumber() {
        return totalTables;
    }

    @SuppressWarnings("unchecked")
    private void removeFalseTables(objects.PPT ppt) {
        //System.out.println("removing empty tables: ");

        for (objects.Slide slide : ppt.getSlides()) {
            ArrayList<objects.Table> toRemove = new ArrayList();
            for (objects.PPTObject obj : slide.getAllPptObjects()) {

                if (obj instanceof objects.Table) {
                    objects.Table tabel = (objects.Table) obj;
                    //System.out.println("tabel met: "+tabel.getRows().size() + " rijen");
                    //System.out.println(tabel.getContent().trim());
                    if (tabel.getRows() != null && tabel.getRows().get(0).getCells().get(0).getContent().contains("•")) {
                        //System.out.println("lijsttabel :o");
                        toRemove.add(tabel);
                        totalTables--;
                    }

                }
            }
            for (objects.Table t : toRemove) {
                slide.getPptObjects().remove(t);
            }

        }
    }

}
