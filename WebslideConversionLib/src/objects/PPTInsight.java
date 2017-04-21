/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.util.HashMap;

/**
 *
 * @author Karel
 */
public class PPTInsight {
    
    private HashMap<String, Integer> objectCount;
    private HashMap<String, Integer> wordCount;

    public PPTInsight() {
        this.objectCount = new HashMap<>();
        this.wordCount = new HashMap<>();
    }
    
    

    public HashMap<String, Integer> getObjectCount() {
        return objectCount;
    }

    public HashMap<String, Integer> getWordCount() {
        return wordCount;
    }

    
}
