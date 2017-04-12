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
import java.sql.Timestamp;
import java.util.zip.ZipOutputStream;
import objects.PPT;
import output.Output;
import openwebslides.writer.HTMLWriter;
import openwebslides.writer.TemplateWriter;
import openwebslides.writer.Writer;
import openwebslides.zip.ZipException;
import openwebslides.zip.Zipper;
import org.apache.commons.io.FileUtils;

/**
 * Converts a file that represents a presentation (pptx and pdf). By calling a convert method the PPT object of the Converter is filled.
 * The content of the PPT object can be written to the filesystem, a zip file or a OutputStream as html code.
 *
 * @author Jonas
 */
public class Converter {

    private final Output output;
    
    /**
     * The PPT object that is filled in a convert method and is used as data in the SaveToStream method.
     */
    private PPT readPpt;

    private static final String OUTPUT_FILE_HTML = "index.html";
    private String course, chapter;
    
    /**
     * The name of the charset used in the saveToStream(OutputStream, outputType, outputFormat) method.
     */
    private String charsetName = "UTF-8";
    
    public enum outputType {RAW, SHOWER};
    public enum outputFormat {HTML};
    
    /**
     * Constructor for a Converter object.
     * @param log The output that is logged to.
     */
    public Converter(Output log){
        this.output = log;
    }
    
    /**
     * Converts the file and fills the PPT object of the Converter. The images are saved into the imageSaveLocation.
     * @param file The file to be converted.
     * @param imageSaveLocation To location the images are saved.
     * @throws openwebslidesconverter.WebslidesConverterException If the conversion has failed.
     */
    public void convert(File file, String imageSaveLocation) throws WebslidesConverterException {
        try {
            output.println("Start conversion");
            
            checkIfValidFile(file); //throws FileNotFoundException if invalid file
            IConverter converter = getConverter(file);
            
            readPpt = new PPT();
            converter.parse(readPpt, imageSaveLocation);
            
            output.println("Input file successfully read");
        } catch (FileNotFoundException ex) {
            throw new WebslidesConverterException(ex);
        }
    }
    
    /**
     * Converts the file and fills the PPT object of the Converter. The images are saved into the ZipOutputStream.
     * @param file The file to be converted.
     * @param zos To ZipOutputStream the images are saved to.
     * @throws openwebslidesconverter.WebslidesConverterException If the conversion has failed.
     */
    public void convert(File file, ZipOutputStream zos) throws WebslidesConverterException  {
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
            System.err.println("dummy code: fout met de temp direcory");
            System.err.println(ex.getMessage());
            throw new WebslidesConverterException("dummy code: fout met de temp direcory\n" + ex.getMessage());
        } catch (ZipException ex) {
            throw new WebslidesConverterException(ex);
        }
    }
    
    /**
     * Checks if the file is a valid input file. It is valid if it does exist and is not a directory.
     * @param file The file to be checked.
     * @throws FileNotFoundException If the file does not exist or is a directory.
     */
    private void checkIfValidFile(File file) throws FileNotFoundException{
        if (!file.exists())
            throw new FileNotFoundException("File \""+ file.getAbsolutePath() +"\" does not exist");
        if (file.isDirectory())
            throw new FileNotFoundException("Directory \""+ file.getAbsolutePath() +"\" can not be converted");
    }
    
    /**
     * Returns the IConverter for the file type. The Output object of the IConverter is set to the Output of the Converter class.
     * @param file The file the IConverter is created for.
     * @return The IConverter for the file type.
     */
    private IConverter getConverter(File file){
        IConverter converter = ConverterFactory.getConverter(file);
        converter.setOutput(output);
        return converter;
    }
    
    /**
     * Saves the content of the PPT object of the class object to the zip. 
     * @param zos The ZipOutputStream that represents the zip file to be saved to.
     * @param type The outputType used to save.
     * @param format The outputFormat used to save.
     * @throws WebslidesConverterException If the output file could not be saved or the template folder could not be copied if the type equals SHOWER.
     */
    public void saveToZip(ZipOutputStream zos, outputType type, outputFormat format) throws WebslidesConverterException{
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
            throw new WebslidesConverterException("something went wrong while adding " + OUTPUT_FILE_HTML + " to the zip");
        } catch (ZipException ex) {
            throw new WebslidesConverterException(ex);
        }
        output.println("saving to .zip done");
    }
    
    /**
     * Saves the content of the PPT object of the class object to the directory dir.
     * @param dir The File that represents the directory to be saved to.
     * @param type The outputType used to save.
     * @param format The outputFormat used to save.
     * @throws WebslidesConverterException WebslidesConverterException If the output file could not be saved or the template folder could not be copied if the type equals SHOWER.
     */
    public void saveToDirectory(File dir, outputType type, outputFormat format) throws WebslidesConverterException  {
        output.println("start saving to "+dir.getAbsolutePath());
        if(!dir.isDirectory())
            throw new WebslidesConverterException("dir \"" + dir.getAbsolutePath() + "\" is not a directory");
        
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
            throw new WebslidesConverterException(dir.getAbsolutePath() + File.separator + OUTPUT_FILE_HTML + " could not be created (" + ex.getMessage() + ")");
        } catch (IOException ex) {
            throw new WebslidesConverterException("the template folder could not be copied");
        }
        output.println("successfully saved to " + dir.getAbsolutePath());
    }
    
    /**
     * Saves the content of the PPT object of the class object to the OutputStream os.
     * @param os The OutputStream to be saved to.
     * @param type The outputType used to save.
     * @param format The outputFormat used to save.
     * @throws WebslidesConverterException If the charset is not valid or an exception is thrown by the write method of the Writer used to write out the PPT object.
     */
    public void saveToStream(OutputStream os, outputType type, outputFormat format) throws WebslidesConverterException{
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, charsetName));
            
            if(format == outputFormat.HTML){
                output.println("creating .html file");
                Writer writer;
                
                if(type == outputType.RAW){
                    writer = new HTMLWriter(); 
                    // TODO htmlWriter output meegeven
                }
                else { // outputType.SHOWER
                    HTMLWriter htmlWriter = new HTMLWriter();
                    // TODO htmlWriter output meegeven
                    writer = new TemplateWriter(htmlWriter, course, chapter);
                }
                
                writer.write(bufferedWriter, readPpt);
            }
            else {
                throw new WebslidesConverterException("outputFormat " + format + "is not supported");
            }
            
            bufferedWriter.flush();
        } catch (IOException ex) { // UnsupportedEncodingException is subclass of IOException
            throw new WebslidesConverterException(ex);
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

    public String getCharsetName() {
        return charsetName;
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }
    
}
