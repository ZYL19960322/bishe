package com.bishe.enums;

/**
 * Created by ZYL on 2019/2/15.
 */
public enum StudentInfoEnum {
    //个人信息中使用
    INFO_SUCCESS(1,"信息无误"),CLASS_EMPTY(2,"班级信息有错误"),NUMBER_EMPTY(3,"学号信息有错误"),PHONE_EMPTY(4,"联系电话有错误"),ENROLL_NAME_EMPTY(5,"姓名有错误"),ACCOUNT_NAME_ERROR(6,"账号名有错误"),
    PASSWORD_ERROR(7,"原密码有错误"),NEW_PASSWORD_ERROR(8,"新密码有错误"),CONFIRM_PASSWORD_ERROR(9,"确认密码有错误"),
    //登录注册中使用
    CheckCodeError(10,"验证码错误"),AccountNameOrPasswordError(11,"账号或密码错误，请重新输入"), CanceledAccount(12,"该用户已被注销,请联系管理员") , AccountNameError(13,"用户名格式不正确"),AccountPasswordError(14,"密码格式不正确"),
    ConfirmPasswordError(15,"密码不相同"),HasRegistered(16,"该账号已被注册"),NameOrPasswordEmpty(17,"请输入账号和密码"),RegisterSuccess(18,"注册成功，请登录"),LoginSuccess(19,"登录成功");


    private int statusId ;
    private String statusDesc;

    public int getStatusId() {
        return statusId;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    StudentInfoEnum(int statusId, String statusDesc) {
        this.statusId = statusId;
        this.statusDesc = statusDesc;
    }

    public static StudentInfoEnum stateOf(int index) {
        for (StudentInfoEnum state : values()) {
            if (state.getStatusId() == index) {
                return state;
            }
        }
        return null;
    }
}
