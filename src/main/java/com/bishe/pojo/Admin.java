package com.bishe.pojo;

/**
 * Created by ZYL on 2019/3/15.
 */
public class Admin {
    private String adminId;
    private String adminName;
    private String adminPassword;

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminId='" + adminId + '\'' +
                ", adminName='" + adminName + '\'' +
                ", adminPassword='" + adminPassword + '\'' +
                '}';
    }
}
