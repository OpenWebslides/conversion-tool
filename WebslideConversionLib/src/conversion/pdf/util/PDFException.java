/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.pdf.util;

/**
 * A PDF Exception can be thrown when the parsing of a pdf fails completely
 * @author Gertjan
 */
public class PDFException extends Exception {
    private String Message;
    public PDFException(){
        String Message = "";
    }
    public PDFException(String message){
        this.Message = message;
    }
    public void setMessage(String message){
        this.Message = message;
    }
    
    @Override
    public String getMessage(){
        return "PDF parsing failed - " + this.Message;
    }
}
