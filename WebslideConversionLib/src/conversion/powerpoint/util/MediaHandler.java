/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.powerpoint.util;

import java.util.List;
import objects.Image;
import objects.PPTObject;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.xml.namespace.QName;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import logger.Logger;
import objects.*;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;
import org.openxmlformats.schemas.presentationml.x2006.main.CTGraphicalObjectFrame;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;
import output.Output;

/**
 *
 * @author Karel
 */
public class MediaHandler {

    private static final String RELATION_NAMESPACE = "http://schemas.openxmlformats.org/officeDocument/2006/relationships";

    //The media counter, media files are saved media1.wav,media2.avi,media3.avi,...
    private static int mediaCount = 1;

    //The link counter, links are saved in order
    private static int linkCount = 0;

    //Read the links and later attach them to the right hyperlink object
    private static ArrayList<String> links;

    //used for image saving
    private static String currentFilename = "";

    /**
     * Handle media, extract images, videos, chartdata from ppt
     *
     * @param slide XSLFSlide
     * @param pptObjects List
     * @param saveLocation String
     * @param file File
     * @param output Output
     */
    public static void handle(XSLFSlide slide, List<PPTObject> pptObjects, String saveLocation, File file, Output output, ZipOutputStream zip) {
        try {
            //Although there are a lot of loops, and nested loops the performance is still okay.
            //This is done for every slide, a slide contains by average 5-10 shapes and 10-15 objects
            //So even though there is some nesting, the performance will be good since we work with small numbers

            links = new ArrayList<>();
            linkCount = 0;
            //Read the shapes on the slide
            for (XSLFShape sh : slide.getShapes()) {

                //A hyperlink is text, therefor it will be saved in a XSLFTextShape object
                if (sh instanceof XSLFTextShape) {
                    //Get all paragraphs
                    for (XSLFTextParagraph p : ((XSLFTextShape) sh).getTextParagraphs()) {
                        //Add all the hyperlinks from this paragraph
                        addHyperlinks(p);
                    }
                } //If the shape is a XSLFPictureShape it's an image
                //The name of the shape will be used as the imagename, they are linked by the same id
                //We will copy the image to 'saveLocation'
                else if (sh instanceof XSLFPictureShape) {
                    //Check our objects for Media objects (Image or Video)
                    for (PPTObject po : pptObjects) {
                        if (po instanceof Media) {
                            //If our PPTObject is a Media object and his Id is the same as the same Id, we can start our work
                            if (((Media) po).getId().equals("" + sh.getShapeId())) {
                                //Set the filename, which we can get from the shape
                                ((Media) po).setFilename(((XSLFPictureShape) sh).getPictureData().getFileName());
                                //Copy the image from the ppt to our filesystem
                                if (zip == null) {
                                    copyImage(((Media) po), ((XSLFPictureShape) sh).getPictureData().getFileName(), file, output, saveLocation, false, zip);
                                } else {
                                    copyImage(((Media) po), ((XSLFPictureShape) sh).getPictureData().getFileName(), file, output, saveLocation, true, zip);
                                }
                                if(po instanceof Video && ((Video)po).getLink()!=null){
                                    for (POIXMLDocumentPart.RelationPart part : slide.getRelationParts()) {
                                        if(((Video)po).getLink().equals(part.getRelationship().getId())){
                                            ((Video)po).setLink(part.getRelationship().getTargetURI().toURL().toString());
                                        }
                                    }
                                }

                            }
                        }
                    }
                } //If the shape is from the table class, we'll build our table
                else if (sh.getClass().equals(XSLFTable.class)) {
                    //Check our object for a Table object
                    for (PPTObject po : pptObjects) {
                        if (po!=null && po.getClass().equals(Table.class)) {
                            //If we find the first table object to check if it is empty, this way we'll know if we already provided data for this one
                            if (((Table) po).getRows().isEmpty()) {
                                //Fill the table                                
                                Table t = (Table) po;
                                List<XSLFTableRow> rows = ((XSLFTable) sh).getRows();
                                for (XSLFTableRow row : rows) {
                                    List<XSLFTableCell> cells = row.getCells();
                                    Row newRow = new Row();
                                    t.getRows().add(newRow);
                                    for (XSLFTableCell cell : cells) {
                                        //The cell can be provided with even more data, for now we only use text, col- and rowspan
                                        newRow.getCells().add(new Cell(cell.getText(), cell.getGridSpan(), cell.getRowSpan()));
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //Charts will be read via relationparts
            for (POIXMLDocumentPart.RelationPart part : slide.getRelationParts()) {
                //If we find a chart object we'll check the shapes for xslfgraphicframes (these contain charts)
                if (part.getDocumentPart() instanceof XSLFChart) {
                    final String relId = part.getRelationship().getId();
                    for (XSLFShape shape : slide.getShapes()) {
                        if (shape instanceof XSLFGraphicFrame) {
                            final CTGraphicalObjectFrame frameXML = (CTGraphicalObjectFrame) shape.getXmlObject();
                            final XmlObject[] children = frameXML.getGraphic().getGraphicData().selectChildren(new QName(XSSFRelation.NS_CHART, "chart"));
                            //Check the shapes children for our chart
                            for (final XmlObject child : children) {
                                final String imageRel = child.getDomNode().getAttributes().getNamedItemNS(RELATION_NAMESPACE, "id").getNodeValue();
                                if (relId.equals(imageRel)) {
                                    for (PPTObject po : pptObjects) {
                                        if (po.getClass().equals(Chart.class)) {
                                            if (((Chart) po).getId().equals("" + shape.getShapeId())) {
                                                //Copy the chart data
                                                copyCharts(part.getDocumentPart(), output, (Chart) po);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //Check our pptobjects for hyperlinks, we will search for lists (because they contain text) and text
            for (PPTObject po : pptObjects) {
                if (po instanceof PPTList) {
                    //Search in all list objects (even the objects in a nested list)
                    for (PPTObject p : ((PPTList) po).getAllObjects()) {
                        if (p instanceof Text) {
                            updateHyperlink(p);
                        }
                    }
                } //Same for text
                else if (po instanceof Text) {
                    updateHyperlink(po);
                }
            }
        } catch (Exception e) {
            output.println(Logger.error("Error while extracting media from powerpoint zip", e));
        }
    }


    /**
     * Copy image to filesystem
     *
     * @param img Media
     * @param name String
     * @param file File
     * @param output Output
     * @param saveLoc String
     * @return
     */
    private static ArrayList<byte[]> copyImage(Media img, String name, File file, Output output, String saveLoc, boolean zip, ZipOutputStream zipoutput) {
        File f;
        ArrayList<byte[]> bytes = new ArrayList<>();
        try {
            //Open streams to open the .pptx file as zip
            FileInputStream fin = new FileInputStream(file.getAbsoluteFile());
            BufferedInputStream bin = new BufferedInputStream(fin);
            ZipInputStream zin = new ZipInputStream(bin);
            ZipEntry ze;
            //Look for our file in the zip
            while ((ze = zin.getNextEntry()) != null) {
                String na = "ppt/media/" + name;
                //If it's a Video, we dont care about the extension, we will just look for the media1.* media2.* ,... 
                if (img instanceof Video) {
                    na = "ppt/media/media" + mediaCount;
                }
                //If it's our file, copy it to the filesystem, but only when asked to save it so filesystem, otherwise just create byte[]
                if (ze.getName().contains(na)) {
                    String n = ze.getName().split("/")[ze.getName().split("/").length - 1];
                    f = new File(saveLoc + "\\" + n);
                    img.setFilename(n);
                    img.setCopied(true);
                    OutputStream out;
                    currentFilename = n;
                    if (!zip) {
                        f.getParentFile().mkdirs();
                        out = new FileOutputStream(f);
                    }
                    else{ 
                        out = zipoutput;
                        ZipEntry zipEntry = new ZipEntry(saveLoc + "\\" + currentFilename);
                        ((ZipOutputStream)out).putNextEntry(zipEntry);
                    }
                    try {
                        
                        byte[] buffer = new byte[8192];
                        int len;
                        
                        while ((len = zin.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                        }
                        if (img instanceof Video) {
                            mediaCount++;
                        }
                    } catch (Exception e) {
                        img.setCopied(false);
                    } finally {
                        if (!zip) {
                            out.close();
                        }
                        else{ 
                            ((ZipOutputStream)out).closeEntry();
                        }
                        }
                    break;
                }
            }
        } catch (Exception e) {
            try{
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                String error = sw.toString();
                if(!error.contains("duplicate entry:")){
                    output.println(Logger.error("Error while extracting images from powerpoint zip " + img.toString(), e));
                    img.setCopied(false);
                }
            }catch (Exception ex) {
                    output.println(Logger.error("Error while extracting images from powerpoint zip " + img.toString(), ex));
                    img.setCopied(false);
            }
        }
        return bytes;
    }

    private static void copyCharts(XSLFChart chart, Output output, Chart chartObj) {
        try {
            CTChart ctChart = chart.getCTChart();
            
            //Read type and title (Easier like this, kind of shitty in SAX)
            chartObj.setChartType(getChartType(ctChart, output));
            chartObj.setTitle(getChartTitle(ctChart, output));

            //SAX for data
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser sp = factory.newSAXParser();

            DefaultHandler handler = new ChartHandler(output, chartObj);
            sp.parse(new InputSource(new StringReader(chart.getCTChart().xmlText())), handler);

        } catch (Exception e) {
            output.println(Logger.error("Error while extracting chart from powerpoint ", e));
        }
    }

    /**
     * Read the chart type
     * @param ctChart CTCHart
     * @param output Output
     * @return 
     */
    private static ChartType getChartType(CTChart ctChart, Output output) {
        try {
            String text = "";
            String[] lines = ctChart.toString().split("\r");
            for (int i = 0; i < lines.length; i++) {
                String tmp = lines[i].replaceFirst("^\\s*", "");
                if (tmp.contains("<c:plotArea>")) {
                    tmp = lines[i + 2].replaceFirst("^\\s*", "");
                    text += tmp.substring(3, tmp.length() - 1) + " ";
                }
            }
            return ChartType.valueOf(text.trim());
        } catch (Exception e) {
            output.println(Logger.error("Error while extracting chart type from powerpoint ", e));
        }
        return null;
    }

    /**
     * Read the chart title
     * @param ctChart CTChart
     * @param output Output
     * @return 
     */
    private static String getChartTitle(CTChart ctChart, Output output) {
        try {
            String text = "";
            for (String line : ctChart.toString().split("\r")) {
                String tmp = line.replaceFirst("^\\s*", "");
                if (tmp.contains("<a:t>")) {
                    text += tmp.substring(5, tmp.length() - 6) + " ";
                }
            }
            return text;
        } catch (Exception e) {
            output.println(Logger.error("Error while extracting chart title from powerpoint ", e));
        }
        return null;
    }

    private static void addHyperlinks(XSLFTextParagraph p) {
        for (XSLFTextRun r : p.getTextRuns()) {
            if (r != null) {
                if (r.getHyperlink() != null) {
                    if (r.getHyperlink().getAddress() != null) {
                        links.add(r.getHyperlink().getAddress());
                    }
                }
            }
        }
    }

    private static void updateHyperlink(PPTObject p) {
        String rid = "";
        for (Textpart tp : ((Text) p).getTextparts()) {
                if (tp.getClass().equals(Hyperlink.class)) {
                    if (!rid.equals(((Hyperlink) tp).getRid())) {
                        //If we linked the hyperlink with the according link, update the hyperlink url and finish the object
                        if(links.size()<=linkCount){
                            ((Hyperlink) tp).setUrl(tp.getContent());
                            ((Hyperlink) tp).getParts().add(0, new Textpart(tp));
                            ((Hyperlink) tp).setContent("");
                        }else{
                            ((Hyperlink) tp).setUrl(links.get(linkCount++));
                            ((Hyperlink) tp).getParts().add(0, new Textpart(tp));
                            ((Hyperlink) tp).setContent("");
                        }
                    }
                }
        }
    }

}
