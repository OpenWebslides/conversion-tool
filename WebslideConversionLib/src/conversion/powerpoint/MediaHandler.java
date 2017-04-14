/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.powerpoint;

import java.util.List;
import objects.Image;
import objects.PPTObject;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.xml.namespace.QName;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import logger.Logger;
import objects.Chart;
import objects.ChartType;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.xslf.usermodel.XSLFChart;
import org.apache.poi.xslf.usermodel.XSLFGraphicFrame;
import org.apache.poi.xssf.usermodel.XSSFRelation;
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
                        if (po.getClass().equals(Image.class)) {
                            if (((Image) po).getId().equals("" + sh.getShapeId())) {
                                ((Image) po).setFilename(saveLocation + "\\" + ((XSLFPictureShape) sh).getPictureData().getFileName());
                                copyImage(((Image) po), ((XSLFPictureShape) sh).getPictureData().getFileName(), file, output);
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

    private static void copyImage(Image img, String name, File file, Output output) {
        try {
            File f = new File(img.getFilename());
            f.getParentFile().mkdirs();
            OutputStream out = new FileOutputStream(f);
            FileInputStream fin = new FileInputStream(file.getAbsoluteFile());
            BufferedInputStream bin = new BufferedInputStream(fin);
            ZipInputStream zin = new ZipInputStream(bin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                if (ze.getName().equals("ppt/media/" + name)) {
                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = zin.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    out.close();
                    break;
                }
            }
        } catch (Exception e) {
            output.println(Logger.error("Error while extracting images from powerpoint zip " + img.toString(), e));
        }
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
