package openwebslideslogger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Queue;
import java.util.logging.Level;

/**
 *
 * @author Karel
 */
public class Logger {

    private final String LOGPATH;
    private final String LOGNAME;

    private String log;
    private final File file;
    private boolean printingLine;

    /**
     * Create a Logger instance
     *
     * @param LOGPATH
     * @param LOGNAME
     * @param identification
     */
    public Logger(String LOGPATH, String LOGNAME, String identification) {
        this.LOGNAME = LOGNAME;
        this.LOGPATH = LOGPATH;
        log = identification + "\r\n";
        
        Date date = new Date();
        //Name format: ServerLog + yyyyMMdd for example; ServerLog_20170223.log
        this.file = new File(LOGPATH + LOGNAME + new SimpleDateFormat("yyyyMMdd").format(date) + ".log");
        file.getParentFile().mkdirs();
    }

    /**
     * Method used by the Logger to create the log file if needed and append the
     * log
     *
     * @param logLines : Contains the log lines
     */
    private static String writeToLog(ArrayList<String> logLines, String logSort) {
            Date date = new Date();
            String temp = "";
            temp += new SimpleDateFormat("HH:mm:ss").format(date);
            temp += "\r\n" + logSort;
            for (String line : logLines) {
                temp += "\r\n" + "\t" + line;
            }
            temp += "\r\n";
            return temp;
    }

    /**
     * Create a not specified log, provide a subject and log information
     *
     * @param subject What is the error about (String)
     * @param logInfo What is the error, is there a fix, other info? (String
     * varargs)
     * @return 
     */
    public static String log(String subject, String... logInfo) {
        ArrayList<String> log = new ArrayList<>();
        log.add(subject);
        for (String logLine : logInfo) {
            log.add(logLine);
        }
        return writeToLog(log, "Log");
    }

    /**
     * Create a File Uploaded log
     *
     * @param file The uploaded file (File)
     * @param userName The username of the user who uploaded the file (String)
     * @return 
     */
    public static String fileUploaded(File file, String userName) {
        ArrayList<String> log = new ArrayList<>();
        log.add(userName + " uploaded " + file.getName() + "(" + file.length() / 931 + "Kb)");
        return writeToLog(log, "File Uploaded");
    }

    /**
     * Create a File Converted log
     *
     * @param file The uploaded file (File)
     * @param userName The username of the user who uploaded the file (String)
     * @return 
     */
    public static String fileConverted(File file, String userName) {
        ArrayList<String> log = new ArrayList<>();
        log.add(file.getName() + "(" + file.length() / 931 + "Kb)" + " from " + userName + " was converted");
        return writeToLog(log, "File Converted");
    }

    /**
     * Create a File Deleted log
     *
     * @param file The deleted file
     * @param userName The owner of the deleted file
     * @return 
     */
    public static String fileDeleted(File file, String userName) {
        ArrayList<String> log = new ArrayList<>();
        log.add(file.getName() + "(" + file.length() / 931 + "Kb)" + " from " + userName + " was deleted");
        return writeToLog(log, "File Deleted");
    }

    /**
     * Create an Exception log
     *
     * @param errorCause what caused the error (String)
     * @param e the catched error (Exception)
     * @return 
     */
    public static String error(String errorCause, Exception e) {
        ArrayList<String> errorLog = new ArrayList<>();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        errorLog.add(sw.toString());
        return writeToLog(errorLog, "Error - " + errorCause);
    }

    /**
     * Print a line to the logger buffer, end with newline
     *
     * @param out
     */
    public void println(String out) {
        Date date = new Date();
        log += new SimpleDateFormat("HH:mm:ss").format(date) + " ";
        log += out + "\r\n";
        writeLogToFile();
        printingLine = false;
    }

    /**
     * Print a line to the logger buffer
     *
     * @param out
     */
    public void print(String out) {
        if(!printingLine){
            Date date = new Date();
            log += new SimpleDateFormat("HH:mm:ss").format(date) + " ";
        }
        printingLine = true;
        log += out;
        writeLogToFile();
    }
    
    private void writeLogToFile(){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))){
            writer.write(log);
            writer.flush();
            log = "";
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void writeQueue(Queue<String> queue){
        if(queue.isEmpty()) return;
        
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))){
            String s = queue.poll();
            while(s != null){
                writer.write(s + "\r\n");
                s = queue.poll();
            }
            writer.write("\r\n");
            writer.flush();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
