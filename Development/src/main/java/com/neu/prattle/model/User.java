package com.neu.prattle.model;

import java.util.Objects;

/***
 * A User object represents a basic account information for a user.
 *
 * @author CS5500 Fall 2019 Teaching staff
 * @version dated 2019-11-07
 */
public class User {

	/** The name. */
	private String name;
	
	/** The status. */
	private UserStatus status;
	
	/** The id number. */
	private int id;
	
	/** The is bot. */
	private final boolean isBot;
	
	/** The searchable. */
	private boolean searchable;
	
	public User() {
		this.isBot = false;
	}
	
	public User(String name) {
		this.name = name;
		this.isBot = false;
	}
	
	public User(String name, boolean isBot) {
		this.name = name;
		this.isBot = isBot;
	}
	
  /**
	 * Sets the name.
	 *
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the status.
	 *
	 * @param status the status
	 */
	public void setStatus(UserStatus status) {
		this.status = status;
	}
	
	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public UserStatus getStatus() {
		return status;
	}
	
	/**
	 * Sets the id number.
	 *
	 * @param id the id number
	 */
	public void setId(int id) {
		if (id > 0) {
			this.id = id;
		}
		else {
			throw new IllegalArgumentException("ID Number must be positive");
		}
	}
	
	/**
	 * Gets the id number.
	 *
	 * @return the id number
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets whether user is a bot.
	 *
	 * @return if the user is a bot
	 */
	public boolean getBot() {
		return isBot;
	}

	
	/**
	 * Sets the searchable.
	 *
	 * @param searchable the searchable
	 */
	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}
	
	/**
	 * Gets if user is searchable.
	 *
	 * @return if user is searchable
	 */
	public boolean getSearchable() {
		return searchable;
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
}
