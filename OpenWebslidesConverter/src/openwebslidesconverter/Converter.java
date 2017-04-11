/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslidesconverter;

import conversion.ConverterFactory;
import conversion.IConverter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipOutputStream;
import objects.PPT;
import output.Output;
import openwebslides.writer.HTMLWriter;
import openwebslides.writer.Indentation;
import openwebslides.writer.TemplateWriter;
import openwebslides.writer.Writer;
import openwebslides.zip.ZipException;
import openwebslides.zip.Zipper;
import org.apache.commons.io.FileUtils;

/**
 * Converts a .pptx file.
 *
 * @author Jonas
 */
public class Converter {

    /*Type output
    *   false = raw (enkel div-tags die slides voorstellen)
    *   true = shower (ook code voor de js plugin 'shower')
     */
    private boolean shower;
    private String inputFile, outputDir;

    private Output output;
    private PPT readPpt;

    /**
     * Create a new converter instance.
     *
     * @param file The file to be converted.
     * @param output The output channel that should be used. All messages and
     * errors are printed to there.
     */
    public Converter(String file, Output output) {
        this(null, file, null, output);
    }

    /**
     * Create a new converter instance
     *
     * @param outputType the type your file should be converted to
     * @param inputFile the file to be converted
     * @param outputDir the output folder
     * @param output The output channel that should be used. All messages and
     * errors are printed to there.
     * @throws InvalidParameterException
     */
    public Converter(String outputType, String inputFile, String outputDir, Output output) throws InvalidParameterException {
        //output type niet opgegeven -> default instellen
        if (outputType == null) {
            outputType = PropertiesReader.getValue(PropertiesReader.OUTPUT_TYPE);
        }

        //controleren of het een geldig type is
        if (!outputType.equals("raw") && !outputType.equals("shower")) {
            throw new InvalidParameterException("\"" + outputType + "\" is not a valid output parameter");
        }
        //shower -> true / raw -> false
        //(gebruikt voor het uitprinten)
        shower = outputType.equals("shower");

        //controleren of bestand juiste extensie
        String extension = inputFile.substring(inputFile.lastIndexOf(".") + 1, inputFile.length());
        if (!extension.equals("pptx")) {
            throw new InvalidParameterException("file must have the extension .pptx");
        }

        //controleren of bestand bestaat
        File f = new File(inputFile);
        if (!f.exists() || f.isDirectory()) {
            throw new InvalidParameterException("file could not be found");
        }

        this.inputFile = inputFile;

        this.outputDir = outputDir == null ? PropertiesReader.getValue(PropertiesReader.OUTPUT_DIR) : outputDir;

        this.output = output;
    }
    
    //****************************************************************************************************************************************************
    public Converter(Output log){
        this.output = log;
    }
    
    public void convert(File file, String imageSaveLocation) throws FileNotFoundException{
        output.println("Start conversion");
        
        checkIfValidFile(file); //throws FileNotFoundException if invalid file
        IConverter converter = getConverter(file);
        
        readPpt = new PPT();
        converter.parse(readPpt, imageSaveLocation);
        
        output.println("Input file successfully read");
    }
    
