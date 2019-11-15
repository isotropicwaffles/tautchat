package com.neu.prattle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.neu.prattle.daos.UserDatabaseImpl;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.Message;
import com.neu.prattle.model.User;
import com.neu.prattle.service.user.UserService;
import com.neu.prattle.service.user.UserServiceImpl;

public class GroupTest {
	@Before
	public void setUp() throws IOException {
		UserServiceImpl.getInstance();
		UserServiceImpl.clearAllUsers();

	}


	@After
	public void destroy() {
		UserServiceImpl.clear();
;
	}
	@Test
	public void testDeactivateReactivate() {
	
		User mod = new User("Sojiro");
		
		UserService serv = UserServiceImpl.getInstance();
		
		try {
			serv.addUser(mod);
		} catch (IOException ignore) {}
		
		Group g = new Group(mod);
		g.setName(mod, "LeBlanc");
		
		g.deactivate(mod);
		g.setName(mod, "Not LeBlanc");
		
		assertEquals("LeBlanc", g.getName());
		
		g.reactivate(mod);
		g.setName(mod, "New LeBlanc");
		
		assertEquals("New LeBlanc", g.getName());
	}
	
	@Test
	public void testAddRemoveUser() {
		
		User mod = new User("Kawakami");
		User user = new User("Makoto");
		
		UserService serv = UserServiceImpl.getInstance();
		
		try {
			serv.addUser(mod);
			serv.addUser(user);
		} catch (IOException ignore) {}
		
		Group g = new Group(mod);
		g.addUser(mod, user);
		
		assertTrue(g.hasMember(user));
		
		g.removeUser(mod, user);
		
		assertFalse(g.hasMember(user));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testIllegalBuild() {
		Group g = new Group.GroupBuilder().build();
	}
	
	
	@Test
	public void testBuilder() {
		User supermod = new User("Ren");
		
		User midmod = new User("Ann");
		User user = new User("Morgana");
		
		User submod = new User("Ryuji");
		
		UserService serv = UserServiceImpl.getInstance();
		
		try {
			serv.addUser(supermod);
			serv.addUser(midmod);
			serv.addUser(user);
			serv.addUser(submod);
		} catch (IOException ignore) {}
		
		//  make subgroup, add to builder -- going to probably have to make a way for mods to change their aliases, too
		Group sub = new Group.GroupBuilder().setName("Underdogs").addModerator(submod).setAlias(submod, "Skull").build();
				
		//  make supergroup, add to builder
		Group sup = new Group.GroupBuilder().setName("Phantom Thieves").addModerator(supermod).setAlias(supermod, "Joker").build();		
		
		// Long line, huh? Splitting it up a bit.
		Group.GroupBuilder b = new Group.GroupBuilder().setName("Felines").addModerator(midmod).addUser(user);
		b.setAlias(midmod, "Panther").setAlias(user, "Mona");
		Group group = b.addSubGroup(sub).addSuperGroup(sup).build();
		
		assertTrue(group.hasSubGroup(sub));
		
		assertFalse(group.hasMember(supermod));
		
		// Might need to add like alias test here or something, but...
	}
	
	@Test
	public void testAddRemoveSubgroup() {
		User mod = new User("mod");
		User u2 = new User("u2");
		
		UserService serv = UserServiceImpl.getInstance();
		try {
			serv.addUser(mod);
			serv.addUser(u2);
		} catch (IOException ignore) {}
		
		Group g = new Group(mod);
		Group sub = new Group(u2);
		g.addSubgroup(mod, sub);
		
		assertTrue(g.hasSubGroup(sub));
		
		g.removeSubgroup(mod, sub);
		
		assertFalse(g.hasSubGroup(sub));
	}
	
	@Test
	public void testAddRemoveSupergroup() {
		User mod = new User("mod2");
		User u2 = new User("u3");
		
		UserService serv = UserServiceImpl.getInstance();
		try {
			serv.addUser(mod);
			serv.addUser(u2);
		} catch (IOException ignore) {}
		
		Group g = new Group(mod);
		Group sup = new Group(u2);
		g.addSupergroup(mod, sup);
		
		assertTrue(sup.hasSubGroup(g));
		
		g.removeSupergroup(mod, sup);
		
		assertFalse(sup.hasSubGroup(g));
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testInvalidGroupCreation() {
		User bot = new User("Gank", true);
		Group g = new Group.GroupBuilder().addModerator(bot).setName("test").build();
	}
	
	@Test
	public void testAddModerator() {
		User mod1 = new User("Futaba");
		User mod2 = new User("Wakaba");
		
		UserService serv = UserServiceImpl.getInstance();
		try {
			serv.addUser(mod1);
			serv.addUser(mod2);
		} catch (IOException ignore) {}
		
		Group g = new Group.GroupBuilder().addModerator(mod1).setName("Palace 4").build();
		assertTrue(g.getModerators().contains(mod1));
		
		g.addModerator(mod1, mod2);
		assertTrue(g.getModerators().contains(mod1));
		assertTrue(g.getModerators().contains(mod2));
	}
}
