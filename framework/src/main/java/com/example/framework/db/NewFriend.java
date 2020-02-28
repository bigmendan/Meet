package com.example.framework.db;

import org.litepal.crud.LitePalSupport;

/**
 * 新朋友 ;
 */
public class NewFriend extends LitePalSupport {

    // 用户id
    private String userId;

    // 留言信息
    private String msg;

    // 还有状态   -1 : 待确认 ， 0: 同意 ， 1 : 拒绝
    private int agree = -1;

    // 时间
    private String time;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getAgree() {
        return agree;
    }

    public void setAgree(int agree) {
        this.agree = agree;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
