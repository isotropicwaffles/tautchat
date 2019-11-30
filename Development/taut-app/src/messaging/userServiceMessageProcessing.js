import {genericMessageResponses} from './generalMessageRouter';
import { withRouter } from 'react-router'

var status_label;

/* Enumeration for message types service messages
 * 
 */

const colors = {
	    RED:  '#d00',
	    GREEN: '#009d00'
}
 
const userServiceContent = {
	
	  /**
	   * Represents Login Command
	   */
	   LOGIN: 'LOGIN',
	  /**
	   * Represents User Create Command
	   */
	   USER_CREATE: 'USER_CREATE',
	  /**
	   * Represents Search for User by Name Command 
	   */
	   SEARCH_USERS_BY_NAME: 'SEARCH_USERS_BY_NAME',
	  /**
	   * Represents a Front User Command (TODO Hookup backend)
	   */
	   FRIEND_USER: 'FRIEND_USER',
	   
}



/* Routes received user service messages to correct logic
 * 
 * @param json message - json of user service type message
 */
function userServiveMessageRouter(message) {

	status_label = document.getElementById("login_status");
	
	//  Check if it's a login response
	if (message.contentType == userServiceContent.LOGIN) {
		processLoginResponse(message);

	}
	
	//  Check if it's a create user response
	if (message.contentType == userServiceContent.USER_CREATE) {
		//  process chat message
		processUserCreateResponse(message);
	}
	
	//  Check if it's a create user response
	if (message.contentType == userServiceContent.SEARCH_USERS_BY_NAME) {
		//  process chat message
		processUserSearchResponse(message);
	}

}


//TODO Chad add your Swap this for your GUI references

/* Process server response for user login
*
* @param json message - json of user service type message of login response
*/
function processLoginResponse(message){
	if (message.content.includes(genericMessageResponses.SUCCESS)){
		console.log("SUCCESS: User successfully logged into session.");
		alert("User successfully logged into session.");
		// If unsuccessful this should run
		// status_label.style.color = colors.GREEN;
	    // status_label.innerHTML =  "SUCCESS: User successfully logged into session.";	
	}else if (message.content.includes(genericMessageResponses.FAILURE)){
		console.log("ERROR: User Name Does not Exist. Please Create User First.");
		alert("User Name Does not Exist. Please Create User First.");
		// If unsuccessful this should run
		// status_label.style.color = colors.RED;
	    // status_label.innerHTML =  "ERROR: User Name Does not Exist. Please Create User First.";	
	}
}

//TODO Chad add your Swap this for your GUI references

/*Process server response for user creation
 * 
* @param json message - json of user service type message of create user response
 */
function processUserCreateResponse(message){
	if (message.content.includes(genericMessageResponses.SUCCESS)){
		console.log("User Successfully Created!");   
		alert("User Successfully Created!"); 
		//If successful this should run
		// status_label.style.color = colors.GREEN;
		// status_label.innerHTML =  "User Successfully Created!";
	}else if (message.content.includes(genericMessageResponses.FAILURE)){
		console.log("ERROR: Could Not Create User Name. Please Try Another Name.");
		alert("This User Name is already taken. Please Try Another Name.");
	    // If unsuccessful this should run
		// status_label.style.color = colors.RED;
		// status_label.innerHTML =  "ERROR: Could Not Create User Name. Please Try Another Name.";	
	}
}


/*Process server response for user friending
 * 
* @param json message - json of user service type message of user friend
 */
function processFriendUserResponse(message){
	
	//TODO Chad add your GUI logic here

	if (message.content.includes(genericMessageResponses.SUCCESS)){

	}else if (message.content.includes(genericMessageResponses.FAILURE)){

	}
}

/*Process server response for searching for user names
 * 
* @param json message - json of user service type message of username search message
 */
function processUserSearchResponse(message){
	//TODO Chad add your GUI logic here
}


export {userServiveMessageRouter, userServiceContent};