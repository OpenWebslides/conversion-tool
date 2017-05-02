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
        resetUsedTags();
        String res = "";
        
        for(Textpart textpart : text.getTextparts()){
            
            //array vullen met alle decorations die niet in de tekst aanwezig zijn
            //close decorations die niet meer nodig zijn (array meegeven)
                //zal de stack overlopen en telkens de eerste sluiten zolang de array niet leeg is
            
            //open nieuwe decorations
                //geef alle decorations mee, zal allemaal overlopen en als ze nog niet open zijn ze openen en op de stack duwen
            
        }
        
        //alle decorations sluiten, van voor beginnen in de stack
        
        resetUsedTags();
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
            res += closeTag(remove);
        }
        
        return res;
    }
    
    private static String closeAllTags(){
        String res = "";
        for(Iterator itr = stack.iterator();itr.hasNext();){
            FontDecoration dec = (FontDecoration)itr.next();
            res += closeTag(dec);
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
    
    private static void resetUsedTags(){
        //alles op false zetten
    }
}
