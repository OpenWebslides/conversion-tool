/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslides.writer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import objects.*;
import org.apache.commons.io.FilenameUtils;
import output.Output;

/**
 * A Writer that prints the content of the slides as HTML5 code.
 * @author Jonas
 */
public class HTMLWriter extends Writer implements Indentation{

    private final static String SLIDE_NORMAL = "slide";
    private final static String SLIDE_TITLE = "title slide";
    
    private final Output output;
    
    private String imagesFolder = "images";
    
    private String TABS = "";
    private int indentation = 0;
    
    private int slideIndex = 0;
    
    /**
     * Constructor of the class.
     * @param output The Output to where messages are logged.
     */
    public HTMLWriter(Output output) {
        this.output = output;
    }
    
    /**
     * Turns a Slide into HTML5 code.
     * @param slide
     * @return The representation of the Slide in HTML5 code. It uses the writeSlide(Slide slide, int indentation) method with 1 al value for the indentation.
     */
    @Override
    public String writeSlide(Slide slide){
        return writeSlide(slide, 1);
    }
    
    /**
     * Implementation of the Indentation interface.
     * @param slide The slide that holds the content.
     * @param indentation The amount of tabs used for the indentation.
     * @return The representation of the Slide in HTML5 code.
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
     * Returns the toHtml method with pptObject as attribute. If such method does not exist a toHtml method with a super class of the pptObject as attribute is returned. 
     * @param pptObject The PPTObject that represents the object.
     * @return The toHtml method with pptObject as attribute. If such method does not exist a toHtml method with a super class of the pptObject as attribute is returned.
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
     * @param n The amount of tabs.
     */
    public void setTABS(int n) {
        if(n>-1){
            this.TABS = new String(new char[n]).replace("\0", "\t");
            indentation = n;
        }
    }

    /**
     * Gets the images folder. This folder is used as path for the images in the HTML output.
     * @return The images folder.
     */
    public String getImagesFolder() {
        return imagesFolder;
    }

    /**
     * Sets the images folder. This folder is used as path for the images in the HTML output.
     * @param imagesFolder The images folder that represents the path of the images.
     */
    public void setImagesFolder(String imagesFolder) {
        this.imagesFolder = imagesFolder;
    }

    /**
     * Adds a tag without attributes.
     * @param tag The name of the tag.
     * @param content The content of the tag.
     * @return The content embedded in the tag.
     */
    private String addSimpleTag(String tag, String content){
        return "<"+tag+">"+content+"</"+tag+">";
    }
    
