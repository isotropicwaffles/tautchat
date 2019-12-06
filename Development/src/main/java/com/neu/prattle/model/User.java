package com.neu.prattle.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/***
 * A User object represents a basic account information for a user.
 *
 * @author CS5500 Fall 2019 Teaching staff
 * @version dated 2019-11-07
 */
public class User {

  /**
   * The name.
   */
  private String name;

  /**
   * The status.
   */
  private UserStatus status;

  /**
   * The id number.
   */
  private int id;

  /**
   * The is bot.
   */
  private boolean isBot;

  /**
   * The searchable.
   */
  private boolean searchable;
  
  /**
  * The friends of the user.
  */
  private Set<User> friends;
  
  /**
  * The users following this user.
  */
  private Set<User> following;
  /**
  * The friend requests that were sent to this user.
  */
  private Set<User> requesters;

  private User() {
    this.isBot = false;
	
	this.friends = new HashSet<>();
	this.following = new HashSet<>();
	this.requesters = new HashSet<>();
  }

  public static UserBuilder userBuilder() {
    return new UserBuilder();
  }

  //Gets the name.
  public String getName() {
    return name;
  }

  //Set the name
  public void setName(String name) {
    this.name = name;
  }

  //Gets the status.
  public UserStatus getStatus() {
    return status;
  }

  //Sets the status.
  public void setStatus(UserStatus status) {
    this.status = status;
  }

  //Gets the id number.
  public int getId() {
    return id;
  }

  //Sets the id number.
  public void setId(int id) {
    if (id > 0) {
      this.id = id;
    } else {
      throw new IllegalArgumentException("ID Number must be positive");
    }
  }

  //Gets if user is searchable.
  public boolean getSearchable() {
    return searchable;
  }

  //Sets if user is searchable.
  public void setSearchable(boolean searchable) {
    this.searchable = searchable;
  }

  public boolean getBot() {
    return isBot;
  }

  public void setBot(boolean isBot) {
    this.isBot = isBot;
  }

/**
	 * Determines if the given user is in this user's friend list.
	 * @param user potential friend
	 * @return true if user is this user's friend
	 */
	public boolean hasFriend(User user) {
		return user != null && this.friends.contains(user);
	}

	/**
	 * Determines if this user follows the given user.
	 * @param user potential followed user
	 * @return true if this user follows given user
	 */
	public boolean isFollowing(User user) {
		return user != null && this.following.contains(user);
	}

	/**
	 * Adds the given user to this user's friend list iff
	 * given user accepts a friend request.
	 * @param user friend to be added
	 */
	public void addFriend(User user) {
		// user calls this method on request accept, so
		// this only occurs if the request was accepted
		if (user.hasFriend(this)) {
			this.friends.add(user);
		}
		else {
			user.requestFriendship(this);
		}
	}

	/**
	 * Sends a friend request to this user from requester.
	 * @param requester user requesting friendship
	 */
	public void requestFriendship(User requester) {
		this.requesters.add(requester);
	}

	/**
	 * Returns a set of the users who sent friend requests
	 * to this user.
	 * @return set of requesters
	 */
	public Set<User> getRequesters() {
		return this.requesters;
	}

	/**
	 * Accepts the friend request from the given user, if such a
	 * request exists.
	 * @param user requesting user
	 */
	public void acceptRequest(User user) {
		if (this.requesters.contains(user)) {
			this.friends.add(user);
			this.requesters.remove(user);

			user.addFriend(this);
		}
	}

	/**
	 * Denies and removes the friend request from user.
	 * @param user sender of declined request
	 */
	public void declineRequest(User user) {
		this.requesters.remove(user);
	}

	/**
	 * Follows the given user.
	 * @param user user to be followed
	 */
	public void follow(User user) {
		if (user != null) {
			this.following.add(user);
		}
	}

	/**
	 * Removes the friend connection between this user and exFriend.
	 * @param exFriend friend to be removed
	 */
	public void removeFriend(User exFriend) {
		this.friends.remove(exFriend);
		exFriend.friends.remove(this);
	}

	/**
	 * Removes user from this user's list of followed users.
	 * @param user user to be unfollowed
	 */
	public void unFollow(User user) {
		this.following.remove(user);
	}


  /***
   * Returns the hashCode of this object.
   *
   * As name can be treated as a sort of identifier for
   * this instance, we can use the hashCode of "name"
   * for the complete object.
   *
   * @return hashCode of "this"
   */
  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  /***
   * Makes comparison between two user accounts.
   *
   * Two user objects are equal if their name are equal ( names are case-sensitive )
   *
   * @param obj Object to compare
   * @return a predicate value for the comparison.
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof User))
      return false;

    User user = (User) obj;
    return user.name.equals(this.name);
  }

  //Determines if the current user is a bot or not.
  public boolean userIsBot() {
    return this.isBot;
  }

  //Builder
  public static class UserBuilder {

    User user;

    //User Builder - Default to being searchable
    public UserBuilder() {
      user = new User();
      user.setSearchable(true);
      user.setStatus(UserStatus.ONLINE);
    }

    //Set the name
    public UserBuilder setName(String name) {
      user.setName(name);
      return this;
    }

    //Set the ID #
    public UserBuilder setId(int id) {
      user.setId(id);
      return this;
    }

    //Set user status
    public UserBuilder setStatus(UserStatus status) {
      user.setStatus(status);
      return this;
    }

    //Set if the user is searchable
    public UserBuilder setSearchable(boolean searchable) {
      user.setSearchable(searchable);
      return this;
    }

    public UserBuilder setBot(boolean isBot) {
      user.setBot(isBot);
      return this;
    }

    public User build() {
      return user;
    }

  }

}