import React, { Component } from 'react';
import MessageItem from './MessageItem'
import PropTypes from 'prop-types';
import axios from 'axios'
import uuid from 'uuid'

class Messages extends Component {
    render() {
        return this.props.messages.map((message) => (
            <MessageItem message={message} filterMessages={this.filterMessages} username={this.props.username} messageWith={this.props.messageWith}/>
        ));
    }
}

Messages.propTypes = {
    messages: PropTypes.array.isRequired
}

export default Messages;