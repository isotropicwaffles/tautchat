package com.neu.prattle.websocket;

/**
 * A simple chat client based on websockets.
 * 
 * @author https://github.com/eugenp/tutorials/java-websocket/src/main/java/com/baeldung/websocket/ChatEndpoint.java
 * @version dated 2017-03-05
 */

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.neu.prattle.messaging.IMessageProcessor;
import com.neu.prattle.messaging.MessageProcessorFactory;
import com.neu.prattle.messaging.TypeOfMessageProcessor;
import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;
import com.neu.prattle.service.user.UserServiceImpl;


/**
 * The Class ChatEndpoint.
 * 
 * This class handles Messages that arrive on the server.
 */
@ServerEndpoint(value = "/chat/", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ChatEndpoint {

    /** The session. */
    private Session session;
    
    /** The Constant chatEndpoints. */
    private static final Set<ChatEndpoint> chatEndpoints = new CopyOnWriteArraySet<>();
    
    /** The users. */
    private static HashMap<String, String> users = new HashMap<>();
    
    /** The sessions. */
    private static HashMap<String, String> sessions = new HashMap<>();

    /** The prelogin users. */
    private static HashMap<String, String> preUserLogins = new HashMap<>();
        
    private static  IMessageProcessor generalMessageProcessor = MessageProcessorFactory.getInstance().getInstanceOf(TypeOfMessageProcessor.GENERAL_MESSAGE_PROCESSOR);
    
    /**
	 * On open.
	 * 
	 * Handles opening a new session (websocket connection). If the user is a known
	 * user (user management), the session added to the pool of sessions and an
	 * announcement to that pool is made informing them of the new user.
	 * 
	 * If the user is not known, the pool is not augmented and an error is sent to
	 * the originator.
	 *
	 * @param session  the web-socket (the connection)
	 * @param username the name of the user (String) used to find the associated
	 *                 UserService object
	 * @throws IOException     Signals that an I/O exception has occurred.
	 * @throws EncodeException the encode exception
	 */
    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {


    	UUID id;
    	Optional<User> user; 
    	//Confirm the id doesn't match the name of a user
    	do {
    		id = UUID.randomUUID();
    		user = UserServiceImpl.getInstance().findUserByName(id.toString());
    	} while (user.isPresent());
         
    	 addEndpoint(session, id);
    }

    /**
     * Adds a newly opened session to the pool of sessions.
     *
     * @param session    the newly opened session
     * @param uuid   the user who connected
     */
    private void addEndpoint(Session session, UUID uuid) {
        this.session = session;
        chatEndpoints.add(this);
        
        /* users is a hashmap between session ids and users */
        preUserLogins.put(session.getId(), uuid.toString());
        sessions.put(uuid.toString(), session.getId());
    }


    /**
     * On message.   
     * 
     * When a message arrives, broadcast it to all connected users.
     *
     * @param session the session originating the message
     * @param message the text of the inbound message
     * @throws IOException 
     */
    @OnMessage
    public void onMessage(Session session, Message message) throws IOException {
    	
        message.setFrom(getSessionFromField(session));
        
       ChatEndpoint.generalMessageProcessor.processMessage(message);

    }

    /**
     * Returns the correct from field for a message from the given session
     *
     * @param session the session originating the message
     * 
     * @return String - from field for message
     */
	public String getSessionFromField(Session session) {
		String from = users.get(session.getId());
    	
    	if (from == null) {
        	from = preUserLogins.get(session.getId());
    	}
    	
		return from;
	}
    
    /**
     * On close.  
     * 
     * Closes the session by removing it from the pool of sessions and 
     * broadcasting the news to everyone else.
     *
     * @param session the session
     */
    @OnClose
    public void onClose(Session session) {
        chatEndpoints.remove(this);
        
        Message message = Message.messageBuilder()
                .setMessageContent("Disconnected!")
                .setFrom(users.get(session.getId()))
                .setDateSent(new Date())
                .build();
        
        broadcast(message);
    }

    /**
     * On error.
     *
     * Handles situations when an error occurs.  Not implemented.
     * 
     * @param session the session with the problem
     * @param throwable the action to be taken.
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    
    /**
     * Logins in user
     * 
     * 
     * @param userName  - the user name being logged in
     * @param userID    - the old id that was used for the session before logging in
     */
    public static void userLogin(String userName, String userId) {
    	Session session = findSessionByUsername(userId);
    	preUserLogins.remove(session.getId());
        sessions.remove(userId);

    	users.put(session.getId(), userName);
        sessions.put(userName, session.getId());
    }
    
    /**
     * Finds the session by the associated user name.
     * 
     * @param id - the associated user name to the session 
     * 
     * @return Session - the associated session with the user otherwise null
     */
    private static Session findSessionByUsername(String user) {
    	
    	String id = sessions.get(user);
    	Session userSession = null;
    	
    	
	    	for (ChatEndpoint chatEndpoint : chatEndpoints) {
	    		if (id.contentEquals(chatEndpoint.session.getId())) {
	    			userSession = chatEndpoint.session;
	    			break;
	    		}
	    	}
	    	
    	if(userSession == null) {
            throw new NullPointerException("ChatEndpoint search resulted in null point exception");
    	}
    	
    	return userSession;
    }
    
    
    /**
     * Direct message.
     * 
     * Send a Message to the user specified in the to field
     * 
     * @param message 
     */
    public static void directedMessage(Message message) {
    	broadcastToSession(message, findSessionByUsername(message.getTo()));
    }
    
    /**
     * Broadcast.
     * 
     * Send a Message to each session in the pool of sessions.
     * The Message sending action is synchronized.  That is, if another
     * Message tries to be sent at the same time to the same endpoint,
     * it is blocked until this Message finishes being sent..
     *
     * @param message 
     */
    public static void broadcast(Message message) {
        chatEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
            	broadcastToSession(message, endpoint.session);
            }
        });
    }
    
    /**
     * broadcastToSession
     * 
     * Send a Message to the user specified in the to field
     * 
     * @param message 
     * @param session 
     */
	private static void broadcastToSession(Message message, Session session) {
		final Logger log = Logger.getLogger(ChatEndpoint.class.getName());
		    try {
		    	RemoteEndpoint.Basic remote = session.getBasicRemote();
		    	remote.sendObject(message);
		    } catch (IOException | EncodeException e) {
		    	log.log(Level.INFO, "broadcastToSession is blowing up.");
		    }
		}
}

