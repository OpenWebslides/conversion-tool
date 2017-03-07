/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslidesconverter;

import java.io.FileInputStream;
import java.util.Properties;

/**
 *
 * @author Jonas
 */
public class PropertiesReader {
    public static String PROPERTIES_FILE = "converter.properties";
    
    public static String DEFAULT_LOG_FILE = "c:\\temp\\";
    public static String DEFAULT_OUTPUT_TYPE = "raw";
    public static String DEFAULT_OUTPUT_DIR = "output";
    
    public static final int LOG_FILE = 0;
    public static final int OUTPUT_TYPE = 1;
    public static final int OUTPUT_DIR = 2;
    
    public static String getValue(int k){
        if(k > 2 || k < 0) return null;
        
        try(FileInputStream stream = new FileInputStream(PROPERTIES_FILE)){
            Properties prop = new Properties();
            prop.load(stream);
            
            String res = null;
            switch(k){
                case LOG_FILE:
                    res = prop.getProperty("LOG_FILE");
                    break;
                case OUTPUT_DIR:
                    res = prop.getProperty("OUTPUT_DIR");
                    break;
                case OUTPUT_TYPE:
                    res = prop.getProperty("OUTPUT_TYPE");
            }
            
            if(res == null)
                return returnDefault(k);
            else
                return res;
        // on every exception use the default values
        } catch (Exception ex) {
            return returnDefault(k);
        }
    }
    
    public static String returnDefault(int k){
        switch(k){
                case LOG_FILE:
                    return DEFAULT_LOG_FILE;
                case OUTPUT_DIR:
                    return DEFAULT_OUTPUT_DIR;
                case OUTPUT_TYPE:
                    return DEFAULT_OUTPUT_TYPE;
                default:
                    return null;
        }
    }
}
