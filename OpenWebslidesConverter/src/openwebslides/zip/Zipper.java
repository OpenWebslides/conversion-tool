/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslides.zip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.IOUtils;

/**
 * The zipper can add files to a zip or copy the content of a zip inside another zip.
 * @author Jonas
 */
public class Zipper {
    
    /**
     * Add the content of the InputStream to the zip.
     * @param zos The ZipOutputStream presents the zip.
     * @param is The InputStream that holds the input.
     * @param zipEntry The name of the ZipEntry. Creating a file: "file.txt". Creating a file in a dir: "subdirectory/file.txt".
     * @throws ZipException If the file could not be added to the zip.
     */
    public static void add(ZipOutputStream zos, InputStream is, String zipEntry) throws ZipException{
        try {
            ZipEntry zipEnt = new ZipEntry(zipEntry);
            zos.putNextEntry(zipEnt);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = is.read(bytes)) >= 0) {
                zos.write(bytes, 0, length);
            }

            zos.closeEntry();
        } catch (Exception e) {
            throw new ZipException(e.getMessage());
        }
    }
    
    /**
     * Creates a new ZipEntry with the name ent.
     * @param zos The zip where the entry is added.
     * @param ent The name of the entry.
     * @throws IOException If the entry could not be added.
     */
    public static void newEntry(ZipOutputStream zos, String ent) throws IOException{
        ZipEntry zipEnt = new ZipEntry(ent);
        zos.putNextEntry(zipEnt);
    }
    
    /**
     * Close the current entry.
     * @param zos The zip.
     * @throws IOException If the entry could not be closed.
     */
    public static void closeEntry(ZipOutputStream zos) throws IOException{
        zos.closeEntry();
    }

    /**
     * Add the file to the zip with the zipEntry as filename.
     * @param zos The ZipOutputStream presents the zip.
     * @param file The file to be added.
     * @param zipEntry The name of the added file.
     * @throws ZipException If the file could not be added.
     */
    public static void addFile(ZipOutputStream zos, File file, String zipEntry) throws ZipException {
        if (!file.isFile()) {
            throw new ZipException("file is not a correct file");
        }

        try {
            try (FileInputStream fis = new FileInputStream(file)) {
                add(zos, fis, zipEntry);
            }
        } catch (Exception e) {
            throw new ZipException(e.getMessage());
        }
    }
    
    /**
     * Add the file to the zip. The added file gets the same name.
     * @param zos The ZipOutputStream presents the zip.
     * @param file The file to be added.
     * @throws ZipException If the file could not be added.
     */
    public static void addFile(ZipOutputStream zos, File file) throws ZipException {
        addFile(zos, file, file.getName());
    }

    /**
     * Add a folder to the zip.
     * @param zos The ZipOutputStream presents the zip.
     * @param node The folder to be added.
     * @throws ZipException If the file is not a directory or a file could not be added.
     */
    public static void addFolder(ZipOutputStream zos, File node) throws ZipException {
        if(!node.isDirectory()){
            throw new ZipException("file \"" + node.getAbsolutePath() + "\" is not a directory");
        }
        String source_folder = node.getAbsoluteFile().toString();
        List<File> files = generateFileList(node);
        for(File f : files){
            String entry = generateZipEntry(f.getAbsoluteFile().toString(), source_folder);
            addFile(zos, f, entry);
        }
    }
    
    /**
     * Creates a list of all the files inside the directory node.
     * @param node The directory.
     * @return A list of the files inside the directory node.
     */
    private static List<File> generateFileList(File node) {
        List<File> res = new ArrayList<>();
        //add file only
        if (node.isFile()) {
            res.add(node.getAbsoluteFile());
        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename : subNote) {
                res.addAll(generateFileList(new File(node, filename)));
            }
        }
        return res;
    }
    
    /**
     * Returns the path of the file, relative from the source folder.
     * @param file The file.
     * @param source_folder The source folder.
     * @return The path of the file, relative from the source folder.
     */
    private static String generateZipEntry(String file, String source_folder){
    	return file.substring(source_folder.length()+1, file.length());
    }

    /**
     * Creates a new zip file and adds the files.
     * @param outputLocation The path and name of the zip file. Must end with ".zip".
     * @param files The files that must be added.
     */
    public static void zipIt(String outputLocation, ArrayList<File> files) {

        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(new File(outputLocation));
            zos = new ZipOutputStream(fos);
            for (File file : files) {
                addFile(zos, file);
            }
        } catch (Exception e) {
            Logger.getLogger(Zipper.class.getName()).log(Level.SEVERE, null, e);
            //Logger.error("Could not create a zip in this location", e);
        } finally {
            try {
                zos.close();
                fos.close();
            } catch (Exception e) {
                Logger.getLogger(Zipper.class.getName()).log(Level.SEVERE, null, e);
                //Logger.error("Could not close outputstreams, perhaps they were never open", ex);
            }
        }
    }
    
    /**
     * Copies the files from the zip into the output folder.
     * @param zis The zip with the source files.
     * @param outputFolder The folder where the files are added.
     * @throws ZipException If a file could not be added.
     */
    public static void copyZip(ZipInputStream zis, File outputFolder) throws ZipException{
        if(!outputFolder.exists() || !outputFolder.isDirectory())
                throw new ZipException("the file "+outputFolder.getAbsolutePath()+" is not a folder or does not exist");
        
        try {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File file = new File(outputFolder.getAbsolutePath() + File.separator + entry.getName());
                file.getParentFile().mkdirs();
                
                if(!entry.isDirectory()){
                    try(FileOutputStream fos = new FileOutputStream(file)){
                        IOUtils.copy(zis, fos);
                    }
                }
            }

        } catch(IOException ex) {
            throw new ZipException("Template folder cannot be copied "+ex.getMessage());
        }
    }
    
    /**
     * Copies the files from the zip into the other zip.
     * @param zis The zip with the source files.
     * @param zos The destination zip.
     * @throws ZipException If a file could not be added.
     */
    public static void copyZip(ZipInputStream zis, ZipOutputStream zos) throws ZipException {
        try {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if(!entry.isDirectory()){
                    zos.putNextEntry(new ZipEntry(entry.getName()));
                    IOUtils.copy(zis, zos);
                    zos.closeEntry();
                }
            }

        } catch(IOException ex) {
            throw new ZipException("Template folder cannot be copied "+ex.getMessage());
        }
    }
}
