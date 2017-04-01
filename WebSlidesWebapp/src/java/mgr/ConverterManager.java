/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mgr;

import java.util.ArrayList;
import java.util.HashMap;
import websocket.InboundMsgDefinition;

/**
 *
 * @author dhoogla
 */
public class ConverterManager {

    private HashMap<String, ArrayList<InboundMsgDefinition>> sessionFiles;

    public ConverterManager() {
        this.sessionFiles = new HashMap<>();
    }

    public void addEntry(String key, InboundMsgDefinition value) {
        if (sessionFiles.containsKey(key)) {
            sessionFiles.get(key).add(value);
        } else {
            sessionFiles.put(key, new ArrayList<>());
            sessionFiles.get(key).add(value);
        }
        System.out.println("***I added to sessionFiles***");
        ArrayList<InboundMsgDefinition> p = sessionFiles.get(key);
        System.out.println("Websocket session key:" + key);
        for (InboundMsgDefinition t : p) {
            System.out.println(t);
        }
    }

    public HashMap<String, ArrayList<InboundMsgDefinition>> getSessionFiles() {
        return sessionFiles;
    }

    public void printSessionFiles() {
        for (String k : sessionFiles.keySet()) {
            System.out.println("Websocket session key:" + k);

            for (InboundMsgDefinition t : sessionFiles.get(k)) {
                System.out.println(t);
            }
        }
    }
}
