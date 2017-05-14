/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslides.writer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import objects.FontDecoration;
import objects.Hyperlink;
import objects.Textpart;

/**
 * Prints a list of Textparts out to HTML code.
 * @author Jonas
 */
public class TextPrinter {
    
    /**
     * LIFO stack. If a FontDecoration is in use in the current Textpart it is
     * pushed to the stack. The first FontDecoration on the stack should be
     * closed as first to remain correct HTML.
     */
    private static Deque<FontDecoration> stack = new ArrayDeque<>();
    
    /**
     * Prints all the Textparts to HTML. Following tags with the same
     * FontDecoration are added inside the same HTML tags as much as
     * possible.
     * @param text The list of Textparts that must be printed.
     * @return HTML representation of the list of Textparts
     */
    public static String printText(List<Textpart> text){
        stack.clear();
        String res = "";
        
        for(Textpart textpart : text){
            if(textpart instanceof Hyperlink){
                res += closeAllTags();
                Hyperlink link = (Hyperlink)textpart;
                res += " <a href=\"" + link.getUrl() + "\" target=\"_blank\">";
                
                for(Textpart linkpart: link.getParts()){
                    res += linkpart.getContent();
                }
                res += "</a> ";
            }
            else{
                res += closeTags(textpart);
                res += openTags(textpart);
                res += textpart.getContent();
            }
        }
        
        res += closeAllTags();
        
        stack.clear();
        return res;
    }
    
    /**
     * Checks for the textpart whether the FontDecorations are already in use.
     * If not the FontDecoration is pushed to the stack and the HTML tag is
     * added to the output.
     * @param textpart The textparts that holds the FontDecorations.
     * @return The opened HTML tags.
     */
    private static String openTags(Textpart textpart){
        String res = "";
        for(FontDecoration dec : textpart.getType()){
            if(!stack.contains(dec)){
                stack.addFirst(dec);
                res += openTag(dec);
            }
        }
        return res;
    }
    
    /**
     * Checks if the stack contains FontDecorations that do not apply to the
     * textpart. If present they are popped from the stack and their closing
     * tag is added to the output.
     * @param textpart The textpart that represents the new element.
     * @return The closed HTML tags.
     */
    private static String closeTags(Textpart textpart){
        String res = "";
        
        //fill close with the tags that should be closed
        List<FontDecoration> close = new ArrayList<>();
        for(Iterator itr = stack.iterator();itr.hasNext();){
            FontDecoration dec = (FontDecoration)itr.next();
            if(!textpart.getType().contains(dec)){
                close.add(dec);
            }
        }
        
        while(!close.isEmpty()){
            FontDecoration remove = stack.pollFirst();
            close.remove(remove);
            res += closeTag(remove);
        }
        
        return res;
    }
    
    /**
     * Close all the tags on the stack in order and pop them off.
     * @return All closing HTML tags of the element on the stack.
     */
    private static String closeAllTags(){
        String res = "";
        while(!stack.isEmpty()){
            res += closeTag(stack.pollFirst());
        }
        return res;
    }
    
    /**
     * Open the tag for the FontDecoration and add HTML opening tag to the
     * output.
     * @param open The FontDecoration for which the tag must be opened.
     * @return The HTML opening tag for the FontDecoration.
     */
    private static String openTag(FontDecoration open){
        String res = "";
        if(open == FontDecoration.BOLD){
            res += "<strong>";
        }
        else if(open == FontDecoration.ITALIC){
            res += "<em>";
        }
        else if(open == FontDecoration.UNDERLINE){
            res += "<span class=\"underline\">";
        }
        else if(open == FontDecoration.STRIKETHROUH){
            res += "<del>";
        }
        return res;
    }
    
    /**
     * Close the tag for the FontDecoration and add HTML closing tag to the
     * output.
     * @param remove The FontDecoration for which the tag must be closed.
     * @return The HTML closing tag for the FontDecoration.
     */
    private static String closeTag(FontDecoration remove){
        String res = "";
        if(remove == FontDecoration.BOLD){
            res += "</strong>";
        }
        else if(remove == FontDecoration.ITALIC){
            res += "</em>";
        }
        else if(remove == FontDecoration.UNDERLINE){
            res += "</span>";
        }
        else if(remove == FontDecoration.STRIKETHROUH){
            res += "</del>";
        }
        return res;
    }
}
