/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websocket;

import java.util.HashMap;

/**
 *
 * @author Laurens
 */
public class OutboundMsgDefinition {
    private final String fileName;
    private final String action; 

    public OutboundMsgDefinition(String name,String action) {
        this.fileName = name;    
        this.action=action;
    }
    
    public HashMap<String,String> getInfo() {
        HashMap<String,String> tmp = new HashMap<>();
        tmp.put("filename", fileName);
        tmp.put("action",action);
        return tmp;
    }
    
    
    
}
