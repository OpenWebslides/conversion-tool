package ziptest;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Karel
 */
public class Zipper {
    
    private FileOutputStream fos;
    private ZipOutputStream zos;
    
    /**
     * Create an instance of zipper
     * @param outputLocation The location the zip should be saved (Path + filename.zip) eg. C://temp//archive.zip
     */
    public Zipper(String outputLocation){
        try{
            fos = new FileOutputStream(new File(outputLocation));
            zos = new ZipOutputStream(fos);
        }catch(Exception e){
            Logger.getLogger(Zipper.class.getName()).log(Level.SEVERE, null, e);
            //Logger.error("Could not create a zip in this location", e);
        }
    }
    
    
    /**
     * Create an instane of zipper, add an arraylist of files to the zip
     * @param outputLocation The location the zip should be saved (Path + filename.zip) eg. C://temp//archive.zip
     * @param files files to be added to the zip
     */
    public Zipper(String outputLocation, ArrayList<File> files){
    }
    
    /**
     * Add a file to the zip
     * @param fileName
     * @param zos
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void addToZipFile(File file) throws FileNotFoundException, IOException{      
                if(file.isFile()){
                    FileInputStream fis = new FileInputStream(file);
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zos.putNextEntry(zipEntry);
                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                            zos.write(bytes, 0, length);
                    }

                    zos.closeEntry();
                    fis.close();
                }
	}
    
    private static void addToZipFile(File file, ZipOutputStream zos) throws FileNotFoundException, IOException{
                System.out.println("Add: " + file);
                if(file.isFile()){
                    FileInputStream fis = new FileInputStream(file);
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zos.putNextEntry(zipEntry);
                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                            zos.write(bytes, 0, length);
                    }

                    zos.closeEntry();
                    fis.close();
                }
	}
    public static void zipIt(String outputLocation, ArrayList<File> files){
        
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try{
            fos = new FileOutputStream(new File(outputLocation));
            zos = new ZipOutputStream(fos);
            for(File file : files){
                addToZipFile(file, zos);
                        }
                }catch(Exception e){
                    Logger.getLogger(Zipper.class.getName()).log(Level.SEVERE, null, e);
                    //Logger.error("Could not create a zip in this location", e);
                }
                finally{
                    try{
                    zos.close();
                    fos.close();
                    }
                    catch(Exception e){
                        Logger.getLogger(Zipper.class.getName()).log(Level.SEVERE, null, e);
                        //Logger.error("Could not close outputstreams, perhaps they were never open", ex);
                    }
                }
    }
    
    public void zipIt(){
        try {
            zos.close();
            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(Zipper.class.getName()).log(Level.SEVERE, null, ex);
            //Logger.error("Could not close outputstreams, perhaps they were never open", ex);
        }
    }
}
