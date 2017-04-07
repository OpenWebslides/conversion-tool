/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import logger.Logger;
import org.apache.commons.io.FileUtils;


/**
 *
 * @author Karel
 */
public class DbFileManagement extends DbManagement implements IDao{

    public DbFileManagement(){
        super();
        
    }
    
    @Override
    public File getFile(String serverId) {
        try{
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("String", serverId);
            DbRow row = super.executeQueryDbRow("select " + 
                                                DbConstants.FILES_TABLE_FILE +  
                                                "," + 
                                                DbConstants.FILES_TABLE_FILENAME +                                                          
                                                " from "+ DbConstants.FILES_TABLE + 
                                                " where " + DbConstants.FILES_TABLE_SERVERID +
                                                " like ?", parameters);
            byte[] file = (byte[])row.getCellByName(DbConstants.FILES_TABLE_FILE);    
            String name = (String)row.getCellByName(DbConstants.FILES_TABLE_FILENAME);
            System.out.println(row.toString());
            FileUtils.writeByteArrayToFile(new File(DbConstants.FILE_SAVE_LOCATION + name), file);
            return new File(DbConstants.FILE_SAVE_LOCATION + name);
        }
        catch(Exception e){
            output.println(Logger.error("There was an error while getting the file from DB", e));
        }
        return null;
    }
    
    @Override
    public void putFile(String serverId, File file) {
        try{
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("Blob", Files.readAllBytes(Paths.get(file.getPath())));
            parameters.put("String", serverId);
            parameters.put("String", "none");
            parameters.put("Date", new Date());
            parameters.put("String", file.getName());
            super.executeUpdate("insert into " + DbConstants.FILES_TABLE + " (" +
                                                 DbConstants.FILES_TABLE_FILE + "," +
                                                 DbConstants.FILES_TABLE_SERVERID +"," + 
                                                 DbConstants.FILES_TABLE_OWNER +"," + 
                                                 DbConstants.FILES_TABLE_UPLOADDATE +"," + 
                                                 DbConstants.FILES_TABLE_FILENAME +
                                                 ") values (?,?,?,?,?)", parameters);
        }
        catch(Exception e){
            output.println(Logger.error("There was an error while putting the file in DB", e));
        }
        
    }
    
    
    @Override
    public DbFile getDbFile(String serverId) {
        try{
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("String", serverId);
            DbRow row = super.executeQueryDbRow("select * from " + DbConstants.FILES_TABLE + " where " + DbConstants.FILES_TABLE_SERVERID +" like ?", parameters);
            DbFile file = new DbFile((byte[])row.getCellByName(DbConstants.FILES_TABLE_FILE),
                                    (String)row.getCellByName(DbConstants.FILES_TABLE_SERVERID),
                                    (java.util.Date)row.getCellByName(DbConstants.FILES_TABLE_UPLOADDATE),
                                    (String)row.getCellByName(DbConstants.FILES_TABLE_OWNER),
                                    (String)row.getCellByName(DbConstants.FILES_TABLE_FILENAME)
                                );
            return file;
        }
        catch (Exception e){
            output.println(Logger.error("There was an error while getting the dbfile from DB", e));
        }
        return null;
    }
    
    
    
}
