/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.UploadedFile;
import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;

/**
 *
 * @author Joann
 */
@RequestScoped
public class SaveBean  {

    private UploadedFile uploadedFile;    
    private String colour;

    private DefaultStreamedContent download;
    private String downloadFileName;
    private boolean downloadDisabled;



   
    /**
     * Creates a new instance of SaveBean
     */
    public SaveBean() {
        downloadDisabled = true;
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
    
    
    public void setDownload(DefaultStreamedContent download) {
        this.download = download;
    }

    public DefaultStreamedContent getDownload() throws Exception {
        System.out.println("GET = " + download.getName());
        return download;
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
    
    public void prepDownload() throws IOException {
        File file = new File("C:\\temp\\pres.pptx");
        InputStream input = new FileInputStream(file);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        setDownload(new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName()));
        downloadFileName = file.getName();
        downloadDisabled = false;
        System.out.println("PREP = " + download.getName() + downloadDisabled);
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

    public boolean isDownloadDisabled() {
        return downloadDisabled;
    }

    public void setDownloadDisabled(boolean downloadDisabled) {
        this.downloadDisabled = downloadDisabled;
    }

    public String getDownloadFileName() {
        return downloadFileName;
    }

    public void setDownloadFileName(String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }

}
