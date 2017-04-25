/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conversion.powerpoint;

import java.util.HashMap;
import java.util.List;
import objects.PPTInsight;
import objects.PPTList;
import objects.PPTObject;
import objects.Text;
import objects.Textpart;

/**
 *
 * @author Karel
 */
class InsightHandler {

    static void handle(PPTInsight insight, List<PPTObject> pptObjects) {
        countObjects(insight, pptObjects);
    }

    private static void countObjects(PPTInsight insight, List<PPTObject> pptObjects) {
        for (PPTObject po : pptObjects) {
            add(insight.getObjectCount(), po);
            if (po instanceof PPTList) {
                countObjects(insight, ((PPTList) po).getAllObjects());
            }
            if (po instanceof Text) {
                for (Textpart word : ((Text) po).getTextparts()) {
                    addWord(insight.getWordCount(), word.getContent());
                }
            }
        }
    }

    private static void add(HashMap<String, Integer> pptobjs, Object po) {
        if (pptobjs.containsKey(po.getClass().toString())) {
            pptobjs.put(po.getClass().toString(), pptobjs.get(po.getClass().toString()) + 1);
        } else {
            pptobjs.put(po.getClass().toString(), 1);
        }
    }

    private static void addWord(HashMap<String, Integer> words, String word) {
        if (word != null && word.length() >= 2) {
            if (words.containsKey(word)) {
                words.put(word, words.get(word) + 1);
            } else {
                words.put(word, 1);
            }
        }

    }

}
