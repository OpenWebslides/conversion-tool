/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslides.writer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import objects.*;


public class HTMLWriter extends Writer implements Indentation{

    private final static String SLIDE_NORMAL = "slide";
    private final static String SLIDE_TITLE = "title slide";
    
    private String TABS = "";
    private int indentation = 0;
    
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
        setTABS(indentation);
        //open slide tag
        String res = TABS + "<div class=\"";
        res += slideIndex==0 ? SLIDE_TITLE : SLIDE_NORMAL;
        res += "\" id=\"slide" + slideIndex++ + "\">";
        
        setTABS(++indentation);
        //content of slide
        for(PPTObject pptObj : slide.getPptObjects()){
            
            //if a method exists for the object
            try {
                Method toHtml = getMethod(pptObj);
                res += "\n" + TABS + (String)toHtml.invoke(this, pptObj);
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
        setTABS(--indentation);
        
        //close slide tag
        res += "\n" + TABS + "</div>";
        return res;
    }
    
    /**
     * Returns the toHtml method with pptObject as attribute. If such method does not exist a toHtml method with a super class of the pptObject is returned. 
     * @param pptObject
     * @return
     * @throws NoSuchMethodException if none method could be found.
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
    
    
    public String getTABS() {
        return TABS;
    }

    /**
     * Fills the String TABS with n times a tab.
     * @param n 
     */
    public void setTABS(int n) {
        if(n>-1){
            this.TABS = new String(new char[n]).replace("\0", "\t");
            indentation = n;
        }
    }

    private String addSimpleTag(String tag, String content){
        return "<"+tag+">"+content+"</"+tag+">";
    }
    
    /**************************************************************************
     * toHtml(pptObject) methods for each pptObject
     **************************************************************************/
    
    private String toHtml(Title title){
        //return "<h2>" + title.getContent() + "</h2>";
        return addSimpleTag("h2", title.getContent());
    }
    
    private String toHtml(Text text){
        String res = "";
        for(Textpart textpart : text.getTextparts()){
            String part = textpart.getContent();
            
            if(textpart.getType().contains(FontDecoration.BOLD))
                part = addSimpleTag("strong", part);
            if(textpart.getType().contains(FontDecoration.UNDERLINE))
                part = "<span style=\"text-decoration: underline;\">" + part + "</span>";
            if(textpart.getType().contains(FontDecoration.ITALIC))
                part = addSimpleTag("em", part);
            if(textpart.getType().contains(FontDecoration.STRIKETHROUH))
                part = addSimpleTag("strike", part);
            
            res += part;
        }
        return addSimpleTag("p", res);
    }
}
