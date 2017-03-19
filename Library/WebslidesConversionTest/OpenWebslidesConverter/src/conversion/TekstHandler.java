/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion;

import java.util.ArrayList;
import objects.FontDecoration;
import objects.Text;
import objects.Textpart;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Karel
 */
public class TekstHandler extends DefaultHandler {

    private Textpart currentTextpart;
    private Text currentText;
    
    private final String TEXT = "xml-fragment";
    private final String TEXTWITHLEVEL = "a:pPr";
    private final String TEXTPARTSTART = "a:r";
    private final String TEXTPARTCONTENT = "a:t";
    private final String TEXTPARTDETAILS = "a:rPr";
    private final String TEXTWITHFONT = "a:latin";
    private final String COLORDETAIL = "a:srgbClr";
    //private final String COLORACCENT = "a:schemeClr"; one of the standard colors
    
    private final String BOLD = "b";
    private final String ITALIC = "i";
    private final String STRIKE = "strike";
    private final String UNDERLINE = "u";
    private final String SIZE = "sz";
    private final String LEVEL = "lvl";
    private final String CHARACTERSPACING = "spc";
    private final String TYPEFACE = "typeface";
    private final String VALUE = "val";
    
    
    
    private boolean teksttitel = false;
    
    
    
    public TekstHandler(Text td) {
        this.currentText = td;
    }
    
    public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
        try{
            if(qName.equals(TEXTWITHLEVEL)){
                if(attributes.getValue(LEVEL)!=null)
                    currentText.setLevel(Integer.parseInt(attributes.getValue(LEVEL)));
            }
            if(qName.equals(TEXTPARTSTART))
                currentTextpart = new Textpart();

            if(qName.equals(TEXTPARTCONTENT)){
                    teksttitel = true;
            }
            if(qName.equals(COLORDETAIL)){
                currentTextpart.setColor(attributes.getValue(VALUE));
            }
            if(qName.equals(TEXTPARTDETAILS)){
                if(attributes.getValue(BOLD)!=null)
                    currentTextpart.addType(FontDecoration.BOLD);
                if(attributes.getValue(ITALIC)!=null)
                    currentTextpart.addType(FontDecoration.ITALIC);
                if(attributes.getValue(STRIKE)!=null)
                    currentTextpart.addType(FontDecoration.STRIKETHROUH);
                if(attributes.getValue(UNDERLINE)!=null)
                    currentTextpart.addType(FontDecoration.UNDERLINE);
                if(attributes.getValue(SIZE)!=null)
                    currentTextpart.setSize(Integer.parseInt(attributes.getValue(SIZE)));
                if(attributes.getValue(CHARACTERSPACING)!=null)
                    currentTextpart.setSize(Integer.parseInt(attributes.getValue(CHARACTERSPACING  )));
            }
            if(qName.equals(TEXTWITHFONT)){
                if(attributes.getValue(TYPEFACE)!=null)
                    currentTextpart.setFont(attributes.getValue(TYPEFACE));
            }
        }
        catch(Exception e)
        {}
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        
        if(qName.equals(TEXTPARTCONTENT)&&currentTextpart != null){
             ////   System.out.println(currentTekstdeel + " added");
                currentText.addTekstdeel(currentTextpart);
        }

    }

    public void characters(char ch[], int start, int length) throws SAXException {
        if(teksttitel){
           try{// System.out.println(currentTekstdeel);
            currentTextpart.setContent(new String(ch, start, length));
          //  System.out.println(currentTekstdeel.getContent());
            teksttitel = false;}catch(Exception e){};
        }

    }
    
}
