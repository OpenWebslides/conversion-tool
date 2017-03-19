/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
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
    private String saveLocation;
    
    public PPTConverter(File file){
        this.file = file;   
    }

    /**
     * Parse the file to the given ppt
     * @param ppt 
     */
    @Override
    public void parse(PPT ppt, String saveLocation) {
        this.saveLocation = saveLocation;
        XMLSlideShow pptSource;
        try {
            pptSource = new XMLSlideShow(new FileInputStream(file));
            List<XSLFSlide> slides = pptSource.getSlides();
            for(XSLFSlide slide : slides){ //XSLFSlide slide = slides.get(13);
                    Slide webslide = new Slide();
                    
                    webslide.addPPTObject(getTitel(slide));
                    webslide.addPPTObjects(getContent(slide));
                    
                    webslide.addPPTObject(getImages(slide));
                    
                  //  webslide.addPPTObject(getCharts(slide));
                    
                    ppt.getSlides().add(webslide);
                    
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
     * Return a 
     * @param slide
     * @return 
     */
    private ArrayList<Text> getContent(XSLFSlide slide) {
        /*
            Een slide bevat meerdere blokken -> iedere blok is een item in slideLijst
            Overloop iedere shape in een lijst, iedere shape komt overeen met een 'kader' in PowerPoint
        */
        ArrayList<Text> slideLijst = new ArrayList<>();
        slide.getXmlObject().getCSld().getSpTree().getSpList().forEach((shape) -> {
            slideLijst.addAll(extractTextPerSlide(shape.getTxBody().getPArray()));
        });
        return slideLijst;
    }

    /**
     * Extract list from block
     * @param pArray
     * @return 
     */
    private ArrayList<Text>  extractTextPerSlide(CTTextParagraph[] pArray) {
        /*
            Een blok bevat een lijst -> iedere lijst is een item in lijst
            Overloop iedere blok en haal er al zijn lijsten uit
        */
       ArrayList<Text> slideData = new ArrayList<>();
        for (CTTextParagraph pArray1 : pArray) {
            Text tekstobj = extractData(pArray1.toString());
            slideData.add(tekstobj);
        }
        return slideData;
    }
    
    private Text extractData(String lines){
        try {
            Text td = new Text();
            
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser sp = factory.newSAXParser();        
            DefaultHandler handler = new TekstHandler(td);
            sp.parse(new InputSource(new StringReader(lines)), handler);
                      
            return td;
        } catch (Exception ex) {
            Logger.getLogger(PPTConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
        
    private PPTObject getImages(XSLFSlide slide) {
        PPTList l = new PPTList();
      //  System.out.println(slide.getXmlObject().getCSld().getSpTree());
        for(XSLFShape sh : slide.getShapes()){
            //size van image is aantal cm * 360000
            //System.out.println(sh.getShapeName() + " " +sh.getShapeId());
            if(sh.getShapeName().contains("Picture")){
                String name = ((XSLFPictureShape) sh).getPictureData().getFileName();
                try{
                File f = new File(saveLocation + "\\img\\" + name);
                f.getParentFile().mkdirs();
                OutputStream out = new FileOutputStream(f);
                FileInputStream fin = new FileInputStream(file.getAbsoluteFile());
                BufferedInputStream bin = new BufferedInputStream(fin);
                ZipInputStream zin = new ZipInputStream(bin);
                ZipEntry ze = null;
                while ((ze = zin.getNextEntry()) != null) {
                    if (ze.getName().equals("ppt/media/"+name)) {
                        byte[] buffer = new byte[8192];
                        int len;
                        while ((len = zin.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                        }
                        out.close();
                        break;
                    }
                }
                }catch (Exception e){
                    e.printStackTrace();
                }
                l.addPPTObject(new Image(((XSLFPictureShape) sh).getPictureData().getFileName(),((XSLFPictureShape) sh).getPictureData().getImageDimension()));
            }
        }
        return l;
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
