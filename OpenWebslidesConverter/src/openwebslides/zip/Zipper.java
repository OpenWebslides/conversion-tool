/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslides.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Jonas
 */
public class Zipper {
    
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
    
    public static void addFile(ZipOutputStream zos, File file) throws ZipException {
        addFile(zos, file, file.getName());
    }

    public static void addFolder(ZipOutputStream zos, File node) throws ZipException {
        if(!node.isDirectory()){
            throw new ZipException("file is not a correct directory");
        }
        String source_folder = node.getAbsoluteFile().toString();
        List<File> files = generateFileList(node);
        for(File f : files){
            String entry = generateZipEntry(f.getAbsoluteFile().toString(), source_folder);
            addFile(zos, f, entry);
        }
    }
    
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
    
    private static String generateZipEntry(String file, String source_folder){
    	return file.substring(source_folder.length()+1, file.length());
    }

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
}
