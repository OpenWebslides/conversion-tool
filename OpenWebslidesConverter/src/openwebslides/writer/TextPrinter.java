/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslides.writer;

import cz.vutbr.web.css.CSSProperty;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import objects.FontDecoration;
import objects.Text;
import objects.Textpart;

/**
 *
 * @author Jonas
 */
public class TextPrinter {
    
    private Map<FontDecoration,Boolean> map = new HashMap<>();
    
    private static Deque<FontDecoration> stack = new ArrayDeque<>();
    
    public static String printText(Text text){
        stack.clear();
        String res = "";
        
        for(Textpart textpart : text.getTextparts()){
            res += closeTags(textpart);
            res += openTags(textpart);
            res += textpart.getContent();
        }
        
        res += closeAllTags();
        
        stack.clear();
        return res;
    }
    
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
    
    private static String closeAllTags(){
        String res = "";
        while(!stack.isEmpty()){
            res += closeTag(stack.pollFirst());
        }
        return res;
    }
    
    private static String openTag(FontDecoration remove){
        String res = "";
        if(remove == FontDecoration.BOLD){
            res += "<strong>";
        }
        else if(remove == FontDecoration.ITALIC){
            res += "<em>";
        }
        else if(remove == FontDecoration.UNDERLINE){
            res += "<span class=\"underline\">";
        }
        else if(remove == FontDecoration.STRIKETHROUH){
            res += "<del>";
        }
        return res;
    }
    
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
