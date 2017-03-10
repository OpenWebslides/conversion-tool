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
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
import objects.PPT;
import openwebslides.output.Output;
import openwebslides.template.Template;

/**
 * Converts a .pptx file.
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
    
    /**
     * Create a new converter instance.
     * @param file The file to be converted.
     * @param output The output channel that should be used. All messages and errors are printed to there.
     */
    public Converter(String file, Output output){
        this(null,file,null,output);
    }
    
    /**
     * Create a new converter instance
     * @param outputType the type your file should be converted to
     * @param inputFile the file to be converted
     * @param outputDir the output folder
     * @param output The output channel that should be used. All messages and errors are printed to there.
     * @throws InvalidParameterException 
     */
    public Converter(String outputType, String inputFile, String outputDir, Output output) throws InvalidParameterException{
        //output type niet opgegeven -> default instellen
        if(outputType == null)
            outputType = PropertiesReader.getValue(PropertiesReader.OUTPUT_TYPE);
        
        //controleren of het een geldig type is
        if(!outputType.equals("raw") && !outputType.equals("shower")){
            throw new InvalidParameterException("\""+outputType+"\" is not a valid output parameter");
        }
        //shower -> true / raw -> false
        //(gebruikt voor het uitprinten)
        shower = outputType.equals("shower");
        
        //controleren of bestand juiste extensie
        String extension = inputFile.substring(inputFile.lastIndexOf(".") + 1, inputFile.length());
        if(!extension.equals("pptx")){
            throw new InvalidParameterException("file must have the extension .pptx");
        }
        
        //controleren of bestand bestaat
        File f = new File(inputFile);
        if(!f.exists() || f.isDirectory())
            throw new InvalidParameterException("file could not be found");
        
        this.inputFile = inputFile;
        
        this.outputDir = outputDir==null ? PropertiesReader.getValue(PropertiesReader.OUTPUT_DIR) : outputDir ;
        
        this.output = output;
    }
    
    /**
     * Starts the conversion. The result is written to the output directory.
     */
    public void convert(){
        output.println("Start conversion");
        
        PPT ppt = new PPT();
        
        IConverter converter = ConverterFactory.getConverter(new File(inputFile));
        converter.parse(ppt);
        
        output.println("Start writing to output file");
        //needed for the error
        String pathError = null;
        try{
            File outputFile = new File(outputDir+"/index.html");
            pathError = outputFile.getParentFile().toString();
            
            //als het pad nog niet bestaat worden alle dir's aangemaakt
            outputFile.getParentFile().mkdirs();

            try(BufferedWriter out = new BufferedWriter(new FileWriter(outputFile))){

                //write the converted files out
                writePPT(ppt, out);

                output.println("Conversion done");
            }
            catch(IOException e){
                output.error("something went wrong while processing the file", e.getMessage());
            }
        }
        catch(SecurityException e){
            output.error("The security manager does not allow access to \""+pathError+"\"", e.getMessage());
        }
    }
    
    /**
     * Writes the html output of the PPT to the BufferedWriter. The slides are embedded in the template code if shower is true.
     * @param out 
     */
    private void writePPT(PPT ppt, BufferedWriter out) throws IOException{
        if(shower){
            Template template = new Template(ppt,null,null);
            template.print(out);
            template.copySharedFolder(outputDir);
        }
        else{
            out.write(ppt.toHTML());
        }
    }
    
    /**
     * Generate the output filename.
     * @return 
     */
    private String getOutputFileName(){
        return inputFile.substring(0, inputFile.lastIndexOf(".pptx"));
    }
}
