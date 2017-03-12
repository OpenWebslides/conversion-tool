/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.UploadedFile;
import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Joann
 */
@RequestScoped
public class SaveBean  {

    private UploadedFile uploadedFile;    
    private String colour;
    //private String visibility = "false";
    
   
    /**
     * Creates a new instance of SaveBean
     */
    public SaveBean() {
    }

   /* public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }
    public void makeVisible(){
        if(visibility.equals("false")){
            visibility = "true";
        }
        else{
            visibility = "false"; //niet zeker wat de gebruiker liever zou hebben...
        }
    }*/
    

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        System.out.println("I set the colour to "+colour);
        this.colour = colour;
    }
    
    
    
    
   
    public UploadedFile getFile() {
        return uploadedFile;
    }

    public void setFile(UploadedFile file) {
        this.uploadedFile = file;
    }

    public void save() throws IOException {
        String filename = FilenameUtils.getName(uploadedFile.getFileName());
        InputStream input = uploadedFile.getInputstream();
        OutputStream output = new FileOutputStream(new File(System.getProperty("user.home")+"/Downloads", filename));

        try {
            IOUtils.copy(input, output);
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }
    }

    public void handleFileUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, message);
        
        String sessionToken = facesContext.getExternalContext().getSessionId(false);       
                
        uploadedFile = event.getFile();
        String filename = FilenameUtils.getName(uploadedFile.getFileName());
        InputStream input = uploadedFile.getInputstream();
        OutputStream output = new FileOutputStream(new File(System.getProperty("user.home")+"/Downloads", sessionToken+"_"+filename));

        try {
            IOUtils.copy(input, output);
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }        
    }

}
