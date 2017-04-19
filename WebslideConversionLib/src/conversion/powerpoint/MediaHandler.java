/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.powerpoint;

import java.util.List;
import objects.Image;
import objects.PPTObject;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.xml.namespace.QName;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import logger.Logger;
import objects.Cell;
import objects.Chart;
import objects.ChartType;
import objects.Row;
import objects.Table;
import objects.Media;
import objects.Video;
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
class MediaHandler {

    private static final String RELATION_NAMESPACE = "http://schemas.openxmlformats.org/officeDocument/2006/relationships";
    private static int mediaCount = 1;

    /**
     * Handle media, extract images, videos, chartdata from ppt
     *
     * @param slide
     * @param pptObjects
     * @param saveLocation
     * @param file
     * @param output
     */
    public static void handle(XSLFSlide slide, List<PPTObject> pptObjects, String saveLocation, File file, Output output) {
        try {
            for (XSLFShape sh : slide.getShapes()) {
                if (sh.getClass().equals(XSLFPictureShape.class)) {
                    for (PPTObject po : pptObjects) {
                        if (po instanceof Media) {
                            if (((Media) po).getId().equals("" + sh.getShapeId())) {
                                ((Media) po).setFilename(saveLocation + "\\" + ((XSLFPictureShape) sh).getPictureData().getFileName());
                                copyImage(((Media) po), ((XSLFPictureShape) sh).getPictureData().getFileName(), file, output, saveLocation);
                            }
                        }
                    }
                }
                if (sh.getClass().equals(XSLFTable.class)) {
                    for (PPTObject po : pptObjects) {
                        if (po.getClass().equals(Table.class)) {
                            if (((Table) po).getRows().isEmpty()) {
                                Table t = (Table) po;
                                List<XSLFTableRow> rows = ((XSLFTable) sh).getRows();
                                for (XSLFTableRow row : rows) {
                                    List<XSLFTableCell> cells = row.getCells();
                                    Row newRow = new Row();
                                    t.getRows().add(newRow);
                                    for (XSLFTableCell cell : cells) {
                                        newRow.getCells().add(new Cell(cell.getText(), cell.getGridSpan(), cell.getRowSpan()));
                                    }

                                }
                            }
                        }
                    }
                }
            }

            for (POIXMLDocumentPart.RelationPart part : slide.getRelationParts()) {
                if (part.getDocumentPart() instanceof XSLFChart) {
                    final String relId = part.getRelationship().getId();

                    for (XSLFShape shape : slide.getShapes()) {
                        if (shape instanceof XSLFGraphicFrame) {
                            final CTGraphicalObjectFrame frameXML = (CTGraphicalObjectFrame) shape.getXmlObject();
                            final XmlObject[] children = frameXML.getGraphic().getGraphicData().selectChildren(new QName(XSSFRelation.NS_CHART, "chart"));

                            for (final XmlObject child : children) {
                                final String imageRel = child.getDomNode().getAttributes().getNamedItemNS(RELATION_NAMESPACE, "id").getNodeValue();

                                if (relId.equals(imageRel)) {
                                    for (PPTObject po : pptObjects) {
                                        if (po.getClass().equals(Chart.class)) {
                                            if (((Chart) po).getId().equals("" + shape.getShapeId())) {
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
        } catch (Exception e) {
            output.println(Logger.error("Error while extracting media from powerpoint zip", e));
        }
    }

    /**
     * Handle media, extract images, videos, chartdata from ppt
     *
     * @param slide
     * @param pptObjects
     * @param saveLocation
     * @param file
     * @param output
     */
    public static void handle(XSLFSlide slide, List<PPTObject> pptObjects, ZipOutputStream zip, String saveLocation, File file, Output output) {
        try {
            for (XSLFShape sh : slide.getShapes()) {
                if (sh.getClass().equals(XSLFPictureShape.class)) {
                    for (PPTObject po : pptObjects) {
                        if (po.getClass().equals(Image.class)) {
                            if (((Image) po).getId().equals("" + sh.getShapeId())) {
                                ((Image) po).setFilename(((XSLFPictureShape) sh).getPictureData().getFileName());
                                copyImage(((Image) po), ((XSLFPictureShape) sh).getPictureData().getFileName(), file, output, zip, saveLocation);
                            }
                        }
                    }
                }
                if (sh.getClass().equals(XSLFTable.class)) {
                    for (PPTObject po : pptObjects) {
                        if (po.getClass().equals(Table.class)) {
                            if (((Table) po).getRows().isEmpty()) {
                                Table t = (Table) po;
                                List<XSLFTableRow> rows = ((XSLFTable) sh).getRows();
                                for (XSLFTableRow row : rows) {
                                    List<XSLFTableCell> cells = row.getCells();
                                    Row newRow = new Row();
                                    t.getRows().add(newRow);
                                    for (XSLFTableCell cell : cells) {
                                        newRow.getCells().add(new Cell(cell.getText(), cell.getGridSpan(), cell.getRowSpan()));
                                    }

                                }
                            }
                        }
                    }
                }
            }

            for (POIXMLDocumentPart.RelationPart part : slide.getRelationParts()) {
                if (part.getDocumentPart() instanceof XSLFChart) {
                    final String relId = part.getRelationship().getId();

                    for (XSLFShape shape : slide.getShapes()) {
                        if (shape instanceof XSLFGraphicFrame) {
                            final CTGraphicalObjectFrame frameXML = (CTGraphicalObjectFrame) shape.getXmlObject();
                            final XmlObject[] children = frameXML.getGraphic().getGraphicData().selectChildren(new QName(XSSFRelation.NS_CHART, "chart"));

                            for (final XmlObject child : children) {
                                final String imageRel = child.getDomNode().getAttributes().getNamedItemNS(RELATION_NAMESPACE, "id").getNodeValue();

                                if (relId.equals(imageRel)) {
                                    for (PPTObject po : pptObjects) {
                                        if (po.getClass().equals(Chart.class)) {
                                            if (((Chart) po).getId().equals("" + shape.getShapeId())) {
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
        } catch (Exception e) {
            output.println(Logger.error("Error while extracting media from powerpoint zip", e));
        }
    }

    private static void copyImage(Media img, String name, File file, Output output, ZipOutputStream zip, String saveLoc) {
        try {
            try {
                
                output.println("gonna copy");
                File f = copyImage(img, name, file, output, saveLoc);
                FileInputStream fis = new FileInputStream(f);
                output.println(saveLoc + "\\" + f.getName());
                ZipEntry zipEntry = new ZipEntry(saveLoc + "\\" + f.getName());
                zip.putNextEntry(zipEntry);
                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                        zip.write(bytes, 0, length);
                }

                zip.closeEntry();
                fis.close();
                

            } catch (Exception e) {
                e.printStackTrace();
            }
            
            
        } catch (Exception e) {
            output.println(Logger.error("Error while extracting images from powerpoint zip " + img.toString(), e));
        }
    }
  
 
    private static File copyImage(Media img, String name, File file, Output output, String saveLoc) {
        File f = null;
        try {
            FileInputStream fin = new FileInputStream(file.getAbsoluteFile());
            BufferedInputStream bin = new BufferedInputStream(fin);
            ZipInputStream zin = new ZipInputStream(bin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                String na = "ppt/media/" + name;
                if (img instanceof Video) {
                    na = "ppt/media/media" + mediaCount;
                }
                if (ze.getName().contains(na)) {
                    String n = ze.getName().split("/")[ze.getName().split("/").length - 1];
                    f = new File(saveLoc + "\\" + n);
                    img.setFilename(n);
                    f.getParentFile().mkdirs();
                    try (OutputStream out = new FileOutputStream(f)) {
                        byte[] buffer = new byte[8192];
                        int len;
                        while ((len = zin.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                        }
                        if (img instanceof Video) {
                            mediaCount++;
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            output.println(Logger.error("Error while extracting images from powerpoint zip " + img.toString(), e));
        }
        return f;
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

            DefaultHandler handler;
            handler = new ChartHandler(output, chartObj);
            sp.parse(new InputSource(new StringReader(chart.getCTChart().xmlText())), handler);

        } catch (Exception e) {
            output.println(Logger.error("Error while extracting chart from powerpoint ", e));
        }
    }

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

}
