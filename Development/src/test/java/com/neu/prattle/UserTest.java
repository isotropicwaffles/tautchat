package com.neu.prattle;


import com.neu.prattle.controller.UserController;
import com.neu.prattle.model.Icon;
import com.neu.prattle.model.User;
import com.neu.prattle.model.UserStatus;
import com.neu.prattle.service.user.UserService;
import com.neu.prattle.service.user.UserServiceImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class UserTest {
  private UserService as;

  @Before
  public void setUp() throws IOException {
    UserServiceImpl.setEnableDBConnection(false);  
    as = UserServiceImpl.getInstance();
  }
  
  


  @After
  public void destroy() {
    UserServiceImpl.clear();
    UserServiceImpl.setEnableDBConnection(true);  

  }

  // This method just tries to add
  @Test
  public void setUserTest() throws IOException {
    User u = new User.UserBuilder()
            .setName("Mike")
            .build();
    as.addUser(u);
  }

  // This method just tries to add
  @Test
  public void getUserTest() throws IOException {
    User u = new User.UserBuilder()
            .setName("Mike")
            .build();
    as.addUser(u);
    Optional<User> user = as.findUserByName("Mike");
    assertTrue(user.isPresent());
  }

  // This method just tries to create an empty user and set the name
  @Test
  public void emptyUserConstructorTest() {
    User user = new User.UserBuilder()
            .setName("John")
            .build();
    assertEquals("John", user.getName());
  }

  // This method to sets that user.equals is false if fed a non User object
  @SuppressWarnings("unlikely-arg-type")
  @Test
  public void notEqualObjectTest() {
    User user = new User.UserBuilder()
            .setName("John")
            .build();
    assertFalse(user.equals(new String()));
  }

  // This method tests the case where multiple users of the same name are created via the user controller
  @Test
  public void createUserControllerFailTest() throws IOException {
    UserController controller = new UserController();
    Response failure409 = Response.status(409).build();

    User u = new User.UserBuilder()
            .setName("Jack")
            .build();
    controller.createUserAccount(u);

    assertEquals(failure409.getStatus(), controller.createUserAccount(u).getStatus());
  }

  // This method tests if a unique user is added via the user controller
  @Test
  public void createUserConterollerSuccessTest() throws IOException {
    UserController controller = new UserController();
    Response okayStatus = Response.ok().build();

    User u = new User.UserBuilder()
            .setName("Tim")
            .build();
    assertEquals(okayStatus.getStatus(), controller.createUserAccount(u).getStatus());
  }

  //Below are tests for the User Builder class

  @Test
  public void testUserName() {
    User u = new User.UserBuilder()
            .setName("Marco")
            .build();
    assertEquals("Marco", u.getName());
  }

  @Test
  public void testUserStatusAway() {
    User u = new User.UserBuilder()
            .setStatus(UserStatus.AWAY)
            .build();
    assertEquals(UserStatus.AWAY, u.getStatus());
  }

  @Test
  public void testUserStatusDNN() {
    User u = new User.UserBuilder()
            .setStatus(UserStatus.DONOTDISTURB)
            .build();
    assertEquals(UserStatus.DONOTDISTURB, u.getStatus());
  }

  @Test
  public void testUserStatusIdle() {
    User u = new User.UserBuilder()
            .setStatus(UserStatus.IDLE)
            .build();
    assertEquals(UserStatus.IDLE, u.getStatus());
  }

  @Test
  public void testUserStatusOnline() {
    User u = new User.UserBuilder()
            .setStatus(UserStatus.ONLINE)
            .build();
    assertEquals(UserStatus.ONLINE, u.getStatus());
  }

  @Test
  public void testUserStatusOffline() {
    User u = new User.UserBuilder()
            .setStatus(UserStatus.OFFLINE)
            .build();
    assertEquals(UserStatus.OFFLINE, u.getStatus());
  }

  @Test
  public void testUserId() {
    User u = new User.UserBuilder()
            .setId(12)
            .build();
    assertEquals(12, u.getId());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testUserIdNeg() {
    new User.UserBuilder()
            .setId(-412)
            .build();
  }

  @Test
  public void testBot() {

    User u = new User.UserBuilder()
            .setName("Mike")
            .setBot(true)
            .build();
    assertTrue(u.userIsBot());
  }

  @Test
  public void testSearchable() {
    User u = new User.UserBuilder()
            .setSearchable(true)
            .build();
    assertTrue(u.getSearchable());
  }



  @Test
  public void testIcon() {
    Icon icon = new Icon("testfiller");
    assertTrue(icon.getIconURL().equalsIgnoreCase("testfiller"));
    icon.setIconURL("changed");
    assertTrue(icon.getIconURL().equalsIgnoreCase("changed"));
  }
  
  	@Test
	public void testAddFriend() {
		User u1 = new User.UserBuilder()
            .setName("Madeline")
            .build();
		User u2 = new User.UserBuilder()
            .setName("Theo")
            .build();

		u1.addFriend(u2);
		u2.acceptRequest(u1);

		assertTrue(u1.hasFriend(u2));
		assertTrue(u2.hasFriend(u1));
	}

	@Test
	public void testAcceptDeclineRequests() {
		User u1 = new User.UserBuilder()
            .setName("You")
            .build();
		User u2 = new User.UserBuilder()
            .setName("Monika")
            .build();
		User u3 = new User.UserBuilder()
            .setName("Sayori")
            .build();

		u3.addFriend(u1);
		u2.addFriend(u1);

		assertTrue(u1.getRequesters().contains(u2));
		assertTrue(u1.getRequesters().contains(u3));

		u1.declineRequest(u3);
		u1.acceptRequest(u2);

		assertTrue(u1.hasFriend(u2));
		assertFalse(u1.hasFriend(u3));

		assertFalse(u1.getRequesters().contains(u2));
		assertFalse(u1.getRequesters().contains(u3));
	}

	@Test
	public void testRemoveFriend() {
		User u1 = new User.UserBuilder()
            .setName("Kaede")
            .build();
		User u2 = new User.UserBuilder()
            .setName("Shuichi")
            .build();
		User u3 = new User.UserBuilder()
            .setName("Kaito")
            .build();

		u1.addFriend(u2);
		u2.addFriend(u3);

		u2.acceptRequest(u1);
		u3.acceptRequest(u2);

		assertTrue(u1.hasFriend(u2));
		assertTrue(u2.hasFriend(u3));

		u2.removeFriend(u1);
		assertFalse(u1.hasFriend(u2));

		u2.removeFriend(u3);
		assertFalse(u3.hasFriend(u2));
	}

	@Test
	public void testFollowUnfollow() {
		User u1 = new User.UserBuilder()
            .setName("Mukuro")
            .build();
		User u2 = new User.UserBuilder()
            .setName("Junko")
            .build();

		u1.follow(u2);
		assertTrue(u1.isFollowing(u2));

		u1.unFollow(u2);
		assertFalse(u1.isFollowing(u2));
	}


}

