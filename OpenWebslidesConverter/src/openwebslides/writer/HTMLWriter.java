/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslides.writer;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import objects.*;
import output.Output;


public class HTMLWriter extends Writer implements Indentation{

    private final static String SLIDE_NORMAL = "slide";
    private final static String SLIDE_TITLE = "title slide";
    
    private final Output output;
    
    private String imagesFolder = "images";
    
    private String TABS = "";
    private int indentation = 0;
    
    private int slideIndex = 0;
    
    public HTMLWriter(Output output) {
        this.output = output;
    }
    
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
            res += "\n" + TABS + objectToHtml(pptObj);
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

    public String getImagesFolder() {
        return imagesFolder;
    }

    public void setImagesFolder(String imagesFolder) {
        this.imagesFolder = imagesFolder;
    }

    private String addSimpleTag(String tag, String content){
        return "<"+tag+">"+content+"</"+tag+">";
    }
    
    private String objectToHtml(PPTObject pptObj){
        String res = "";
        //if a method exists for the object
        try {
            Method toHtml = getMethod(pptObj);
            res += (String)toHtml.invoke(this, pptObj);
        }
        //if none method found for the object
        catch (NoSuchMethodException ex) {
            //TODO default method
        }
        //error
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            //TODO throw exception of default method?
            output.error("cannot convert PPTObject \""+pptObj.getClass()+"\" to html", ex.getMessage());
        }
        return res;
    }
    
    /**************************************************************************
     * toHtml(pptObject) methods for each pptObject
     **************************************************************************/
    
    private String toHtml(Title title){
        return addSimpleTag("h2", title.getContent());
    }
    /*
    private String toHtml(Title title){
        return addSimpleTag("h2", printTextparts(title.getTextparts()));
    }
    */
    private String toHtml(Text text){
        return addSimpleTag("p", printTextparts(text.getTextparts()));
    }
    
    private String printTextparts(List<Textpart> parts){
        String res = "";
        for(Textpart textpart : parts){
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
        return res;
    }
    
    private String toHtml(PPTList list){
        String res = "";
        
        setTABS(++indentation);
        for(PPTObject object : list.getBullets()){
            String html = objectToHtml(object);
            res += "\n" + TABS + addSimpleTag("li", html);
        }
        setTABS(--indentation);
        
        res += "\n" + TABS;
        if(list.isOrdered())
            return addSimpleTag("ol", res);
        else
            return addSimpleTag("ul", res);
    }
    
    private String toHtml(Image image){
        setTABS(++indentation);
        String res = TABS + "<img src=\"" + imagesFolder + File.separator + image.getFilename() + "\">";
        setTABS(--indentation);
        
        if(false){ //full height    // TODO check
            res = "<figure class=\"cover height\">\n" + res;
        }
        else if(false){ //full width    // TODO check
            res = "<figure class=\"cover width\">\n" + res;
        }
        else{ //normal image
            res = "<figure>\n" + res;
        }
        res += "\n" + TABS + "</figure>";
        
        return res;
    }
    
    private String toHtml(Chart chart){
        String res = "";
        
        return res;
    }
    
    // TODO toHtml(Table table)
}
