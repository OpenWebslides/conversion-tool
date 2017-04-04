/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.EncodeException;
import javax.websocket.Session;
import org.json.JSONObject;

/**
 *
 * @author dhoogla
 */
@ApplicationScoped
public class SocketSession {
    private final HashMap<String,Session> sessions = new HashMap<>();
    
     public void addSession(Session session) {
        sessions.put(session.getId(),session);    
         System.out.println("New socket session + "+session);
    }

    public void removeSession(Session session) {
        sessions.remove(session.getId());
    }
    
    public static void sendToSession(Session session, JSONObject message) {
        try {
            System.out.println("going to send to session: "+session.getId()+" the following message: "+message);
            session.getBasicRemote().sendObject(message);
        } catch (IOException ex) {            
            Logger.getLogger(SocketSession.class.getName()).log(Level.SEVERE, null, ex);
        } catch (EncodeException ex) {
            Logger.getLogger(SocketSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Session getOneSession(String sessionKey) {
        return sessions.get(sessionKey);
    }   
    
}
