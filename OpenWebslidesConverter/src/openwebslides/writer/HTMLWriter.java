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


public class HTMLWriter extends Writer {

    @Override
    public String writeSlide(Slide slide) {
        String res = "<slide>";
        
        for(PPTObject pptObj : slide.getPptObjects()){
            try {
                Method toHtml = getMethod(pptObj);
                res += "\n\t" + (String)toHtml.invoke(this, pptObj);
            }
            catch (NoSuchMethodException ex) {
                res += "\n\t<empty></empty>";
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                
            }
        }
        res += "\n</slide>";
        return res;
    }
    
    private Method getMethod(PPTObject pptObject) throws NoSuchMethodException{
        Class c = pptObject.getClass();
                
        while(!c.equals(Object.class)){
            try{
                Method toHtml = this.getClass().getDeclaredMethod("toHtml", new Class[]{c});
                return toHtml;
            }
            catch(NoSuchMethodException ex){
                c = c.getSuperclass();
            }
        }
        throw new NoSuchMethodException();
    }
    
    private String toHtml(PPTObject obj){
        return null;
    }
    
}
