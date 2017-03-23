/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.powerpoint;

import conversion.IConverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;
import objects.*;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import output.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import logger.Logger;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Karel
 */
public class PPTConverter implements IConverter{

    private final File file;
    private Output output;
    
    public PPTConverter(File file){
        this.file = file;   
        output = new StdOutput();
    }

    @Override
    public void setOutput(Output output){
        this.output = output;
    }
    
    @Override
    public void parse(PPT ppt, String saveLocation){
        
        XMLSlideShow pptSource;
        
        try {
            pptSource = new XMLSlideShow(new FileInputStream(file));
            List<XSLFSlide> slides = pptSource.getSlides();
            
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser sp = factory.newSAXParser();        
            DefaultHandler handler;
            
            for(XSLFSlide slide : slides){
                    try{
                        //Webslide object
                        Slide webslide = new Slide();

                        //for testing
                        //output.println(slide.getXmlObject().getCSld().getSpTree().toString());

                        //handler that will parse the xml data
                        handler = new PowerpointHandler(webslide.getPptObjects(),output);

                        //parse
                        sp.parse(new InputSource(new StringReader(slide.getXmlObject().getCSld().getSpTree().toString())), handler);

                        //remove null values from list that got there thanks to irregularities in xml

                        GarbageHandler.handle(webslide.getPptObjects(), output);
                        
                        //copy the images
                        MediaHandler.handle(slide, webslide.getPptObjects(), saveLocation, file,output);

                        //print the slide for testing
                        output.println(webslide.toString());

                        output.println("");
                    }catch(Exception e){
                        output.println(Logger.error("Error while parsing slide + " + slides.indexOf(slide)+1 + " in the powerpoint", e));
                    }
            }
            
            pptSource.close();
            
        } catch (Exception ex) {
            output.println(Logger.error("Error while parsing the powerpoint", ex));
        }
    }   
    
}
