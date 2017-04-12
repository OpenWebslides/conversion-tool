/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslidesconverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.Queue;
import java.util.logging.Level;
import java.util.zip.ZipOutputStream;
import output.*;
import logger.Logger;
import openwebslidesconverter.Converter.outputType;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Resolves the parameters and starts the matching converter.
 * @author Laurens
 */
public class OpenWebslidesConverter {
    
    private static final String FLAG_INPUT_FILE = "i";
    private static final String FLAG_OUTPUT_FOLDER = "o";
    private static final String FLAG_OUTPUT_TYPE = "t";
    
    private static final String FLAG_LOGGING_FILE = "fl";
    private static final String FLAG_LOGGING_CONSOLE = "cl";
    
    private static final String FLAG_ZIP = "zip";
    
    private static final String FLAG_COURSE = "co";
    private static final String FLAG_CHAPTER = "ch";
    
    public static void main(String[] args) {
        try {
            CommandLine cmd = parseArgs(args);
            Output output = getOutput(cmd);
            
            Converter converter = new Converter(output);
            setCourseAndChapter(converter, cmd);
            
            File outputFolder = getOutputFolder(cmd);
            outputType outputType = getOutputType(cmd);
            
            if(cmd.hasOption(FLAG_ZIP)){
                try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outputFolder.getAbsolutePath() + File.separator + "webslides.zip"))) {
                    converter.convert(getInputFile(cmd), zos);
                    converter.saveToZip(zos, outputType, Converter.outputFormat.HTML);
                }
            }
            else {
                String imageDir = outputFolder.getAbsolutePath() + File.separator + "images";
                converter.convert(getInputFile(cmd), imageDir);
                converter.saveToDirectory(outputFolder, outputType, Converter.outputFormat.HTML);
            }
            
            output.println("Conversion done");
            
        } catch (ParseException ex) { // TODO error message wegschrijven naar output
            java.util.logging.Logger.getLogger(OpenWebslidesConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(OpenWebslidesConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(OpenWebslidesConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void queueEntry(String[] args, OutputStream outputStream, Queue<String> queue, long id){
        try {
            CommandLine cmd = parseArgs(args);
            Output output = new GuardOutput(queue, id);
            
            Converter converter = new Converter(output);
            setCourseAndChapter(converter, cmd);
            
            outputType outputType = getOutputType(cmd);
            
            try (ZipOutputStream zos = new ZipOutputStream(outputStream)) {
                converter.convert(getInputFile(cmd), zos);
                converter.saveToZip(zos, outputType, Converter.outputFormat.HTML);
            }
            
        } catch (ParseException ex) {
            java.util.logging.Logger.getLogger(OpenWebslidesConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(OpenWebslidesConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static CommandLine parseArgs(String[] args) throws ParseException{
        Options options = new Options();
        options.addOption(FLAG_INPUT_FILE, true, "input file");
        options.addOption(FLAG_OUTPUT_FOLDER, true, "output folder");
        options.addOption(FLAG_OUTPUT_TYPE, true, "webslide output type");
        
        options.addOption(FLAG_LOGGING_FILE, "enable file logging");
        options.addOption(FLAG_LOGGING_CONSOLE, "enable console logging");
        
        options.addOption(FLAG_ZIP, "output in .zip");
        
        options.addOption(FLAG_COURSE, true, "course");
        options.addOption(FLAG_CHAPTER, true, "chapter");
        
        CommandLineParser parser = new DefaultParser();
        
        return parser.parse(options, args);
    }
    
    private static Output getOutput(CommandLine cmd) {
        Output output = new StdOutput();
        
        // console or file log wanted
        boolean consolelog = cmd.hasOption(FLAG_LOGGING_CONSOLE);
        boolean filelog = cmd.hasOption(FLAG_LOGGING_FILE);
        
        // file log -> create logger
        Logger logger = null;
        if(filelog)
            logger = new Logger(PropertiesReader.getValue(PropertiesReader.LOG_FILE),"OpenWebslidesConverterLog","OpenWebslidesConverter");
        
        //both console and file log
        if(consolelog && filelog)
            output = new StdLogOutput(logger);
        //only console log
        else if(consolelog)
            output = new StdOutput();
        //only file log
        else if(filelog)
            output = new LogOutput(logger);
        //else.. use default
        
        return output;
    }
    
    private static void setCourseAndChapter(Converter converter, CommandLine cmd) {
        if(cmd.hasOption(FLAG_COURSE))
            converter.setCourse(cmd.getOptionValue(FLAG_COURSE));
        if(cmd.hasOption(FLAG_CHAPTER))
            converter.setChapter(cmd.getOptionValue(FLAG_CHAPTER));
    }
    
    private static File getInputFile(CommandLine cmd) throws InvalidParameterException{
        if(!cmd.hasOption("i")) // no input file given
            throw new InvalidParameterException("No file provided to convert");
        else
            return new File(cmd.getOptionValue("i"));
    }
    
    private static File getOutputFolder(CommandLine cmd) throws IOException {
        //default dir or dir set in properties file
        String path = PropertiesReader.getValue(PropertiesReader.OUTPUT_DIR);
        
        //if other value is given
        if(cmd.hasOption(FLAG_OUTPUT_FOLDER))
            path = cmd.getOptionValue(FLAG_OUTPUT_FOLDER);
        
        File outputFolder = new File(path);
        if(!outputFolder.exists() && !outputFolder.mkdirs())
            throw new IOException("directory "+outputFolder.getAbsolutePath()+" does not exist or could not be created");
        
        return outputFolder;
    }
    
    private static outputType getOutputType(CommandLine cmd) {
        try{    
            outputType type = outputType.valueOf(PropertiesReader.getValue(PropertiesReader.OUTPUT_TYPE).toUpperCase());
        
            if(cmd.hasOption(FLAG_OUTPUT_TYPE))
                type = outputType.valueOf(cmd.getOptionValue(FLAG_OUTPUT_TYPE).toUpperCase());
            
            return type;
        }
        catch (IllegalArgumentException e){
            return outputType.valueOf(PropertiesReader.returnDefault(PropertiesReader.OUTPUT_TYPE));
        }
    }
    
}
