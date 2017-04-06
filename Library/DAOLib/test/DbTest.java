/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import database.DbFileManagement;
import database.DbRow;
import java.io.File;
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
    public void main() throws InterruptedException {
            ArrayList<Multi> t = new ArrayList<>();
            for(int i = 0; i < 1000; i++){
                t.add(new Multi(i));  
                t.get(i).start();  
            }
            
            
            while(true){
            TimeUnit.SECONDS.sleep(1);
            System.out.println("READY");}
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
            dbm.putFile(""+i,new File(""+i));
            this.stop();
        }
    } 
}
