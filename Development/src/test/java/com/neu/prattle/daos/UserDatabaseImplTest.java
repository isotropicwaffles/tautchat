package com.neu.prattle.daos;

import com.neu.prattle.model.User;
import com.neu.prattle.model.UserStatus;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserDatabaseImplTest {

	private static UserDatabaseImpl userImplTest = new UserDatabaseImpl();
	private static User testPerson;
	private static User testIndividual;
	private static User testHuman;

	@BeforeClass
	public static void setUp() {
	    testPerson = new User.UserBuilder()
	    		.setName("HughMann")
	    		.setSearchable(true)
	    		.setStatus(UserStatus.ONLINE)
	    		.build();
	    
	    testIndividual = new User.UserBuilder()
	    		.setName("NotaRobot")
	    		.setSearchable(true)
	    		.setStatus(UserStatus.OFFLINE)
	    		.build();

	    testHuman = new User.UserBuilder()
	    		.setName("Bender")
	    		.setSearchable(false)
	    		.setStatus(UserStatus.ONLINE)
	    		.build();

			userImplTest.createUser(testPerson);
			userImplTest.createUser(testIndividual);
			userImplTest.createUser(testHuman);
	}

	@AfterClass
	public static void tearDown() {
		userImplTest.deleteAllUsers();
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
	public void findUserByUsernameMissTest() {
		assertNull(userImplTest.findUserByUsername("Nobody"));
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
		userImplTest.createUser(changer);
		changer.setStatus(UserStatus.DONOTDISTURB);
		userImplTest.updateUser(changer);
		assertEquals(UserStatus.DONOTDISTURB, userImplTest.retrieveStatus(changer));
	}

	@Test
	public void deleteUserByIdTest() {
		User doomed = new User.UserBuilder()
				.setName("theJudged")
				.setStatus(UserStatus.IDLE)
				.setSearchable(false)
				.build();
		userImplTest.createUser(doomed);
		userImplTest.deleteUserById(doomed.getId());
		assertFalse(userImplTest.userExists(doomed.getName()));
	}

	@Test
	public void deleteUserUsernameTest() {
		User doomed = new User.UserBuilder()
						.setName("condemned")
						.setStatus(UserStatus.IDLE)
						.setSearchable(false)
						.build();
		if (!userImplTest.userExists(doomed.getName())) {
			userImplTest.createUser(doomed);
		}
		userImplTest.deleteUserByUsername(doomed.getName());
		assertFalse(userImplTest.userExists(doomed.getName()));
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

	@Test(expected = SQLException.class)
	public void breakCreateUser() {
		UserDatabaseImpl mockImpl = mock(UserDatabaseImpl.class);
		doThrow(SQLException.class).when(mockImpl).createUser(any(User.class));
		mockImpl.createUser(testPerson);
	}

	@Test(expected = SQLException.class)
	public void breakFindAllUsers() {
		UserDatabaseImpl mockImpl = mock(UserDatabaseImpl.class);
		when(mockImpl.findAllUsers()).thenThrow(SQLException.class);
		mockImpl.findAllUsers();
	}



	@Test
	public void deleteAllUsersTest() {
		userImplTest.deleteAllUsers();
		assertEquals(0, userImplTest.findAllUsers().size());
		setUp();
	}
}