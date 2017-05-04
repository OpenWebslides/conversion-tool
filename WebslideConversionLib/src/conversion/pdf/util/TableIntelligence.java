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
import org.apache.pdfbox.pdmodel.PDPage;
import static org.apache.poi.hssf.usermodel.HeaderFooter.file;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.PageIterator;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;
import technology.tabula.TextElement;
import technology.tabula.extractors.BasicExtractionAlgorithm;
import technology.tabula.extractors.ExtractionAlgorithm;

/**
 * This class can be used to check if a textpart could contain a table.
 *
 * @author Gertjan
 */
public class TableIntelligence {

    /*public void checkTables(PPT ppt) {
     for (Slide slide : ppt.getSlides()) {
     List<Integer> toRemove = new ArrayList<>();

     int i = 0;
     while (i < slide.getPptObjects().size()) {
     if (slide.getPptObjects().get(i) != null && slide.getPptObjects().get(i) instanceof Text) {
     Text text = (Text) slide.getPptObjects().get(i);
     //System.out.println("Tekstpart behandeld: " + tp.getContent() + " " + tp.getXPosition() + "," + tp.getYPosition());
     if (isTable(text)) {
     //...
     //make table!
                        
     //add next line to table 
     int ii = i;
     while (ii + 1 < slide.getPptObjects().size() && slide.getPptObjects().get(ii + 1) instanceof Text && isTable((Text) slide.getPptObjects().get(ii + 1))) {
     //zolang het volgende ook nog een tabel kan zijn
                            
                            
     toRemove.add(ii + 1);
     ii++;
     //System.out.println("Tekstpart behandeld: " + tp.getContent() + " " + tp.getXPosition() + "," + tp.getYPosition());

     }

     //slide.getPptObjects().set(i, text); !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     i = ii + 1;
     //System.out.println(i);
     } else {
     //not a table... 
     i++;
     //System.out.println(i);
     }
     } else {
     //no text
     i++;
     }
     }
     Object[] keys = toRemove.toArray();
     for (int j = keys.length - 1; j >= 0; j--) {
     slide.getPptObjects().remove((int) keys[j]);
     }
     }
     }
     */
    /*
    private boolean isTable(Text text) {
        boolean isTabel = false;

        //table logic here
        ArrayList<Textpart> parts = text.getTextparts();
        for (Textpart part : parts) {
            // System.out.println("found part on posX: "+ part.getXPosition());
        }

        //System.out.println("return");
        return isTabel;
    }
    */

    public ArrayList<Pair<Integer,objects.Table>> extractTables(PDDocument d) {
        ObjectExtractor oe;
        int pagenr = -1;
        ArrayList<Pair<Integer,objects.Table>> tabellen = new ArrayList();
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
                                rij.getCells().add(new objects.Cell(cell.getText(), 0,0));
                            }
                            tabel.getRows().add(rij);
                        }
                        else if(growing = true){
                        //hij was aan het groeien maar nu heb je dingen te kort -> tabel afsluiten
                           tabellen.add(new Pair(pagenr,tabel));
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
    
    public void placeTables(PPT ppt, ArrayList<Pair<Integer, objects.Table>> tab){
        
    }
    
}
