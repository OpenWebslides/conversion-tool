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
        //begin of slide
        String res = getTabs(indentation) + "<slide>";
        
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
        //end of slide
        res += "\n" + getTabs(indentation) + "</slide>";
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
