package com.neu.prattle.model;

import java.util.Objects;

/***
 * A User object represents a basic account information for a user.
 *
 * @author CS5500 Fall 2019 Teaching staff
 * @version dated 2019-10-06
 */
public class User {

	//(TODO) COMPLETE COMMITS
	public String getName() {
		return name;
	}

	//(TODO) COMPLETE COMMITS
	public void setName(String name) {
		this.name = name;
	}

	//(TODO) COMPLETE COMMITS
	private String name;

	//(TODO) COMPLETE COMMITS
	public User() {

	}

	//(TODO) COMPLETE COMMITS
    public User(String name) {
        this.name = name;
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
