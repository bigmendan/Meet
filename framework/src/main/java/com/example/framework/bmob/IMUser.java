package com.example.framework.bmob;

import android.os.Parcel;
import android.os.Parcelable;

import cn.bmob.v3.BmobUser;

public class IMUser extends BmobUser implements Parcelable {

    private String nick;
    private String head;



    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }


    @Override
    public String toString() {
        return "IMUser{" +
                "nick='" + nick + '\'' +
                ", head='" + head + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.head);
        dest.writeString(this.nick);
    }

    public IMUser() {
    }

    protected IMUser(Parcel in) {
        this.head = in.readString();
        this.nick = in.readString();
    }

    public static final Parcelable.Creator<IMUser> CREATOR = new Parcelable.Creator<IMUser>() {
        @Override
        public IMUser createFromParcel(Parcel source) {
            return new IMUser(source);
        }

        @Override
        public IMUser[] newArray(int size) {
            return new IMUser[size];
        }
    };
}
