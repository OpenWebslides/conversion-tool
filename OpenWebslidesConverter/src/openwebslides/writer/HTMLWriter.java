/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslides.writer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import objects.PPTObject;
import objects.Slide;


public class HTMLWriter extends Writer implements Indentation{

    private final static String SLIDE_TAG_OPEN_0 = "<div class=\"";
    private final static String SLIDE_TAG_OPEN_1 = "\" id=\"slide";
    private final static String SLIDE_TAG_OPEN_2 = "\">";
    private final static String SLIDE_NORMAL = "slide";
    private final static String SLIDE_TITLE = "title slide";
    private final static String SLIDE_TAG_CLOSE = "</div>";
    
    private int slideIndex = 0;
    /**
     * 
     * @param slide
     * @return 
     */
    @Override
    public String writeSlide(Slide slide){
        return writeSlide(slide, 0);
    }
    
    /**
     * 
     * @param slide
     * @param indentation
     * @return 
     */
    @Override
    public String writeSlide(Slide slide, int indentation) {
        //open slide tag
        String res = getTabs(indentation) + SLIDE_TAG_OPEN_0;
        res += slideIndex==0 ? SLIDE_TITLE : SLIDE_NORMAL;
        res += SLIDE_TAG_OPEN_1 + slideIndex++ + SLIDE_TAG_OPEN_2;
        
        //content of slide
        for(PPTObject pptObj : slide.getPptObjects()){
            
            //if a method exists for the object
            try {
                Method toHtml = getMethod(pptObj);
                res += "\n" + getTabs(indentation) + (String)toHtml.invoke(this, pptObj);
            }
            //if none method found for the object
            catch (NoSuchMethodException ex) {
                //TODO default method
            }
            //error
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                //TODO handle output
            }
            
        }
        //close slide tag
        res += "\n" + getTabs(indentation) + SLIDE_TAG_CLOSE;
        return res;
    }
    
    /**
     * 
     * @param pptObject
     * @return
     * @throws NoSuchMethodException 
     */
    private Method getMethod(PPTObject pptObject) throws NoSuchMethodException{
        //get the most specific class
        Class c = pptObject.getClass();
        
        //search the method with the most specific class as argument, as long it is not Object.class
        while(!c.equals(Object.class)){
            try{
                Method toHtml = this.getClass().getDeclaredMethod("toHtml", new Class[]{c});
                return toHtml;
            }
            catch(NoSuchMethodException ex){
                //search for the super class
                c = c.getSuperclass();
            }
        }
        throw new NoSuchMethodException();
    }
    
    /*private String toHtml(PPTObject obj){
        return null;
    }*/
    
    /**
     * 
     * @param indentation
     * @return 
     */
    private String getTabs(int indentation){
        if(indentation>0)
            return new String(new char[indentation]).replace("\0", "\t");
        return "";
    }

    
    
}
