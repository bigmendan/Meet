package com.example.framework.bmob;

import android.content.Context;
import android.util.Log;

import com.example.framework.db.NewFriend;

import java.io.File;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class BMobManager {

    private String TAG = "==BMobManager";

    private static String BMOB_SDK_ID = "1337e13a6be24ab3747b6e585e990942";
    private static BMobManager INSTANCE = null;


    private BMobManager() {

    }

    public static BMobManager getInstance() {
        if (INSTANCE == null) {
            synchronized (BMobManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BMobManager();
                }
            }
        }

        return INSTANCE;
    }

    public void initBMob(Context mContext) {
        Bmob.initialize(mContext, BMOB_SDK_ID);
    }


    public IMUser getUser() {
        return BmobUser.getCurrentUser(IMUser.class);


    }


    /**
     * 发送短信验证码
     *
     * @param phone
     * @param listener
     */
    public void sendCode(String phone, QueryListener<Integer> listener) {
        BmobSMS.requestSMSCode(phone, "", listener);
    }


    /**
     * 手机号码一键登录或注册
     *
     * @param phone
     * @param code
     * @param listener
     */
    public void registerOrLogin(String phone, String code, LogInListener<IMUser> listener) {
        BmobUser.signOrLoginByMobilePhone(phone, code, listener);

    }

    /**
     * 使用密码登录
     *
     * @param phone
     * @param pwd
     * @param listener
     */
    public void pwdLogin(String phone, String pwd, LogInListener<IMUser> listener) {
        BmobUser.loginByAccount(phone, pwd, listener);
    }

    /**
     * 注册
     */
    public void singUp() {
        final IMUser user = new IMUser();
        user.setUsername("付建明");
        user.setPassword("111111");

        user.signUp(new SaveListener<IMUser>() {

            @Override
            public void done(IMUser imUser, BmobException e) {
                if (e == null) {
                    Log.e(TAG, "done: 付建明注册成功");
                } else {
                    Log.e(TAG, "done: 付建明注册失败" + e.getErrorCode());
                }
            }
        });

    }

    /**
     * 通过手机号查询用户 ；
     */
    public void queryPhoneNumber(String phone, FindListener<IMUser> listener) {
        BmobQuery<IMUser> query = new BmobQuery<>();
        query.addWhereEqualTo("username", phone);
        query.findObjects(listener);
    }

    /**
     * 通过用户 id 查询；
     *
     * @param userId
     * @param listener
     */
    public void queryUserId(String userId, FindListener<IMUser> listener) {
        BmobQuery<IMUser> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", userId);
        query.findObjects(listener);
    }

    /**
     * 查询我的好友
     *
     * @param listener
     */
    public void queryMyFriend(FindListener<Friend> listener) {
        BmobQuery<Friend> query = new BmobQuery<>();
        query.addWhereEqualTo("user", getUser());
        query.findObjects(listener);
    }


    /**
     * 添加好友 ；
     *
     * @param friendUser
     * @param listener
     */
    public void addFriend(IMUser friendUser, SaveListener<String> listener) {

        Friend friend = new Friend();
        friend.setUser(getUser());
        friend.setFriendUser(friendUser);
        friend.save(listener);

    }

    /**
     * 通过 userId 添加好友；
     *
     * @param userId
     * @param listener
     */
    public void addFriend(String userId, final SaveListener<String> listener) {
        queryUserId(userId, new FindListener<IMUser>() {
            @Override
            public void done(List<IMUser> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        IMUser imUser = list.get(0);
                        addFriend(imUser, listener);
                    }
                }
            }
        });
    }


    /**
     * 上传文件
     *
     * @param file
     */
    public void uploadPhoto(File file, final String nickName, final UploadListener listener) {


        IMUser imUser = getUser();
        // bmobFile.getFileUrl() 图片上传后的地址 ;
        imUser.setHead("http://a3.att.hudong.com/68/61/300000839764127060614318218_950.jpg");
        imUser.setNick(nickName);


        imUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    listener.upLoadSuccess();
                } else {
                    listener.upLoadFail(e);
                }
            }
        });

        // 暂不上传 图片；

//        final BmobFile bmobFile = new BmobFile(file);
//        bmobFile.upload(new UploadFileListener() {
//            @Override
//            public void done(BmobException e) {
//                if (listener != null) {
//                    if (e == null) {
//
//                        // 更新用户信息 ;
//                        IMUser imUser = getUser();
//                        // bmobFile.getFileUrl() 图片上传后的地址 ;
//                        imUser.setHead(bmobFile.getFileUrl());
//                        imUser.setNick(nickName);
//
//
//                        imUser.update(new UpdateListener() {
//                            @Override
//                            public void done(BmobException e) {
//                                if (e == null) {
//                                    listener.upLoadSuccess();
//                                } else {
//                                    listener.upLoadFail(e);
//                                }
//                            }
//                        });
//
//
//                    }
//                }
//            }
//        });
    }


    /**
     * 文件上传的监听
     */
    public interface UploadListener {
        void upLoadSuccess();

        void upLoadFail(BmobException e);
    }




}
