/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.powerpoint;

import java.util.Arrays;
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
    private boolean titleheader = false;
    private boolean subtitleheader = false;
    private boolean title = false;
    private boolean defaultsize = false;

    //Variables optimalization of if statements in startelement
    private boolean textbody = false;
    private boolean imagebody = false;
    private boolean gframe;
    private boolean canRead = true;

    //Variables list
    private PPTList list;
    private HashMap<Integer, PPTList> lists;
    private int previousLevel;
    private boolean textAdded;

    //Variables image
    private Media media;
    private boolean imagesize;
    

    //Variables chart
    private Chart chart;

    //
    private String lastId;

    PowerpointHandler(List<PPTObject> pptobjects, Output output) {
        this.pptobjects = pptobjects;
        this.output = output;
    }

    @Override
    /**
     * The implementation of startElement, to keep it organized it has been
     * split it startText, startImage,..
     */
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            if(canRead){
                switch (qName) {
                    case PPTXMLConstants.IDELEMENT:
                        lastId = attributes.getValue(PPTXMLConstants.ID);
                        break;
                    case PPTXMLConstants.TEXTBODY:
                        textbody = true;
                        imagebody = false;
                        break;
                    case PPTXMLConstants.MEDIABODY:
                        imagebody = true;
                        textbody = false;
                        break;
                    case PPTXMLConstants.GFRAME:
                        gframe = true;
                        break;
                    default:
                        break;
                }
                if (textbody) {
                    startText(qName, attributes);
                } else if (imagebody) {
                    startImage(qName, attributes);
                } else if (gframe) {
                    startChart(qName, attributes);
                } else {
                    startRest(qName, attributes);
                }
            }
        } catch (Exception e) {
            output.println(Logger.error("Error while reading slide data (DefaultHandler startElement)", e));
        }
    }

    @Override
    /**
     * The implementation of endElement, to keep it organized it has been split
     * it endText, endImage,..
     */
    public void endElement(String uri, String localName, String qName) throws SAXException {
        try{
            if (textbody) {
                endText(qName);
            } else if (imagebody) {
                endImage(qName);
            } else {
                endRest(qName);
            }
        }
    catch (Exception e){
        output.println(Logger.error("Error while ending xml tags data (DefaultHandler endElement)", e));
    }

    }

    @Override
    /**
     * The implementation of characters
     */
    public void characters(char ch[], int start, int length) throws SAXException {
        try {
            if (textpartContent) {
                if(textpart!=null)
                    textpart.setContent( textpart.getContent() + new String(ch, start, length));
                else
                    textpart.setContent( new String(ch, start, length));
                textpartContent = false;
            }
        } catch (Exception e) {
            output.println(Logger.error("Error while reading slide text data (DefaultHandler characters)", e));
        }
    }

    /**
     * Check the tags that have something to do with text -start
     *
     * @param qName
     * @param attributes
     */
    private void startText(String qName, Attributes attributes) {
        try {
            switch (qName) {
                case PPTXMLConstants.TEXT:
                    text = new Text();
                    textAdded = false;
                    if(defaultsize){
                        text.setLevel("0");
                        startList(0);
                    }
                    break;
                case PPTXMLConstants.UNORDEREDLIST:
                    if (list != null) {
                        list.setOrdered(false);
                    }
                    break;
                case PPTXMLConstants.ORDEREDLIST:
                    if (list != null) {
                        list.setOrdered(true);
                    }
                    break;
                case PPTXMLConstants.TEXTLEVEL:
                    if (attributes.getValue(PPTXMLConstants.LEVEL) != null) {
                        int level = Integer.parseInt(attributes.getValue(PPTXMLConstants.LEVEL));
                        text.setLevel(attributes.getValue(PPTXMLConstants.LEVEL));
                        startList(level);
                    } else {
                        text.setLevel("0");
                        startList(0);
                    }
                    break;
                case PPTXMLConstants.TEXTPART:
                    textpart = new Textpart();
                    break;
                case PPTXMLConstants.TEXTDETAILS:
                    if (attributes.getValue(PPTXMLConstants.BOLD) != null) {
                        textpart.addType(FontDecoration.BOLD);
                    }
                    if (attributes.getValue(PPTXMLConstants.ITALIC) != null) {
                        textpart.addType(FontDecoration.ITALIC);
                    }
                    if (attributes.getValue(PPTXMLConstants.STRIKE) != null) {
                        textpart.addType(FontDecoration.STRIKETHROUH);
                    }
                    if (attributes.getValue(PPTXMLConstants.UNDERLINE) != null) {
                        textpart.addType(FontDecoration.UNDERLINE);
                    }
                    if (attributes.getValue(PPTXMLConstants.SIZE) != null) {
                        textpart.setSize(Integer.parseInt(attributes.getValue(PPTXMLConstants.SIZE)));
                    }
                    if (attributes.getValue(PPTXMLConstants.CHARACTERSPACING) != null) {
                        textpart.setSize(Integer.parseInt(attributes.getValue(PPTXMLConstants.CHARACTERSPACING)));
                    }
                    if (attributes.getValue(PPTXMLConstants.SOLOWORD) == null) {
                        textpart.setContent(" ");
                    }
                    break;
                case PPTXMLConstants.TEXTCONTENT:
                    textpartContent = true;
                    break;
                case PPTXMLConstants.TEXTFONT:
                    if (attributes.getValue(PPTXMLConstants.TYPEFACE) != null && textpart != null) {
                        textpart.setFont(attributes.getValue(PPTXMLConstants.TYPEFACE));
                    }
                    break;
                case PPTXMLConstants.COLORDETAIL:
                    if (textpart != null) {
                        textpart.setColor(attributes.getValue(PPTXMLConstants.VALUE));
                    }
                    break;
                case PPTXMLConstants.TEXTTYPE:
                    if (attributes.getValue(PPTXMLConstants.TEXTTYPEATTR) != null) {
                        if (attributes.getValue(PPTXMLConstants.TEXTTYPEATTR).equals(PPTXMLConstants.TEXT_TITLE_HEADER)) {
                            titleheader = true;
                        } else if (attributes.getValue(PPTXMLConstants.TEXTTYPEATTR).equals(PPTXMLConstants.TEXT_SUBTITLE_HEADER)) {
                            subtitleheader = true;
                        } else if (attributes.getValue(PPTXMLConstants.TEXTTYPEATTR).equals(PPTXMLConstants.TEXT_TITLE)) {
                            title = true;
                        }

                    } else {
                        defaultsize = true;
                    }

                default:
                    break;
            }

        } catch (Exception e) {
            output.println(Logger.error("Error while reading slide text tags (DefaultHandler startElement)", e));
        }
    }

    /**
     * Check the tags that have something to do with text -end
     *
     * @param qName
     */
    private void endText(String qName) {
        try {
            switch (qName) {
                case PPTXMLConstants.TEXTPART:
                    if (textpart.getSize() == 0) {
                        if (titleheader) {
                            textpart.setSize(PPTXMLConstants.TEXT_TITLE_HEADER_SIZE);
                            titleheader = false;
                        } else if (subtitleheader) {
                            textpart.setSize(PPTXMLConstants.TEXT_SUBTITLE_HEADER_SIZE);
                            subtitleheader = false;
                        } else if (title) {
                            textpart.setSize(PPTXMLConstants.TEXT_TITLE_SIZE);
                            title = false;
                        } else if (defaultsize) {
                            textpart.setSize(PPTXMLConstants.TEXT_DEFAULT_LARGE_SIZE);
                            defaultsize = false;
                        } else {
                            textpart.setSize(PPTXMLConstants.TEXT_DEFAULT_SMALL_SIZE);
                        }
                    }
                    text.addTextpart(textpart);
                    break;
                case PPTXMLConstants.TEXT:
                    if (list != null) {
                        if(text.getLevel() == null){
                            pptobjects.add(lists.get(0));
                            pptobjects.add(text);
                            textAdded = true;
                            list = null;
                        }
                    } else if (list == null && text.getLevel() == null) {
                        pptobjects.add(text);
                        textAdded = true;
                        text = null;
                    }
                    textbody = false;
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            output.println(Logger.error("Error while reading slide text tags (DefaultHandler endElement)", e));
        }
    }

    /**
     * Make the lists we read
     *
     * @param level
     */
    private void startList(int level) {
        try {
            if (list == null) {
                list = new PPTList();
                lists = new HashMap<>();
                lists.put(level, list);
            } else if (level > previousLevel) {
                PPTList list2 = new PPTList();
                list.addPPTObject(list2);
                list = list2;
                lists.put(level, list);
            } else if (level < previousLevel) {
                list = lists.get(level);
                if (list == null) {
                    list = new PPTList();
                    lists.put(level, list);
                }
            }
            list.addPPTObject(text);
          //  output.println(Arrays.toString(lists.values().toArray()));
            textAdded = true;
            previousLevel = level;
        } catch (Exception e) {
            output.println(Logger.error("Error while creating lists from extracted data", e));
        }
    }

    /**
     * Check the tags that have something to do with images -start
     *
     * @param qName
     * @param attributes
     */
    private void startImage(String qName, Attributes attributes) {
        try {
            if (qName.equals(PPTXMLConstants.MEDIADETAILS)) {
                media = new Image();
                media.setId(attributes.getValue(PPTXMLConstants.ID));
            } else if(qName.equals(PPTXMLConstants.VIDEOBODY)){
                Video video = new Video();
                video.getDimension().setSize(media.getDimension());
                video.getLocation().setSize(media.getLocation());
                video.setId(media.getId());
                video.setFilename(media.getFilename());
                media = video;
            } else if (qName.equals(PPTXMLConstants.MEDIABOX)) {
                imagesize = true;
            } else if (qName.equals(PPTXMLConstants.MEDIASIZE) && imagesize) {
                int width = Integer.parseInt(attributes.getValue(PPTXMLConstants.MEDIAWIDTH)) / 360000;
                int height = Integer.parseInt(attributes.getValue(PPTXMLConstants.MEDIAHEIGHT)) / 360000;
                media.getDimension().setSize(width, height);
                imagesize = false;
            } else if (qName.equals(PPTXMLConstants.MEDIALOCATION) && imagesize) {
                int offsetX = Integer.parseInt(attributes.getValue(PPTXMLConstants.MEDIALOCX)) / 360000;
                int offsetY = Integer.parseInt(attributes.getValue(PPTXMLConstants.MEDIALOCY)) / 360000;
                media.getLocation().setSize(offsetX, offsetY);
            }
        } catch (Exception e) {
            output.println(Logger.error("Error while reading slide image tags (DefaultHandler startElement)", e));
        }
    }

    /**
     * Check the tags that have something to do with images -end
     *
     * @param qName
     */
    private void endImage(String qName) {
        try {
            if (qName.equals(PPTXMLConstants.MEDIABOX)) {
                pptobjects.add(media);
                media = null;
                imagebody = false;
            }
        } catch (Exception e) {
            output.println(Logger.error("Error while reading slide image tags (DefaultHandler endElement)", e));
        }
    }

    private void startChart(String qName, Attributes attributes) {
        try {
            if (qName.equals(PPTXMLConstants.CHARTBODY)) {
                chart = new Chart(lastId);
                pptobjects.add(chart);
            }

        } catch (Exception e) {
            output.println(Logger.error("Error while reading slide chart tags (DefaultHandler startElement)", e));
        }
    }

    private void endRest(String qName) {
        try {
            if (qName.equals(PPTXMLConstants.TABLE)) {
                pptobjects.add(new Table());
                canRead = true;
            }
            else if (qName.equals(PPTXMLConstants.FRAGMENT)){ 
                    if (text != null && !textAdded) {
                        pptobjects.add(text);
                    }
                    if (list != null) {             
                        pptobjects.add(lists.get(0));
                    }
            }
        } catch (Exception e) {
            output.println(Logger.error("Error while ending slide chart tags (DefaultHandler endElement)", e));
        }
    }

    private void startRest(String qName, Attributes attributes) {
        try {
            switch (qName) {
                case PPTXMLConstants.TEXTTYPE:
                    if (attributes.getValue(PPTXMLConstants.TEXTTYPEATTR) != null) {
                        if (attributes.getValue(PPTXMLConstants.TEXTTYPEATTR).equals(PPTXMLConstants.TEXT_TITLE_HEADER)) {
                            titleheader = true;
                        } else if (attributes.getValue(PPTXMLConstants.TEXTTYPEATTR).equals(PPTXMLConstants.TEXT_SUBTITLE_HEADER)) {
                            subtitleheader = true;
                        } else if (attributes.getValue(PPTXMLConstants.TEXTTYPEATTR).equals(PPTXMLConstants.TEXT_TITLE)) {
                            title = true;
                        }
                    } else {
                        defaultsize = true;
                    }
                    break;
                case PPTXMLConstants.TABLE:
                    canRead = false;
            }
        } catch (Exception e) {
            output.println(Logger.error("Error while ending slide tags (DefaultHandler startElement)", e));
        }
    }

}
