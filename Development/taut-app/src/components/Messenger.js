import React, { Component } from 'react';
import MessageItem from './MessageItem'
import PropTypes from 'prop-types';
import axios from 'axios'
import uuid from 'uuid'
import AddMessage from './AddMessage';
import Messages from './Messages';
import { generalMessageRouter } from '../components/messaging/GeneralMessageRouter'
import {sendDirectMessage} from '../components/messaging/ChatMessages'

class Messenger extends Component {

    state = {
        messages: []
    }

    componentDidMount() {
        this.loadPersistentMessages();
    }

    loadPersistentMessages() {
        const { currentUser, messageWith } = this.props.match.params
        console.log('Current user is: %s', currentUser)
        console.log('Current friend is: %s', messageWith)

        axios.get("/../exampleJSON.json")
            .then(res => this.setState({ messages: res.data }, function () {
                console.log(this.state);
                this.filterMessages(currentUser, messageWith);
            }));
    }

    filterMessages = (currentUser, messageWith) => {
        console.log(this.state.messages)
        const newMessages = this.state.messages.filter(messages =>
            ((messages.from === currentUser) || (messages.from === messageWith)));
        const newMessages1 = newMessages.filter(messages =>
            ((messages.to === currentUser) || (messages.to === messageWith)));
        console.log(newMessages1);
        this.setState({ messages: newMessages1 });

    };

    //Add Item
    addMessage = (content) => {
        const { currentUser, messageWith } = this.props.match.params

        const newMessage = {
            content,
            contentType: "DIRECT_MESSAGE",
            from: currentUser,
            to: messageWith,
            key: uuid.v4()
        }

        this.setState({ messages: [...this.state.messages, newMessage] });

        sendDirectMessage(newMessage);

    }
    render() {
        return (
            <React.Fragment>
                <Messages messages={this.state.messages} />
                <AddMessage addMessage={this.addMessage} />
            </React.Fragment>
        );
    }
}

export default Messenger;