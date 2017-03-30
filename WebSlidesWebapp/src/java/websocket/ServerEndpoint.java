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

/**
 *
 * @author dhoogla
 */

@ApplicationScoped
@javax.websocket.server.ServerEndpoint("/pipe")
public class ServerEndpoint {
    
    @Inject
    private SocketSession _session;
    
    @OnOpen
    public void open(Session s){
        _session.addSession(s);
        System.out.println("WEBSOCKET SESSION OPENED"+s);
    }
    
    @OnClose
    public void close(Session s){
        _session.removeSession(s);
        System.out.println("WEBSOCKET SESSION CLOSED"+s);
    }
    
     @OnError
        public void onError(Throwable error) {
            Logger.getLogger(ServerEndpoint.class.getName()).log(Level.SEVERE, null, error);
    }

    @OnMessage
        public void handleMessage(String message, Session session) {
        System.out.println("RECEIVED MESSAGE "+message+" FROM SESSION "+session);
    }
        
        
}
