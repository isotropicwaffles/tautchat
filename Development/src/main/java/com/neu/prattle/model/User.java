package com.neu.prattle.model;

import java.util.Objects;

/***
 * A User object represents a basic account information for a user.
 *
 * @author CS5500 Fall 2019 Teaching staff
 * @version dated 2019-10-06
 */
public class User {

	/** The name. */
	private String name;
	
	/** The status. */
	private UserStatus status;
	
	/** The id number. */
	private int idNumber;
	
	/** The is bot. */
	private final boolean isBot;
	
	/** The searchable. */
	private boolean searchable;
	
	
	/**
	 * Sets the name.
	 *
	 * @param name the name
	 * @return the user
	 */
	
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
	
	public User setName(String name) {
		this.name = name;
		return this;
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
	 * @return the user
	 */
	public User setStatus(UserStatus status) {
		this.status = status;
		return this;
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
	 * @param idNumber the id number
	 * @return the user
	 */
	public User setIdNumber(int idNumber) {
		if (idNumber > 0) {
			this.idNumber = idNumber;
			return this;
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
	public int getIdNumber() {
		return idNumber;
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
	 * @return the user
	 */
	public User setSearchable(boolean searchable) {
		this.searchable = searchable;
		return this;
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
