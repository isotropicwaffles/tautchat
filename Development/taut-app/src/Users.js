import React, { Component } from 'react';
import PropTypes from 'prop-types';

class Users extends Component {

    setMessageWith = (a) => {
        this.props.setTo(a);
    }

    filtered = (these) => {
        return these.filter(item => (item !== this.props.username))
    }


    render() {

        return this.filtered(this.props.users).map((user) => (
            <li>
                <button class="button is-white is-fullwidth" onClick={() => this.setMessageWith(user)}>{user}</button>
            </li>
        ));
    }
}

Users.propTypes = {
    users: PropTypes.array.isRequired
}

export default Users;