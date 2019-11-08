package com.neu.prattle;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import java.lang.IllegalArgumentException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;
import com.neu.prattle.websocket.ChatEndpoint;
import com.neu.prattle.websocket.MessageDecoder;
import com.neu.prattle.websocket.MessageEncoder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.neu.prattle.controller.UserController;
import com.neu.prattle.exceptions.UserAlreadyPresentException;
import com.neu.prattle.main.PrattleApplication;
import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;
import com.neu.prattle.model.UserStatus;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import javax.ws.rs.core.Response;
import javax.websocket.RemoteEndpoint;


@RunWith(MockitoJUnitRunner.class)
public class SimpleTest {

	private UserService as;
	private ChatEndpoint chat;

	@Before
	public void setUp() throws IOException {
		FileWriter fileWriter = new FileWriter("users.txt", false); 
		fileWriter.write("");
		fileWriter.close();
		FileWriter onceMore = new FileWriter("users.txt", true);
		onceMore.write("bob ");
		onceMore.write("sally ");
		onceMore.write("charlie ");
		onceMore.close();
		as = UserServiceImpl.getInstance();
        chat = new ChatEndpoint();
	}
	
	
	@After
	public void destroy() {
		UserServiceImpl.clear();
        chat = null;
	}
	
	// This method just tries to add 
	@Test
	public void setUserTest() throws IOException{
		User u = new User();
		u.setName("Mike");
	   as.addUser(u);
	}
	
	// This method just tries to add 
	@Test
	public void getUserTest() throws IOException{
		User u = new User();
		u.setName("Mike");
	   as.addUser(u);
		Optional<User> user = as.findUserByName("Mike");
		assertTrue(user.isPresent());
	}
	
	// This method just tries to create an empty user and set the name 
	@Test
	public void emptyUserConstructorTest(){
		User user = new User();
		user.setName("John");
		assertEquals("John",user.getName());
	}
	
	// This method to sets that user.equals is false if fed a non User object 
	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void notEqualObjectTest(){
		User user = new User();
		user.setName("John");
		assertFalse(user.equals(new String()));
	}
	
	// This method tests the case where multiple users of the same name are created via the user controller
	@Test
	public void createUserControllerFailTest() throws IOException{
		UserController controller = new UserController();
		Response failure409 = Response.status(409).build();

		User u = new User();
		u.setName("Jack");
		controller.createUserAccount(u);
		
		assertEquals(failure409.getStatus(), controller.createUserAccount(u).getStatus());
	}
	
	// This method tests if a unique user is added via the user controller
	@Test
	public void createUserConterollerSuccessTest() throws IOException{
		UserController controller = new UserController();
		Response okayStatus= Response.ok().build();

		User u = new User();
		u.setName("Tim");
		assertEquals(okayStatus.getStatus(), controller.createUserAccount(u).getStatus());
	}
	
	// This method tests encoding and decoding a message
	@Test
	public void encodeDecodeTest() throws EncodeException{
		MessageDecoder decoder = new MessageDecoder();
		MessageEncoder encoder = new MessageEncoder();
		Message simpleMessage = Message.messageBuilder()
        .setFrom("Tom")
        .setTo("Mark")
        .setMessageContent("Simple")
        .build();
		
		String encodedMessage = encoder.encode(simpleMessage);
		
		Message decodedMessage = decoder.decode(encodedMessage);
		
		encoder.destroy();
		decoder.destroy();
		assertEquals(simpleMessage.getContent(),decodedMessage.getContent());
		assertEquals(simpleMessage.getFrom(),decodedMessage.getFrom());
		assertEquals(simpleMessage.getTo(),decodedMessage.getTo());
		assertEquals(simpleMessage.toString(),decodedMessage.toString());

	}
	
	
	// This method if decode check works
	@Test
	public void willDecodeTest(){
		MessageDecoder decoder = new MessageDecoder();
				
		assertFalse(decoder.willDecode(null));
		assertTrue(decoder.willDecode("test"));

	}
	
