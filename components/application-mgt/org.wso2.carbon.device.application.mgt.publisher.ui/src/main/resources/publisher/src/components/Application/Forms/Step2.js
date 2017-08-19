/*
 * Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import React, {Component} from 'react';
import RaisedButton from 'material-ui/RaisedButton';
import FlatButton from 'material-ui/FlatButton';

class Step2 extends Component {
    constructor() {
        super();
    }

    handleNext() {
        this.props.handleNext();
    }

    handlePrev() {
        this.props.handlePrev();
    }

    render() {
        const contentStyle = {margin: '0 16px'};
        return (
                <div style={contentStyle}>
                    Step2
                    <div>
                        <div style={{marginTop: 12}}>
                            <FlatButton
                                label="< Back"
                                disabled= {false}
                                onClick={this.handlePrev.bind(this)}
                                style={{marginRight: 12}}
                            />
                            <RaisedButton
                                label="Next >"
                                primary={true}
                                onClick={this.handleNext.bind(this)}
                            />
                        </div>
                    </div>
                </div>
        );
    }
}

export default Step2;