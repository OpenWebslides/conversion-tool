package conversion;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import conversion.pdf.PDFConverter;
import conversion.pdf.util.PDFException;
import conversion.powerpoint.PPTConverter;
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
     * @throws conversion.pdf.util.PDFException 
     */
    public static IConverter getConverter(File file) throws PDFException{
        String ext2 = getExtension(file.getName());
        switch(ext2){
            case "pptx" : return new PPTConverter(file);
            case "pdf" : return new PDFConverter(file);
            default: throw new IllegalArgumentException("This is not a supported file");
        }
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
