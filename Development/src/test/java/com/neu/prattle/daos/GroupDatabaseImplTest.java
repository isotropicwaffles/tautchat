package com.neu.prattle.daos;

import com.neu.prattle.model.Group;
import com.neu.prattle.model.User;
import com.neu.prattle.model.UserStatus;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GroupDatabaseImplTest {

  private GroupDatabaseImpl groupDatabase = new GroupDatabaseImpl();
  private UserDatabaseImpl userImplTest = new UserDatabaseImpl();
  private User testPerson;
  private User testIndividual;
  private User testHuman;
  private Group testGroup;
  private Group testSubGroup;
  private Group testGroupOther;

  @Before
  public void setUp() {
    testPerson = new User();
    testPerson.setName("HughMann");
    testPerson.setSearchable(true);
    testPerson.setStatus(UserStatus.ONLINE);
    testIndividual = new User();
    testIndividual.setName("NotarObot");
    testIndividual.setSearchable(true);
    testIndividual.setStatus(UserStatus.OFFLINE);
    testHuman = new User();
    testHuman.setName("Bender");
    testHuman.setSearchable(false);
    testHuman.setStatus(UserStatus.ONLINE);
    if (!userImplTest.userExists(testPerson.getName())) {
      userImplTest.createUser(testPerson);
    }
    if (!userImplTest.userExists(testIndividual.getName())) {
      userImplTest.createUser(testIndividual);
    }
    if (!userImplTest.userExists(testHuman.getName())) {
      userImplTest.createUser(testHuman);
    }
    testGroup = new Group(testPerson);
    testGroup.setActive(true);
    testGroup.setName(testPerson, "Super Cool Group");
    if (!groupDatabase.groupExists(testGroup.getName())) {
      groupDatabase.createGroup(testGroup);
    }
    testSubGroup = new Group(testIndividual);
    testSubGroup.setActive(false);
    testSubGroup.setName(testIndividual, "Baby Group");
    if (!groupDatabase.groupExists(testSubGroup.getName())) {
      groupDatabase.createGroup(testSubGroup);
    }
    testGroupOther = new Group(testHuman);
    testGroupOther.setActive(true);
    testGroupOther.setName(testHuman, "Other collection of folks");
    if (!groupDatabase.groupExists(testGroupOther.getName())) {
      groupDatabase.createGroup(testGroupOther);
    }
    if (!groupDatabase.subgroupForGroupExists(testGroup, testSubGroup)) {
      groupDatabase.createSubGroupForGroup(testGroup, testSubGroup);
    }
    if (!groupDatabase.subgroupForGroupExists(testGroup, testGroupOther)) {
      groupDatabase.createSubGroupForGroup(testGroup, testGroupOther);
    }
    if (!groupDatabase.moderatorForGroupExists(testPerson, testGroup)) {
      groupDatabase.createModeratorForGroup(testPerson, testGroup);
    }
    if (!groupDatabase.moderatorForGroupExists(testIndividual, testSubGroup)) {
      groupDatabase.createModeratorForGroup(testIndividual, testSubGroup);
    }
    if (!groupDatabase.moderatorForGroupExists(testHuman, testGroupOther)) {
      groupDatabase.createModeratorForGroup(testHuman, testGroupOther);
    }
    if (!groupDatabase.userAliasForGroupExists(testIndividual, testSubGroup)) {
      groupDatabase.createUserAliasForGroup("McTesterson", testIndividual, testSubGroup);
    }
    if (!groupDatabase.isUserMemberOfGroup(testPerson, testGroup)) {
      groupDatabase.addUserToGroup(testPerson, testGroup);
    }
    if (!groupDatabase.isUserMemberOfGroup(testIndividual, testSubGroup)) {
      groupDatabase.addUserToGroup(testIndividual, testSubGroup);
    }
    if (!groupDatabase.isUserMemberOfGroup(testHuman, testGroupOther)) {
      groupDatabase.addUserToGroup(testHuman, testGroupOther);
    }
    if (!groupDatabase.isUserMemberOfGroup(testIndividual, testGroup)) {
      groupDatabase.addUserToGroup(testIndividual, testGroup);
    }
    if (!groupDatabase.isUserMemberOfGroup(testHuman, testGroup)) {
      groupDatabase.addUserToGroup(testHuman, testGroup);
    }
  }

  @After
  public void tearDown() {
    groupDatabase.deleteUserAliasForGroup(testIndividual, testSubGroup);
    groupDatabase.deleteUserFromGroup(testPerson, testGroup);
    groupDatabase.deleteUserFromGroup(testIndividual, testSubGroup);
    groupDatabase.deleteUserFromGroup(testHuman, testGroupOther);
    groupDatabase.deleteUserFromGroup(testIndividual, testGroup);
    groupDatabase.deleteUserFromGroup(testHuman, testGroup);
    groupDatabase.deleteModeratorForGroup(testPerson, testGroup);
    groupDatabase.deleteModeratorForGroup(testIndividual, testSubGroup);
    groupDatabase.deleteModeratorForGroup(testHuman, testGroupOther);
    groupDatabase.deleteSubGroupForGroup(testGroup, testSubGroup);
    groupDatabase.deleteSubGroupForGroup(testGroup, testGroupOther);
    userImplTest.deleteUserByUsername("HughMann");
    userImplTest.deleteUserByUsername("NotarObot");
    userImplTest.deleteUserByUsername("Bender");
    groupDatabase.deleteGroup(testGroup);
    groupDatabase.deleteGroup(testSubGroup);
    groupDatabase.deleteGroup(testGroupOther);
  }

  @Test
  public void createGroup() {
    Group localGroup = new Group(testHuman);
    localGroup.setName(testHuman, "temp");
    localGroup.setActive(true);
    groupDatabase.createGroup(localGroup);
    assertTrue(groupDatabase.groupExists(localGroup.getName()));
  }

  @Test
  public void findAllGroups() {
    assertTrue(groupDatabase.findAllGroups().size() > 0);
  }

  @Test
  public void findGroupByName() {
    assertTrue("Super Cool Group".equalsIgnoreCase(
            groupDatabase.findGroupByName(testGroup.getName()).getName()));
  }

  @Test
  public void groupExists() {
    assertTrue(groupDatabase.groupExists(testGroupOther.getName()));
  }

  @Test
  public void updateGroup() {
    testGroupOther.setActive(true);
    groupDatabase.updateGroup(testGroupOther);
    assertTrue(groupDatabase.isGroupActive(testGroupOther));
  }

  @Test
  public void deleteGroup() {
    groupDatabase.deleteGroup(testSubGroup);
    assertFalse(groupDatabase.groupExists(testSubGroup.getName()));
  }

  @Test
  public void isGroupActive() {
    assertTrue(groupDatabase.isGroupActive(testGroup));
    assertFalse(groupDatabase.isGroupActive(testSubGroup));
  }

  @Test
  public void findPrimaryModerator() {
    assertEquals(testPerson, groupDatabase.findPrimaryModerator(testGroup));
  }

  @Test
  public void findAllModeratorsByGroup() {
    assertTrue(groupDatabase.findAllModeratorsByGroup(testSubGroup).size() > 0);
  }

  @Test
  public void findAllModerators() {
    assertTrue(groupDatabase.findAllModerators().size() > 0);
  }

  @Test
  public void findAllSubGroupsByGroup() {
    assertTrue(groupDatabase.findAllSubGroupsByGroup(testGroup).size() > 0);
    assertFalse(groupDatabase.findAllSubGroupsByGroup(testSubGroup).size() > 0);
  }

  @Test
  public void findAllSubGroups() {
    assertTrue(groupDatabase.findAllSubGroups().size() > 0);
  }

  @Test
  public void findUserAlias() {
    assertTrue("McTesterson".equalsIgnoreCase(groupDatabase.
            findUserAlias(testSubGroup, testIndividual)));
  }

  @Test
  public void createModeratorForGroup() {
    groupDatabase.createModeratorForGroup(testHuman, testGroup);
    assertTrue(groupDatabase.findAllModeratorsByGroup(testGroup).size() > 1);
  }

  @Test
  public void deleteModeratorForGroup() {
    groupDatabase.createModeratorForGroup(testHuman, testGroup);
    groupDatabase.deleteModeratorForGroup(testHuman, testGroup);
    assertEquals(1, groupDatabase.findAllModeratorsByGroup(testGroup).size());
  }

  @Test
  public void moderatorForGroupExists() {
    assertTrue(groupDatabase.moderatorForGroupExists(testPerson, testGroup));
  }

  @Test
  public void createSubGroupForGroup() {
    groupDatabase.createSubGroupForGroup(testSubGroup, testGroup);
    assertTrue(groupDatabase.findAllSubGroupsByGroup(testSubGroup).size() > 0);
  }

  @Test
  public void subgroupForGroupExists() {
    assertTrue(groupDatabase.subgroupForGroupExists(testGroup, testSubGroup));
    assertFalse(groupDatabase.subgroupForGroupExists(testSubGroup, testGroupOther));
  }

  @Test
  public void deleteSubGroupForGroup() {
    groupDatabase.createSubGroupForGroup(testSubGroup, testGroup);
    assertTrue(groupDatabase.findAllSubGroupsByGroup(testSubGroup).size() > 1);
    groupDatabase.deleteSubGroupForGroup(testSubGroup, testGroup);
    assertEquals(0, groupDatabase.findAllSubGroupsByGroup(testSubGroup).size());
  }

  @Test
  public void createUserAliasForGroup() {
    groupDatabase.createUserAliasForGroup("Godzilla", testHuman, testGroupOther);
    assertTrue("Godzilla".equalsIgnoreCase(groupDatabase.findUserAlias(testGroupOther, testHuman)));
  }

  @Test
  public void deleteUserAliasForGroup() {
    groupDatabase.createUserAliasForGroup("Godzilla", testHuman, testGroupOther);
    groupDatabase.deleteUserAliasForGroup(testHuman, testGroupOther);
    assertFalse("Godzilla".equalsIgnoreCase(groupDatabase.findUserAlias(testGroupOther, testHuman)));
  }

  @Test
  public void userAliasForGroupExists() {
    assertTrue(groupDatabase.userAliasForGroupExists(testIndividual, testSubGroup));
  }

  @Test
  public void addUserToGroup() {
    groupDatabase.addUserToGroup(testIndividual, testGroupOther);
    assertTrue(groupDatabase.findAllGroupMembers(testGroupOther).size() > 1);
  }

  @Test
  public void deleteUserFromGroup() {
    groupDatabase.addUserToGroup(testIndividual, testGroupOther);
    groupDatabase.deleteUserFromGroup(testIndividual, testGroupOther);
    assertEquals(1, groupDatabase.findAllGroupMembers(testGroupOther).size());
  }

  @Test
  public void isUserMemberOfGroup() {
    assertTrue(groupDatabase.isUserMemberOfGroup(testPerson, testGroup));
  }

  @Test
  public void findAllGroupMembersTest() {
    assertTrue(groupDatabase.findAllGroupMembers(testGroup).size() > 2);
  }
}