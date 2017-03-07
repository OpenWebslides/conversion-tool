/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import openwebslideslogger.Logger;
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
public class LoggerUnitTest {
    
    public LoggerUnitTest() {
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
    
    @Test
    public void logTest1(){
        Logger logger = new Logger("C:\\temp\\","Testlog1","Unit Test");
        logger.println("Test line 0");
        logger.println("Test line 1");
        logger.print("Test li");
        logger.print("ne 2\r\n");
        logger.println("Test line 3");
    }
    
    @Test
    public void logTest2(){
        Logger logger = new Logger("C:\\temp\\logtest\\","Testlog2","Unit Test");
        logger.println("Test line 0");
        logger.println("Test line 1");
        logger.print("Test li");
        logger.print("ne 2\r\n");
        logger.println("Test line 3");
    }
    
    @Test
    public void logTest3(){
        Logger logger = new Logger("C:\\temp\\logtest2\\subdir\\","Testlog3","Unit Test");
        logger.println("Test line 0");
        logger.println("Test line 1");
        logger.print("Test li");
        logger.print("ne 2\r\n");
        logger.println("Test line 3");
    }
    
    @Test
    public void logQueueTest(){
        Queue<String> queue = new ConcurrentLinkedQueue<>();
        queue.offer("Test line 0");
        queue.offer("Test line 1");
        queue.offer("Test line 2");
        queue.offer("Test line 3");
        
        Logger logger = new Logger("C:\\temp\\","Testlog1","Unit Test");
        logger.writeQueue(queue);
    }
}
