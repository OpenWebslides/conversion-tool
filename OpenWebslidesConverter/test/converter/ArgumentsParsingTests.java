/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import java.io.File;
import java.lang.reflect.Method;
import junit.framework.Assert;
import openwebslidesconverter.Converter.outputType;
import openwebslidesconverter.OpenWebslidesConverter;
import org.apache.commons.cli.CommandLine;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import output.Output;

/**
 *
 * @author Jonas
 */
public class ArgumentsParsingTests {
    
    public ArgumentsParsingTests() {
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
    public void outputTest1() throws Exception{
        String[] args = new String[] {};
        
        // Reflection needed because parseArgs method is private -> set temporary accessible here
        Class owc = OpenWebslidesConverter.class;
        Method parseArgs = owc.getDeclaredMethod("parseArgs", args.getClass());
        parseArgs.setAccessible(true);
        
        CommandLine cmd = (CommandLine)parseArgs.invoke(owc, (Object)args);
        
        Method getOutput = owc.getDeclaredMethod("getOutput", CommandLine.class);
        getOutput.setAccessible(true);
        
        Output output = (Output)getOutput.invoke(owc, (Object)cmd);
        
        Assert.assertEquals("class output.StdOutput", output.getClass().toString());
    }
    
    @Test
    public void outputTest2() throws Exception{
        String[] args = new String[] {"-cl"};
        
        // Reflection needed because parseArgs method is private -> set temporary accessible here
        Class owc = OpenWebslidesConverter.class;
        Method parseArgs = owc.getDeclaredMethod("parseArgs", args.getClass());
        parseArgs.setAccessible(true);
        
        CommandLine cmd = (CommandLine)parseArgs.invoke(owc, (Object)args);
        
        Method getOutput = owc.getDeclaredMethod("getOutput", CommandLine.class);
        getOutput.setAccessible(true);
        
        Output output = (Output)getOutput.invoke(owc, (Object)cmd);
        
        Assert.assertEquals("class output.StdOutput", output.getClass().toString());
    }
    
    @Test
    public void outputTest3() throws Exception{
        String[] args = new String[] {"-fl"};
        
        // Reflection needed because parseArgs method is private -> set temporary accessible here
        Class owc = OpenWebslidesConverter.class;
        Method parseArgs = owc.getDeclaredMethod("parseArgs", args.getClass());
        parseArgs.setAccessible(true);
        
        CommandLine cmd = (CommandLine)parseArgs.invoke(owc, (Object)args);
        
        Method getOutput = owc.getDeclaredMethod("getOutput", CommandLine.class);
        getOutput.setAccessible(true);
        
        Output output = (Output)getOutput.invoke(owc, (Object)cmd);
        
        Assert.assertEquals("class output.LogOutput", output.getClass().toString());
    }
    
    @Test
    public void outputTest4() throws Exception{
        String[] args = new String[] {"-fl","-cl"};
        
        // Reflection needed because parseArgs method is private -> set temporary accessible here
        Class owc = OpenWebslidesConverter.class;
        Method parseArgs = owc.getDeclaredMethod("parseArgs", args.getClass());
        parseArgs.setAccessible(true);
        
        CommandLine cmd = (CommandLine)parseArgs.invoke(owc, (Object)args);
        
        Method getOutput = owc.getDeclaredMethod("getOutput", CommandLine.class);
        getOutput.setAccessible(true);
        
        Output output = (Output)getOutput.invoke(owc, (Object)cmd);
        
        Assert.assertEquals("class output.StdLogOutput", output.getClass().toString());
    }
    
    @Test
    public void outputDirTest1() {
        File file = new File("C:\\temp\\tests\\outputDirTest1\\sub\\sub2");
        if(!file.exists() && !file.mkdirs())
            System.out.println("fout");
        else
            System.out.println("ok");
    }
    
    @Test
    public void outputTypeTest1() throws Exception{
        
        /*
         * Make sure the properties file does not override the type value
         */
        
        String[] args = new String[] {};
        
        // Reflection needed because parseArgs method is private -> set temporary accessible here
        Class owc = OpenWebslidesConverter.class;
        Method parseArgs = owc.getDeclaredMethod("parseArgs", args.getClass());
        parseArgs.setAccessible(true);
        
        CommandLine cmd = (CommandLine)parseArgs.invoke(owc, (Object)args);
        
        Method getOutput = owc.getDeclaredMethod("getOutputType", CommandLine.class);
        getOutput.setAccessible(true);
        
        outputType type = (outputType)getOutput.invoke(owc, (Object)cmd);
        
        Assert.assertEquals(outputType.RAW, type);
    }
    
    @Test
    public void outputTypeTest2() throws Exception{
        String[] args = new String[] {"-t","raw"};
        
        // Reflection needed because parseArgs method is private -> set temporary accessible here
        Class owc = OpenWebslidesConverter.class;
        Method parseArgs = owc.getDeclaredMethod("parseArgs", args.getClass());
        parseArgs.setAccessible(true);
        
        CommandLine cmd = (CommandLine)parseArgs.invoke(owc, (Object)args);
        
        Method getOutput = owc.getDeclaredMethod("getOutputType", CommandLine.class);
        getOutput.setAccessible(true);
        
        outputType type = (outputType)getOutput.invoke(owc, (Object)cmd);
        
        Assert.assertEquals(outputType.RAW, type);
    }
    
    @Test
    public void outputTypeTest3() throws Exception{
        String[] args = new String[] {"-t","Raw"};
        
        // Reflection needed because parseArgs method is private -> set temporary accessible here
        Class owc = OpenWebslidesConverter.class;
        Method parseArgs = owc.getDeclaredMethod("parseArgs", args.getClass());
        parseArgs.setAccessible(true);
        
        CommandLine cmd = (CommandLine)parseArgs.invoke(owc, (Object)args);
        
        Method getOutput = owc.getDeclaredMethod("getOutputType", CommandLine.class);
        getOutput.setAccessible(true);
        
        outputType type = (outputType)getOutput.invoke(owc, (Object)cmd);
        
        Assert.assertEquals(outputType.RAW, type);
    }
    
    @Test
    public void outputTypeTest4() throws Exception{
        String[] args = new String[] {"-t","shower"};
        
        // Reflection needed because parseArgs method is private -> set temporary accessible here
        Class owc = OpenWebslidesConverter.class;
        Method parseArgs = owc.getDeclaredMethod("parseArgs", args.getClass());
        parseArgs.setAccessible(true);
        
        CommandLine cmd = (CommandLine)parseArgs.invoke(owc, (Object)args);
        
        Method getOutput = owc.getDeclaredMethod("getOutputType", CommandLine.class);
        getOutput.setAccessible(true);
        
        outputType type = (outputType)getOutput.invoke(owc, (Object)cmd);
        
        Assert.assertEquals(outputType.SHOWER, type);
    }
    
    @Test
    public void outputTypeTest5() throws Exception{
        String[] args = new String[] {"-t","SHOWER"};
        
        // Reflection needed because parseArgs method is private -> set temporary accessible here
        Class owc = OpenWebslidesConverter.class;
        Method parseArgs = owc.getDeclaredMethod("parseArgs", args.getClass());
        parseArgs.setAccessible(true);
        
        CommandLine cmd = (CommandLine)parseArgs.invoke(owc, (Object)args);
        
        Method getOutput = owc.getDeclaredMethod("getOutputType", CommandLine.class);
        getOutput.setAccessible(true);
        
        outputType type = (outputType)getOutput.invoke(owc, (Object)cmd);
        
        Assert.assertEquals(outputType.SHOWER, type);
    }
}
