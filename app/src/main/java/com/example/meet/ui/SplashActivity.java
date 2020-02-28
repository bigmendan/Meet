package com.example.meet.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.meet.R;
import com.example.meet.utils.SpUtils;
import com.example.framework.constant.Constant;
import com.example.meet.login.LoginActivity;

/**
 * 闪屏页
 */
public class SplashActivity extends AppCompatActivity {


    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                toMain();
            }
        }, 2000);


    }

    private void toMain() {
        Intent intent = new Intent();
        Boolean isLogin = SpUtils.getInstance().getBoolean(Constant.IS_LOGIN);
        if (isLogin) {

            intent.setClass(SplashActivity.this, MainActivity.class);
        } else {
            intent.setClass(SplashActivity.this, LoginActivity.class);
        }


        startActivity(intent);
        finish();
    }


}
