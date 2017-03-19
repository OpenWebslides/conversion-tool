/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import objects.*;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.xslf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextLineBreak;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
        
/**
 *
 * @author Karel
 */
public class PPTConverter implements IConverter{
    
    private final File file;
    
    public PPTConverter(File file){
        this.file = file;   
    }

    /**
     * Parse the file to the given ppt
     * @param ppt 
     */
    @Override
    public void parse(PPT ppt) {
        XMLSlideShow pptSource;
        try {
            pptSource = new XMLSlideShow(new FileInputStream(file));
            List<XSLFSlide> slides = pptSource.getSlides();
            int slideNr = 0;
            loadPictures(pptSource);
            for(XSLFSlide slide : slides){ //XSLFSlide slide = slides.get(13);
                    Slide webslide = new Slide();
                    
                  //  webslide.addPPTObject(getTitel(slide));
                    
                   webslide.addPPTObject(getContent(slide));
                    
                  //  webslide.addPPTObject(getImages(slide));
                    
                  //  webslide.addPPTObject(getCharts(slide));
                    
                    webslide.setSlideNr(slideNr);
                    
                    ppt.getSlides().add(webslide);
                    
                    slideNr++;
            }
            pptSource.close();
        } catch (Exception ex) {
            Logger.getLogger(PPTConverter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Get title from a slide. If there is no title object, take the first textitem. If there is no first textitem return an empty Titel
     * @param slide
     * @return 
     */
    private PPTObject getTitel(XSLFSlide slide) {
        if(slide.getTitle() != null){
            return new Title(slide.getTitle());
        }
        return null;
    }

    /**
     * Get the rest of the content from the slide. The rest of the content will be collected in a lijst
     * @param slide
     * @return 
     */
    private PPTObject getContent(XSLFSlide slide) {
        /*
            Een slide bevat meerdere blokken -> iedere blok is een item in slideLijst
            Overloop iedere shape in een lijst, iedere shape komt overeen met een 'kader' in PowerPoint
        */
        PPTList slideLijst = new PPTList();
        slide.getXmlObject().getCSld().getSpTree().getSpList().forEach((shape) -> {
            extractLijst(shape.getTxBody().getPArray());
        });
        return slideLijst;
    }

    /**
     * Extract list from block
     * @param pArray
     * @return 
     */
    private ArrayList<ArrayList<Text>>  extractLijst(CTTextParagraph[] pArray) {
        /*
            Een blok bevat een lijst -> iedere lijst is een item in lijst
            Overloop iedere blok en haal er al zijn lijsten uit
        */
      //  System.out.println(Arrays.toString(pArray))
       // Lijst blokLijst = new Lijst();
       // ArrayList<Lijst> currentList = new ArrayList<>();
       // currentList.add(blokLijst);
        //HashMap<Integer, String> temp = new HashMap<>();
       // int lastLevel = 0;
       ArrayList<ArrayList<Text>> slideData = new ArrayList<>();
        for (CTTextParagraph pArray1 : pArray) {
            System.out.println(pArray1);
            //evaluateBlock(temp, pArray1.toString().split("\r"));
            //addToList(temp, lastLevel, currentList);
            ArrayList<Text> tekstobj = extractData(pArray1.toString());
          //  lastLevel = (int)temp.keySet().toArray()[0];
            //temp = new HashMap<>();
            slideData.add(tekstobj);
        }
        return slideData;
    }
    
    private ArrayList<Text> extractData(String lines){
        try {
            
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser sp = factory.newSAXParser();
            
            ArrayList<Text> td = new ArrayList<>();
            DefaultHandler handler = new TekstHandler(td);
            
            sp.parse(new InputSource(new StringReader(lines)), handler);
            
            for(Text t : td){
                System.out.println(t.toString());
            }
            return td;
            
      /*      System.out.println(lines);
            System.out.println(td.size()); */
        } catch (Exception ex) {
            Logger.getLogger(PPTConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Extract items from list
     * @param map
     * @param lines 
     */
    private void evaluateBlock(HashMap<Integer, String> map, String[] lines) {
        /*
            Een lijst bevat ev. meerdere lijsten 
            haal de info per lijst eruit (a:pPr is het level van lijst, a:t is de tekst)
        */
        int level = 0;
        String text = "";
        
        for (String line : lines) {
            String tmp = line.replaceFirst("^\\s*", "");
            if (tmp.contains("<a:pPr") && tmp.contains("lvl=")) {
             //  System.out.println(tmp);
               /* System.out.println(tmp.substring(tmp.indexOf("lvl=")+5,tmp.indexOf("lvl=")+6));*/
                level = Integer.parseInt(tmp.substring(tmp.indexOf("lvl=")+5,tmp.indexOf("lvl=")+6));
              //  level = Integer.parseInt(tmp.substring(k1,k2));
            } else if (tmp.contains("<a:t>")) {
              // System.out.println(tmp);
                text += tmp.substring(5, tmp.length() - 6) + " ";
            } 
        }
             map.put(level,text);
        
    }

    /**
     * Add bullet to the correct list
     * @param lijst
     * @param temp
     * @param lastLevel
     * @param currentList 
     */
    /*private void addToList(HashMap<Integer, String> temp, int lastLevel, ArrayList<Lijst> currentList) {
        
         //   Maak een nieuw lijstobject en steek hierin een bullet met de tekstvalue
         //   Plaats de lijst in zijn juiste level
        
        try{
            Lijst lst = new Lijst();
            lst.addPPTObject(new Bullet((String)temp.values().toArray()[0]));
            if(lastLevel < (int)temp.keySet().toArray()[0]){
                    Lijst l1 = currentList.get(currentList.size()-1);
                    Lijst l1b = (Lijst) l1.getBullets().get(l1.getBullets().size()-1);
                    currentList.add(l1b);

    //currentList.add(((Lijst)currentList.get(currentList.size()-1).getBullets().get(currentList.get(currentList.size()-1).getBullets().size()-1)));
            }
            else if(lastLevel > (int)temp.keySet().toArray()[0]){
                for(int i = 0; i < lastLevel - (int)temp.keySet().toArray()[0];i++)
                    currentList.remove(currentList.size()-1);
            }

            currentList.get(currentList.size()-1).addPPTObject(lst);
        }
        catch(ArrayIndexOutOfBoundsException e){
                
         }
    }
*/
    
    private PPTObject getImages(XSLFSlide slide) {
        PPTList l = new PPTList();
        System.out.println(slide.getXmlObject().getCSld().getSpTree());
        for(XSLFShape sh : slide.getShapes()){
            //size van image is aantal cm * 360000
            //System.out.println(sh.getShapeName() + " " +sh.getShapeId());
            if(sh.getShapeName().contains("Picture")){
                //System.out.println(((XSLFPictureShape) sh).getPictureData().getFileName());
                
                l.addPPTObject(new Image(((XSLFPictureShape) sh).getPictureData().getFileName(),((XSLFPictureShape) sh).getPictureData().getImageDimension()));
            }
        }
        return l;
    }
    

    private void loadPictures(XMLSlideShow ppt) {
        
        for(XSLFPictureData data : ppt.getPictureData()){
         
         byte[] bytes = data.getData();
         String fileName = data.getFileName();
       //  System.out.println("picture name: " + fileName);
         InputStream in = new ByteArrayInputStream(bytes);
	 BufferedImage bImageFromConvert = null;
            try {
                bImageFromConvert = ImageIO.read(in);
            } catch (IOException ex) {
                Logger.getLogger(PPTConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                ImageIO.write(bImageFromConvert, "jpg", new File("c:\\temp\\"+fileName));
            } catch (Exception ex) {
                Logger.getLogger(PPTConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }	    
    }

    private PPTObject getCharts(XSLFSlide slide) {
        PPTList l = new PPTList();
        XSLFChart chart = null; 
        
        for(POIXMLDocumentPart part : slide.getRelations()){
            if(part instanceof XSLFChart){
                chart = (XSLFChart) part;
                break;
            }
        } 
        if(chart == null) return l;
        
        try{
            CTChart ctChart = chart.getCTChart();
            //System.out.println(ctChart);
            Chart c = new Chart();
            c.setTitle(getChartTitle(ctChart));
            c.setChartType(getChartType(ctChart));
            addChartContent(c.getChartType(), ctChart);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        return null;
    }

    private String getChartTitle(CTChart ctChart) {
        String text = "";
        for (String line : ctChart.toString().split("\r")) {
            String tmp = line.replaceFirst("^\\s*", "");
            if (tmp.contains("<a:t>")) {
                text += tmp.substring(5, tmp.length() - 6) + " ";
            } 
        }
        return text;
    }

    private String getChartType(CTChart ctChart) {
        String text = "";
        String[] lines = ctChart.toString().split("\r");
        for (int i = 0; i < lines.length; i++) {
            String tmp = lines[i].replaceFirst("^\\s*", "");
            if (tmp.contains("<c:plotArea>")) {
                tmp = lines[i+2].replaceFirst("^\\s*", "");
                text += tmp.substring(3, tmp.length() - 1) + " ";
            } 
        }
        return text;
    }

    private void addChartContent(String chartType, CTChart ctChart) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
