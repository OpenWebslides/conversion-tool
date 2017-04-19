/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.pdf.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import objects.PPTObject;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;

/**
 * TextExtractor overrides certain methodes from PDFTextStripper in order to put information in the arrayList of PPTObjects objecten.
 * @author Gertjan
 */
public class PDFTextExtractor extends PDFTextStripper {

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

    @Override
    public void processStream(PDPage aPage, PDResources resources, COSStream cosStream) throws IOException {
        super.processStream(aPage, resources, cosStream); //To change body of generated methods, choose Tools | Templates.
        //en dan een persoonlijke naverwerking:
        objecten.add(Inf2Obj.convert(woord));
        woord = new ArrayList<>();
    }

    public PDFTextExtractor() throws IOException {
        super.setSortByPosition(true);
       
    }

    @Override
    protected void processTextPosition(TextPosition text) {
        /*deze methode haalt letters met info uit de text... hier gaan we de tekst opvangen 
         en opslaan in woorden, de woorden hebben een fontsize en een schaal... aan de hand van de 
         coordinaten xdir en ydir wordt de lijn en inspringing bepaald... */

        /*Het idee is om tekstdelen terug te geven uiteindelijk want dat heb je nodig
         Daarom is er de klasse tekstpartMaker die een enorme string van char's met hun info binnen krijgt
         -> char's met info = nieuwe klasse!! 
         */
        teken.setTeken(text.getCharacter());
        teken.setFontsize(text.getFontSize());
        teken.setHeigt(text.getHeightDir());
        teken.setPosX(text.getXDirAdj());
        teken.setPosY(text.getYDirAdj());
        teken.setSpace(text.getWidthOfSpace());
        teken.setXscale(text.getXScale());
        
        //System.out.println(text.getFont().getFontDescriptor());
         if (text.getFont().getBaseFont().toLowerCase().contains("bold")){
            teken.setBold(true);
            // System.out.println("made bold!");
         }
        if (text.getFont().getBaseFont().toLowerCase().contains("italic")){
            teken.setItalic(true);
           // System.out.println("made italic!");
        }
        /*enum bepalen -> kijken watvoor letter het is gebeurt hier al!!!*/
        if (text.getCharacter().matches("[A-Za-z]")) {
            //System.out.println("letter gevonden!!!" + text.getCharacter());
            teken.setType(TekenPlus.type.LETTER);
        } else if (text.getCharacter().matches("[0-9]")) {
            //System.out.println("---cijfer gevonden!!!" + text.getCharacter());
            teken.setType(TekenPlus.type.CIJFER);
        } else if(text.getCharacter().matches("[--?()., ]")) { //OPM laat die - vanvoor staan anders klopt regex niet!!!
            //System.out.println("+++niet splitsend symbool" + text.getCharacter());
            teken.setType(TekenPlus.type.NONSPLITTING);
        } 
        /*BELANGRIJKE OPM: een spatie is geen split teken... een spatie houdt meestal dingen gewoon samen...*/
        else {
            //System.out.println(">>>onbekend split teken gevonden" + text.getCharacter());
            teken.setType(TekenPlus.type.SPLITTING);
        }

        //teken.schrijfPlus();

        /*
         System.out.println("Methode die info over tekst eruit haalt");
         System.out.println("String[" + text.getXDirAdj() + ","
         + text.getYDirAdj() + " fs=" + text.getFontSize() + " xscale="
         + text.getXScale() + " height=" + text.getHeightDir() + " space="
         + text.getWidthOfSpace() + " width="
         + text.getWidthDirAdj() + "]" + text.getCharacter());
         */
        //een korte commercial break om ons teken nu ook hogerop te krijgen!
        verwerkTeken(teken);

    }

    private void verwerkTeken(TekenPlus teken) {
        /*Het idee is als volgt, indien het teken de zelfde eigenschappen heeft als het vorige dan wordt het
         erbij gepusht in een tijdelijk tekstdeel, is het anders dan wordt het in een nieuw tekstdeel gezet*/
        /*
         tekstdeel moet vervolgens nog tekst worden...  OPM tekst wordt niet gesplitst op basis van spaties!!
         */
        //System.out.println("te verwerken teken: " + teken.getTeken());
        if (isNewWord) {
          //  System.out.println("gekozen voor : new word optie");
            woord = new ArrayList<>();
            isNewWord = false;
        }
        //anders gewoon verder schrijven aan je arraylist...
        /*Hier wordt onderzocht welke soort delimiter je hebt, is het een ander teken dan gewone tekst dan splits je daarop ook...*/
        if (teken.getType() == TekenPlus.type.SPLITTING) {
            //System.out.println("gekozen voor: splitting optie");
           // System.out.println("SPLITTING teken");
            objecten.add(Inf2Obj.convert(woord));
            woord = new ArrayList<>();
            }
        
        if(!woord.isEmpty() && !tekenControle(teken)){ //doet hetzelfde als kijken of het splitting is! 
            //controle op leeg zijn anders nullpointers!!
            //System.out.println("INFO splitting");
            objecten.add(Inf2Obj.convert(woord));
            woord = new ArrayList<>();
            }
        
        //even een kopie nemen, hij doet iets vreemd anders...
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(teken);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            TekenPlus kopie = (TekenPlus) ois.readObject();

            woord.add(kopie);
        } catch (Exception e) {
            System.out.println("fout bij kopiëren..." + e.getMessage());
        }

    }
    /*
    Teken controle is een functie die kijkt of het teken er wel nog bij mag bij het zelfde woord...
    geeft false terug indien er een soort splitting event optreed... (ander lettertype ofzo...)
    */
    private boolean tekenControle(TekenPlus teken){
        
        TekenPlus vorigeLetter = woord.get(woord.size() - 1); //dit haalt de laatste letter eraf...
        //System.out.print("De vorige letter was: ");
        //vorigeLetter.schrijfPlus();
        //opm = xpos is dat hori of verti? (programma assen of menselijker... :p
        if(vorigeLetter.getPosY() != teken.getPosY()){
            //System.out.println("split op PosY!");
            return false;
            
        }
        if(vorigeLetter.getFontsize() != teken.getFontsize()){
            //System.out.println("split op FontSize");
            return false;
        }
        
        
        return true;
    }

}
