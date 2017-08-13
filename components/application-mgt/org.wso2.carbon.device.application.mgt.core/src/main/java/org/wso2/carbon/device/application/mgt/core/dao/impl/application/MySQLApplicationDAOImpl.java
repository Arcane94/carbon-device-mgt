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
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.device.application.mgt.core.dao.impl.application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.wso2.carbon.device.application.mgt.common.*;
import org.wso2.carbon.device.application.mgt.common.exception.DBConnectionException;
import org.wso2.carbon.device.application.mgt.core.exception.ApplicationManagementDAOException;
import org.wso2.carbon.device.application.mgt.core.dao.common.Util;
import org.wso2.carbon.device.application.mgt.core.util.ConnectionManagerUtil;
import org.wso2.carbon.device.application.mgt.core.util.JSONUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class holds the generic implementation of ApplicationDAO which can be used to support ANSI db syntax.
 */
public class MySQLApplicationDAOImpl extends AbstractApplicationDAOImpl {

    private static final Log log = LogFactory.getLog(MySQLApplicationDAOImpl.class);

    @Override
    public ApplicationList getApplications(Filter filter) throws ApplicationManagementDAOException {

        if (log.isDebugEnabled()) {
            log.debug("Getting application data from the database");
            log.debug(String.format("Filter: limit=%s, offset=%", filter.getLimit(), filter.getOffset()));
        }

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        ApplicationList applicationList = new ApplicationList();
        List<Application> applications = new ArrayList<>();
        Pagination pagination = new Pagination();

        if (filter == null) {
            throw new ApplicationManagementDAOException("Filter need to be instantiated");
        } else {
            pagination.setLimit(filter.getLimit());
            pagination.setOffset(filter.getOffset());
        }

        try {

            conn = this.getConnection();

            sql += "SELECT SQL_CALC_FOUND_ROWS APP.*, APL.NAME AS APL_NAME, APL.IDENTIFIER AS APL_IDENTIFIER, ";
            sql += "CAT.ID AS CAT_ID, CAT.NAME AS CAT_NAME ";
            sql += "FROM APPM_APPLICATION AS APP ";
            sql += "INNER JOIN APPM_PLATFORM AS APL ON APP.PLATFORM_ID = APL.ID ";
            sql += "INNER JOIN APPM_APPLICATION_CATEGORY AS CAT ON APP.APPLICATION_CATEGORY_ID = CAT.ID ";

            if (filter.getSearchQuery() != null && !filter.getSearchQuery().isEmpty()) {
                sql += "WHERE APP.NAME LIKE ? ";
            }
            sql += "LIMIT ?,?;";

            stmt = conn.prepareStatement(sql);
            int index = 0;
            if (filter.getSearchQuery() != null && !filter.getSearchQuery().isEmpty()) {
                stmt.setString(++index, "%" + filter.getSearchQuery() + "%");
            }
            stmt.setInt(++index, filter.getOffset());
            stmt.setInt(++index, filter.getLimit());

            rs = stmt.executeQuery();

            int length = 0;
            sql = "SELECT FOUND_ROWS() AS COUNT;"; //TODO: from which tables????
            stmt = conn.prepareStatement(sql);
            ResultSet rsCount = stmt.executeQuery();
            if (rsCount.next()) {
                pagination.setCount(rsCount.getInt("COUNT"));
            }

            while (rs.next()) {

                //Getting properties
                sql = "SELECT * FROM APPM_APPLICATION_PROPERTY WHERE APPLICATION_ID=?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, rs.getInt("ID"));
                ResultSet rsProperties = stmt.executeQuery();

                //Getting tags
                sql = "SELECT * FROM APPM_APPLICATION_TAG WHERE APPLICATION_ID=?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, rs.getInt("ID"));
                ResultSet rsTags = stmt.executeQuery();

                applications.add(Util.loadApplication(rs, rsProperties, rsTags));
                Util.cleanupResources(null, rsProperties);
                Util.cleanupResources(null, rsTags);
                length++;
            }

            pagination.setSize(length);

            applicationList.setApplications(applications);
            applicationList.setPagination(pagination);

        } catch (SQLException e) {
            throw new ApplicationManagementDAOException("Error occurred while getting application List", e);
        } catch (JSONException e) {
            throw new ApplicationManagementDAOException("Error occurred while parsing JSON", e);
        } catch (DBConnectionException e) {
            throw new ApplicationManagementDAOException("Error occurred while obtaining the DB connection.", e);
        } finally {
            Util.cleanupResources(stmt, rs);
        }
        return applicationList;
    }


