import React, { Component } from 'react';
import UserItem from './UserItem'
import PropTypes from 'prop-types';
import {Link} from 'react-router-dom'

class Users extends Component {
    render() {
        // return (this.props.users.map((user) => (
        //         <li key={user}>
        //           <Link to="/">Click</Link>
        //         </li>
        //       )
        // )

        
        return this.props.users.map((user) => (
            <UserItem user={user} username={this.props.username}/>
        ));
    }
}

Users.propTypes = {
    users: PropTypes.array.isRequired
}

export default Users;