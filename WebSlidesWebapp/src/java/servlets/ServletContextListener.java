/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;


/**
 *
 * @author Laurens
 */
@WebListener
public class ServletContextListener implements javax.servlet.ServletContextListener { 
    
    private static final String DIRGUARD_JARPATH = "C:\\Temp\\dist\\DirectoryGuard.jar";
    private Process pb;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
//        try {
//            // in commentaar want niet meer nodig & resource hog
//            //pb = new ProcessBuilder("java", "-jar", ServletContextListener.DIRGUARD_JARPATH, "C:\\Temp\\uploads").start();
//        } catch (IOException ex) {
//            Logger.getLogger(ServletContextListener.class.getName()).log(Level.SEVERE,"failed to start process", ex);
//        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //may be dangerous in the case of random front-end breakage
        //pb.destroy();
        //Logger.getLogger(ServletContextListener.class.getName()).log(Level.SEVERE,"destroyed directory_guard");
    }

}
