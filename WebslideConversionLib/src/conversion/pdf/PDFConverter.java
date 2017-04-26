/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.pdf;

import conversion.IConverter;
import conversion.pdf.util.PDFException;
import conversion.pdf.util.TextIntelligence;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipOutputStream;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import objects.PPT;
import objects.PPTObject;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import output.Output;

/**
 * PDFConvertor has 1 main function, parse. Parse can be invoked with a string
 * or a ZipOutputStream. A pdfConvertor gets a File on construction. This has to
 * be a PDF file. The parse method will change a PPT object and fill it with
 * information from the File.
 *
 * @author Gertjan
 */
public class PDFConverter implements IConverter {

    private final File file;
    private PDDocument document;
    private Output output;
    private Document dom;
    private FileWriter fw; //testing

    /**
     * The parameter file has to be a PDF file. It will be decrypted for further
     * use.
     *
     * @param file
     * @throws conversion.pdf.util.PDFException
     */
    public PDFConverter(File file) throws PDFException {
        try {
            /*
             for testing purposes only:
             */
            fw = new FileWriter(new File("C:\\temp\\nodes.txt"));
        } catch (IOException ex) {
            Logger.getLogger(PDFConverter.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.file = file;
        try {
            document = PDDocument.load(file);
            // create the DOM parser
            PDFDomTree parser = new PDFDomTree();
            // parse the file and get the DOM Document
            dom = parser.createDOM(document);

        } catch (Exception ex) {
            //System.out.println("Er ging iets mis met de file... ");
            output.println("Er ging iets mis met de file... ");
            throw new PDFException("Unrecognized error, check if file fits pdf standard."
                    + "--TIP: try 'printing to pdf' instead of 'saving as'");
        }
    }

    /**
     * finalized should be called after using this function, this way the
     * document is closed properly. garbage collection will call this method
     * thus the method acts like a destructor in C
     */
    @Override
    public void finalize() {
        try {
            document.close();
        } catch (Exception e) {
        } finally {
            try {
                super.finalize();
            } catch (Throwable ex) {
            }
        }
    }

    /**
     * this function will fill the ppt object with content - if any the param s
     * represents the location where a images folder will be created and
     * filled...
     *
     * @param ppt
     * @param Location
     * @throws conversion.pdf.util.PDFException
     */
    @Override
    public void parse(PPT ppt, String Location) throws PDFException {

        output.println("laat het parsen beginnen!");
        //controleren of plaats bestaat of niet...
        //indien niet proberen aanmaken... (wat?)
        File directory = new File(Location);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // wegschrijven naar xml
        //kan hier gebeuren maar is dus niet nodig..
        try {
            retrieveImagesToFile(Location);
        } catch (IOException ex) {
            output.println("io exception bij ophalen afbeeldingen...");
        }
        parse(ppt);

    }

    @Override
    public void parse(PPT ppt, ZipOutputStream zOS, String saveLocation) throws PDFException {

        ZipOutputStream outputStream = zOS;
        System.out.println("laat het parsen beginnen!");
        try {
            retrieveImagesToZOS(zOS, saveLocation);
        } catch (IOException ex) {
            output.println("IO exception... ");
        }
        parse(ppt);

    }

    private void retrieveImagesToZOS(ZipOutputStream ZOS, String saveLocation) throws IOException {
        /*DomImageExtractor imEx = new DomImageExtractor();
         imEx.extractImage(document, ZOS, saveLocation);*/
    }

    private void retrieveImagesToFile(String Location) throws IOException {
        /*DomImageExtractor imEx = new DomImageExtractor();
         imEx.extractImage(document, Location);*/
    }

    private void parse(PPT ppt) throws PDFException {
        try {
//==============invullen van DOM object=================================================================================
            /*gebeurt bij aanmaken Converter!*/
            if (dom == null) {
                output.println("the pdf file was empty or not readable");
                return;
            }

//==============aanpassen van ppt object met door DOM te overlopen======================================================
            visitRecursively(dom, -1);

            fw.close();

//==============naverwerking op ppt loslaten============================================================================
            //testPPT(ppt);
            TextIntelligence tI = new TextIntelligence();
            tI.makeText(ppt);
            // testPPT(ppt);

            //output.println("er zijn " + (imLocParser.getImageNumber() - 1) + " afbeeldingen gevonden.");
        } catch (Exception e) {
            output.println("onherkende fout, wss de schult van apache..."
                    + e.getMessage());
            //e.printStackTrace();

            throw new PDFException("Parsing aborded to soon");
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(PDFConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void visitRecursively(Node node, int niveau) throws IOException {

        // get all child nodes
        NodeList list = node.getChildNodes();
        niveau++;
        for (int i = 0; i < list.getLength(); i++) {
            // get child node
            Node childNode = list.item(i);
            for (int j = 0; j < niveau; j++) {
                fw.write("\t");
            }
            fw.write(childNode.getNodeName() + " ");
            
            if (childNode.hasAttributes()) {
                NamedNodeMap attrs = childNode.getAttributes();
                for (int k = 0; k < attrs.getLength(); k++) {
                    Attr attribute = (Attr) attrs.item(i);
                    if (attribute != null) {
                        fw.write(attribute.getName() + " = " + attribute.getValue());
                    }
                }
            }
            fw.write(System.lineSeparator());
            if (childNode.getNodeName().equalsIgnoreCase("style")) {
                return;
            }
            // visit child node
            visitRecursively(childNode, niveau);
        }
    }

    private void testPPT(PPT ppt) {
        System.out.println("PPT CONTROLE");
        for (PPTObject obj : ppt.getSlides()) {
            System.out.println(obj.toString());
        }
    }

    @Override
    public void setOutput(Output output) {
        this.output = output;
    }

    public void maakXML(String Location) {
        Transformer transformer;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            Result outputXML = new StreamResult(new File(Location + File.separator + "output.xml"));
            Source input = new DOMSource(dom);

            transformer.transform(input, outputXML);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(PDFConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(PDFConverter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
