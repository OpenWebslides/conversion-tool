
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
    
    public File getFile(String serverId);
    
    public void putFile(String serverId,File file);
    
}
