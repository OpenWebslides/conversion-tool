/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import objects.*;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
import org.openxmlformats.schemas.presentationml.x2006.main.CTShape;

/**
 *
 * @author Karel
 */
public class PPTConverter implements IConverter{
    
    private File file;
    
    public PPTConverter(File file){
        this.file = file;   
    }

    public void parse(PPT ppt) {
        XMLSlideShow pptSource;
        try {
            pptSource = new XMLSlideShow(new FileInputStream(file));
            List<XSLFSlide> slides = pptSource.getSlides();
            int slideNr = 0;
            for(XSLFSlide slide : slides){ //XSLFSlide slide = slides.get(2);
                    Slide webslide = new Slide();
                    
                    webslide.addPPTObject(getTitel(slide));
                    
                    webslide.addPPTObject(getContent(slide));
                    
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
        if(slide.getTitle() != null) return new Titel(slide.getTitle());
        List<XSLFShape> shapes = slide.getShapes();
        for(XSLFShape shape : shapes){
            if(shape.getClass().equals(XSLFTextShape.class)){
                return new Titel(((XSLFTextShape)shape).getText());
            }
        }
        return new Titel("");
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
        Lijst slideLijst = new Lijst();
        slide.getXmlObject().getCSld().getSpTree().getSpList().forEach((shape) -> {
            slideLijst.addPPTObject(extractLijst(shape.getTxBody().getPArray()));
        });
        return slideLijst;
    }

    /**
     * Extract list from block
     * @param pArray
     * @return 
     */
    private PPTObject extractLijst(CTTextParagraph[] pArray) {
        /*
            Een blok bevat een lijst -> iedere lijst is een item in lijst
            Overloop iedere blok en haal er al zijn lijsten uit
        */
        Lijst blokLijst = new Lijst();
        ArrayList<Lijst> currentList = new ArrayList<>();
        currentList.add(blokLijst);
        HashMap<Integer, String> temp = new HashMap<>();
        int lastLevel = 0;
        for (CTTextParagraph pArray1 : pArray) {
         //   System.out.println(pArray1);
            evaluateBlock(temp, pArray1.toString().split("\r"));
            addToList(temp, lastLevel, currentList);
            lastLevel = (int)temp.keySet().toArray()[0];
            temp = new HashMap<>();
        }
        return blokLijst;
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
            String tmp = line;
            if (line.contains("<a:pPr") && line.contains("lvl=")) {
                System.out.println(tmp);
                System.out.println(tmp.substring(tmp.indexOf("lvl=")+5,tmp.indexOf("lvl=")+6));
                level = Integer.parseInt(tmp.substring(tmp.indexOf("lvl=")+5,tmp.indexOf("lvl=")+6));
              //  level = Integer.parseInt(tmp.substring(k1,k2));
            } else if (line.contains("<a:t>")) {
              //  System.out.println(line);
                text += tmp.substring(5, tmp.length() - 6);
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
    private void addToList(HashMap<Integer, String> temp, int lastLevel, ArrayList<Lijst> currentList) {
        /*
            Maak een nieuw lijstobject en steek hierin een bullet met de tekstvalue
            Plaats de lijst in zijn juiste level
        */
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
    
}
