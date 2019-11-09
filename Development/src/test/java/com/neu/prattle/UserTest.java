package com.neu.prattle;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.lang.IllegalArgumentException;
import java.util.Optional;

import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.neu.prattle.controller.UserController;
import com.neu.prattle.model.User;
import com.neu.prattle.model.UserStatus;
import com.neu.prattle.service.UserService;
import com.neu.prattle.service.UserServiceImpl;


public class UserTest {
	private UserService as;

	@Before
	public void setUp() throws IOException {
		as = UserServiceImpl.getInstance();
	}


	@After
	public void destroy() {
		UserServiceImpl.clear();
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

