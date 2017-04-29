/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.pdf.util;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.List;
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

    public void extract(PDPage page) throws IOException {

//    PDFTextStripperByArea stripper = new PDFTextStripperByArea();
//    List<PDAnnotation> annotations = page.getAnnotations();
//    if(annotations == null){
//        System.out.println("there are no annotations on this page...");
//    }
//    //first setup text extraction regions
//    for( int j=0; j<annotations.size(); j++ )
//    {
//        PDAnnotation annot = annotations.get(j);
//        if( annot instanceof PDAnnotationLink )
//        {
//            PDAnnotationLink link = (PDAnnotationLink)annot;
//            PDRectangle rect = link.getRectangle();
//            //need to reposition link rectangle to match text space
//            float x = rect.getLowerLeftX();
//            float y = rect.getUpperRightY();
//            float width = rect.getWidth();
//            float height = rect.getHeight();
//            int rotation = page.getRotation();
//            if( rotation == 0 )
//            {
//                PDRectangle pageSize = page.getMediaBox();
//                y = pageSize.getHeight() - y;
//            }
//            else if( rotation == 90 )
//            {
//                //do nothing
//            }
//
//            Rectangle2D.Float awtRect = new Rectangle2D.Float( x,y,width,height );
//            stripper.addRegion( "" + j, awtRect );
//        }
//    }
//
//    stripper.extractRegions( page );
//    System.out.println("regions extracted...");
//    for( int j=0; j<annotations.size(); j++ )
//    {
//        PDAnnotation annot = annotations.get(j);
//        if( annot instanceof PDAnnotationLink )
//        {
//            PDAnnotationLink link = (PDAnnotationLink)annot;
//            PDAction action = link.getAction();
//            String urlText = stripper.getTextForRegion( "" + j );
//            if( action instanceof PDActionURI )
//            {
//                PDActionURI uri = (PDActionURI)action;
//                System.out.println( "found: " + urlText.trim() + "'=" + uri.getURI() );
//            }
//        }
//            
//    }
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
                        } else {
                            if (action instanceof PDActionGoTo) {
                                // get internal link
                                PDDestination destination = ((PDActionGoTo) action).getDestination();
                                PDPageDestination pageDestination;
                                if (destination instanceof PDPageDestination) {
                                    pageDestination = (PDPageDestination) destination;
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
    }

}
