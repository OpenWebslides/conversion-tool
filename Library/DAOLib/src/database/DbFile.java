 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Karel
 */
public class DbFile {
    
    private File file;
    private String serverId;
    private Date uploaded;
    private String owner;
    private String fileName;

    /**
     * Create a dbfile intance
     * @param file
     * @param serverId
     * @param uploaded
     * @param owner
     * @param fileName
     * @throws IOException 
     */
    public DbFile(byte[] file, String serverId, Date uploaded, String owner, String fileName) throws IOException {
        System.out.println(fileName);
        FileUtils.writeByteArrayToFile(new File(DbConstants.FILE_SAVE_LOCATION + fileName), file);
        this.file = new File(DbConstants.FILE_SAVE_LOCATION + fileName);
        this.serverId = serverId;
        this.uploaded = uploaded;
        this.owner = owner;
        this.fileName = fileName;
    }

    /**
     * Get the file
     * @return 
     */
    public File getFile() {
        return file;
    }

    /**
     * Set the file
     * @param file 
     */
    public void setFile(File file) {
        this.file = file;
    }

    
    
    /**
     * Get the serverId
     * @return 
     */
    public String getServerId() {
        return serverId;
    }

    /**
     * Set the serverid
     * @param serverId 
     */
    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    /**
     * Get the date the file was stored in DB
     * @return 
     */
    public Date getUploaded() {
        return uploaded;
    }

    /**
     * Set the date the file was stored in DB
     * @param uploaded 
     */
    public void setUploaded(Date uploaded) {
        this.uploaded = uploaded;
    }

    /**
     * Get the owner
     * @return 
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Set the owner
     * @param owner 
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    /**
     * Return a string with some info about the dbfile
     * @return 
     */
    @Override
    public String toString(){
        return serverId + ": " + fileName + "->" + owner + " uploadDate: " + uploaded;
    }

    /**
     * Get the filename
     * @return 
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Set the filename
     * @param fileName 
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
}
