package com.example.meet.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.framework.bmob.BMobManager;
import com.example.framework.bmob.IMUser;
import com.example.meet.utils.SpUtils;
import com.example.meet.ui.MainActivity;
import com.example.meet.R;
import com.example.framework.constant.Constant;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * 登录
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mPhoneEt, mCodeEt, mPhoneEt2, mPwdEt;
    private Button mRegisterOrLoginBtn;
    TextView mSendTv;
    private final int H_WHAT = 1000;
    private int TIME = 60;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case H_WHAT:

                    if (TIME > 0) {
                        TIME--;
                        mSendTv.setText(TIME + "s");
                        handler.sendEmptyMessageDelayed(H_WHAT, 1000);
                    } else {
                        mSendTv.setText("发送验证码");
                        mSendTv.setEnabled(true);
                    }

                    break;
            }
            return true;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initWidget();

    }


    private void initWidget() {
        mPhoneEt = findViewById(R.id.mPhoneEt);
        mCodeEt = findViewById(R.id.mCodeEt);
        mRegisterOrLoginBtn = findViewById(R.id.mLoginOrRegisterBtn);
        mSendTv = findViewById(R.id.mSendTv);


        mPhoneEt2 = findViewById(R.id.mPhoneEt2);
        mPwdEt = findViewById(R.id.mPwdEt);


    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mSendTv:

                sendCode();

                break;

            case R.id.mLoginOrRegisterBtn:
                registerOrLogin();
                break;

            case R.id.mPwdLoginBtn:

                pwdLogin();
                break;
        }
    }

    /**
     * 使用密码登录 ;
     */
    private void pwdLogin() {
        String phone = mPhoneEt2.getText().toString();
        String psd = mPwdEt.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(psd)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        BMobManager.getInstance().pwdLogin(phone, psd, new LogInListener<IMUser>() {
            @Override
            public void done(IMUser imUser, BmobException e) {
                if (e == null) {

                    SpUtils.getInstance().putBoolean(Constant.IS_LOGIN, true);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();

                } else {

                    Log.e("==== Login", "done: " + e.getErrorCode() + ":" + e.getMessage());
                    Toast.makeText(LoginActivity.this, "登录失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    /**
     * 登录或者注册
     */
    private void registerOrLogin() {
        String phone = mPhoneEt.getText().toString().trim();
        String code = mCodeEt.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }


        BMobManager.getInstance().registerOrLogin(phone, code, new LogInListener<IMUser>() {
            @Override
            public void done(IMUser imUser, BmobException e) {
                if (e == null) {

                    SpUtils.getInstance().putBoolean(Constant.IS_LOGIN, true);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();

                } else {

                    Log.e("==== Login", "done: " + e.getErrorCode() + ":" + e.getMessage());
                    Toast.makeText(LoginActivity.this, "登录失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    /**
     * 发送验证码
     */
    private void sendCode() {
        String phone = mPhoneEt.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        //  发送验证码

        BMobManager.getInstance().sendCode(phone, new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    // 验证码发送成功;
                    Toast.makeText(LoginActivity.this, "验证码发送成功",
                            Toast.LENGTH_SHORT).show();
                    mSendTv.setEnabled(false);
                    handler.sendEmptyMessage(H_WHAT);

                } else {
                    Toast.makeText(LoginActivity.this, "验证码发送失败",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
