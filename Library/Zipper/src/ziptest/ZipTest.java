/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ziptest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 *
 * @author Karel
 */
public class ZipTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        example1();
        example2();
    }
    
    /**
     * OOP
     * @throws IOException 
     */
    public static void example1() throws IOException{
        //Maak zipper object, en waar zip ogeslaan moet worden
        Zipper zipper = new Zipper("C://temp//archive1.zip");
        
        //Voeg een voor een zips toe
        zipper.addToZipFile(new File("C://temp//Serverlog_20170228.log"));        
        zipper.addToZipFile(new File("C://temp//Serverlog_20170228Copy.log"));
        
        //Maak zip bestand
        zipper.zipIt();
    }
    
    /**
     * Static
     */
    public static void example2(){
        
        //Lijst van bestanden die moeten toegevoegd worden
        ArrayList<File> files = new ArrayList();
        files.add(new File("C://temp//Serverlog_20170228.log"));
        files.add(new File("C://temp//Serverlog_20170228Copy.log"));
        
        //Maak zip
        Zipper.zipIt("C://temp//archive2.zip", files);
    }
    
     
    
    
}
