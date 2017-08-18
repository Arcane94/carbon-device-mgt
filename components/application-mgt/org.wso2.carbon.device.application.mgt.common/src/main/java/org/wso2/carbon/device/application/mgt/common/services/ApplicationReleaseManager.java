/*
 *   Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *   WSO2 Inc. licenses this file to you under the Apache License,
 *   Version 2.0 (the "License"); you may not use this file except
 *   in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing,
 *   software distributed under the License is distributed on an
 *   "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *   KIND, either express or implied.  See the License for the
 *   specific language governing permissions and limitations
 *   under the License.
 *
 */
package org.wso2.carbon.device.application.mgt.common.services;

import org.wso2.carbon.device.application.mgt.common.ApplicationRelease;
import org.wso2.carbon.device.application.mgt.common.exception.ApplicationManagementException;

import java.util.List;

/**
 * ApplicationReleaseManager is responsible for handling all the operations related with
 * {@link org.wso2.carbon.device.application.mgt.common.ApplicationRelease} which involving addition, updation ,
 * deletion and viewing.
 *
 */
public interface ApplicationReleaseManager {

    /**
     * To create an application release for an Application.
     *
     * @param UUID UUID of the Application
     * @param applicationRelease ApplicatonRelease that need to be be created.
     * @return the unique id of the application release, if the application release succeeded else -1
     */
    public ApplicationRelease createRelease(String UUID, ApplicationRelease applicationRelease) throws
            ApplicationManagementException;

    /**
     * To get the application release of the Application/
     * @param UUID UUID of the Application.
     * @param version Version of the ApplicationRelease that need to be retrieved.
     * @return ApplicationRelease related with particular Application UUID and version.
     * @throws ApplicationManagementException ApplicationManagementException
     */
    public ApplicationRelease getRelease(String UUID, String version) throws ApplicationManagementException;

    /**
     * To get all the releases of a particular Application.
     * @param UUID UUID of the Application to get all the releases.
     * @return the List of the Application releases related with the particular Application.
     * @throws ApplicationManagementException Application Management Exception.
     */
    public List<ApplicationRelease> getReleases(String UUID)  throws ApplicationManagementException;

    /**
     * To make a release as the default one for an application.
     *
     * @param id ID of the ApplicationRelease, that need to be made default.
     */
    public void makeDefaultRelease(int id) throws ApplicationManagementException;

    /**
     * To update with a new release for an Application.
     *
     * @param applicationRelease ApplicationRelease
     * @throws ApplicationManagementException Application Management Exception.
     */
    public void updateRelease(ApplicationRelease applicationRelease) throws ApplicationManagementException;
}
