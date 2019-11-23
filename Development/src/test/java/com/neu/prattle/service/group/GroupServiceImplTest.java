package com.neu.prattle.service.group;

import com.neu.prattle.exceptions.GroupAlreadyPresentException;
import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.service.user.UserService;
import com.neu.prattle.service.user.UserServiceImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.*;

public class GroupServiceImplTest {

  private static GroupService groupService = GroupServiceImpl.getInstance();

  @Before
  public void setUp() {
    UserServiceImpl.getInstance();
    UserServiceImpl.clearAllUsers();
  }

  @After
  public void destroy() {
    UserServiceImpl.clear();
  }

  @Test
  public void getUserGroupMemberships() {

    User mod = new User.UserBuilder()
            .setName("BobLoblaw")
            .build();

    UserService serv = UserServiceImpl.getInstance();

    try {
      serv.addUser(mod);
    } catch (IOException ignore) {
    }

    Group g = new Group(mod);
    g.setName(mod, "PowerRangers");

    Set testing = groupService.getUserGroupMemberships(mod);
    assertNotNull(testing);
  }

  @Test (expected = GroupAlreadyPresentException.class)
  public void testAddGroup() {

    User mod = new User.UserBuilder()
            .setName("Dennis")
            .build();

    UserService serv = UserServiceImpl.getInstance();

    try {
      serv.addUser(mod);
    } catch (IOException ignore) {
    }

    Group g = new Group(mod);
    g.setName(mod, "Charlie");

    try {
      groupService.addGroup(g);
      groupService.addGroup(g);
    } catch (IOException e) {
      // Should break before this
    }
  }
}