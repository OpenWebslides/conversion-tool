/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websocket;

import java.util.HashMap;

/**
 * The OutboundMsgDefinition is a template for messages that will be sent to the client
 An outbound message contains
 The filename of the processed file
 The status that 
 * 
 * @author Laurens
 */
public class OutboundMsgDefinition {
    private final String fileName;
    private final String status; 

    public OutboundMsgDefinition(String name,String action) {
        this.fileName = name;    
        this.status=action;
    }
    
    public HashMap<String,String> getInfo() {
        HashMap<String,String> tmp = new HashMap<>();
        tmp.put("fileName", fileName);
        tmp.put("action",status);
        return tmp;
    }
    
    
    
}
