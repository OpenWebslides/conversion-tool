/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslidesconverter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidParameterException;
import openwebslides.output.Output;

/**
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
    
    /**
     * Create a new converter instance
     * @param file The file to be converted
     * @param output
     */
    public Converter(String file, Output output){
        this(null,file,null,output);
    }
    
    /**
     * Create a new converter instance
     * @param outputType the type your file should be converted to
     * @param inputFile the file to be converted
     * @param outputDir the output folder
     * @param output
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
     * Create a dummy output
     */
    public void dummyOutput(){
        output.println("Start conversion");
        
        //nodig om mee te geven in de error
        String pathError = null;
        
        try{
            File outputFile = new File(outputDir+"/index.html");
            pathError = outputFile.getParentFile().toString();
            
            //als het pad nog niet bestaat worden alle dir's aangemaakt
            outputFile.getParentFile().mkdirs();

            try(BufferedWriter out = new BufferedWriter(new FileWriter(outputFile))){

                if(shower)
                    showerOutput(out);
                else
                    contentOutput(out);

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
    
    //schrijft de slides samen de code voor shower uit
    //gebruikt contentOutput(BufferedWriter out)
    /**
     * Create a shower output
     * @param out BufferedWriter the slide should be written to
     * @throws IOException 
     */
    private void showerOutput(BufferedWriter out) throws IOException{
        //te kopiëren tekst openen
        try(BufferedReader br = new BufferedReader(new FileReader("shower.html"))){
            
            String currentLine;
            
            while ((currentLine = br.readLine()) != null) {
                
                if(currentLine.equals("###title###"))
                    out.write("\t<title>"+getOutputFileName()+"</title>");
                else if(currentLine.equals("###module###"))
                    out.write("\t<a class=\"module\" href=\"#title\">"+getOutputFileName()+"</a>");
                else if(currentLine.equals("###content###"))
                    contentOutput(out);
                else
                    out.write(currentLine);
                
                out.write("\n");
            }
        }
    }
    
    /**
     * Create a raw output
     * @param out BufferedWriter the slide should be written to
     * @throws IOException 
     */
    private void contentOutput(BufferedWriter out) throws IOException{
        //te kopiëren tekst openen
        try(BufferedReader br = new BufferedReader(new FileReader("content.html"))){
            
            String currentLine;
            
            while ((currentLine = br.readLine()) != null) {
                if(shower) out.write("\t");
                out.write(currentLine+"\n");
            }
        }
    }
    
    /**
     * Generate the output filename
     * @return 
     */
    private String getOutputFileName(){
        return inputFile.substring(0, inputFile.lastIndexOf(".pptx"));
    }
}
