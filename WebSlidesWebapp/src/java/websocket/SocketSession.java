/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websocket;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.websocket.Session;

/**
 *
 * @author dhoogla
 */
@ApplicationScoped
public class SocketSession {
    private final Set<Session> sessions = new HashSet<>();
    
     public void addSession(Session session) {
        sessions.add(session);    
         System.out.println("New socket session + "+session);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
    }
    
    private void sendToSession(Session session, JsonObject message) {
        try {
            session.getBasicRemote().sendText(message.toString());
        } catch (IOException ex) {
            sessions.remove(session);
            Logger.getLogger(SocketSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Set<Session> getSessions() {
        return sessions;
    }   
    
}
