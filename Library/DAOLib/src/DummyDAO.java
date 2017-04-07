import database.DbFile;
import database.IDao;
import java.io.File;
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Karel
 */
public class DummyDAO implements IDao {

    HashMap<String, File> content;
    
    public DummyDAO(){
        content = new HashMap<>();
    }
    /**
     * get file with a serverid
     * @param serverId
     * @return 
     */
    @Override
    public File getFile(String serverId){
        return content.get(serverId);
    }
    
    /**
     * put a file with a unique serverid
     * @param serverId
     * @param file 
     */
    @Override
    public void putFile(String serverId, File file){
        content.put(serverId,file);
    }

    /**
     * Not supported in dummydao
     * @param serverId
     * @return 
     */
    @Override
    public DbFile getDbFile(String serverId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
