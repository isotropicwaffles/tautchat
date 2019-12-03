import React, { Component } from 'react';
import MessageItem from './MessageItem'
import PropTypes from 'prop-types';

class Messages extends Component {
    filtered = (these) => {
        const messageWith = this.props.messageWith
        const currentUser = this.props.username
        var newMessages
        var newMessages1

        if (this.props.groups.includes(messageWith)) {
            //Filter Group Message
            console.log("Filtering Group")
            newMessages1 = these.filter(messages =>(messages.to === messageWith))
        }
        else {
            //Direct Message Filtering
            console.log("Filtering Direct")
            newMessages = these.filter(messages =>
                ((messages.from === currentUser) || (messages.from === messageWith)));
            newMessages1 = newMessages.filter(messages =>
                ((messages.to === currentUser) || (messages.to === messageWith)));
            
        }

        return newMessages1;
    }


    render() {
        return this.filtered(this.props.messages).map((message) => (
            <MessageItem message={message} filterMessages={this.filterMessages} username={this.props.username} messageWith={this.props.messageWith}/>
        ));
    }
}

Messages.propTypes = {
    messages: PropTypes.array.isRequired
}

export default Messages;