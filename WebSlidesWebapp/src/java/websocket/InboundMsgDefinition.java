/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websocket;

/**
 * The InboundMsgDefinition is a template for messages received from the client
 * 
 * @author Laurens
 */
public class InboundMsgDefinition {
    private final String fileName;
    private final String fileType;
    private final long timestamp;
    private final String outputType;
    //private final InboundMsgDefinition.MSGTYPE msgType;
    
    //public static enum MSGTYPE {INFO,FILE};
    
    public InboundMsgDefinition(String n,long tm, String t,String o){
    this.fileName=n;
    this.timestamp=tm;
    this.fileType=t;    
    this.outputType=o;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public long getTimestamp() {
        return timestamp;
    }
    
    public String getOutputType(){
        return outputType;
    }

    @Override
    public String toString() {
        return "=> InboundMsgDefinition{" + "name=" + fileName + ", fileType=" + fileType + ", timestamp=" + timestamp + '}';
    }
    
    
    
    
}
