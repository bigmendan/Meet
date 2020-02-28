package com.example.meet.app;

import android.app.Application;
import android.content.Context;

import com.example.framework.bmob.BMobManager;
import com.example.framework.db.LitePalManager;
import com.example.framework.manager.RongCloudManager;

import org.litepal.LitePal;


public class MeetApplication extends Application {

    private static Context context;

    /**
     * Application 的优化
     * 1， 必要的组件可以放在MainActivity 中去初始化
     * 2，如果必须要在Application 中初始化的组件，尽可能延时
     * 3，非必要的组件，可以放在子线程中去做。
     */
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        //Bmob 初始化
        BMobManager.getInstance().initBMob(MeetApplication.this);

        // 融云初始化 ;
        RongCloudManager.getInstance().init(this);


        // 数据库初始化
        LitePalManager.getInstance().init(this);


    }


    public static Context getContext() {
        return context;
    }
}
