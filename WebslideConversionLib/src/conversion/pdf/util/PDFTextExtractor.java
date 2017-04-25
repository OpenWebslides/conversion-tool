/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.pdf.util;

import java.io.IOException;
import java.util.ArrayList;
import objects.PPTObject;

/**
 * TextExtractor extracts text elements from DOM  
 * @author Gertjan
 */
public class PDFTextExtractor {

    private TekenPlus teken = new TekenPlus();
    private ArrayList<TekenPlus> woord = new ArrayList<>();
    private boolean isNewWord = true;
    private ArrayList<PPTObject> objecten = new ArrayList<>();
    private Info2Object Inf2Obj = new Info2Object();
   
    public TekenPlus getTekenPlus() {
        return this.teken;
    }
    /*get geeft niet alleen de objecten terug maar maakt de lijst ook weer leeg, dit is zodat je telkens je ze ophaalt
     je een hele pagina ophaalt... */
    
    public ArrayList<PPTObject> getObjecten() {
        try {
            return objecten;
        } finally {
            objecten = new ArrayList<>();
        }

    }


    public PDFTextExtractor() throws IOException {
        
       
    }

    
}
