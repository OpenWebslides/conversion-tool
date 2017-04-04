/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websocket;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.inject.Inject;
import mgr.ConverterManager;
import org.json.JSONObject;

/**
 *
 * @author dhoogla
 */
@ApplicationScoped
@javax.websocket.server.ServerEndpoint("/pipe")
public class ServerEndpoint implements ConversionCompleteCallback{

    @Inject
    private SocketSession session;

    private int nrOpen = 0;

    private final ConverterManager mgr = ConverterManager.getConverterManager(this);

    @OnOpen
    public void open(Session s) {
        session.addSession(s);
        if (nrOpen == 0) {
            mgr.startLogThread();
        }
        ++nrOpen;
        System.out.println("Serverendpoint opened a session, the new sessioncount is: " + nrOpen);
        System.out.println("WEBSOCKET SESSION OPENED " + s);        
    }

    @OnClose
    public void close(Session s) {
        session.removeSession(s);
        --nrOpen;
        System.out.println("Serverendpoint closed a session, the new sessioncount is: " + nrOpen);
        if (nrOpen == 0) {
            mgr.stopLogThread();
        }
        System.out.println("WEBSOCKET SESSION CLOSED " + s);
        mgr.removeEntry(s.getId());
    }

    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(ServerEndpoint.class.getName()).log(Level.SEVERE, null, error);
    }

    @OnMessage
    public void handleMessage(String message, Session session) {
        System.out.println("RECEIVED MESSAGE " + message + " FROM SESSION " + session);
        if (message.contains("\"msgType\":\"FILE\"")) {
            JSONObject msg = new JSONObject(message);
            System.out.println("***************" + msg);
            String filename = msg.getString("name");
            long timestamp = msg.getLong("timestamp");
            String filetype = msg.getString("fileType");
            mgr.addEntry(session.getId(), new InboundMsgDefinition(filename, timestamp, filetype));
        }
    }

    @Override
    public void conversionComplete(String sessionKey, String file) {
        Session s = session.getOneSession(sessionKey);
        OutboundMsgDefinition msg = new OutboundMsgDefinition(file,"download-ready");        
        System.out.println(new JSONObject(msg.getInfo()).toString());
        SocketSession.sendToSession(s,new JSONObject(msg.getInfo()));             
    }
    
   

}
