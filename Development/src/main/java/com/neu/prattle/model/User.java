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
	private boolean isBot;
	
	/** The searchable. */
	private boolean searchable;
	
	private User() {
		this.isBot = false;
	}
	
    public static UserBuilder userBuilder()   {
        return new UserBuilder();
    }
	
    //Set the name
	public void setName(String name) {
		this.name = name;
	}
	
	//Gets the name.
	public String getName() {
		return name;
	}
	
	//Sets the status.
	public void setStatus(UserStatus status) {
		this.status = status;
	}
	
	//Gets the status.
	public UserStatus getStatus() {
		return status;
	}
	
	//Sets the id number.
	public void setId(int id) {
		if (id > 0) {
			this.id = id;
		}
		else {
			throw new IllegalArgumentException("ID Number must be positive");
		}
	}
	
	//Gets the id number.
	public int getId() {
		return id;
	}

	//Sets if user is searchable.
	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}
	
	//Gets if user is searchable.
	public boolean getSearchable() {
		return searchable;
	}
	
	public boolean getBot() {
		return isBot;
	}
	
	public void setBot(boolean isBot) {
		this.isBot = isBot;
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