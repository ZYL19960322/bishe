package com.bishe.dto;

/**
 * Created by ZYL on 2019/4/5.
 */
public class StudentMessageResult {
    //活动id
    private String actiId;
    //活动主题
    private String actiTheme;
    //活动主题图片
    private String actiThemeImage;

    //活动的消息id
    private String studMessageId;
    //消息内容
    private String studMessageComment;
    //消息产生时间
    private String studMessageCreateTime;

    public String getActiId() {
        return actiId;
    }

    public void setActiId(String actiId) {
        this.actiId = actiId;
    }

    public String getActiTheme() {
        return actiTheme;
    }

    public void setActiTheme(String actiTheme) {
        this.actiTheme = actiTheme;
    }

    public String getActiThemeImage() {
        return actiThemeImage;
    }

    public void setActiThemeImage(String actiThemeImage) {
        this.actiThemeImage = actiThemeImage;
    }

    public String getStudMessageId() {
        return studMessageId;
    }

    public void setStudMessageId(String studMessageId) {
        this.studMessageId = studMessageId;
    }

    public String getStudMessageComment() {
        return studMessageComment;
    }

    public void setStudMessageComment(String studMessageComment) {
        this.studMessageComment = studMessageComment;
    }

    public String getStudMessageCreateTime() {
        return studMessageCreateTime;
    }

    public void setStudMessageCreateTime(String studMessageCreateTime) {
        this.studMessageCreateTime = studMessageCreateTime;
    }

    @Override
    public String toString() {
        return "StudentMessageResult{" +
                "actiId='" + actiId + '\'' +
                ", actiTheme='" + actiTheme + '\'' +
                ", actiThemeImage='" + actiThemeImage + '\'' +
                ", studMessageId='" + studMessageId + '\'' +
                ", studMessageComment='" + studMessageComment + '\'' +
                ", studMessageCreateTime='" + studMessageCreateTime + '\'' +
                '}';
    }
}
