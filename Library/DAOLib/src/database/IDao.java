package database;


import java.io.File;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Karel
 */
public interface IDao {
    
    /**
     * Get the file from database
     * @param serverId
     * @return 
     */
    public File getFile(String serverId);
    
    /**
     * Get a DbFile from database, this contains the file, uploadDate, owner,...
     * @param serverId
     * @return 
     */
    public DbFile getDbFile(String serverId);
    
    /**
     * Put the file in the database with serverId as unique id
     * @param serverId
     * @param file 
     */
    public void putFile(String serverId,File file);
    
}
