import { messageTypes } from './GeneralMessageRouter'
import { send } from '../../WebSocket'
import { contentTypes } from './ChatMessageProcessing'

/* Sends a broadcastmessage from chat
 *
 * @param contentType - the content type being sent (see enums)
 * @param content - the content to send
 *
 */
function sendBroadCastMessage(message) {

    var json = JSON.stringify({
        "type": messageTypes.BROADCAST_MESSAGE,
        "content_type": message.contentType,
        "content": message.content
    });

    send(json);
}

/* Sends a direct user message from chat
*
* @param contentType - the content type being sent (see enums)
* @param content - the content to send
* @param username - username to send message to 
*
*/
function sendDirectMessage(message) {

    //Send user creation request
    var json = JSON.stringify({
    	"type": messageTypes.BROADCAST_MESSAGE,
        "from": message.from,
        "to" :  message.to,
        "contentType": contentTypes.ASCII,
        "content": message.content
    });

    console.log(json);
    send(json);
}

/* Sends a group ascii message rom chat
*
* @param content - the content to send
* @param groupname - username to send message to 
*
*/
function sendGroupMessage(message) {

    var json = JSON.stringify({
        "to": message.to,
        "type": messageTypes.DIRECT_MESSAGE,
        "content_type": contentTypes.GROUP_MESSAGE,
        "content": message.content
    });

    send(json);
}

export { sendDirectMessage, sendGroupMessage, sendBroadCastMessage }