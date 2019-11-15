package com.neu.prattle.daos;

import com.neu.prattle.model.User;
import com.neu.prattle.model.UserStatus;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

public class UserDatabaseImplTest {

	private UserDatabaseImpl userImplTest = new UserDatabaseImpl();
	private User testPerson;
	private User testIndividual;
	private User testHuman;

	@Before
	public void setUp() {
	    testPerson = new User.UserBuilder()
	    		.setName("HughMann")
	    		.setSearchable(true)
	    		.setStatus(UserStatus.ONLINE)
	    		.build();
	    
	    testIndividual = new User.UserBuilder()
	    		.setName("NotarObot")
	    		.setSearchable(true)
	    		.setStatus(UserStatus.OFFLINE)
	    		.build();

	    testHuman = new User.UserBuilder()
	    		.setName("Bender")
	    		.setSearchable(false)
	    		.setStatus(UserStatus.ONLINE)
	    		.build();
		
		if (!userImplTest.userExists(testPerson.getName())) {
			userImplTest.createUser(testPerson);
		}
		if (!userImplTest.userExists(testIndividual.getName())) {
			userImplTest.createUser(testIndividual);
		}
		if (!userImplTest.userExists(testHuman.getName())) {
			userImplTest.createUser(testHuman);
		}
	}

	@After
	public void tearDown() {
		userImplTest.deleteUserByUsername("HughMann");
		userImplTest.deleteUserByUsername("NotarObot");
		userImplTest.deleteUserByUsername("Bender");
	}

	@Test
	public void createUserTest() {
		User testMan = new User.UserBuilder()
				.setName("Testerberg")
				.setSearchable(true)
				.setStatus(UserStatus.IDLE)
				.build();
		userImplTest.createUser(testMan);
		ArrayList<User> testList = new ArrayList<>();
		testList = (ArrayList<User>) userImplTest.findAllUsers();
		assertTrue(testList.size() > 0);
		userImplTest.deleteUserByUsername("Testerberg");
	}

	@Test
	public void findAllUsersTest() {
		List<User> users = (List<User>) userImplTest.findAllUsers();
		assertTrue(users.size() > 0);
	}

	@Test
	public void findUserByIdTest() {
		User testUser = userImplTest.findUserByUsername("HughMann");
		User testUser2 = userImplTest.findUserById(testUser.getId());
		assertEquals("HughMann", testUser2.getName());
	}

	@Test
	public void findUserByIdMissTest() {
		assertNull(userImplTest.findUserById(0));
	}

	@Test
	public void findUserByUsernameTest() {
		User testUser = userImplTest.findUserByUsername("HughMann");
		assertEquals("HughMann", testUser.getName());
	}

	@Test
	public void findUserByUsernameMissTest() {
		assertNull(userImplTest.findUserByUsername("Nobody"));
	}

	@Test
	public void userExistsTest() {
		assertTrue(userImplTest.userExists("HughMann"));
	}

	@Test
	public void userExistsNotTest() {
		assertFalse(userImplTest.userExists("Nightmares"));
	}

	@Test
	public void updateUserTest() {
		User changer = new User.UserBuilder()
				.setName("Original")
				.setSearchable(true)
				.setStatus(UserStatus.AWAY)
				.build();
		if (!userImplTest.userExists(changer.getName())) {
			userImplTest.createUser(changer);
		}
		changer.setId(userImplTest.findUserByUsername("HughMann").getId());
		changer.setName("NewName");
		userImplTest.updateUser(changer);
		assertEquals("NewName", userImplTest.findUserByUsername("NewName").getName());
	}

	@Test
	public void deleteUserByIdTest() {
		User doomed = new User.UserBuilder()
				.setName("condemned")
				.setStatus(UserStatus.IDLE)
				.setSearchable(false)
				.build();
		if (!userImplTest.userExists(doomed.getName())) {
			userImplTest.createUser(doomed);
		}
		int dbSize = userImplTest.findAllUsers().size();
		User testUser = userImplTest.findUserByUsername(doomed.getName());
		userImplTest.deleteUserById(testUser.getId());
		assertTrue(userImplTest.findAllUsers().size() < dbSize);
	}

	@Test
	public void deleteUserByUsernameTest() {
		User doomed = new User.UserBuilder()
				.setName("condemned")
				.setStatus(UserStatus.IDLE)
				.setSearchable(false)
				.build();
		if (!userImplTest.userExists(doomed.getName())) {
			userImplTest.createUser(doomed);
		}
		int dbSize = userImplTest.findAllUsers().size();
		userImplTest.deleteUserByUsername("condemned");
		assertTrue(userImplTest.findAllUsers().size() < dbSize);
	}


	@Test
	public void deleteAllUsersTest() {
		userImplTest.deleteAllUsers();
		assertEquals(0, userImplTest.findAllUsers().size());
	}

	@Test
	public void isBotTest() {
		assertFalse(userImplTest.isBot(testPerson));
	}

	@Test
	public void isSearchableTest() {
		assertTrue(userImplTest.isSearchable(testIndividual));
		assertFalse(userImplTest.isSearchable(testHuman));
	}

	@Test
	public void retrieveStatusTest() {
		assertEquals(UserStatus.ONLINE, userImplTest.retrieveStatus(testPerson));
	}

}