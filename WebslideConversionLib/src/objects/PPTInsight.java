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
 * Insights of the PowerPoint
 */
public class PPTInsight {

    //The amount of objects per sort
    private final HashMap<String, Integer> objectCount;
    
    //The amout of text objects in the ppt
    private final HashMap<String, Integer> wordCount;
    
    //The time it took to convert the ppt
    private long convertTime;

    /**
     * Create a new instance of a PPTInsight
     */
    public PPTInsight() {
        this.objectCount = new HashMap<>();
        this.wordCount = new HashMap<>();
    }

    /**
     * Return the hashmap which contains the objectscount
     * @return HashMap
     */
    public HashMap<String, Integer> getObjectCount() {
        return objectCount;
    }

    /**
     * Return the hashmap which contains the wordcount
     * @return HashMap
     */
    public HashMap<String, Integer> getWordCount() {
        return wordCount;
    }

    /**
     * Generate a String which describes the objectcount
     * @return String
     */
    public String generateDifferentObjectsString() {
        String toret = "";
        for (String key : getObjectCount().keySet()) {
            toret += (key + " : " + getObjectCount().get(key)) + "\n";
        }
        return toret.trim();
    }

    /**
     * Generate a String which describes the wordcount
     * @return String
     */
    public String generateDifferentWordsString() {
        return "" + getWordCount().keySet().size();
    }

    /**
     * Generate a String which describes the words
     * @return String
     */
    public String generateWordString() {
        int total = 0;
        for (String key : getWordCount().keySet()) {
            total += getWordCount().get(key);
        }
        return "" + total;
    }

    /**
     * Return the converttime
     * @return long
     */
    public long getConvertTime() {
        return convertTime;
    }

    /**
     * Set the converttime
     * @param convertTime  long
     */
    public void setConvertTime(long convertTime) {
        this.convertTime = convertTime;
    }
    
    /**
     * Return a String which describes all of the Insights
     * @param ppt PPT
     * @return String
     */
    public String printInsights(PPT ppt) {
        String toret = "";
        toret += "***** Insights ******\n";
        toret += "Convert time\n";
        toret += (double) ((double)ppt.getInsight().getConvertTime()/1000) + "\n\n";
        toret += "Objectcount\n";
        toret += ppt.getInsight().generateDifferentObjectsString() + "\n\n";
        toret += "Wordcount\n";
        toret += "Number of different words: " + ppt.getInsight().generateDifferentWordsString() + "\n";
        toret += "Number of words: ";
        toret +="Number of words: " + ppt.getInsight().generateWordString();
        return toret;
    }

}
