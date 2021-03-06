/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.pdf;

import conversion.IConverter;
import conversion.pdf.util.ImageIntelligence;
import conversion.pdf.util.PDFException;
import conversion.pdf.util.PDFHyperlinkExtractor;
import conversion.pdf.util.PDFTextExtractor;
import conversion.pdf.util.PDFImageExtractor;
import conversion.pdf.util.TableIntelligence;
import conversion.pdf.util.TextIntelligence;
import conversion.pdf.util.getImageLocations;
import conversion.pdf.util.getImageLocations2;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;
import objects.PPT;
import objects.PPTObject;
import objects.Slide;
import org.apache.pdfbox.exceptions.CryptographyException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import output.Output;

/**
 * PDFConvertor has 1 main function, parse. Parse can be invoked with a string
 * or a ZipOutputStream. A pdfConvertor get's a File on consturction. This has
 * to be a PDF file. The parse method will change a PPT object and fill it with
 * information from the File.
 *
 * @author Gertjan
 */
public class PDFConverter implements IConverter {

    private final File file;
    private PDDocument document;
    private Output output;
    private int currentPageNumber;
    private ImageIntelligence imageIntel;
    private TableIntelligence tableIntel;
    private boolean isOpen;

    /**
     * The parameter file has to be a PDF file. It will be decrypted for further
     * use.
     *
     * @param file File
     * @throws conversion.pdf.util.PDFException exception
     */
    public PDFConverter(File file) throws PDFException {
        this.file = file;
        openDocument();
    }

    /**
     * opens the file, opening the file outside of the constructor is usefull
     * since at the end of every parse method the file is closed. This way the
     * converter can continue to exist and a file can be reopend.
     *
     * @throws PDFException
     */
    private void openDocument() throws PDFException {
        try {

            document = PDDocument.load(file);
            //DOM dom = new DOM(document);
            if (document.isEncrypted()) {
                document.decrypt("");
            }
            imageIntel = new ImageIntelligence();
            tableIntel = new TableIntelligence();

            isOpen = true;
        } catch (CryptographyException ex) {
            //System.out.println("er ging iets mis bij de decryptie....");
            output.println("Something went wrong when decrypting the file. Check if the file follows the pdf standard.");
            throw new PDFException("Decription failed");

        } catch (Exception ex) {
            //System.out.println("Er ging iets mis met de file... ");
            output.println("Something went wrong with this file, check if this file if conform the pdf standard.");
            throw new PDFException("Unrecognized error, check if file fits pdf standard."
                    + "--TIP: try 'printing to pdf' instead of 'saving as'");
        }
    }

    private void closeDocument() {
        //System.out.println("closing document...");
        try {
            document.close();
            isOpen = false;

        } catch (IOException ex) {
            output.println("Couldn't close document - parsing should be fine");
        } catch (Exception e) {
            output.println("Couldn't close docuemnt - parsing should be fine");
        }

    }

    /**
     * this function will fill the ppt object with content - if any the param s
     * represents the location where a images folder will be created and
     * filled...
     *
     * @param ppt PPT
     * @param Location String
     * @throws conversion.pdf.util.PDFException exception
     */
    @Override
    @SuppressWarnings("unchecked")
    public void parse(PPT ppt, String Location) throws PDFException {
        if (!isOpen) {
            openDocument();
        }
        output.println("parsing started...");
        //controleren of plaats bestaat of niet...
        //indien niet proberen aanmaken... (wat?)
        File directory = new File(Location);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            retrieveImagesToFile(Location);

        } catch (IOException ex) {
            output.println("io exception bij ophalen afbeeldingen...");
        }
        parse(ppt);

        tableIntel.placeTables(ppt, tableIntel.extractTables(document));
        imageIntel.checkImages(ppt, Location);
        output.println("parser found " + tableIntel.getTableNumber() + " table(s).");
        //tableIntel.checkTables(ppt);

