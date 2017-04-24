/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.powerpoint;

import java.util.ArrayList;
import java.util.HashMap;
import logger.Logger;
import objects.Chart;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import output.Output;

/**
 *
 * @author Karel
 */
public class ChartHandler extends DefaultHandler {

    private final Output output;
    private final HashMap<String, HashMap<String, ArrayList<Double>>> content;

    private final String SERIE = "c:tx";
    private final String CATEGORY = "c:cat";
    private final String VALUE = "c:v";
    private final String NUMDATA = "c:numCache";

    private boolean serie;
    private boolean category;
    private boolean numdata;

    private String currentSerie;

    private int categorycount = 0;
    private final ArrayList<String> categories;

    private boolean serievalue;
    private boolean categoryvalue;
    private boolean numvalue;

    public ChartHandler(Output output, Chart chart) {
        this.output = output;

        serie = false;
        category = false;
        numdata = false;

        categories = new ArrayList<>();
        content = chart.getContents();
    }

    @Override
    /**
     * The implementation of startElement
     */
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {

            switch (qName) {
                case SERIE:
                    serie = true;
                    break;
                case CATEGORY:
                    category = true;
                    break;
                case NUMDATA:
                    numdata = true;
                    break;
                case VALUE:
                    if (serie) {
                        serievalue = true;
                    } else if (category) {
                        categoryvalue = true;
                    } else if (numdata) {
                        numvalue = true;
                    }
                    break;
                default:
                    break;
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

            if (qName.equals(SERIE)) {
                serie = false;
            } else if (qName.equals(CATEGORY)) {
                category = false;
            }
        } catch (Exception e) {
            output.println(Logger.error("Error while reading slide chart data (DefaultHandler endElement)", e));
        }

    }

    @Override
    /**
     * The implementation of characters
     */
    public void characters(char ch[], int start, int length) throws SAXException {
        try {
            if (serievalue) {
                currentSerie = new String(ch, start, length);
                content.put(currentSerie, new HashMap<>());
                serievalue = false;
            } else if (categoryvalue) {
                String currentCategory = new String(ch, start, length);
                content.get(currentSerie).put(currentCategory, new ArrayList<>());
                categories.add(currentCategory);
                categoryvalue = false;
            } else if (numvalue) {
                Double data = Double.parseDouble(new String(ch, start, length));
                content.get(currentSerie).get(categories.get(categorycount)).add(data);
                categorycount = (categorycount + 1) % categories.size();
                numvalue = false;
            }

        } catch (Exception e) {
            output.println(Logger.error("Error while reading slide chart data (DefaultHandler characters)", e));
        }
    }

}
