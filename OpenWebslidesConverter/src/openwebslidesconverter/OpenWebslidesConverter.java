/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslidesconverter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidParameterException;
import java.util.Queue;
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
    
    /**
     * Starts the converter with the given arguments.
     * @param args The arguments for the converter.
     */
    public static void main(String[] args) {
        Output output = new StdOutput();
        try {
            CommandLine cmd = parseArgs(args);
            output = getOutput(cmd);
            
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
            
        } catch (ParseException ex) { // exception in parseArgs method
            // output is not initialized yet, print to console
            System.err.println("cannot start the converter because of false argument(s): " + ex.getMessage());
        } catch (IOException | InvalidParameterException | WebslidesConverterException ex) {
            output.error("error while converting: "+ex.getMessage(), ex.getMessage());
        }
    }
    
    /**
     * Starts the converter with the given arguments. The result is written as a zip file to the outputStream. Logging is done to queue with the id.
     * @param args The arguments for the converter.
     * @param outputStream The OutputStream the zip file is written to.
     * @param queue The Queue of String where the log messages are collected.
     * @param id The id used in the logging to identify the converter.
     * @throws WebslidesConverterException If something went wrong while converting.
     */
    public static void queueEntry(String[] args, OutputStream outputStream, Queue<String> queue, long id) throws WebslidesConverterException{
        Output output = new GuardOutput(queue, id);
        try {
            CommandLine cmd = parseArgs(args);
            
            Converter converter = new Converter(output);
            setCourseAndChapter(converter, cmd);
            
            outputType outputType = getOutputType(cmd);
            
            try (ZipOutputStream zos = new ZipOutputStream(outputStream)) {
                converter.convert(getInputFile(cmd), zos);
                converter.saveToZip(zos, outputType, Converter.outputFormat.HTML);
            }
            
        } catch (ParseException ex) {
            output.error("cannot start the converter because of false argument(s): " + ex.getMessage(), ex.getMessage());
            throw new WebslidesConverterException(ex);
        } catch (IOException | InvalidParameterException | WebslidesConverterException ex) {
            output.error(ex.getMessage(), ex.getMessage());
            throw new WebslidesConverterException(ex);
        }
    }
    
    /**
     * Parses the arguments args and returns a CommandLine object which holds the info.
     * @param args The arguments to be parsed.
     * @return a CommandLine object that holds the info from the args.
     * @throws ParseException If the args could not be parsed.
     */
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
    
    /**
     * Gets the Output object specified in cmd. If none is specified a StdOutput object is returned.
     * @param cmd The CommandLine object that holds the wanted output info.
     * @return The Output object specified in cmd.
     */
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
    
    /**
     * Reads the CommandLine and sets the course and chapter of the converter if available.
     * @param converter The converter on which the course and chapter will be set.
     * @param cmd The CommandLine which may contain the course and chapter.
     */
    private static void setCourseAndChapter(Converter converter, CommandLine cmd) {
        if(cmd.hasOption(FLAG_COURSE))
            converter.setCourse(cmd.getOptionValue(FLAG_COURSE));
        if(cmd.hasOption(FLAG_CHAPTER))
            converter.setChapter(cmd.getOptionValue(FLAG_CHAPTER));
    }
    
    /**
     * Returns the input file specified in cmd.
     * @param cmd The CommandLine that must hold the input file.
     * @return Returns the input file specified in cmd.
     * @throws InvalidParameterException If the input file is not specified in cmd.
     */
    private static File getInputFile(CommandLine cmd) throws InvalidParameterException{
        if(!cmd.hasOption("i")) // no input file given
            throw new InvalidParameterException("No file provided to convert");
        else
            return new File(cmd.getOptionValue("i"));
    }
    
    /**
     * Returns the output directory in which the converted file should be saved. If not specified in cmd the directory set in the converter.properties file is used.
     * If none of both is set the default is used.
     * @param cmd The CommandLine that could hold the output directory.
     * @return Returns the output folder in which the converted file should be saved.
     * @throws IOException If the output directory does not exist or could not be created.
     */
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
    
    /**
     * Returns the output type specified in cmd. If not specified in cmd the directory set in the converter.properties file is used.
     * If none of both is set the default is used.
     * @param cmd The CommandLine object that could hold the output type.
     * @return The output type that should be used.
     */
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
