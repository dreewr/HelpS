package com.ebmacs.helpapp.Models;

public class Groups {
    public String groupName = null;
    public String groupDescription = null;
    public String groupId = null;
    public String groupCreaterId = null;


    public Groups(String groupName, String groupDescription, String groupId) {
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.groupId = groupId;
    }

    public Groups() {
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupCreaterId() {
        return groupCreaterId;
    }

    public void setGroupCreaterId(String groupCreaterId) {
        this.groupCreaterId = groupCreaterId;
    }
}
