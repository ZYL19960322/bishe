package com.bishe.dto;

import com.bishe.pojo.Student;

/**
 * Created by ZYL on 2019/2/17.
 */
public class UserResponse {

    private Student student;

    //去use.html时使用的数据参数
    private int checkCount;
    private int enrolledCount;
    private int duringCount;
    private int endCount;

    //去publish.html时使用的数据参数
    private int checkingCount;
    private int enrollingCount;
    private int preparingCount;
    private int holdingCount;
    private int hasEndedCount;

    public int getCheckCount() {
        return checkCount;
    }

    public void setCheckCount(int checkCount) {
        this.checkCount = checkCount;
    }

    public int getEnrolledCount() {
        return enrolledCount;
    }

    public void setEnrolledCount(int enrolledCount) {
        this.enrolledCount = enrolledCount;
    }

    public int getDuringCount() {
        return duringCount;
    }

    public void setDuringCount(int duringCount) {
        this.duringCount = duringCount;
    }

    public int getEndCount() {
        return endCount;
    }

    public void setEndCount(int endCount) {
        this.endCount = endCount;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getCheckingCount() {
        return checkingCount;
    }

    public void setCheckingCount(int checkingCount) {
        this.checkingCount = checkingCount;
    }

    public int getEnrollingCount() {
        return enrollingCount;
    }

    public void setEnrollingCount(int enrollingCount) {
        this.enrollingCount = enrollingCount;
    }

    public int getPreparingCount() {
        return preparingCount;
    }

    public void setPreparingCount(int preparingCount) {
        this.preparingCount = preparingCount;
    }

    public int getHoldingCount() {
        return holdingCount;
    }

    public void setHoldingCount(int holdingCount) {
        this.holdingCount = holdingCount;
    }

    public int getHasEndedCount() {
        return hasEndedCount;
    }

    public void setHasEndedCount(int hasEndedCount) {
        this.hasEndedCount = hasEndedCount;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "student=" + student +
                ", checkCount=" + checkCount +
                ", enrolledCount=" + enrolledCount +
                ", duringCount=" + duringCount +
                ", endCount=" + endCount +
                ", checkingCount=" + checkingCount +
                ", enrollingCount=" + enrollingCount +
                ", preparingCount=" + preparingCount +
                ", holdingCount=" + holdingCount +
                ", hasEndedCount=" + hasEndedCount +
                '}';
    }
}
