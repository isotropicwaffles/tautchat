import React from 'react';
import logo from './logo.svg';
import './App.css';
import { login, createUser } from './login'
import { useAlert } from 'react-alert'

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
        login(this.state.value);
    }

    handleCreate(event) {
        event.preventDefault();
        createUser(this.state.value);
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