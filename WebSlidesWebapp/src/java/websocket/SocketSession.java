/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package websocket;

import java.util.HashMap;
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;


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
   
    public Session getOneSession(String sessionKey) {
        return sessions.get(sessionKey);
    }   
    
}
