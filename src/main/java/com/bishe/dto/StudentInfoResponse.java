package com.bishe.dto;

/**
 * Created by ZYL on 2019/2/15.
 */
public class StudentInfoResponse {
    private int status;
    private String statusDesc;



    public StudentInfoResponse(int status, String statusDesc) {
        this.status = status;
        this.statusDesc = statusDesc;
    }
    public StudentInfoResponse() {

    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    @Override
    public String toString() {
        return "StudentInfoResponse{" +
                "status=" + status +
                ", statusDesc='" + statusDesc + '\'' +
                '}';
    }
}