    public void convert(File file, ZipOutputStream zos) throws FileNotFoundException{
        try {
            output.println("Start conversion");
            
            checkIfValidFile(file); //throws FileNotFoundException if invalid file
            IConverter converter = getConverter(file);
            
            readPpt = new PPT();
            
            //temp dir for temp storage
            Path tempDir = Files.createTempDirectory("images"+new Timestamp(System.currentTimeMillis()).getTime());
            String imagesPath = tempDir.toAbsolutePath().toString() + File.separator + "images";
            
            converter.parse(readPpt, imagesPath);

            Zipper.addFolder(zos, tempDir.toFile());
            
            //delete temp dir with content
            FileUtils.deleteDirectory(tempDir.toFile());
            
            output.println("Input file successfully read");
        } catch (IOException ex) {
            System.err.println("Fout in convert(File, ZipOutputStream) met de temp direcory");
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ZipException ex) {
            System.err.println("Fout in convert(File, ZipOutputStream) bij het kopieren van de temp direcory naar de zip");
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void checkIfValidFile(File file) throws FileNotFoundException{
        String errorMsg1 = "Conversion ended with error";
        if (!file.exists()){
            String errorMsg2 = "File does not exist";
            output.error(errorMsg1, errorMsg2);
            throw new FileNotFoundException(errorMsg2);
        }
        if (file.isDirectory()){
            String errorMsg2 = "Directory can not be converted";
            output.error(errorMsg1, errorMsg2);
            throw new FileNotFoundException(errorMsg2);
        }
    }
    
    private IConverter getConverter(File file){
        IConverter converter = ConverterFactory.getConverter(file);
        converter.setOutput(output);
        return converter;
    }
    
    private static final String OUTPUT_FILE_HTML = "index.html";
    private String course, chapter;
    
    public enum outputType {RAW, SHOWER};
    public enum outputFormat {HTML};
    
    public void saveToZip(ZipOutputStream zos, outputType type, outputFormat format){
        output.println("start saving to .zip");
        try {
            //write PPT object to zip
            Zipper.newEntry(zos, OUTPUT_FILE_HTML);
            saveToStream(zos, type, format);
            Zipper.closeEntry(zos);
            output.println(OUTPUT_FILE_HTML + " successfully created");
            
            if(type == outputType.SHOWER){
                File source = new File("Template");
                Zipper.addFolder(zos, source);
                output.println("template files copied into .zip");
            }
            
        } catch (IOException ex) {
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ZipException ex) {
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
        }
        output.println("saving to .zip done");
    }
    
    public void saveToDirectory(File dir, outputType type, outputFormat format) throws Exception{
        output.println("start saving to "+dir.getAbsolutePath());
        if(!dir.isDirectory())
            throw new Exception("dir is not a directory"); // TODO vervangen door converterException
        
        try {
            //Write PPT object to the file in the os
            FileOutputStream os = new FileOutputStream(dir.getAbsolutePath() + File.separator + OUTPUT_FILE_HTML);
            saveToStream(os, type, format);
            output.println(OUTPUT_FILE_HTML + " successfully created");
            
            if(type == outputType.SHOWER){
                File source = new File("Template/_shared");
                File target = new File(dir.getAbsolutePath() + File.separator + "_shared");
                FileUtils.copyDirectory(source, target);
                output.println("template files copied");
            }
            // TODO copySharedFolder-methode in TemplateWriter verwijderen
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
        }
        output.println("successfully saved to " + dir.getAbsolutePath());
    }
    
    public void saveToStream(OutputStream os, outputType type, outputFormat format){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            
            if(format == outputFormat.HTML){
                output.println("creating .html file");
                Writer writer;
                
                if(type == outputType.RAW){
                    writer = new HTMLWriter(); 
                    // TODO htmlWriter (output meegeven EN) exceptie opwerpen bij fout
                }
                else { // outputType.SHOWER
                    HTMLWriter htmlWriter = new HTMLWriter();
                    // TODO htmlWriter (output meegeven EN) exceptie opwerpen bij fout
                    writer = new TemplateWriter(htmlWriter, course, chapter);
                }
                
                writer.write(bufferedWriter, readPpt);
            }
            else {
                // TODO throw converterException
            }
            
            bufferedWriter.flush();
        } catch (IOException ex) {
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setCourse(String course){
        this.course = course;
    }
    
    public void setChapter(String chapter){
        this.chapter = chapter;
    }

    public PPT getPPT() {
        return readPpt;
    }

    public void setPPT(PPT Ppt) {
        this.readPpt = Ppt;
    }
    
    //****************************************************************************************************************************************************

    /**
     * Starts the conversion. The result is written to the output directory.
     */
    public void convert() {
        output.println("Start conversion");

        PPT ppt = new PPT();

        IConverter converter = ConverterFactory.getConverter(new File(inputFile));
        converter.setOutput(output);
        converter.parse(ppt,""); //TODO: dir voor images instellen

        output.println("Start writing to output file");
        //needed for the error
        String pathError = null;
        try {
            File outputFile = new File(outputDir + "/index.html");
            pathError = outputFile.getParentFile().toString();

            //als het pad nog niet bestaat worden alle dir's aangemaakt
            outputFile.getParentFile().mkdirs();

            try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"))) {
                //write the converted files out
                writePPT(ppt, out);
                output.println("Conversion done");
            } catch (IOException e) {
                output.error("something went wrong while processing the file", e.getMessage());
            }
        } catch (SecurityException e) {
            output.error("The security manager does not allow access to \"" + pathError + "\"", e.getMessage());
        }
    }

    /**
     * Writes the html output of the PPT to the BufferedWriter. The slides are
     * embedded in the template code if shower is true.
     *
     * @param out
     */
    private void writePPT(PPT ppt, BufferedWriter out) throws IOException {
        Writer writer;
        
        if (shower){
            TemplateWriter tw = new TemplateWriter(new HTMLWriter(), null, null);
            tw.copySharedFolder(outputDir);
            writer = tw;
        }
        else
            writer = new HTMLWriter();
        
        writer.write(out, ppt);
    }

    /**
     * Generate the output filename.
     *
     * @return
     */
    private String getOutputFileName() {
        return inputFile.substring(0, inputFile.lastIndexOf(".pptx"));
    }
}
