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
package org.wso2.carbon.device.application.mgt.common;


import org.wso2.carbon.device.application.mgt.common.jaxrs.Exclude;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Application represents the an Application in Application Store.
 */
public class Application {

    @Exclude
    private int id;

    private String uuid;

    private String identifier;

    private String name;

    private String shortDescription;

    private String description;

    private String videoName;

    private List<String> tags;

    private Platform platform;

    private List<Comment> comments;

    private Category category;

    private Map<String, String> properties;

    private Date createdAt;

    private Date modifiedAt;

    private Payment payment;

    private Lifecycle currentLifecycle;

    private List<ApplicationRelease> releases;

    private Visibility visibility;

    private int screenShotCount;

    private User user;

    public int getId() {
        return id;
    }

    public List<ApplicationRelease> getReleases() {
        return releases;
    }

    public void setReleases(List<ApplicationRelease> releases) {
        this.releases = releases;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Lifecycle getCurrentLifecycle() {
        return currentLifecycle;
    }

    public void setCurrentLifecycle(Lifecycle currentLifecycle) {
        this.currentLifecycle = currentLifecycle;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setScreenShotCount (int screenShotCount) {
        this.screenShotCount = screenShotCount;
    }

    public int getScreenShotCount() {
        return screenShotCount;
    }

    @Override
    public String toString() {
        return "UUID : " + uuid + "\tIdentifier : " + identifier + "\tName : " + name + "\tShort Description : "
                + shortDescription + "\tLifecycle State : " + currentLifecycle.getLifecycleState();
    }
}
