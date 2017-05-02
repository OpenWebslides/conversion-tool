/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.powerpoint.sax;

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
    private Slide pptobjects;
    private Slide previousPptobjects;
    private final Output output;

    //Variables text
    private Text text;
    private Textpart textpart;
    private boolean textpartContent = false;
    private boolean titleheader = false;
    private boolean subtitleheader = false;
    private boolean title = false;
    private boolean defaultsize = false;
    private boolean isList = false;
    private boolean textpartadded;

    //Variables optimalization of if statements in startelement
    private boolean textbody = false;
    private boolean imagebody = false;
    private boolean gframe = false;
    private boolean canRead = true;

    private HashMap<String, Hyperlink> hyperlinks;

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

    public PowerpointHandler(Slide pptobjects, Output output) {
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
            if (canRead) {
                switch (qName) {
                    case PPTXMLConstants.IDELEMENT:
                        lastId = attributes.getValue(PPTXMLConstants.ID);
                        break;
                    case PPTXMLConstants.TEXTBODY:
                        textbody = true;
                        imagebody = false;
                        gframe = false;
                        break;
                    case PPTXMLConstants.MEDIABODY:
                        imagebody = true;
                        textbody = false;
                        gframe = false;
                        break;
                    case PPTXMLConstants.GFRAME:
                        gframe = true;
                        imagebody = false;
                        textbody = false;
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
        try {
            if (textbody) {
                endText(qName);
            } else if (imagebody) {
                endImage(qName);
            } else {
                endRest(qName);
            }
        } catch (Exception e) {
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
                if (textpart != null) {
                    textpart.setContent(textpart.getContent() + new String(ch, start, length).trim());
                }
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
                    if (defaultsize) {
                        isList = true;
                    }
                    break;
                case PPTXMLConstants.NOLIST:
                    isList = false;
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
                case PPTXMLConstants.TEXTLINK:
                    if (hyperlinks == null) {
                        hyperlinks = new HashMap<>();
                    }
                    if (attributes.getValue(PPTXMLConstants.RID) != null) {
                        if (hyperlinks.containsKey(attributes.getValue(PPTXMLConstants.RID))) {
                            hyperlinks.get(attributes.getValue(PPTXMLConstants.RID)).getParts().add(textpart);
                            textpartadded = true;
                        } else {
                            textpart = new Hyperlink(textpart);
                            ((Hyperlink) textpart).setRid(attributes.getValue(PPTXMLConstants.RID));
                            hyperlinks.put(((Hyperlink) textpart).getRid(), (Hyperlink) textpart);
                        }
                    }
                    break;
                case PPTXMLConstants.TEXTPART:
                    textpart = new Textpart();
                    break;
                case PPTXMLConstants.TEXTDETAILS:
                    if (attributes.getValue(PPTXMLConstants.BOLD) != null && !attributes.getValue(PPTXMLConstants.BOLD).equals("0")) {
                        textpart.addType(FontDecoration.BOLD);
                    }
                    if (attributes.getValue(PPTXMLConstants.ITALIC) != null && !attributes.getValue(PPTXMLConstants.ITALIC).equals("0")) {
                        textpart.addType(FontDecoration.ITALIC);
                    }
                    if (attributes.getValue(PPTXMLConstants.STRIKE) != null) {
                        if (!attributes.getValue(PPTXMLConstants.STRIKE).equals(PPTXMLConstants.NOSTRIKE)) {
                            textpart.addType(FontDecoration.STRIKETHROUH);
                        }
                    }
                    if (attributes.getValue(PPTXMLConstants.UNDERLINE) != null) {
                        textpart.addType(FontDecoration.UNDERLINE);
                    }
                    if (attributes.getValue(PPTXMLConstants.SIZE) != null) {
                        textpart.setSize(Integer.parseInt(attributes.getValue(PPTXMLConstants.SIZE)));
                    }
                    if (attributes.getValue(PPTXMLConstants.CHARACTERSPACING) != null) {
                        textpart.setCharachterSpacing(Integer.parseInt(attributes.getValue(PPTXMLConstants.CHARACTERSPACING)));
                    }
                    if (attributes.getValue(PPTXMLConstants.DIRTY) != null) {
                        textpart.setDirty(true);
                    }
                    if (attributes.getValue(PPTXMLConstants.ERR) != null) {
                        textpart.setErr(true);
                    }
                    if (isList) {
                        if (defaultsize) {
                            if (text.getLevel() == null) {
                                text.setLevel("0");
                                startList(0);
                            }
                        }
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
                        switch (attributes.getValue(PPTXMLConstants.TEXTTYPEATTR)) {
                            case PPTXMLConstants.TEXT_TITLE_HEADER:
                                titleheader = true;
                                break;
                            case PPTXMLConstants.TEXT_SUBTITLE_HEADER:
                                subtitleheader = true;
                                break;
                            case PPTXMLConstants.TEXT_TITLE:
                                title = true;
                                break;
                            default:
                                break;
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
                        } else if (subtitleheader) {
                            textpart.setSize(PPTXMLConstants.TEXT_SUBTITLE_HEADER_SIZE);
                        } else if (title) {
                            textpart.setSize(PPTXMLConstants.TEXT_TITLE_SIZE);
                        } else if (defaultsize) {
                            textpart.setSize(PPTXMLConstants.TEXT_DEFAULT_LARGE_SIZE);
                        } else {
                            textpart.setSize(PPTXMLConstants.TEXT_DEFAULT_SMALL_SIZE);
                        }
                        textpartContent = false;
                    }
                    if (!textpartadded) {
                        text.getTextparts().add(textpart);
                    }
                    break;
                case PPTXMLConstants.TEXT:
                    if (list != null) {
                        if (text.getLevel() == null) {
                            Object[] keys = lists.keySet().toArray();
                            Arrays.sort(keys);
                            pptobjects.getPptObjects().add(lists.get((Integer)keys[0]));
                            pptobjects.getPptObjects().add(text);
                            textAdded = true;
                            lists = null;
                            list = null;
                        }
                    } else if (list == null && text.getLevel() == null) {
                        pptobjects.getPptObjects().add(text);
                        textAdded = true;
                        text = null;
                        titleheader = false;
                    }
                    break;
                case PPTXMLConstants.TEXTBODY:
                    textbody = false;
                    defaultsize = false;
                    title = false;
                    subtitleheader = false;
                    if (text != null && !textAdded) {
                        pptobjects.getPptObjects().add(text);
                    }
                    if (list != null) {
                        Object[] keys = lists.keySet().toArray();
                        Arrays.sort(keys);
                        pptobjects.getPptObjects().add(lists.get((Integer)keys[0]));
                        lists = null;
                        list = null;
                    }
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
                media = (Media) new Image();
                media.setId(attributes.getValue(PPTXMLConstants.ID));
            } else if (qName.equals(PPTXMLConstants.VIDEOBODY)) {
                Video video = new Video();
                video.getDimension().setSize(media.getDimension());
                video.getLocation().setSize(media.getLocation());
                video.setId(media.getId());
                video.setFilename(media.getFilename());
                media = video;
            } else if (qName.equals(PPTXMLConstants.MEDIABOX)) {
                imagesize = true;
            } else if (qName.equals(PPTXMLConstants.MEDIASIZE) && imagesize) {
                double width = Double.parseDouble(attributes.getValue(PPTXMLConstants.MEDIAWIDTH)) / 3600 / 33;
                double height = Double.parseDouble(attributes.getValue(PPTXMLConstants.MEDIAHEIGHT)) / 3600 / 19;
                media.getDimension().setSize(width, height);
                imagesize = false;
            } else if (qName.equals(PPTXMLConstants.MEDIALOCATION) && imagesize) {
                double offsetX = Double.parseDouble(attributes.getValue(PPTXMLConstants.MEDIALOCX)) / 3600 / 33;
                double offsetY = Double.parseDouble(attributes.getValue(PPTXMLConstants.MEDIALOCY)) / 3600 / 19;
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
                pptobjects.getPptObjects().add(media);
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
                pptobjects.getPptObjects().add(chart);
                canRead = false;
            }

        } catch (Exception e) {
            output.println(Logger.error("Error while reading slide chart tags (DefaultHandler startElement)", e));
        }
    }

    private void endRest(String qName) {
        try {
            switch (qName) {
                case PPTXMLConstants.TABLE:
                    pptobjects.getPptObjects().add(new Table());
                    canRead = true;
                    break;
                case PPTXMLConstants.FRAGMENT:
                    break;
                case PPTXMLConstants.GFRAME:
                    gframe = false;
                    canRead = true;
                    break;
                case PPTXMLConstants.SECTION:
                    if(!canRead){
                        canRead = true;
                    }
                    else if(previousPptobjects != pptobjects && previousPptobjects!=null){  
                        previousPptobjects.getPptObjects().add(pptobjects);
                        pptobjects = previousPptobjects;
                    }
                default:
                    break;
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
                        switch (attributes.getValue(PPTXMLConstants.TEXTTYPEATTR)) {
                            case PPTXMLConstants.TEXT_TITLE_HEADER:
                                titleheader = true;
                                break;
                            case PPTXMLConstants.TEXT_SUBTITLE_HEADER:
                                subtitleheader = true;
                                break;
                            case PPTXMLConstants.TEXT_TITLE:
                                title = true;
                                break;
                            case PPTXMLConstants.FOOTER:
                                previousPptobjects = pptobjects;
                                pptobjects = new Footer();
                                break;
                            case PPTXMLConstants.DIANR:
                                canRead = false;
                                break;
                            default:
                                break;
                        }
                    } else {
                        defaultsize = true;
                    }
                    break;
                case PPTXMLConstants.TABLE:
                    canRead = false;
                    textbody = false;
                    imagebody = false;
                    break;
            }
        } catch (Exception e) {
            output.println(Logger.error("Error while ending slide tags (DefaultHandler startElement)", e));
        }
    }

}
