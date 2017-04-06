/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.File;
import java.util.Date;

/**
 *
 * @author Karel
 */
public class DbFile {
    
    private String file;
    private String serverId;
    private Date uploaded;
    private String owner;

    public DbFile(String file, String serverId, Date uploaded, String owner) {
        this.file = file;
        this.serverId = serverId;
        this.uploaded = uploaded;
        this.owner = owner;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    
    

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public Date getUploaded() {
        return uploaded;
    }

    public void setUploaded(Date uploaded) {
        this.uploaded = uploaded;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    public String toString(){
        return serverId + ": " + file + "->" + owner + " uploadDate: " + uploaded;
    }
    
}
