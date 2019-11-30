import React from 'react';
import logo from './logo.svg';
import './App.css';

class Chat extends React.PureComponent {

    state = {
        chat: ['Hi', 'Hey', 'Good chat']
    }

    saveMsg = (msg) => this.setState({
        chat: [
            ...this.state.chat,
            msg
        ]
    })

    render() {
        return (
            <div class="columns">
                <div class="column is-narrow">
                    <aside class="menu">
                        <p class="menu-label">
                            Users
                        </p>
                        <ul class="menu-list">
                            <li><a>Michael</a></li>
                            <li><a>Karen</a></li>
                        </ul>
                        <p class="menu-label">
                            Groups
                        </p>
                        <ul class="menu-list">
                            <li><a>New Group</a></li>
                            <li><a>Cat Lovers</a></li>
                            <li><a>Slack Users</a></li>
                            <li><a>Frisbee Team</a></li>
                            <li><a>The boys</a></li>
                        </ul>
                    </aside>
                </div>
                <div class="column">
                    <div class="container">
                        <div class="box">
                            <div class="section has-height-100">
                                <Messages chat={this.state.chat} />
                            </div>

                            <div class="section-foot">
                                <footer class="section">
                                    <ChatBox saveMsg={this.saveMsg} />
                                </footer>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        )
    }
}

const ChatBox = ({ saveMsg }) => (
    <form onSubmit={(e) => {
        e.preventDefault();
        saveMsg(e.target.elements.userInput.value);
        e.target.reset();
    }}>
        <div class="field has-addons">
            <div class="control is-expanded">
                <input class="input" name="userInput" type="text" placeholder="Type your message" />
            </div>
            <div class="control">
                <button class="button is-info">
                    Send
          </button>
            </div>
        </div>
    </form>
);

const Messages = ({ chat }) => (
    <div style={{ heigth: '100%', width: '100%' }}>
        {chat.map((m, i) => {
            const msgClass = i === 0 || i % 2 === 0 // for demo purposes, format every other msg
            return (
                <p style={{ padding: '.25em', textAlign: msgClass ? 'left' : 'right', overflowWrap: 'normal' }}>
                    <span key={i} class={`tag is-medium ${msgClass ? 'is-success' : 'is-info'}`}>{m}</span>
                </p>
            )
        }
        )}
    </div>
);

export default Chat;