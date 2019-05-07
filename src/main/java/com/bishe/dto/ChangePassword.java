package com.bishe.dto;

/**
 * Created by ZYL on 2019/2/18.
 */
public class ChangePassword {
    private String name;
    private String password;
    private String newPassword;
    private String comfirmPassword;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getComfirmPassword() {
        return comfirmPassword;
    }

    public void setComfirmPassword(String comfirmPassword) {
        this.comfirmPassword = comfirmPassword;
    }

    @Override
    public String toString() {
        return "ChangePassword{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", comfirmPassword='" + comfirmPassword + '\'' +
                '}';
    }
}
