package com.example.framework.bmob;

import com.example.framework.bmob.IMUser;

import cn.bmob.v3.BmobObject;

public class Friend extends BmobObject {
    private IMUser user;

    private IMUser friendUser;

    public IMUser getUser() {
        return user;
    }

    public void setUser(IMUser user) {
        this.user = user;
    }

    public IMUser getFriendUser() {
        return friendUser;
    }

    public void setFriendUser(IMUser friendUser) {
        this.friendUser = friendUser;
    }
}
