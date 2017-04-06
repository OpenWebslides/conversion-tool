/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.File;
import java.util.Date;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
        List<Map.Entry<String, Object>> parameters = new ArrayList<>();
        parameters.add(new AbstractMap.SimpleEntry<>("String", serverId));
        File file = new File((String)super.executeQueryDbRow("select file from uploads where serverId like ?", parameters).getCellByName("file"));        
        return file;
    }
    
    @Override
    public void putFile(String serverId, File file) {
        List<Map.Entry<String, Object>> parameters = new ArrayList<>();
        parameters.add(new AbstractMap.SimpleEntry<>("String", serverId));
        parameters.add(new AbstractMap.SimpleEntry<>("String", file.getName()));
        parameters.add(new AbstractMap.SimpleEntry<>("String", "none"));
        parameters.add(new AbstractMap.SimpleEntry<>("Date", new Date()));
        super.executeUpdate("insert into uploads (file, serverId, owner, uploadDate) values (?,?,?,?)", parameters);
        
    }
    
    public DbFile getFile(String serverId, int nullvalue) {
        List<Map.Entry<String, Object>> parameters = new ArrayList<>();
        parameters.add(new AbstractMap.SimpleEntry<>("String", serverId));
        DbRow row = super.executeQueryDbRow("select * from uploads where serverId like ?", parameters);
        DbFile file = new DbFile((String)row.getCellByName("file"),(String)row.getCellByName("serverId"),(java.util.Date)row.getCellByName("uploadDate"),(String)row.getCellByName("owner"));
        return file;
    }
    
    
    
}
