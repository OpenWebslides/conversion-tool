/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.pdf.util;
/*
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;*/
/*
import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;
import org.w3c.dom.Document;
*/
/**
 *
 * @author Gertjan
 */
public class DOM {
    //private Document dom;
    
    public DOM(/*PDDocument document*/) {
        // create the DOM parser
    /*    PDFDomTree parser;
        try {
            parser = new PDFDomTree();
            // parse the file and get the DOM Document
            dom = parser.createDOM(document);

            // wegschrijven naar xml
            Transformer transformer;

            transformer = TransformerFactory.newInstance().newTransformer();

            Result outputx = new StreamResult(new File("output.xml"));
            Source input = new DOMSource(dom);

            transformer.transform(input, outputx);

        } catch (IOException ex) {
            Logger.getLogger(DOM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(DOM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(DOM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(DOM.class.getName()).log(Level.SEVERE, null, ex);
        }
*/
    }
}
