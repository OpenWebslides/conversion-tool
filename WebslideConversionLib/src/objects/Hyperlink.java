/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.util.ArrayList;

/**
 *
 * @author Karel
 */
public class Hyperlink extends Textpart{
    
    //A hyperlink contains multiple textparts, for example "click <strong>here</strong>" where these 2 words have one hyperlink
    private ArrayList<Textpart> parts;
    
    //The url of the hyperlink
    private String url;
    
    //The relative id used to identify the hyperlink
    private String rid;
    
    /**
     * Create an instance of a Hyperlink based on a textpart
     * @param textpart Textpart
     */
    public Hyperlink(Textpart textpart) {
        super.setFont(textpart.getFont());
        super.setType(textpart.getType());
        super.setSize(textpart.getSize());
        super.setContent(textpart.getContent());
        super.setCharachterSpacing(textpart.getCharachterSpacing());
        super.setColor(textpart.getColor());
        parts = new ArrayList<>();  
    }
    
    /**
     * Create an instance of an empty Hyperlink
     */
    public Hyperlink(){
        parts = new ArrayList<>();
    }

    /**
     * Get the textparts
     * @return ArrayList
     */
    public ArrayList<Textpart> getParts() {
        return parts;
    }

    /**
     * Get the url of the hyperlink
     * @return String
     */
    public String getUrl() {
        return url;
    }
    
    /**
     * Set the url of the hyperlink
     * @param url String
     */
    public void setUrl(String url) {
        this.url = url;
    }
    
    @Override
    /**
     * Return the content of the hyperlink, this will be empty. The 'content' is saved in the textparts.
     * Use getParts() to get the text.
     */
    public String getContent(){
        return super.getContent();
    } 
    
    @Override
    /**
     * Return a String which contains the data behind this hyperlink
     */
    public String toString(){
        String toret = this.getClass().toString()+ ": ";
        for(Textpart par : parts){
            toret += par.toString();

        }
        return toret + "("+ url +")";
    }

    /**
     * Get the relative id
     * @return String
     */
    public String getRid() {
        return rid;
    }

    /**
     * Set the relative id
     * @param rid Strings
     */
    public void setRid(String rid) {
        this.rid = rid;
    }

    
    
}
