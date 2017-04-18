/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zip;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.zip.ZipOutputStream;
import openwebslides.zip.ZipException;
import openwebslides.zip.Zipper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jonas
 */
public class ZipTest {
    
    public ZipTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    /*
     * Tests is afhankelijk van filesystem
    
    @Test
    public void test1() throws FileNotFoundException, ZipException, IOException{
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File("C:\\temp\\ziptest\\result.zip")));
        File folder = new File("C:\\temp\\ziptest\\source");
        Zipper.addFolder(zos, folder);
        zos.close();
    }
    */
    @Test
    public void test2() throws FileNotFoundException, ZipException, IOException{
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File("C:\\temp\\ziptest\\result.zip")));
        File folder = new File("C:\\temp\\ziptest\\source");
        Zipper.addFolder(zos, folder);
        
        
        
        Zipper.newEntry(zos, "test.txt");
        
        OutputStreamWriter writer = new OutputStreamWriter(zos);
        writer.write("Lorem ipsum dolor sit amet.");
        writer.flush();
        
        Zipper.closeEntry(zos);
        
        
        
        zos.close();
    }
    
    
}
