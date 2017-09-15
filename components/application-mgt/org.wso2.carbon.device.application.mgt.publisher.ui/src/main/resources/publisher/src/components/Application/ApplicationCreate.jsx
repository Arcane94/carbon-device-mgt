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
import Dialog from 'material-ui/Dialog';
import ApplicationMgtApi from '../../api/applicationMgtApi';
import {withRouter} from 'react-router-dom';
import FlatButton from 'material-ui/FlatButton';
import {Step1, Step2, Step3} from './CreateSteps';
import RaisedButton from 'material-ui/RaisedButton';
import {Card, CardActions, CardTitle} from 'material-ui/Card';
import {Step, StepLabel, Stepper,} from 'material-ui/Stepper';
import Theme from '../../theme';

/**
 * The App Create Component.
 *
 * Application creation is handled through a Wizard. (We use Material UI Stepper.)
 *
 * In each step, data will be set to the state separately.
 * When the wizard is completed, data will be arranged and sent to the api.
 * */
class ApplicationCreate extends Component {
    constructor() {
        super();
        this.scriptId = "application-create";
        this.setStepData = this.setStepData.bind(this);
        this.removeStepData = this.removeStepData.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleCancel = this.handleCancel.bind(this);
        this.handleYes = this.handleYes.bind(this);
        this.handleNo = this.handleNo.bind(this);
        this.handlePrev = this.handlePrev.bind(this);
        this.handleNext = this.handleNext.bind(this);
        this.state = {
            finished: false,
            stepIndex: 0,
            stepData: [],
            isDialogOpen: false
        };
    }

    componentWillMount() {
        /**
         *Loading the theme files based on the the user-preference.
         */
        Theme.insertThemingScripts(this.scriptId);
    }

    componentWillUnmount() {
        Theme.removeThemingScripts(this.scriptId);
    }

    /**
     * Handles next button click event.
     * */
    handleNext() {
        console.log("Handle Next");
        const {stepIndex} = this.state;
        this.setState({
            stepIndex: stepIndex + 1,
            finished: stepIndex >= 2,
        });
    };

    /**
     * Handles form submit.
     * */
    handleSubmit() {
        let stepData = this.state.stepData;
        let applicationCreationPromise = ApplicationMgtApi.createApplication(stepData);
        applicationCreationPromise.then( response => {
                this.handleYes();
            }
        ).catch(
            function (err) {
                console.log(err);
            }
        );

    };

    /**
     * Handles cancel button click event.
     * This will show a confirmation dialog to cancel the application creation process.
     * */
    handleCancel() {
        this.setState({isDialogOpen: true});
    };

    /**
     * Handled [ < Prev ] button click.
     * This clears the data in the current step and returns to the previous step.
     * */
    handlePrev() {
        const {stepIndex} = this.state;
        if (stepIndex > 0) {
            this.removeStepData();
            this.setState({stepIndex: stepIndex - 1});
        }
    };

    /**
     * Saves form data in each step in to the state.
     * @param step: The step number of the step data.
     * @param data: The form data of the step.
     * */
    setStepData(step, data) {
        console.log(step, data, this.state.stepData);
        let tmpStepData = this.state.stepData;
        tmpStepData.push({step: step, data: data});

        this.setState({stepData: tmpStepData}, this.handleNext())
    };

    /**
     * Remove the last data point
     * */
    removeStepData() {
        let tempData = this.state.stepData;
        tempData.pop();
        this.setState({stepData: tempData});
    };

    /**
     * Handles the Yes button in app creation cancellation dialog.
     * Clears all the form data and reset the wizard.
     * */
    handleYes() {
        this.setState({finished: false, stepIndex: 0, stepData: [], isDialogOpen: false});
    };

    /**
     * Handles No button in app creation cancellation dialog.
     * Returns to the same step.
     * */
    handleNo() {
        this.setState({isDialogOpen: false});
    };

    /**
     * Defines all the Steps in the stepper. (Wizard)
     *
     * Extension Point: If any extra steps needed, follow the instructions below.
     *                   1. Create the required form ./Forms directory.
     *                   2. Add defined case statements.
     *                   3. Define the Step in render function.
     *
     * */
    getStepContent(stepIndex) {
        switch (stepIndex) {
            case 0:
                return (
                    <Step1
                        handleNext={this.handleNext}
                        setData={this.setStepData}
                        removeData={this.removeStepData}
                    />
                );
            case 1:
                return (
                    <Step2
                        handleNext={this.handleNext}
                        handlePrev={this.handlePrev}
                        setData={this.setStepData}
                        removeData={this.removeStepData}
                    />
                );
            case 2:
                return (
                    <Step3
                        handleFinish={this.handleNext}
                        handlePrev={this.handlePrev}
                        setData={this.setStepData}
                        removeData={this.removeStepData}
                    />
                );
            default:
                return <div/>;
        }
    }

    render() {
        const {finished, stepIndex} = this.state;
        const contentStyle = {margin: '0 16px'};

        /**
         * Defines the dialog box actions. [Yes][No]
         * */
        const actions = [
            <FlatButton
                label="Yes"
                primary={true}
                onClick={this.handleYes}
            />,
            <FlatButton
                label="No"
                secondary={true}
                onClick={this.handleNo}
            />,
        ];

        return (
            <div className="middle createapplicationmiddle">
                <Card className="creataapplicationcard">
                    <CardTitle title="Create Application"/>

                    {/**
                     * The stepper goes here.
                     */}
                    <CardActions>
                        <div className="createapplicationcardaction">
                            <Stepper activeStep={stepIndex}>
                                <Step>
                                    <StepLabel>Select Application Platform</StepLabel>
                                </Step>
                                <Step>
                                    <StepLabel>Enter Application Details</StepLabel>
                                </Step>
                                <Step>
                                    <StepLabel>Release</StepLabel>
                                </Step>
                            </Stepper>
                            <div className="createapplicationcontent">
                                {finished ? (
                                    <div>
                                        <p>Create App?</p>
                                        <form>
                                            <RaisedButton primary={true} label="Create" onClick={this.handleSubmit}/>
                                            <FlatButton label="Cancel" onClick={this.handleCancel}/>
                                        </form>
                                    </div>
                                ) : (
                                    <div>
                                        {this.getStepContent(stepIndex)}
                                    </div>
                                )}
                            </div>
                        </div>
                    </CardActions>
                </Card>
                <Dialog
                    actions={actions}
                    modal={false}
                    open={this.state.isDialogOpen}
                    onRequestClose={this.handleNo}
                >
                    Do you really want to cancel?
                </Dialog>
            </div>);
    }
}

ApplicationCreate.propTypes = {};

export default withRouter(ApplicationCreate);
