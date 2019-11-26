

/* Enumeration for non-service message content types
 * 
 */
const contentTypes = {
	/**
	* Represents Standard ASCII Text content
	*/
	ASCII: 'ASCII',

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
		
		if (message.contentType == contentTypes.GROUP_MESSAGE){
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


// TODO Chad, you can use these colors or do something else
// I was just putting them here as an example

/* Enumeration colors to use for sentiment
 * 
 */
const sentimentColors = {
    NEGATIVE:  '#d00', //Red
    NEUTRAL: '	#383838', //Dark Gray
    POSITIVE: '#009d00' //Green
}


/* Enumeration representing positive, negative, and neutral sentiments
* 
*/
const sentiment = {
    NEGATIVE:  '0', 
    NEUTRAL: '1',	 
    POSITIVE: '2' 
}

//TODO Chad, I'm not sure how you'll want to display the sentiment information
// in the GUI. Below is just an example of a template you can use to iterate through
// The sentiments of the content.
// The message.content should be the same size as the message.sentiment
// Each character of the content has a sentiment value associated with it, either 0,1, or 2 (see the sentiment enumeration above)


/* Template function that shows how content and sentiment are associate
 * Feel free to update this to something with useful functionality
 *  
 * @param json message - json of chat type message
 */
function processSentimentContent(message){
	
	
	for (var i = 0; i < message.content.length; i++) {
		  
		switch(message.sentiment.charAt(i)){
			
			case sentiment.NEGATIVE:
				//TODO chad feel free to add logic
				break;
				
			case sentiment.POSITIVE:
				//TODO chad feel free to add logic
				break;
				
			case sentiment.NEUTRAL:
				//TODO chad feel free to add logic

		}
	}
	
}



