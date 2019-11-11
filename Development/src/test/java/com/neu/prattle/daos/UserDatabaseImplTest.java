package com.neu.prattle.daos;

import com.neu.prattle.model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

public class UserDatabaseImplTest {

	private UserDatabaseImpl userImplTest = new UserDatabaseImpl();

	@Before
	public void setUp() {
		User testPerson = new User();
		testPerson.setName("HughMann");
		User testIndividual = new User();
		testIndividual.setName("NotarObot");
		User testHuman = new User();
		testHuman.setName("Bender");
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
		User testMan = new User();
		testMan.setName("Testerberg");
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
		User testUser2 = userImplTest.findUserById(testUser.getIdNumber());
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
		User changer = new User();
		changer.setName("Original");
		if (!userImplTest.userExists(changer.getName())) {
			userImplTest.createUser(changer);
		}
		changer.setIdNumber(userImplTest.findUserByUsername("HughMann").getIdNumber());
		changer.setName("NewName");
		userImplTest.updateUser(changer);
		assertEquals("NewName", userImplTest.findUserByUsername("NewName").getName());
	}

	@Test
	public void deleteUserByIdTest() {
		User doomed = new User();
		doomed.setName("condemned");
		if (!userImplTest.userExists(doomed.getName())) {
			userImplTest.createUser(doomed);
		}
		int dbSize = userImplTest.findAllUsers().size();
		User testUser = userImplTest.findUserByUsername(doomed.getName());
		userImplTest.deleteUserById(testUser.getIdNumber());
		assertTrue(userImplTest.findAllUsers().size() < dbSize);
	}

	@Test
	public void deleteUserByUsernameTest() {
		User doomed = new User();
		doomed.setName("condemned");
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


}