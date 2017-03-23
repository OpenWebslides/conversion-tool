/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.powerpoint;


import java.util.HashMap;
import java.util.List;
import logger.Logger;
import objects.*;
import output.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;

/**
 *
 * @author Karel
 */
public class PowerpointHandler extends DefaultHandler {
    
    //Variables from constructor
    private final List<PPTObject> pptobjects;
    private final Output output;
    
    //Variables text
    private Text text;
    private Textpart textpart;
    private boolean textpartContent = false;
    
    //Variables optimalization of if statements in startelement
    private boolean textbody = false;
    private boolean imagebody = false;
    
    //Variables list
    private PPTList list;
    private HashMap<Integer, PPTList> lists;
    private int previousLevel;
    
    //Variables image
    private Image image;
    private boolean imagesize;
    
    
    PowerpointHandler(List<PPTObject> pptobjects, Output output) {
        this.pptobjects = pptobjects;
        this.output = output;
    }
    
    @Override
    /**
     * The implementation of startElement, to keep it organized it has been split it startText, startImage,..
     */
    public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
        if(qName.equals(PPTXMLConstants.TEXTBODY)){
            textbody = true;
            imagebody = false;
        }else if(qName.equals(PPTXMLConstants.IMAGEBODY)){
            imagebody = true;
            textbody = false;
        }
        if(textbody)
            startText(qName, attributes);
        else if (imagebody)
            startImage(qName, attributes);
    }
    
    @Override
    /**
     * The implementation of endElement, to keep it organized it has been split it endText, endImage,..
     */
    public void endElement(String uri, String localName, String qName) throws SAXException {
        
        endText(qName);
        endImage(qName);
        
    }

    @Override
    /**
     * The implementation of characters
     */
    public void characters(char ch[], int start, int length) throws SAXException {
        try{
            if(textpartContent){
                textpart.setContent(new String(ch, start, length));
                textpartContent = false;
            }
        }
        catch (Exception e){
            output.println(Logger.error("Error while reading slide text data (DefaultHandler characters)", e));
        }
    }

    /**
     * Check the tags that have something to do with text -start
     * @param qName
     * @param attributes 
     */
    private void startText(String qName, Attributes attributes) {
         try{
             switch (qName) {
                 case PPTXMLConstants.TEXT:
                     text = new Text();
                     break;
                 case PPTXMLConstants.TEXTLEVEL:
                     if(attributes.getValue(PPTXMLConstants.LEVEL)!=null){
                         int level = Integer.parseInt(attributes.getValue(PPTXMLConstants.LEVEL));
                         text.setLevel(attributes.getValue(PPTXMLConstants.LEVEL));
                         startList(level);
                     }
                     else{
                        text.setLevel("0");
                         startList(0);
                     }    break;
                 case PPTXMLConstants.TEXTPART:
                     textpart = new Textpart();
                     break;
                 case PPTXMLConstants.TEXTDETAILS:
                     if(attributes.getValue(PPTXMLConstants.BOLD)!=null)
                         textpart.addType(FontDecoration.BOLD);
                     if(attributes.getValue(PPTXMLConstants.ITALIC)!=null)
                         textpart.addType(FontDecoration.ITALIC);
                     if(attributes.getValue(PPTXMLConstants.STRIKE)!=null)
                         textpart.addType(FontDecoration.STRIKETHROUH);
                     if(attributes.getValue(PPTXMLConstants.UNDERLINE)!=null)
                         textpart.addType(FontDecoration.UNDERLINE);
                     if(attributes.getValue(PPTXMLConstants.SIZE)!=null)
                         textpart.setSize(Integer.parseInt(attributes.getValue(PPTXMLConstants.SIZE)));
                     if(attributes.getValue(PPTXMLConstants.CHARACTERSPACING)!=null)
                         textpart.setSize(Integer.parseInt(attributes.getValue(PPTXMLConstants.CHARACTERSPACING)));
                     break;
                 case PPTXMLConstants.TEXTCONTENT:
                     textpartContent = true;
                     break;
                 case PPTXMLConstants.TEXTFONT:
                     if(attributes.getValue(PPTXMLConstants.TYPEFACE)!=null && textpart!=null)
                         textpart.setFont(attributes.getValue(PPTXMLConstants.TYPEFACE));
                     break;
                 case PPTXMLConstants.COLORDETAIL:
                     if(textpart!=null)
                        textpart.setColor(attributes.getValue(PPTXMLConstants.VALUE));
                     break;
                 default:
                     break;
             }
            
        }
        catch (Exception e){
            output.println(Logger.error("Error while reading slide text tags (DefaultHandler startElement)", e));
        }
    }

    /**
     * Check the tags that have something to do with text -end
     * @param qName 
     */
    private void endText(String qName) {
        try{
        switch (qName) {
            case PPTXMLConstants.TEXTPART:
                text.addTextdeel(textpart);
                break;
            case PPTXMLConstants.TEXT:
                if(list!=null && text.getLevel()==null){
                    pptobjects.add(list);
                    pptobjects.add(text);
                    list = null;
                }
                else if(list==null && text.getLevel()==null){
                    pptobjects.add(text);
                    text = null;
                }    break;
            case PPTXMLConstants.FRAGMENT:
                if(text!=null){
                    pptobjects.add(text);
                }   if(list!=null){
                    pptobjects.add(lists.get(0));
                }   break;
            default:
                break;
        }
        }
        catch(Exception e){
            output.println(Logger.error("Error while reading slide text tags (DefaultHandler endElement)", e));
        }
    }

    /**
     * Make the lists we read
     * @param level 
     */
    private void startList(int level) {
        try{
            if(list==null){
                list = new PPTList();
                lists = new HashMap<>();
                lists.put(level, list);
            }
            else if(level>previousLevel){
                PPTList list2 = new PPTList();
                list.addPPTObject(list2);
                list = list2;
                lists.put(level, list);
            }
            else if(level<previousLevel){
                list = lists.get(level);
                if(list==null){
                    list = new PPTList();
                    lists.put(level, list);
                }
            }
            list.addPPTObject(text);
            previousLevel = level;
        }
        catch(Exception e){
            output.println(Logger.error("Error while creating lists from extracted data", e));
        }
    }

    /**
     * Check the tags that have something to do with images -start
     * @param qName
     * @param attributes 
     */
    private void startImage(String qName, Attributes attributes) {
        try{
            if(qName.equals(PPTXMLConstants.IMAGEDETAILS)){
                image = new Image();
                image.setId(attributes.getValue(PPTXMLConstants.ID));
            }
            else if(qName.equals(PPTXMLConstants.IMAGESIZE)&&imagesize==true){
                int width = Integer.parseInt(attributes.getValue(PPTXMLConstants.IMAGEWIDTH))/360000;
                int height = Integer.parseInt(attributes.getValue(PPTXMLConstants.IMAGEHEIGHT))/360000;
                image.getDimension().setSize(width, height);
            }else if(qName.equals(PPTXMLConstants.IMAGELOCATION)&&imagesize==true){
                int offsetX = Integer.parseInt(attributes.getValue(PPTXMLConstants.IMAGELOCX))/360000;
                int offsetY = Integer.parseInt(attributes.getValue(PPTXMLConstants.IMAGELOCY))/360000;
                image.getLocation().setSize(offsetX, offsetY);
                imagesize = false;
            }else if(qName.equals(PPTXMLConstants.IMAGEBOX)){
                imagesize = true;
            }else if(qName.equals(PPTXMLConstants.IMAGEDETAILS)){
            }
        }catch(Exception e){
            output.println(Logger.error("Error while reading slide image tags (DefaultHandler startElement)", e));
        }
    }
    /**
     * Check the tags that have something to do with images -end
     * @param qName 
     */
    private void endImage(String qName) {
        try{
            if(qName.equals(PPTXMLConstants.IMAGEBOX)){
                pptobjects.add(image);
                image = null;
            }
        }catch(Exception e){
            output.println(Logger.error("Error while reading slide image tags (DefaultHandler startElement)", e));
        }
    }

}
