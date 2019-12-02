import React from 'react';
import '../App.css';
import {generalMessageRouter} from './messaging/GeneralMessageRouter'
import {sendLoginMessage, sendCreateUserMessage} from './messaging/UserServiceMessages'

class LoginPage extends React.Component {

    constructor(props) {
        super(props);
        this.state = { value: '' };

        this.handleChange = this.handleChange.bind(this);
        this.handleLogin = this.handleLogin.bind(this);
        this.handleCreate = this.handleCreate.bind(this);

    }

    handleChange(event) {
        this.setState({ value: event.target.value });
    }

    handleLogin(event) {
        event.preventDefault();
        sendLoginMessage(this.state.value);
    }

    handleCreate(event) {
        event.preventDefault();
        sendCreateUserMessage(this.state.value);
    }

    render() {
        return (
            <div className="section is-fullheight">
                <div className="container">
                    <div className="column is-4 is-offset-4">
                        <div className="box">
                            <form onSubmit={this.handleSubmit}>
                                <div className="field">
                                    <label className="label">User Name</label>
                                    <div className="control">
                                        <input className="input" type="text" name="username" onChange={this.handleChange} />
                                    </div>
                                </div>
                                <div className="buttons is-grouped">
                                    <button type="button" className="button is-block is-info is-fullwidth" onClick={this.handleLogin}>Login</button>
                                    <button type="button" className="button is-block is-info is-fullwidth" onClick={this.handleCreate}>Create</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default LoginPage;