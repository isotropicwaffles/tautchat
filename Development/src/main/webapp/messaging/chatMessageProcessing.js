

/* Enumeration for non-service message content types
 * 
 */
const contentTypes = {
	/**
	* Represents Standard ASCII Text content
	*/
	ASCII: 'ASCII',
	/**
	* Represents SENTIMENT Analyized text content
	*/
	SENTIMENT_TEXT: 'SENTIMENT_TEXT',
	
	/**
	* Represents GROUP_MESSAGE ASCII content
	*/
	GROUP_MESSAGE: 'GROUP_MESSAGE',
	
	
}

/* Routes received user service messages to correct logic
 * 
 * @param json message - json of user service type message
 */
function chatMessageRouter(message) {
	//  Process Broadcast
	if (message.type == messsageType.BROADCAST_MESSAGE) {
		processBroadcastChatMessage(message);

	} else if (message.type == messsageType.DIRECT_MESSAGE) {
		//Process Direct Message 
		
		// Process Sentiement Text Message
		if (message.contentType == contentTypes.SENTIMENT_TEXT){
			
			processSentimentAnalyizedChatMessage(message);
			
		}else if (message.contentType == contentTypes.GROUP_MESSAGE){
			//Process Group Message
			processGroupMessage(message);
		}
		else{
			//Process ASCII Message (assumed content for now)
			processDirectChatMessage(message);
		}
			
	
	}
}


/* Processes recieved direct message chat
 * 
 * @param json message - json of chat type message
 */
function processDirectChatMessage(message) {
	//TODO Chad put in GUI logic

}

/* Processes recieved sentiment analyized message chat
 * 
 * @param json message - json of chat type message
 */
function processSentimentAnalyizedChatMessage(message) {
	//TODO Chad put in GUI logic

}

/* Processes recieved broadcast message chat
 * 
 * @param json message - json of chat type message
 */
function processBroadcastChatMessage(message) {
	//TODO Chad put in GUI logic

}



/* Processes recieved group message chat
 * 
 * @param json message - json of chat type message
 */
function processGroupMessage(message) {
	//TODO Chad put in GUI logic
}


