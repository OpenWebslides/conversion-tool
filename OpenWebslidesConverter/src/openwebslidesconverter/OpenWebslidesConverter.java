/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslidesconverter;

import java.security.InvalidParameterException;
import java.util.Queue;
import openwebslides.output.GuardOutput;
import openwebslides.output.LogOutput;
import openwebslides.output.Output;
import openwebslides.output.StdLogOutput;
import openwebslides.output.StdOutput;
import openwebslideslogger.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author Laurens
 */
public class OpenWebslidesConverter {
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Output output = new StdOutput();
        startConverter(args, output);
    }
    
    /**
     * Entrypoint with a queue for output
     * @param args equal to the command line arguments
     * @param queue the queue where the GuardOutput writes to
     * @param id the id of the process or thread
     */
    public static void queueEntry(String[] args, Queue<String> queue, long id){
        Output output = new GuardOutput(queue, id);
        startConverter(args, output);
    }
    
    private static void startConverter(String[] args, Output output){
        try{
            Converter converter = resolveParams(args, output);
            converter.convert();
        }
        catch(ParseException | InvalidParameterException e){
            output.error("Error: " + e.getMessage(), null);
        }
    }
    
    private static Converter resolveParams(String[] args, Output output) throws ParseException,InvalidParameterException {
        Options options = new Options();
        options.addOption("i", true, "input file");
        options.addOption("o", true, "output folder");
        options.addOption("t", true, "webslide output type");
        options.addOption("fl", "enable file logging");
        options.addOption("cl", "enable console logging");
        
        CommandLineParser parser = new DefaultParser();
        
        //try {
            CommandLine cmd = parser.parse(options, args);
            return createConverter(cmd, output);
        /*} catch (ParseException | InvalidParameterException ex) {
            
        }*/
    }
    
    private static Converter createConverter(CommandLine cmd, Output out) throws InvalidParameterException{
        String inputFile;
        String outputType = PropertiesReader.getValue(PropertiesReader.OUTPUT_TYPE);
        String outputDir = null;
        
        //default output
        Output output = out;
        
        boolean consolelog, filelog;
        
        // no input file
        if(!cmd.hasOption("i"))
            throw new InvalidParameterException("No file provided to convert");
        else
            inputFile = cmd.getOptionValue("i");
        
        // output directory
        if(cmd.hasOption("o"))
            outputDir = cmd.getOptionValue("o");
        
        // output type
        if(cmd.hasOption("t"))
            outputType = cmd.getOptionValue("t");
        
        // console or file log wanted
        consolelog = cmd.hasOption("cl");
        filelog = cmd.hasOption("fl");
        
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
        
        return new Converter(outputType, inputFile, outputDir, output);
    }
    
}
