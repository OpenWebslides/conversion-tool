/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import database.DbFileManagement;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 *
 * @author Karel
 */
public class DbTest {
    
    public DbTest() {
    }
    

    @Test
    public void testMeth() throws InterruptedException, IOException {
            ArrayList<Multi> t = new ArrayList<>();
            
            for(int i = 0; i < 1; i++){
                t.add(new Multi(i));  
                t.get(i).start();  
            }
            
            for(int i = 0; i < 1; i++){ 
                t.get(i).join();  
            }
    }
    
    class Multi extends Thread{  
        
        private int i;
        private DbFileManagement dbm;
        

        private Multi(int i) {
            this.i = i;
            System.out.println("created " + i);
            this.dbm = new DbFileManagement();
        }
        @Override
        public void run(){  
            dbm.putFile(""+i,new File("C:\\temp\\test.exe"));
            System.out.println(dbm.getFile(""+i).toString());
            this.stop();
        }
    } 
}
