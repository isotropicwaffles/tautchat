import React, { Component } from 'react';
import { Redirect } from "react-router-dom";
import AddMessage from './AddMessage';
import Messages from './Messages';
import ToField from './ToField';
import Users from './Users';
import Groups from './Groups'
import NewGroup from './NewGroup'

class Messenger extends Component {

    state = {
        messageWith: '',
        messages: [],
    }

    componentDidUpdate(prevProps) {
        // Typical usage (don't forget to compare props):
        if (this.props.queuedMessage !== prevProps.queuedMessage) {
            this.setState({
                messages: [...this.state.messages, this.props.queuedMessage]
            }, () => {
                console.log(this.state.messages)
                // this.filterMessages();
            });
            // this.setState      
            // this.addToChatBox(this.props.queuedMessage)
        }
    }

    componentDidMount() {
    }

    filterMessages = () => {

        if (this.props.groups.includes(this.state.messageWith)) {
            //Filter Group Message
            const messageWith = this.state.messageWith
            const currentUser = this.props.username
            const newMessages1 = this.state.messages.filter(messages =>
                (messages.to === messageWith));
            this.setState({ messages: newMessages1 });
        }
        else {
            //Direct Message Filtering
            const messageWith = this.state.messageWith
            const currentUser = this.props.username
            const newMessages = this.state.messages.filter(messages =>
                ((messages.from === currentUser) || (messages.from === messageWith)));
            const newMessages1 = newMessages.filter(messages =>
                ((messages.to === currentUser) || (messages.to === messageWith)));
            this.setState({ messages: newMessages1 });
        }


    };

    addToChatBox = (newMessage) => {
        this.setState({
            messages: [...this.state.messages, newMessage]
        }, () => {
            this.filterMessages();
        });
    }

    setTo = (to) => {
        this.setState({ messageWith: to })//, () => {
        // this.filterMessages();
        // });
    }

    addGroup = (group) => {
        this.props.addGroup(group)
        // console.log("Add the group")
        var json = JSON.stringify({
            "type": "GROUP_SERVICE",
            "contentType": "GROUP_CREATE",
            "content": group
        });

        this.props.send(json);
    }

    //Add Item
    addMessage = (content) => {
        if (this.props.groups.includes(this.state.messageWith)) {
            //Send group message
            var json = JSON.stringify({
                "type": "BROADCAST_MESSAGE",
                "from": this.props.username,
                "to": this.state.messageWith,
                "contentType": "GROUP_MESSAGE",
                "content": content
            });
        }
        else if (this.props.users.includes(this.state.messageWith)) {
            var json = JSON.stringify({
                "type": "BROADCAST_MESSAGE",
                "from": this.props.username,
                "to": this.state.messageWith,
                "contentType": "ASCII",
                "content": content
            });
        }
        this.props.send(json);
    }

    sendSearchForUsernameMessage(username) {
        //Send user creation request
        var json = JSON.stringify({
            "type": "USER_SERVICE",
            "contentType": "SEARCH_USERS_BY_NAME",
            "content": username
        });
        this.props.send(json);
    }

    render() {

        return this.props.username === null ? (
            <Redirect to="/" />
        ) : (
                <div className="container">
                    <div className="columns is-mobile">
                        <div className="column is-narrow">
                            <h1 class="title">Users</h1>
                            <React.Fragment>
                                <Users users={this.props.users} setTo={this.setTo} username={this.props.username} />
                            </React.Fragment>
                            <h1 class="title">Groups</h1>
                            <React.Fragment>
                                <NewGroup addGroup={this.addGroup} />
                                <Groups groups={this.props.groups} setTo={this.setTo} />
                            </React.Fragment>
                        </div>
                        <div className="column">
                            <React.Fragment>
                                <ToField messageWith={this.state.messageWith} />
                                <Messages messages={this.state.messages} username={this.props.username} messageWith={this.state.messageWith} groups={this.props.groups} />
                                <AddMessage addMessage={this.addMessage} />
                            </React.Fragment>

                        </div>
                    </div>
                </div >
            );
    }
}

export default Messenger;