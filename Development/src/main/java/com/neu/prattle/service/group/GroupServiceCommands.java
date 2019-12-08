package com.neu.prattle.service.group;


/**
 * This enumeration represents the different types of GroupService Message Content
 *
 * @author Richard Alexander Showalter-Bucher
 * @version 1.0 11/01/2019
 */
public enum GroupServiceCommands {


  /**
   * Represents Group Create Command
   */
  GROUP_CREATE("GROUP_CREATE"),

  /**
   * Represents Group Create Command
   */
  GROUP_DELETE("GROUP_DELETE"),

  /**
   * Represents Invite User Command
   */
  INVITE_USER("INVITE_USER"),

  /**
   * Represents Group Add User Command
   */
  APPROVE_ADD_USER("APPROVE_ADD_USER"),

  /**
   * Represents Group Add User Command
   */
  APPROVE_ADD_GROUP("APPROVE_ADD_GROUP"),

  /**
   * Represents Remove USER from Group Command
   */
  REMOVE_USER("REMOVE_USER"),

  /**
   * Represents Remove Group from Group Command
   */
  REMOVE_GROUP("REMOVE_GROUP"),

  /**
   * Represents a Request to Add User to GROUP Command
   */
  REQUEST_ADD_USER("ADD_USER"),

  /**
   * Represents a Request to Add Group to Group Command
   */
  REQUEST_ADD_GROUP("ADD_GROUP"),
  /**
   * Represents a request to get the group users
   */
  GET_GROUP_USERS("GET_GROUP_USERS"),
  /**
   * Represents a request to get the group sub groups
   */
  GET_GROUP_SUBGROUPS("GET_GROUP_SUBGROUPS"),
  /**
   * Represents a request to get the group super groups
   */
  GET_GROUP_SUPERGROUPS("GET_GROUP_SUPERGROUPS"),
  /**
   * Represents a request to get the group moderator
   */
  GET_GROUP_MODERATORS("GET_GROUP_MODERATORS"),
  /**
   * Represents a request to get the pending users requests fo the group
   */
  GET_PENDING_USER_REQUESTS("GET_PENDING_USER_REQUESTS"),
  /**
   * Represents a request to get the pending subgroup requests fo the group
   */
  GET_PENDING_SUBGROUP_REQUESTS("GET_PENDING_SUBGROUP_REQUESTS");


  /**
   * Variable to store string associate with enum
   */
  public final String label;

  /**
   * This constructor associates the string values to each enumeration
   * 
   * @param label -enum string
   */
  GroupServiceCommands(String label) {
    this.label = label;
  }
}
