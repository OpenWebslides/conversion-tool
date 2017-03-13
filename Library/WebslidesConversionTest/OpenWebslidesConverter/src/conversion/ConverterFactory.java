/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion;

import java.io.File;

/**
 *
 * @author Karel
 */
public class ConverterFactory {
    
    /**
     * Return an IConverter object
     * The correct implementation is decided by the file type
     * @param file
     * @return 
     */
    public static IConverter getConverter(File file){
        String ext2 = getExtension(file.getName());
        switch(ext2){
            case "pptx" : return new PPTConverter(file);
        }
        return null;
    }
    
    private static String getExtension(String fileName){
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i+1);
        }
        return extension;
    }
}
