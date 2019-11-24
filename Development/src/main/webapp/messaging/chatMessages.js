
/* Sends a broadcastmessage from chat
 *
 * @param contentType - the content type being sent (see enums)
 * @param content - the content to send
 *
 */
function sendBroadCastMessage(contentType, content) {
	
    var json = JSON.stringify({
    	"type": messsageType.BROADCAST_MESSAGE,
    	"content_type": contentType,
        "content":content,
    });

    ws.send(json);
}

/* Sends a direct user message from chat
*
* @param contentType - the content type being sent (see enums)
* @param content - the content to send
* @param username - username to send message to 
*
*/
function sendDirectMessage(contentType, content, username) {
	
   var json = JSON.stringify({
   	"to": username,
   	"type": messsageType.DIRECT_MESSAGE,
   	"content_type": contentType,
    "content":content,
   });

   ws.send(json);
}

/* Sends a direct user message with sentiment analysis from chat
*
* @param content - the content to send
* @param username - username to send message to 
*
*/
function sendDirectSentimentAnalyizedMessage(content, username) {
	
   var json = JSON.stringify({
   	"to": username,
   	"type": messsageType.SENTIMENT_ANALYSIS,
   	"content_type": contentTypes.ASCII,
    "content":content,
   });

   ws.send(json);
}

/* Sends a group ascii message rom chat
*
* @param content - the content to send
* @param groupname - username to send message to 
*
*/
function sendGroupMessage( content, groupname) {
	
   var json = JSON.stringify({
   	"to": groupname,
   	"type": messsageType.DIRECT_MESSAGE,
   	"content_type": contentTypes.GROUP_MESSAGE,
    "content":content,
   });

   ws.send(json);
}

