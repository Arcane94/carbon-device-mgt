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
import {Redirect, Switch} from 'react-router-dom';
import {Card, CardActions, CardMedia, CardTitle} from 'material-ui/Card';
import RaisedButton from 'material-ui/RaisedButton';
import {TextValidator, ValidatorForm} from 'react-material-ui-form-validator';
import Checkbox from 'material-ui/Checkbox';
import qs from 'qs';

//todo: remove the {TextValidator, ValidatorForm} and implement it manually.

class Login extends Component {
    constructor() {
        super();
        this.state = {
            isLoggedIn: true,
            referrer: "/",
            userName: "",
            password: "",
            rememberMe: true
        }
    }

    componentDidMount() {
        let queryString = this.props.location.search;
        console.log(queryString);
        queryString = queryString.replace(/^\?/, '');
        /* With QS version up we can directly use {ignoreQueryPrefix: true} option */
        let params = qs.parse(queryString);
        if (params.referrer) {
            this.setState({referrer: params.referrer});
        }
    }

    handleLogin(event) {
        event.preventDefault();
    }

    onUserNameChange(event) {
        this.setState(
            {
                userName: event.target.value
            }
        );
    }

    onPasswordChange(event) {
        this.setState(
            {
                password: event.target.value
            }
        );
    }

    rememberMe() {
        this.setState(
            {
                rememberMe: !this.state.rememberMe
            }
        )
    }

    render() {

        if (!this.state.isLoggedIn) {
            return (
                <div>

                    {/*TODO: Style the components.*/}

                    <Card>
                        <CardTitle title="WSO2 IoT App Publisher"/>

                        <CardActions>
                            <ValidatorForm
                                ref="form"
                                onSubmit={this.handleLogin.bind(this)}
                                onError={errors => console.log(errors)}>

                                <TextValidator
                                    floatingLabelText="User Name"
                                    floatingLabelFixed={true}
                                    onChange={this.onUserNameChange.bind(this)}
                                    name="userName"
                                    validators={['required']}
                                    errorMessages={['User Name is required']}
                                    value={this.state.userName}
                                />
                                <br/>
                                <TextValidator
                                    floatingLabelText="Password"
                                    floatingLabelFixed={true}
                                    onChange={this.onPasswordChange.bind(this)}
                                    name="password"
                                    type="password"
                                    value={this.state.password}
                                    validators={['required']}
                                    errorMessages={['Password is required']}
                                />
                                <br/>
                                <Checkbox label="Remember me."
                                          onCheck={this.rememberMe.bind(this)}
                                          checked={this.state.rememberMe}/>
                                <br/>
                                <RaisedButton type="submit" label="Login"/>
                            </ValidatorForm>

                        </CardActions>
                    </Card>
                </div>);
        } else {
            return (
                <Switch>
                    <Redirect to={this.state.referrer}/>
                </Switch>
            );
        }

    }
}

export default Login;