        output.println("Parsing finished");
        closeDocument();
        //System.out.println("document closed succesfully");
        //return;
    }

    /**
     * saves images to a zip output stream and parses the opened file of the
     * convertor content will be added to the ppt object. The saveLocation is
     * used to identify images
     *
     * @param ppt
     * @param zOS
     * @param saveLocation
     * @throws PDFException
     */
    @Override
    @SuppressWarnings("unchecked")
    public void parse(PPT ppt, ZipOutputStream zOS, String saveLocation) throws PDFException {
        //System.out.println("parsing to ZIP...");
        if (!isOpen) {
            openDocument();
        }
        output.println("Parsing started...");
        ArrayList<String> afbeeldingen = null;
        try {
            afbeeldingen = retrieveImagesToZOS(zOS, saveLocation);
        } catch (IOException ex) {
            output.println("IO exception caused an internal error, some images might be lost in convertion...");
        }

        parse(ppt);
        //System.out.println("parsing done");
        //System.out.println("table exctration...");
        tableIntel.placeTables(ppt, tableIntel.extractTables(document));

        imageIntel.checkImages(ppt, afbeeldingen);
        output.println("parser found " + tableIntel.getTableNumber() + " table(s).");
        //tableIntel.checkTables(ppt);
        closeDocument();
        output.println("Parsing finished");
        //System.out.println("document closed succesfully");
        //return;
    }

    private ArrayList<String> retrieveImagesToZOS(ZipOutputStream ZOS, String saveLocation) throws IOException {
        PDFImageExtractor imEx = new PDFImageExtractor();
        ArrayList<String> images = imEx.extractImage(document, ZOS, saveLocation);
        return images;
    }

    private void retrieveImagesToFile(String Location) throws IOException {
        PDFImageExtractor imEx = new PDFImageExtractor();
        imEx.extractImage(document, Location);
    }

    /**
     * general parse method, will extract text and hyperlinks
     *
     * @param ppt
     * @throws PDFException
     */
    private void parse(PPT ppt) throws PDFException {
        try {
            getImageLocations imLocParser = new getImageLocations(this);
            //getImageLocations2 imLocParser2 = new getImageLocations2();
            PDFTextExtractor parser = new PDFTextExtractor();
            PDFHyperlinkExtractor hyperExtract = new PDFHyperlinkExtractor();
            List allPages = document.getDocumentCatalog().getAllPages();

            for (int i = 0; i < allPages.size(); i++) {
                //System.out.println("page-start=============================");
                PDPage page = (PDPage) allPages.get(i);
                //System.out.println("Processing page: " + i);
                currentPageNumber = i;

                //elke pagina is 1 slide!!! -> als slide af is moet je die hier dus aanmaken en de objecten uit extractor halen
                PDStream contents = page.getContents();
                if (contents != null) {
                    parser.processStream(page, page.findResources(), page.getContents().getStream());
                    //voor afbeelding posities (hopelijk)
                    imLocParser.processStream(page, page.findResources(), page.getContents().getStream());
                    imLocParser.resetNumber();
                    //imLocParser2.processStream(page, page.findResources(), page.getContents().getStream());
                }

                //na het parsen halen we de objecten op... van 1 pagina!!!
                ArrayList<PPTObject> paginaobjects = parser.getObjecten();

                //na het parsen halen we ook de imLocacties voor die pagina op...
                paginaobjects.addAll(imLocParser.getObjecten());
                //even testen wat er in zit in die objecten:
               /* for (PPTObject obj : paginaobjects) {
                 // System.out.println("object: " + obj.toString() + "\tType: "+obj.getClass().getName());
                 System.out.printf("object: %-30.30s  type: %-30.30s%n", obj.toString(), obj.getClass().getName());
                 }
                 */
                //System.out.println("page-end=========================");
                Slide slide = new Slide();
                slide.getPptObjects().addAll(paginaobjects);
                slide.getPptObjects().addAll(hyperExtract.extract(page));
                ppt.getSlides().add(slide);

                //testPPT(ppt);
            }
            TextIntelligence tI = new TextIntelligence();
            tI.makeText(ppt);
            //testPPT(ppt);

            output.println("parser found: " + (imLocParser.getImageNumber() - 1) + " image(s).");

        } catch (IOException ex) {
            output.println("Something went wrong in convertion... ");
            throw new PDFException("Parsing aborded to soon");
        } catch (Exception e) {
            output.println("unrecognised error, might be a bug in the apache library..."
                    + e.getMessage());
            //e.printStackTrace();
            throw new PDFException("Parsing aborded to soon");
        }

    }

    private void testPPT(PPT ppt) {
        //System.out.println("PPT CONTROLE");
        for (Slide slds : ppt.getSlides()) {
            for (PPTObject obj : slds.getPptObjects()) {
                System.out.println(obj.getContent());

            }

        }
    }

    @Override
    public void setOutput(Output output) {
        this.output = output;
        imageIntel.setOutput(this.output);
    }
    /**
     * method needed in extraction classes! (used to identify images etc correctly)
     * @return 
     */
    public int getCurrentPageNumber() {
        return currentPageNumber;
    }
}
