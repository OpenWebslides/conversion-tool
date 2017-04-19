/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.pdf;

import conversion.IConverter;
import conversion.pdf.util.PDFTextExtractor;
import conversion.pdf.util.PDFImageExtractor;
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
     */
    public PDFConverter(File file) {
        this.file = file;
        try {
            document = PDDocument.load(file);
            if (document.isEncrypted()) {
                document.decrypt("");
            }
        } catch (CryptographyException ex) {
            //System.out.println("er ging iets mis bij de decryptie....");
            output.println("er ging iets mis bij de decryptie....");

        } catch (Exception ex) {
            //System.out.println("Er ging iets mis met de file... ");
            output.println("Er ging iets mis met de file... ");
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
     * fill the ppt object with content - if any additional images will be given
     * to the zip output stream... next the normal parse function for retrieving
     * text will be invoked.
     *
     * @param ppt
     * @param zOS
     */
    public void parse(PPT ppt, ZipOutputStream zOS) {
        //deze methode kan meteen naar een zip mee wegschrijven...
        ZipOutputStream outputStream = zOS;
        System.out.println("laat het parsen beginnen!");
        try {
            retrieveImagesToZOS(zOS);
        } catch (IOException ex) {
            System.out.println("IO exception... ");
        }
        parse(ppt);
    }

    /**
     * this function will fill the ppt object with content - if any the param s
     * represents the location where a images folder will be created and
     * filled...
     *
     * @param ppt
     * @param Location
     * @param s
     */
    @Override
    public void parse(PPT ppt, String Location) {

        System.out.println("laat het parsen beginnen!");
        //controleren of plaats bestaat of niet...
        //indien niet proberen aanmaken... (wat?)
        File directory = new File(Location);
        if (!directory.exists()) {
            directory.mkdirs();
        
        }
        try {
            retrieveImagesToFile(Location);
        } catch (IOException ex) {
            System.out.println("io exception bij ophalen afbeeldingen...");
        }
        parse(ppt);

    }

    private void retrieveImagesToZOS(ZipOutputStream ZOS) throws IOException {
        PDFImageExtractor imEx = new PDFImageExtractor();
        imEx.extractImage(document, ZOS);
    }

    private void retrieveImagesToFile(String Location) throws IOException {
        PDFImageExtractor imEx = new PDFImageExtractor();
        imEx.extractImage(document, Location);
    }

    private void parse(PPT ppt) {
        try {

            getImageLocations imLocParser = new getImageLocations();
            getImageLocations2 imLocParser2 = new getImageLocations2();
            PDFTextExtractor parser = new PDFTextExtractor();

            List allPages = document.getDocumentCatalog().getAllPages();

            for (int i = 0; i < allPages.size(); i++) {
                System.out.println("page-start=============================");
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
                for (PPTObject obj : paginaobjects) {
                    //System.out.println("object: " + obj.toString() + "\tType: "+obj.getClass().getName());
                    System.out.printf("object: %-30.30s  type: %-30.30s%n", obj.toString(), obj.getClass().getName());
                }
                System.out.println("page-end=========================");
                Slide slide = new Slide();
                slide.getPptObjects().addAll(paginaobjects);
                ppt.getSlides().add(slide);

            }

            System.out.println("er zijn " + imLocParser.getImageNumber() + " afbeeldingen gevonden.");

        } catch (IOException ex) {
            System.out.println("er ging iets mis in de conversie.... ");
            //hier moet nog logging in gestoken worden!!
        } catch (Exception e) {
            System.out.println("onherkende fout, wss de schult van apache...");
        }

    }

    /*public void testPPT(PPT ppt){
     System.out.println("PPT CONTROLE");
     for(PPTObject obj : ppt.getSlides()){
     System.out.println(obj.toString());
     }
     }*/
    @Override
    public void setOutput(Output output) {
        this.output = output;
    }

    @Override
    public void parse(PPT ppt, ZipOutputStream outputStream, String saveLocation) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