    /**
     * Returns the HTML5 code for the PPTObject.
     * @param pptObj The PPTObject that holds the content.
     * @return The HTML5 code for the PPTObject
     */
    private String objectToHtml(PPTObject pptObj){
        String res = "";
        //if a method exists for the object
        try {
            Method toHtml = getMethod(pptObj);
            res += (String)toHtml.invoke(this, pptObj);
        }
        //if none method found for the object
        catch (NoSuchMethodException ex) {
            Placeholder p = null;
            return toHtml(p);
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
        return addSimpleTag("h2", printTextparts(title.getTextparts()));
    }
    
    private String toHtml(Text text){
        return addSimpleTag("p", printTextparts(text.getTextparts()));
    }
    
    private String printTextparts(List<Textpart> parts){
        String res = "";
        for(Textpart textpart : parts){
            String part = "";
            if(textpart instanceof Hyperlink){
                Hyperlink link = (Hyperlink)textpart;
                part = " <a href=\"" + link.getUrl() + "\">";
                
                for(Textpart linkpart: link.getParts()){
                    part += linkpart.getContent();
                }
                part += "</a> ";
            }
            else {
                part = textpart.getContent();

                if(textpart.getType().contains(FontDecoration.BOLD))
                    part = addSimpleTag("strong", part);
                if(textpart.getType().contains(FontDecoration.UNDERLINE))
                    part = "<strong class=\"underline\">" + part + "</strong>";
                if(textpart.getType().contains(FontDecoration.ITALIC))
                    part = addSimpleTag("em", part);
                if(textpart.getType().contains(FontDecoration.STRIKETHROUH))
                    part = addSimpleTag("strike", part);
            }
            res += part;
        }
        return res;
    }
    
    private String toHtml(PPTList list){
        return toHtml(changeStructure(list));
        
        /*String res = "";
        
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
        */
    }
    
    private String toHtml(DeepPPTList list){
        String res = "";
        
        setTABS(++indentation);
        for(List<PPTObject> item : list){
            String itemString = "";
            for(PPTObject object : item){
                if(!itemString.equals(""))
                    itemString += "\n" + TABS;
                if(object instanceof Text){
                    Text text = (Text)object;
                    itemString += printTextparts(text.getTextparts());
                }
                else{
                    itemString += objectToHtml(object);
                }
            }
            res += "\n" + TABS + addSimpleTag("li", itemString);
        }
        setTABS(--indentation);
        res += "\n" + TABS;
        if(list.isOrdered())
            return addSimpleTag("ol", res);
        else
            return addSimpleTag("ul", res);
    }
    
    private DeepPPTList changeStructure(PPTList list){
        if(list==null) return null;
        DeepPPTList res = new DeepPPTList();
        res.setOrdered(list.isOrdered());
        
        for(PPTObject obj : list.getBullets()){
            if(obj instanceof PPTList){
                List<PPTObject> lastItem = res.getLastItem();
                PPTList objList = (PPTList)obj;
                lastItem.add(changeStructure(objList));
            }
            else{
                List<PPTObject> newItem = res.getNewItem();
                newItem.add(obj);
            }
        }
        
        return res;
    }
    
    /*
    private PPTList extractElementen(PPTList list) {
        PPTList res = new PPTList();
        
        for(PPTObject obj : list.getBullets()){
            if(obj instanceof PPTList){
                PPTList l = (PPTList)obj;
                PPTList inside = extractElementen(l);
                res.getBullets().addAll(inside.getBullets());
            }
            else{
                res.addPPTObject(obj);
            }
        }
        
        return res;
    }
    
    
    private DeepPPTList changeStructure(PPTList list){
        list = extractElementen(list);
        DeepPPTList res = new DeepPPTList();
        int lastParent = list.getBullets().get(0) instanceof Text && getLevel((Text)list.getBullets().get(0))!=null ? getLevel((Text)list.getBullets().get(0)) : 0;
        
        int i=0;
        while(i<list.getBullets().size()){
            PPTObject obj = list.getBullets().get(i);
            Text text = setText(obj);
            
            //moet while zijn, opletten wat als je een lijst van afbeeldingen hebt, niet op 1 lijn plaatsen!
            if(text==null || (getLevel(text)!=null && getLevel(text) > lastParent)){
                //bij vorige item
                res.getLast().add(obj);
            }
            //else if(getLevel(text)==null || getLevel(text) <= lastParent){
            else{
                //nieuw item
                List<PPTObject> nw = new ArrayList<>();
                nw.add(obj);
                res.add(nw);
            }
            i++;
        }
        
        return res;
    }
    
    private Text setText(PPTObject obj){
        Text text = null;
        if(obj instanceof Text){
            text = (Text)obj;
        }
        return text;
    }
    
    private Integer getLevel(Text text){
        if(text.getLevel()==null)
            return null;
        return Integer.parseInt(text.getLevel());
    }*/
    
    private String toHtml(Image image){
        if(image.getFilename() == null){
            Placeholder ph = new Placeholder();
            ph.setContent("image");
            return toHtml(ph);
        }
        
        setTABS(++indentation);
        String res = TABS + "<img src=\"" + getImageSource(image) + "\">";
        setTABS(--indentation);
        
        double W = image.getDimension().getWidth()/33;
        double H = image.getDimension().getHeight()/19;
        
        if(W>0.9 && (H>1.1 || H<=0.9)){ //full width
            res = "<figure class=\"cover width\">\n" + res;
        }
        else if(H>0.9){ //full height
            res = "<figure class=\"cover height\">\n" + res;
        }
        else{ //normal image
            if(image.getDimension().getWidth()>0 && image.getDimension().getHeight()>0){
                setTABS(++indentation);
                int width = (int) (image.getDimension().getWidth()/33.0*100.0);
                res = TABS + "<img src=\"" + getImageSource(image) + "\" width=\"" + width + "%\">";
                setTABS(--indentation);
            }
            res = "<figure>\n" + res;
        }
        res += "\n" + TABS + "</figure>";
        
        return res;
    }
    
    private String getImageSource(Image image){
        return imagesFolder + "/" + image.getFilename();
    }
    
    private String toHtml(Chart chart){
        Placeholder ph = new Placeholder();
        ph.setContent("chart");
        return toHtml(ph);
    }
    
    private String toHtml(Table table){
        String res = "<table>";
        
        setTABS(++indentation);
        for(Row row : table.getRows()){
            res += "\n" + TABS + "<tr>";
            
            setTABS(++indentation);
            for(Cell cell : row.getCells()){
                res += "\n" + TABS + "<td";
                if(cell.getColspan()>0)
                    res += " colspan=\"" + cell.getColspan() + "\"";
                if(cell.getRowspan()>0)
                    res += " rowspan=\"" + cell.getRowspan() + "\"";
                res += ">" + cell.getContent() + "</td>"; // TODO printTextparts(cell.getTextparts())
            }
            setTABS(--indentation);
            
            res += "\n" + TABS + "</tr>";
        }
        setTABS(--indentation);
        
        res += "\n" + TABS + "</table>";
        return res;
    }
    
    private String toHtml(Placeholder placeholder){
        String res = "<div class=\"placeholder\"";
        if(placeholder == null || placeholder.getContent() == null){
            res += " />";
        }
        else{
            res += ">" + placeholder.getContent() + "</div>";
        }
        return res;
    }
    
    private String toHtml(Video video){
        if(video.getFilename() == null || !validVideoExtension(video)){
            Placeholder ph = new Placeholder();
            ph.setContent("video");
            return toHtml(ph);
        }
        
        String res = "<video controls>";
        setTABS(++indentation);
        res += "\n" + TABS + "<source src=\"" + video.getFilename() + "\" type=\"" + getVideoType(video) + "\">";
        setTABS(--indentation);
        return res += "\n" + TABS + "</video>";
    }
    
    private boolean validVideoExtension(Video video){
        String ext = FilenameUtils.getExtension(video.getFilename()).toLowerCase();
        return ext.equals("mp4") || ext.equals("ogg") || ext.equals("webm");
    }
    
    private String getVideoType(Video video){
        return "video/" + FilenameUtils.getExtension(video.getFilename()).toLowerCase();
    }
    
}
