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
'use strict';

import Axios from 'axios';
import AuthHandler from './authHandler';
import Constants from '../common/constants';
import Helper from './helpers/appMgtApiHelpers';



export default class Endpoint {

    /* =================================================================
     *               Application related apis
     * */

    /**
     * Api for create an application.
     * @param: applicationData: The application data object. This contains an object array of each step data from
     * application creation wizard.
     *
     * From applicationData, the proper application object will be created and send it to the api.
     * */
    static createApplication(applicationData) {
        let app = Helper.buildApplication(applicationData).application;
        const headers = AuthHandler.createAuthenticationHeaders("application/json");
        return Axios.post(Constants.appManagerEndpoints.CREATE_APP, app, {headers: headers});
    }

    /**
     * Upload the image artifacts (banner, icon, screenshots) related to the application.
     * @param appId: The application uuid of the application which the images should be uploaded to.
     * */
    static uploadImageArtifacts(appId) {
        let user = AuthHandler.getUser();
        const headers = AuthHandler.createAuthenticationHeaders("multipart/form-data");
        return Axios.post(Constants.appManagerEndpoints.UPLOAD_IMAGES + appId, appId, {headers: headers});
    }

    /**
     * Method to handle application release process.
     * */
    static releaseApplication(appId) {

    }

    /**
     * Promote the current life cycle state of the application.
     * @param appId: The uuid of the application which the state should be updated.
     * */
    static updateLifeCycleState(appId) {

    }

    /**
     * Get the next possible state, which the application can be promoted to.
     * @param appId: The application uuid.
     */
    static getNextLifeCycleState(appId) {

    }

    /**
     * Edit created application.
     * @param applicationData: The modified application data.
     * */
    static editApplication(applicationData) {
        let app = Helper.buildApplication(applicationData).application;
        const headers = AuthHandler.createAuthenticationHeaders("application/json");
        return Axios.put(Constants.appManagerEndpoints.CREATE_APP, app, {headers: headers});
    }

    static editApplicationArtofacts(appId) {
        const headers = AuthHandler.createAuthenticationHeaders("application/json");
        return Axios.put(Constants.appManagerEndpoints.CREATE_APP, appId, {headers: headers});
    }

    /**
     * Get all the created applications for the user.
     * @return Object: The response object from the axios post.
     * */
    static getApplications() {
        let user = AuthHandler.getUser();
        console.log("Get all applications", user.getAuthToken());
        const headers = AuthHandler.createAuthenticationHeaders("application/json");
        return Axios.get(Constants.appManagerEndpoints.GET_ALL_APPS, {headers: headers});
    }

    /**
     * Get specific application.
     * @param appId: The application Id.
     * */
    static getApplication(appId) {
        let user = AuthHandler.getUser();
        console.log("Get Application",appId, user.getAuthToken());
        const headers = AuthHandler.createAuthenticationHeaders("application/json");
        return Axios.get(Constants.appManagerEndpoints.GET_ALL_APPS + appId, {headers: headers});
    }

    /**
     * Delete specified application.
     * @param appId: The id of the application which is to be deleted.
     * */
    static deleteApplication(appId) {

    }

    /*
     *              End of Application management apis.
     * =================================================================
     * =================================================================
     *                   Platform related apis
     * */

    /**
     * Create a new Platform
     * @param platformData: The platform data object.
     * */
    static createPlatform(platformData) {
        const headers = AuthHandler.createAuthenticationHeaders("application/json");
        Axios.post(Constants.platformManagerEndpoints.CREATE_PLATFORM, platformData, {headers: headers}).then(
            function (response) {
                console.log(response);
            }
        ).catch(function (err) {
            console.log(err);
        });
    }

    /**
     * Get available platforms
     * */
    static getPlatforms() {
        const headers = AuthHandler.createAuthenticationHeaders("application/json");
        return Axios.get(Constants.platformManagerEndpoints.GET_ENABLED_PLATFORMS, {headers: headers});
    }

    /**
     * Get the user specified platform
     * @param platformId: The identifier of the platform
     * */
    static getPlatform(platformId) {
        const headers = AuthHandler.createAuthenticationHeaders("application/json");
        return Axios.get(Constants.platformManagerEndpoints.GET_PLATFORM + platformId, {headers: headers});
    }

    /**
     * Delete specified platform
     * @param platformId: The id of the platform which is to be deleted.
     * */
    static deletePlatform(platformId) {
        const headers = AuthHandler.createAuthenticationHeaders("application/json");
        return Axios.delete(Constants.platformManagerEndpoints.GET_PLATFORM + platformId, {headers: headers});
    }

    /*
    *              End of Platform management apis.
    * =================================================================
    * */
}