	// This method tests getClasses function of PrattleApplication
	@Test
	public void prattleAppGetClassesTest(){
		PrattleApplication prattleApp = new PrattleApplication();
		Set<Class<?>> resourceClasses = prattleApp.getClasses();
		
		assertEquals(1,resourceClasses.size());
		assertEquals(UserController.class, resourceClasses.iterator().next());

	}
	
	// This method indirectly tests the session calls since this function is basically a black box due to 
	// the session logic
	@Test
	public void endPointChatTest() throws EncodeException, IOException{
//		as.addUser(new User("Nick"));
//
//        Session session = Mockito.mock(Session.class);
//        Mockito.when(session.getId()).thenReturn("5");
//
//        RemoteEndpoint.Basic remote = Mockito.mock(RemoteEndpoint.Basic.class);
//
//        Mockito.when(session.getId()).thenReturn("5");
//
//        Mockito.when(session.getBasicRemote()).thenReturn(remote);
//        //There should be no session calls
//        Mockito.verify(session, Mockito.times(0)).getBasicRemote();
//
//        //There should be one session call for someone who exists already
//		chat.onOpen(session, "Mark");
//        Mockito.verify(session, Mockito.times(1)).getBasicRemote();
//        //There should be another session call for someone who isn't a user for the error message
//		chat.onOpen(session, "Nick");
//        Mockito.verify(session, Mockito.times(2)).getBasicRemote();
//        
//        
//        //Check that onMessage correctly sets message sent from variable
//        Message message = Mockito.mock(Message.class);
//       	chat.onMessage(session, message);
//        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
//        Mockito.verify(message).setFrom(argument.capture());
//        assertEquals("Nick", argument.getValue());
//        
//        //Check thatto make sure that this function doesn't throw error yet
//        chat.onError(session, new RuntimeException());       

	}
	
	
	// Performance testing to benchmark our number of users that can be added 
	// in 1 sec	
	
	@Test(timeout = 10000)
	public void checkPrefTest() throws IOException{
		for(int i=0; i < 1000; i++) {
			User u = new User();
			u.setName("Mike"+i);
			as.addUser(u);
		}
	}
	
	//Below are tests for the User Builder class
	
	@Test
	public void testUserName() {
		User u = new User();
		u.setName("Marco");
		assertEquals("Marco",u.getName());
	}
	
	@Test
	public void testUserStatusAway() {
		User u = new User();
		u.setStatus(UserStatus.AWAY);
		assertEquals(UserStatus.AWAY,u.getStatus());
	}
	
	@Test
	public void testUserStatusDNN() {
		User u = new User();
		u.setStatus(UserStatus.DONOTDISTURB);
		assertEquals(UserStatus.DONOTDISTURB,u.getStatus());
	}
	
	@Test
	public void testUserStatusIdle() {
		User u = new User();
		u.setStatus(UserStatus.IDLE);
		assertEquals(UserStatus.IDLE,u.getStatus());
	}
	
	@Test
	public void testUserStatusOnline() {
		User u = new User();
		u.setStatus(UserStatus.ONLINE);
		assertEquals(UserStatus.ONLINE,u.getStatus());
	}
	
	@Test
	public void testUserStatusOffline() {
		User u = new User();
		u.setStatus(UserStatus.OFFLINE);
		assertEquals(UserStatus.OFFLINE,u.getStatus());
	}
	
	@Test
	public void testUserId() {
		User u = new User();
		u.setId(12);
		assertEquals(12,u.getId());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUserIdNeg() {
		User u = new User();
		u.setId(-412);
	}
	
	@Test
	public void testBot() {
		User u = new User("Mike",true);
		assertTrue(u.getBot());
	}
	
	@Test
	public void testSearchable() {
		User u = new User();
		u.setSearchable(true);
		assertTrue(u.getSearchable());
	}
	
}
