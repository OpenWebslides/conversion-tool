/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion;

import objects.PPT;
import java.io.File;

/**
 *
 * @author Karel
 */
public interface IConverter {
    
    
    /**
     * Parse the file provided to the ConverterFactory. The content of the file will be inserted in the PPT object
     * @param ppt 
     */
    public void parse (PPT ppt);
    
}
