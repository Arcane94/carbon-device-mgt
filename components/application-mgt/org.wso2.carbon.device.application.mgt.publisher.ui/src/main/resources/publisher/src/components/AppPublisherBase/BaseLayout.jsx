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

import PropTypes from 'prop-types';
import React, {Component} from 'react';
import {withRouter} from 'react-router-dom';
import Axios from 'axios';
import AuthHandler from "../../api/authHandler";
import {Button, Col, Container, Input, Row,} from 'reactstrap';
import ApplicationCreate from '../Application/Create/ApplicationCreate';
import FloatingButton from "../UIComponents/FloatingButton/FloatingButton";
import {FormattedMessage} from 'react-intl';

/**
 * Base Layout:
 * App bar
 * Left Navigation
 * Middle content.
 * */
class BaseLayout extends Component {

    constructor() {
        super();
        this.logout = this.logout.bind(this);
        this.closeModal = this.closeModal.bind(this);
        this.onClickPlatforms = this.onClickPlatforms.bind(this);
        this.state = {
            notifications: 0,
            user: 'Admin',
            openModal: false,
            currentPage: "Applications",
            logo: {}
        };
    }

    componentWillMount() {
        Axios.get("/images/logo.png", {responseType: 'arraybuffer'}).then(
            response => {
                let image = "data:image/jpeg;base64," + new Buffer(response.data, 'binary').toString('base64');
                this.setState({logo: image});
            }
        ).catch(err => {
            console.log(err);
        });
    }

    handleApplicationClick() {
        this.handleHistory('/assets/apps');
    }

    handleApplicationCreateClick(event) {
        event.preventDefault();
        event.stopPropagation();
        this.setState({openModal: true});
    }

    /**
     * The method to update the history.
     * to: The URL to route.
     * */
    handleHistory(to) {
        this.props.history.push(to);
    }

    logout(event, index, value) {
        AuthHandler.logout();
    }

    closeModal() {
        this.setState({openModal: false});
    }

    onClickPlatforms() {
        window.location.href = "/publisher/assets/platforms";
        this.setState({currentPage: "Platforms"});

    }

    render() {
        return (
            <div>
                <div id="header-content">

                    <div id="header">
                        <span id="header-text">
                            <img className="header-image" src={this.state.logo}/>
                            IoT <FormattedMessage id="App.Publisher" defaultMessage="Application Publisher"/>
                        </span>
                        <div id="header-btn-container">
                            <Button id="header-button"><i className="fw fw-notification btn-header"></i></Button>
                            <Button id="header-button"><i className="fw fw-user btn-header"></i></Button>
                        </div>
                        <div id="search-box">
                            <i className="fw fw-search search-icon">
                            </i>
                            <Input
                                id="search"
                                name="search"
                                placeholder={'Search for Applications'}
                                onChange={(event) => console.log(event.target.value)} //TODO: Remove this
                            />
                        </div>
                    </div>
                    <Container>
                        <div id="add-btn-container">
                            <FloatingButton
                                className="add-btn small"
                                onClick={this.handleApplicationCreateClick.bind(this)}
                            />
                        </div>
                    </Container>
                </div>
                <Container className="application-container">
                    <div id="app-main-content">
                        <Row id="sub-title-container">
                            <Col>
                                <div id="sub-title">
                                    {/*TODO: Add the current page title*/}
                                    {/*{window.location.pathname}*/}
                                </div>
                            </Col>
                            <Col>
                                <div className="platform-link-placeholder">
                                    <Button id="secondary-button" onClick={this.onClickPlatforms}>
                                        <i className="fw fw-settings"></i> Platforms</Button>
                                </div>
                            </Col>
                        </Row>
                        <Row>
                            <div id="application-content">
                                <Row>
                                    <Col>
                                        {this.props.children}
                                    </Col>
                                </Row>
                            </div>
                        </Row>
                    </div>
                </Container>
                <ApplicationCreate open={this.state.openModal} close={this.closeModal}/>
            </div>
        );
    }
}

BaseLayout.propTypes = {
    children: PropTypes.element
};

export default withRouter(BaseLayout);
