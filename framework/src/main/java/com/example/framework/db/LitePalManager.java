package com.example.framework.db;

import android.content.Context;
import android.util.Log;

import com.example.framework.bmob.Friend;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 * 本地数据库
 */
public class LitePalManager {


    private String TAG = "== LitePalManager";

    private static volatile LitePalManager INSTANCE = null;

    private LitePalManager() {
    }

    public static LitePalManager getInstance() {
        if (INSTANCE == null) {
            synchronized (LitePalManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LitePalManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        LitePal.initialize(context);
    }


    /**
     * 保存
     *
     * @param support
     */
    private void baseSave(LitePalSupport support) {
        support.save();
    }

    /**
     * 保存 新好友;
     *
     * @param msg
     * @param userId
     */
    public void saveNewFriend(String msg, String userId) {
        NewFriend friend = new NewFriend();

        friend.setAgree(-1);
        friend.setMsg(msg);
        friend.setUserId(userId);
        friend.setTime(String.valueOf(System.currentTimeMillis()));

        baseSave(friend);


    }


    /**
     * 查询
     *
     * @param clazz
     * @return
     */
    private List<? extends LitePalSupport> baseQuery(Class clazz) {
        return LitePal.findAll(clazz);
    }

    /**
     * 查找新朋友
     *
     * @return
     */
    public List<NewFriend> queryNewFriend() {
        List<NewFriend> list = (List<NewFriend>) baseQuery(NewFriend.class);
        return list;
    }

    public void updateNewFriend(String userId, int agree) {
        NewFriend friend = new NewFriend();
        friend.setAgree(agree);
        friend.updateAll("userId = ?", userId);

    }


}
