package conversion;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.zip.ZipOutputStream;
import objects.PPT;
import output.*;

/**
 *
 * @author Karel
 */
public interface IConverter {

    /**
     * Parse the file provided to the ConverterFactory. The content of the file
     * will be inserted in the PPT object
     *
     * @param ppt PPT
     * @param saveLocation String
     */
    public void parse(PPT ppt, String saveLocation);
    
    

    public void parse(PPT ppt, ZipOutputStream outputStream, String saveLocation);

    public void setOutput(Output output);

}
