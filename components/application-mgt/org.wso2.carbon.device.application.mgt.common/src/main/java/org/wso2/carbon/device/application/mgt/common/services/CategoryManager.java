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

import org.wso2.carbon.device.application.mgt.common.Category;
import org.wso2.carbon.device.application.mgt.common.Filter;
import org.wso2.carbon.device.application.mgt.common.exception.ApplicationManagementException;

import java.util.List;

/**
 * CategoryManager is responsible for handling add, delete, update opertaions related with {@link Category}
 */
public interface CategoryManager {

    public Category createCategory(Category application) throws ApplicationManagementException;

    public List<Category> getCategories() throws ApplicationManagementException;

    public Category getCategory(String name) throws ApplicationManagementException;

    public void deleteCategory(String name) throws ApplicationManagementException;
}
