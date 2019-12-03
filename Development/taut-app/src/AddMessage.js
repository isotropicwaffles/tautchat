import React, { Component } from 'react'

export class AddMessage extends Component {
    state = {
        content: ''
    }

    onChange = (e) => this.setState({[e.target.name]: e.target.value});

    onSubmit = (e) => {
        e.preventDefault();
        this.props.addMessage(this.state.content);
        this.setState({content:''})
    }

    render() {
        return (
            <form onSubmit={this.onSubmit} style = {{display: 'flex'}}>
                <input
                    type="text"
                    name="content"
                    placeholder="Type Message Here"
                    style={{flex: '10', padding: '5px'}}
                    autoComplete="off"
                    value={this.state.content}
                    onChange={this.onChange}
                />
                <button type="submit" class="button is-dark">Submit</button>
                {/* <input
                    type="submit"
                    value="Submit"
                    className="btn"
                    style = {{flex: '1'}}
                /> */}
            </form>

        )
    }
}

export default AddMessage