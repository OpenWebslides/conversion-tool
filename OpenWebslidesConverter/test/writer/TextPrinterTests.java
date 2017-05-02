/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package writer;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import objects.FontDecoration;
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
public class TextPrinterTests {
    
    public TextPrinterTests() {
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
    public void main(){
        Deque<FontDecoration> stack = new ArrayDeque<>();
        stack.addFirst(FontDecoration.BOLD);
        stack.addFirst(FontDecoration.BOLD);
        stack.addFirst(FontDecoration.ITALIC);
        
        for(Iterator itr = stack.iterator();itr.hasNext();){
            System.out.println("**");
            System.out.println(itr.next());
        }
    }
}
