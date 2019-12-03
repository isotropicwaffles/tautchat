import React from 'react';
import { Redirect } from "react-router-dom";
import './App.css';

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
        //Send user creation request
        var json = JSON.stringify({
            "type": "USER_SERVICE",
            "contentType": "LOGIN",
            "content": this.state.value
        });

        console.log("Logging in as %s", this.state.value)

        this.props.send(json);
    }

    handleCreate(event) {
        event.preventDefault();

        var json = JSON.stringify({
            "type": "USER_SERVICE",
            "contentType": "USER_CREATE",
            "content": this.state.value
        });

        console.log("Creating new user: %s", this.state.value)
        this.props.send(json);


    }

    render() {

        return this.props.username !==  null ? (
            <Redirect to="/chat" />
        ) : (
                <div className="section is-fullheight">
                    <div className="container">
                        <div className="column is-4 is-offset-4">
                            <div className="box">
                                <form onSubmit={this.handleLogin}>
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