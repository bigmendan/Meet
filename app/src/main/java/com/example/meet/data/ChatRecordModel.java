package com.example.meet.data;

/**
 * 会话管理
 */
public class ChatRecordModel {


    private String userId;
    private String headerUrl;

    private String name;

    // 最后一条消息
    private String endMsg;

    private int unReadMessNum;

    private String time;


    @Override
    public String toString() {
        return "ChatRecordModel{" +
                "userId='" + userId + '\'' +
                ", headerUrl='" + headerUrl + '\'' +
                ", name='" + name + '\'' +
                ", endMsg='" + endMsg + '\'' +
                ", unReadMessNum=" + unReadMessNum +
                ", time='" + time + '\'' +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEndMsg() {
        return endMsg;
    }

    public void setEndMsg(String endMsg) {
        this.endMsg = endMsg;
    }

    public String getHeaderUrl() {
        return headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnReadMessNum() {
        return unReadMessNum;
    }

    public void setUnReadMessNum(int unReadMessNum) {
        this.unReadMessNum = unReadMessNum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
