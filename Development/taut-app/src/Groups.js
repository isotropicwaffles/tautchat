import React, { Component } from 'react';
import PropTypes from 'prop-types';

class Groups extends Component {

    setGroup = (a) => {
        this.props.setTo(a);
    }

    render() {

        return this.props.groups.map((group) => (
            <li>
                <button class="button is-white is-fullwidth" onClick={() => this.setGroup(group)}>{group}</button>
            </li>
        ));

    }
}

Groups.propTypes = {
    groups: PropTypes.array.isRequired
}

export default Groups;