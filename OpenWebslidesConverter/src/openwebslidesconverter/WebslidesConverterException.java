/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslidesconverter;

/**
 *
 * @author Jonas
 */
public class WebslidesConverterException extends Exception {

    WebslidesConverterException(String msg) {
        super(msg);
    }
    
    WebslidesConverterException(Exception ex) {
        super(ex.getMessage());
    }
}
