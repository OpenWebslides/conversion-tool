/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websocket;

/**
 *
 * @author Laurens
 */
public class InboundMsgDefinition {
    private final String name;
    private final String fileType;
    private final long timestamp;
    //private final InboundMsgDefinition.MSGTYPE msgType;
    
    //public static enum MSGTYPE {INFO,FILE};
    
    public InboundMsgDefinition(String n,long tm, String t){
    this.name=n;
    this.timestamp=tm;
    this.fileType=t;
    
    }

    public String getName() {
        return name;
    }

    public String getFileType() {
        return fileType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "=> InboundMsgDefinition{" + "name=" + name + ", fileType=" + fileType + ", timestamp=" + timestamp + '}';
    }
    
    
    
    
}
