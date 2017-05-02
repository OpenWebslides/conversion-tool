/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package openwebslides.writer;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import objects.FontDecoration;
import objects.Text;
import objects.Textpart;

/**
 *
 * @author Jonas
 */
public class TextPrinter {
    
    private Map<FontDecoration,Boolean> map = new HashMap<>();
    
    private static Deque<Integer> stack = new ArrayDeque<Integer>();
    
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
    
    private static void closeTags(Textpart textpart){
        for(Iterator itr = stack.iterator();itr.hasNext();){
            
        }
    }
    
    private static void resetUsedTags(){
        //alles op false zetten
    }
}
