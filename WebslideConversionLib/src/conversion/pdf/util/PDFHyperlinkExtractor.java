/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.pdf.util;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import objects.FontDecoration;
import static objects.FontDecoration.ITALIC;
import static objects.FontDecoration.UNDERLINE;
import objects.Hyperlink;
import objects.PPTObject;
import objects.Placeholder;
import objects.Textpart;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.action.type.PDAction;
import org.apache.pdfbox.pdmodel.interactive.action.type.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.action.type.PDActionURI;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;

/**
 *
 * @author Gertjan
 */
public class PDFHyperlinkExtractor {

    public ArrayList<PPTObject> extract(PDPage page) throws IOException {
        ArrayList<PPTObject> links = new ArrayList();
        List<PDAnnotation> annotations = page.getAnnotations();
        for (PDAnnotation annot : annotations) {
            if (annot instanceof PDAnnotationLink) {
                // get dimension of annottations
                PDAnnotationLink link = (PDAnnotationLink) annot;
                // get link action include link url and internal link
                PDAction action = link.getAction();
                // get link internal some case specal
                PDDestination pDestination = link.getDestination();

                if (action != null) {
                    if (action instanceof PDActionURI || action instanceof PDActionGoTo) {
                        if (action instanceof PDActionURI) {
                            // get uri link
                            PDActionURI uri = (PDActionURI) action;
                            System.out.println("uri link:" + uri.getURI());
                            Textpart tp = new Textpart();
                            tp.setContent(uri.getURI());
                            tp.addType(UNDERLINE);
                            tp.setColor("#0000FF");
                            Hyperlink h = new Hyperlink(tp);
                            links.add(h);
                        } else {
                            if (action instanceof PDActionGoTo) {
                                // get internal link
                                PDDestination destination = ((PDActionGoTo) action).getDestination();
                                PDPageDestination pageDestination;
                                if (destination instanceof PDPageDestination) {
                                    pageDestination = (PDPageDestination) destination;
                                    System.out.println("internal link found - placeholder inserted");
                                   
                                    Placeholder p = new Placeholder();
                                    p.setContent("Internal Link");
                                    links.add(p);
                                } else {
                                    break;
                                }

                            }
                        }
                    }
                } else {

                    //    
                }
            }
        }
        return links;
    }

}
