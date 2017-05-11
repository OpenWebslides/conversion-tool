/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.powerpoint;

import conversion.powerpoint.sax.PowerpointHandler;
import conversion.powerpoint.util.InsightHandler;
import conversion.powerpoint.util.MediaHandler;
import conversion.powerpoint.util.TextHandler;
import conversion.powerpoint.util.GarbageHandler;
import conversion.IConverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipOutputStream;
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
public class PPTConverter implements IConverter {

    private final File file;
    private Output output;
    
    private long startTime;

    /**
     * Create a PPTConverter instance
     *
     * @param file File
     */
    public PPTConverter(File file) {
        this.file = file;
        output = new StdOutput();
    }

    /**
     * Set the output of the saveLocation
     *
     * @param output Output
     */
    @Override
    public void setOutput(Output output) {
        this.output = output;
    }

    /**
     * Parse the file to PPT, save media to savelocation
     *
     * @param ppt PPT
     * @param saveLocation String
     */
    @Override
    public void parse(PPT ppt, String saveLocation) {
        parse(ppt, null, saveLocation);
    }

    /**
     * Parse the file to PPT, save media to savelocation in zipoutputstream
     *
     * @param ppt PPT
     * @param zip ZipOutputStream
     * @param saveLocation String
     */
    @Override
    public void parse(PPT ppt, ZipOutputStream zip, String saveLocation) {
        XMLSlideShow pptSource;
        startTime = new Date().getTime();
        try {
            pptSource = new XMLSlideShow(new FileInputStream(file));
            List<XSLFSlide> slides = pptSource.getSlides();

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser sp = factory.newSAXParser();
            DefaultHandler handler;

           for (XSLFSlide slide : slides) {
               // XSLFSlide slide = slides.get(13);
                try {
                    //output.println("+++++++++++++ Slide " + slides.indexOf(slide) + " +++++++++++++");
                    //Webslide object
                    Slide webslide = new Slide();

                    String xml = slide.getXmlObject().getCSld().getSpTree().toString();
                                        
                    //for testing
                    output.println(xml);
                    
                    //handler that will parse the xml data
                    handler = new PowerpointHandler(webslide, output);

                    //parse
                    sp.parse(new InputSource(new StringReader(xml)), handler);

                    //Handle media: video, image, hyperlink, chart
                    MediaHandler.handle(slide, webslide.getPptObjects(), saveLocation, file, output, zip);

                    //remove null values from list that got there thanks to irregularities in xml
                    GarbageHandler.handle(webslide.getPptObjects(), output);
                    
                    //Add spaces when necessary between textparts, change list structure
                    TextHandler.handle(webslide.getPptObjects());

                    //Generate insights for the ppt, amount of (different/non-different) words, amount of objects,...
                    //OPTIONAL
                    //Adds 0-0.3s (the bigger the ppt the longer, 0.3s for 200mb ppt) to conversion proces
                    InsightHandler.handle(ppt.getInsight(), webslide.getPptObjects());

                    //print the slide for testing toString details
                    output.println("------------ toString -------------");
                  output.println(webslide.toString());

                    //print the slide for testing getContent
                  output.println("------------ getContent -------------");
                  output.println(webslide.getContent());

                    //Add to ppt
                    ppt.getSlides().add(webslide);
                        
                    // output.println("");
                } catch (Exception e) {
                    output.println(Logger.error("Error while parsing slide + " + slides.indexOf(slide) + 1 + " in the powerpoint", e));
                }
            }

            pptSource.close();

        } catch (Exception ex) {
            throw new IllegalArgumentException("This is probably not a valid powerpoint file");
        }
        ppt.getInsight().setConvertTime(new Date().getTime() - startTime);
    }

    


}
