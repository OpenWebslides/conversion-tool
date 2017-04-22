/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.pdf;

import conversion.IConverter;
import conversion.pdf.util.PDFException;
import conversion.pdf.util.PDFTextExtractor;
import conversion.pdf.util.PDFImageExtractor;
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

    /**
     * The parameter file has to be a PDF file. It will be decrypted for further
     * use.
     *
     * @param file
     * @throws conversion.pdf.util.PDFException
     */
     
    public PDFConverter(File file) throws PDFException {
        this.file = file;
        try {
            document = PDDocument.load(file);
            if (document.isEncrypted()) {
                document.decrypt("");
            }
        } catch (CryptographyException ex) {
            //System.out.println("er ging iets mis bij de decryptie....");
            output.println("er ging iets mis bij de decryptie....");
            throw new PDFException("Decription failed");

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
        try {
            retrieveImagesToFile(Location);
        } catch (IOException ex) {
            output.println("io exception bij ophalen afbeeldingen...");
        }
        parse(ppt);

    }

    @Override
    public void parse(PPT ppt, ZipOutputStream zOS, String saveLocation) throws PDFException{

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
        PDFImageExtractor imEx = new PDFImageExtractor();
        imEx.extractImage(document, ZOS, saveLocation);
    }

    private void retrieveImagesToFile(String Location) throws IOException {
        PDFImageExtractor imEx = new PDFImageExtractor();
        imEx.extractImage(document, Location);
    }

    private void parse(PPT ppt) throws PDFException {
        try {
            getImageLocations imLocParser = new getImageLocations();
            getImageLocations2 imLocParser2 = new getImageLocations2();
            PDFTextExtractor parser = new PDFTextExtractor();

            List allPages = document.getDocumentCatalog().getAllPages();

            for (int i = 0; i < allPages.size(); i++) {
                //System.out.println("page-start=============================");
                PDPage page = (PDPage) allPages.get(i);
                System.out.println("Processing page: " + i);
                //elke pagina is 1 slide!!! -> als slide af is moet je die hier dus aanmaken en de objecten uit extractor halen
                PDStream contents = page.getContents();
                if (contents != null) {
                    parser.processStream(page, page.findResources(), page.getContents().getStream());
                    //voor afbeelding posities (hopelijk)
                    imLocParser.processStream(page, page.findResources(), page.getContents().getStream());
                    imLocParser2.processStream(page, page.findResources(), page.getContents().getStream());
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
                ppt.getSlides().add(slide);

                //testPPT(ppt);
            }
            TextIntelligence tI = new TextIntelligence();
            tI.makeText(ppt);
            // testPPT(ppt);

            output.println("er zijn " + (imLocParser.getImageNumber() - 1) + " afbeeldingen gevonden.");

        } catch (IOException ex) {
            output.println("er ging iets mis in de conversie.... ");
            throw new PDFException("Parsing aborded to soon");
        } catch (Exception e) {
            output.println("onherkende fout, wss de schult van apache..."
                    + e.getMessage());
            //e.printStackTrace();
            throw new PDFException("Parsing aborded to soon");
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

}
