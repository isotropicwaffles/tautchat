import {messageTypes} from './GeneralMessageRouter'
import {userServiceContent} from './UserServiceMessageProcessing'
import {send} from '../../WebSocket'

/* Sends a user login request to server
 * 
 * @param username - user name to login into
 */
function sendLoginMessage(username) {
	
	//Send user creation request
    var json = JSON.stringify({
    	"type": messageTypes.USER_SERVICE,
		"contentType": userServiceContent.LOGIN,
        "content": username
    });
    
    console.log("Logging in as %s", username)

    send(json);
    
}

/*Sends a user creation request to server
 * 
 * @param username - user name to create
 */
function sendCreateUserMessage(username) {

	//Send user creation request
    var json = JSON.stringify({
    	"type": messageTypes.USER_SERVICE,
		"contentType": userServiceContent.USER_CREATE,
        "content": username
   });

   console.log("Creating new user: %s", username)
   send(json);
}



/*Sends a user name search request to server
 * 
 * @param username - user name search for, could be partial characters
 */
function sendSearchForUsernameMessage(username) {
	//Send user creation request
    var json = JSON.stringify({
    	"type": messageTypes.USER_SERVICE,
		"contentType": userServiceContent.SEARCH_USERS_BY_NAME,
        "content": username
   });
   send(json);
}

/*Sends a user friend request to server
 * 
 * @param username - user name to friend
 */
function sendFriendUser(username) {

	//Send user creation request
    var json = JSON.stringify({
    	"type": messageTypes.USER_SERVICE,
		"contentType": userServiceContent.FRIEND_USER,
        "content": username
   });
    
   send(json);
}

export {sendLoginMessage, sendCreateUserMessage};