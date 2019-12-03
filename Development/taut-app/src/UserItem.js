import React, { Component } from 'react'
import PropTypes from 'prop-types';

export class UserItem extends Component {
    render() {

        const { chatWith } = this.props.user;

        return (
            <div>
                <p>
                    {chatWith}
                </p>
            </div >
        )
    }
}

UserItem.propTypes = {
    user: PropTypes.object.isRequired
}

export default UserItem