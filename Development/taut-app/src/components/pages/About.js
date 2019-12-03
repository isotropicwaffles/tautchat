import React, { Component } from 'react'

export class About extends Component {

    getStyle() {
        return {
            fontSize: '12px',
            padding: '30px',
        }
    }

    render() {
        return (
            <React.Fragment>
                <div style={this.getStyle()}>
                    <p>TautChat was established in 2019 by 4 dashing young professionals looking to expand their careers into the world of software development. They would like to thank to the following people for their support during development:</p>
                    <p>Michael Weintraub</p>
                    <p>Vaibhav Dave</p>
                    <p>Alex Grob</p>

                </div>


            </React.Fragment>
        )
    }
}

export default About