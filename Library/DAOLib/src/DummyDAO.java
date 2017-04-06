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
    
    @Override
    //Later outputstream
    public File getFile(String serverId){
        return content.get(serverId);
    }
    
    @Override
    public void putFile(String serverId, File file){
        content.put(serverId,file);
    }
    
}
