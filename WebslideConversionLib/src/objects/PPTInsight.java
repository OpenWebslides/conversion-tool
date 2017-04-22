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

    private final HashMap<String, Integer> objectCount;
    private final HashMap<String, Integer> wordCount;
    private long convertTime;

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

    public String generateDifferentObjectsString() {
        String toret = "";
        for (String key : getObjectCount().keySet()) {
            toret += (key + " : " + getObjectCount().get(key)) + "\n";
        }
        return toret.trim();
    }

    public String generateDifferentWordsString() {
        return "" + getWordCount().keySet().size();
    }

    public String generateWordString() {
        int total = 0;
        for (String key : getWordCount().keySet()) {
            total += getWordCount().get(key);
        }
        return "" + total;
    }

    public long getConvertTime() {
        return convertTime;
    }

    public void setConvertTime(long convertTime) {
        this.convertTime = convertTime;
    }

}