    @Override
    public void deleteApplication(String uuid) throws ApplicationManagementDAOException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.getDBConnection();
            String sql = "DELETE FROM APPM_APPLICATION WHERE UUID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, uuid);
            stmt.executeUpdate();

        } catch (DBConnectionException e) {
            throw new ApplicationManagementDAOException("Error occurred while obtaining the DB connection.", e);
        } catch (SQLException e) {
            throw new ApplicationManagementDAOException("Error occurred while deleting the application: " + uuid, e);
        } finally {
            Util.cleanupResources(stmt, rs);
        }
    }

    @Override
    public int getApplicationId(String uuid) throws ApplicationManagementDAOException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";

        int id = 0;

        try {
            conn = this.getDBConnection();
            sql += "SELECT ID FROM APPM_APPLICATION WHERE UUID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, uuid);
            rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (DBConnectionException e) {
            throw new ApplicationManagementDAOException("Error occurred while obtaining the DB connection.", e);
        } catch (SQLException e) {
            throw new ApplicationManagementDAOException("Error occurred while getting application List", e);
        } finally {
            Util.cleanupResources(stmt, rs);
        }

        return id;
    }

    @Override
    public Application editApplication(Application application) throws ApplicationManagementDAOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";

        try {
            conn = this.getConnection();
            sql += "UPDATE APPM_APPLICATION SET ";
            sql += "NAME = IFNULL (?, NAME), ";
            sql += "SHORT_DESCRIPTION = IFNULL (?, SHORT_DESCRIPTION), DESCRIPTION = IFNULL (?, DESCRIPTION), ";
            sql += "ICON_NAME = IFNULL (?, ICON_NAME), BANNER_NAME = IFNULL (?, BANNER_NAME), ";
            sql += "VIDEO_NAME = IFNULL (?, VIDEO_NAME), SCREENSHOTS = IFNULL (?, SCREENSHOTS), ";
            sql += "MODIFIED_AT = IFNULL (?, MODIFIED_AT), ";
            if (application.getPayment() != null) {
                sql += " IS_FREE = IFNULL (?, IS_FREE), ";
                if (application.getPayment().getPaymentCurrency() != null) {
                    sql += "PAYMENT_CURRENCY = IFNULL (?, PAYMENT_CURRENCY), ";
                }
                sql += "PAYMENT_PRICE = IFNULL (?, PAYMENT_PRICE), ";
            }
            if (application.getCategory() != null && application.getCategory().getId() != 0) {
                sql += "APPLICATION_CATEGORY_ID = IFNULL (?, APPLICATION_CATEGORY_ID), ";
            }
            if (application.getPlatform() != null && application.getPlatform().getId() != 0) {
                sql += "PLATFORM_ID = IFNULL (?, PLATFORM_ID), ";
            }

            sql += "TENANT_ID = IFNULL (?, TENANT_ID) ";
            sql += "WHERE UUID = ?";

            int i = 0;
            stmt = conn.prepareStatement(sql);
            stmt.setString(++i, application.getName());
            stmt.setString(++i, application.getShortDescription());
            stmt.setString(++i, application.getDescription());
            stmt.setString(++i, application.getIconName());
            stmt.setString(++i, application.getBannerName());
            stmt.setString(++i, application.getVideoName());
            stmt.setString(++i, JSONUtil.listToJsonArrayString(application.getScreenshots()));
            stmt.setDate(++i, new Date(application.getModifiedAt().getTime()));
            if (application.getPayment() != null) {
                stmt.setBoolean(++i, application.getPayment().isFreeApp());
                if (application.getPayment().getPaymentCurrency() != null) {
                    stmt.setString(++i, application.getPayment().getPaymentCurrency());
                }
                stmt.setFloat(++i, application.getPayment().getPaymentPrice());
            }

            if (application.getCategory() != null && application.getCategory().getId() != 0) {
                stmt.setInt(++i, application.getCategory().getId());
            }
            if (application.getPlatform() != null && application.getPlatform().getId() != 0) {
                stmt.setInt(++i, application.getPlatform().getId());
            }
            stmt.setInt(++i, application.getUser().getTenantId());
            stmt.setString(++i, application.getUuid());
            stmt.executeUpdate();

            application.setId(getApplicationId(application.getUuid()));

            if (application.getTags() != null && application.getTags().size() > 0) {
                sql = "DELETE FROM APPM_APPLICATION_TAG WHERE APPLICATION_ID = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, application.getId());
                stmt.executeUpdate();

                sql = "INSERT INTO APPM_APPLICATION_TAG (NAME, APPLICATION_ID) VALUES (?, ?); ";
                stmt = conn.prepareStatement(sql);
                for (String tag : application.getTags()) {
                    stmt.setString(1, tag);
                    stmt.setInt(2, application.getId());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }

            // delete existing properties and add new ones. if no properties are set, existing ones will be deleted.
            sql = "DELETE FROM APPM_APPLICATION_PROPERTY WHERE APPLICATION_ID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, application.getId());
            stmt.executeUpdate();

            sql = "INSERT INTO APPM_APPLICATION_PROPERTY (PROP_KEY, PROP_VAL, APPLICATION_ID) VALUES (?, ?, ?); ";
            stmt = conn.prepareStatement(sql);
            for (String propKey : application.getProperties().keySet()) {
                stmt.setString(1, propKey);
                stmt.setString(2, application.getProperties().get(propKey));
                stmt.setInt(3, application.getId());
                stmt.addBatch();
            }
            stmt.executeBatch();

        } catch (DBConnectionException e) {
            throw new ApplicationManagementDAOException("Error occurred while obtaining the DB connection.", e);
        } catch (SQLException e) {
            throw new ApplicationManagementDAOException("Error occurred while adding the application", e);
        }

        return application;
    }

    @Override
    public void addProperties(Map<String, String> properties) throws ApplicationManagementDAOException {

    }

    @Override
    public void editProperties(Map<String, String> properties) throws ApplicationManagementDAOException {

    }

    @Override
    public void deleteProperties(int applicationId) throws ApplicationManagementDAOException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = this.getDBConnection();
            String sql = "DELETE FROM APPM_APPLICATION_PROPERTY WHERE APPLICATION_ID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, applicationId);
            stmt.executeUpdate();

        } catch (DBConnectionException e) {
            throw new ApplicationManagementDAOException("Error occurred while obtaining the DB connection.", e);
        } catch (SQLException e) {
            throw new ApplicationManagementDAOException("Error occurred while deleting properties of application: " + applicationId, e);
        } finally {
            Util.cleanupResources(stmt, null);
        }
    }

    @Override
    public void deleteTags(int applicationId) throws ApplicationManagementDAOException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.getDBConnection();
            String sql = "DELETE FROM APPM_APPLICATION_TAG WHERE APPLICATION_ID = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, applicationId);
            stmt.executeUpdate();

        } catch (DBConnectionException e) {
            throw new ApplicationManagementDAOException("Error occurred while obtaining the DB connection.", e);
        } catch (SQLException e) {
            throw new ApplicationManagementDAOException("Error occurred while deleting tags of application: " + applicationId, e);
        } finally {
            Util.cleanupResources(stmt, rs);
        }
    }

    @Override
    public void addRelease(ApplicationRelease release) throws ApplicationManagementDAOException {

    }
}
