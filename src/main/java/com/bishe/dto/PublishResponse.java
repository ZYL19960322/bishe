package com.bishe.dto;

/**
 * Created by ZYL on 2019/1/19.
 */

//发布功能响应给前端页面数据的bean
public class PublishResponse {
    //状态
    private Boolean success;
    //错误信息
    private  String msg;

    public PublishResponse(Boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }
    public PublishResponse() {

    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "PublishResponse{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                '}';
    }
}
