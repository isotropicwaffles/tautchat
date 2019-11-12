package com.neu.prattle.exceptions;

import com.neu.prattle.model.Group;

/***
 * An representation of an error which is thrown where a request has been made
 * for creation of a group object that already exists in the system.
 * Refer {@link com.neu.prattle.model.Group#equals}
 * Refer {@link com.neu.prattle.service.GroupService#addGroup(Group)}
 *
 * @author Richard Alexander Showalter-Bucher
 * @version dated 2019-11-10
 */
public class GroupAlreadyPresentException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4845176561270017896L;

	public GroupAlreadyPresentException(String message)  {
        super(message);
    }
}
