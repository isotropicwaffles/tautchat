
import { userServiveMessageRouter } from './UserServiceMessageProcessing'
import {chatMessageRouter} from './ChatMessageProcessing'

/* Enumeration for message types service messages
 * 
 */
const messageTypes = {

	USER_SERVICE: 'USER_SERVICE',
	/**
	* Represents Session Service Address
	*/
	SESSION_SERVICE: 'SESSION_SERVICE',
	/**
	* Represents Group Service Address
	*/
	GROUP_SERVICE: 'GROUP_SERVICE',
	/**
	* Represents group message address
	*/
	GROUP_MESSAGE: 'GROUP_MESSAGE',
    /**
	* Represents a direct message address
	*/
	DIRECT_MESSAGE: 'DIRECT_MESSAGE',
    /**
	* Represents a broadcast message address
	*/
	BROADCAST_MESSAGE: 'BROADCAST_MESSAGE',

}



const genericMessageResponses = {
	/**
	* Represents a Success message
	*/
	SUCCESS: 'SUCCESS',
	/**
	* Represents a Failure message
	*/
	FAILURE: 'FAILURE',

}


/* Routes received messages to correct logic
 * 
 * @param json message - json of message
 */
function generalMessageRouter(message) {
	console.log("General message router")
	console.log(message);
	if (message.type == messageTypes.BROADCAST_MESSAGE) {
		// process chat message
		console.log("I'm going to go process this broadcast")
		chatMessageRouter(message);
	}
	if (message.from == messageTypes.USER_SERVICE) {
		//  process user server message
		userServiveMessageRouter(message);
	}
}


export { generalMessageRouter, messageTypes, genericMessageResponses };