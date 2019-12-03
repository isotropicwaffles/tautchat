import React, { Component } from 'react';
import { Redirect } from "react-router-dom";
import AddMessage from './AddMessage';
import Messages from './Messages';
import ToField from './ToField';
import Users from './Users';

class Messenger extends Component {

    state = {
        messageWith: '',
        messages: [],
        users: [],
        groups: []
    }

    componentDidUpdate(prevProps) {
        // Typical usage (don't forget to compare props):
        if (this.props.queuedMessage !== prevProps.queuedMessage) {
            this.addToChatBox(this.props.queuedMessage)
        }
    }

    componentDidMount() {
        console.log(this.props.username)
    }

    filterMessages = () => {
        const messageWith = this.state.messageWith
        const currentUser = this.props.username
        console.log("Starting filtered messages")
        console.log(this.state.messages)
        const newMessages = this.state.messages.filter(messages =>
            ((messages.from === currentUser) || (messages.from === messageWith)));
        const newMessages1 = newMessages.filter(messages =>
            ((messages.to === currentUser) || (messages.to === messageWith)));
        console.log(newMessages1);
        this.setState({ messages: newMessages1 });
    };

    addToChatBox = (newMessage) => {
        this.setState({
            messages: [...this.state.messages, newMessage]
        }, () => {
            this.filterMessages();
        });
    }


    setTo = (to) => {
        this.setState({ messageWith: to })
    }

    //Add Item
    addMessage = (content) => {
        var json = JSON.stringify({
            "type": "BROADCAST_MESSAGE",
            "from": this.props.username,
            "to": this.state.messageWith,
            "contentType": "ASCII",
            "content": content
        });

        console.log(json);
        this.props.send(json);
    }

    render() {

        return this.props.username === null ? (
            <Redirect to="/" />
        ) : (
                <React.Fragment>
                    <Users users={this.state.users} />
                    <ToField setTo={this.setTo} />
                    <Messages messages={this.state.messages} username={this.props.username} messageWith={this.state.messageWith} />
                    <AddMessage addMessage={this.addMessage} />
                </React.Fragment>
            );
    }
}

export default Messenger;