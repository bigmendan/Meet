package com.example.meet.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RadioGroup;

import com.example.framework.bmob.BMobManager;
import com.example.framework.bmob.IMUser;
import com.example.framework.manager.HttpManager;
import com.example.framework.utils.SystemUI;
import com.example.meet.R;
import com.example.framework.constant.Constant;
import com.example.meet.data.RongToken;
import com.example.meet.services.RongCloudService;
import com.example.meet.ui.base.BaseActivity;
import com.example.meet.ui.fragments.ChatFragment;
import com.example.meet.ui.fragments.HomeFragment;
import com.example.meet.ui.fragments.MarketFragment;
import com.example.meet.ui.fragments.MyFragment;
import com.example.meet.utils.SpUtils;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 *
 */
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {


    private String TAG = "== MAIN";
    private RadioGroup rg;


    private HomeFragment homeFragment;
    private MarketFragment marketFragment;
    private ChatFragment chatFragment;
    private MyFragment myFragment;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SystemUI.fixSystemUI(this);

        initFragment();
        initListener();

        checkToken();
    }


    private void checkToken() {
        String token = SpUtils.getInstance().getString(Constant.TOKEN);


        /**
         *  判断 token
         *  TOKEN 有三个条件  用户头像，用户id  和 用户昵称
         *  1, 如果不为空 去连接 融云服务
         *  2，如果为空，去获取 token ,
         */


        if (!TextUtils.isEmpty(token)) {

            startCloudService();

        } else {

            // 用户 id  是固定的， 获取到用户头像 和 昵称;
            String head = BMobManager.getInstance().getUser().getHead();
            String nick = BMobManager.getInstance().getUser().getNick();

            if (!TextUtils.isEmpty(head) && !TextUtils.isEmpty(nick)) {
                createToken();
            } else {
                toUpLoadDialog();
            }
        }

    }

    /**
     * 跳转到编辑资料；
     */
    private void toUpLoadDialog() {
        startActivityForResult(new Intent(this, HeadUpLoadActivity.class), 1005);
    }

    /**
     * 获取到 token
     */
    private void createToken() {
        IMUser imUser = BMobManager.getInstance().getUser();
        final HashMap<String, String> map = new HashMap<>();
        map.put("userId", imUser.getObjectId());
        map.put("name", imUser.getNick());
        map.put("portraitUri", imUser.getHead());


        new Thread(new Runnable() {
            @Override
            public void run() {
                String rongCloudToken = HttpManager.getInstance().getRongCloudToken(map);
                RongToken rongToken = new Gson().fromJson(rongCloudToken, RongToken.class);

                // 获取到 Token , 保存起来 ;
                if (rongToken.getCode() == 200) {
                    if (!TextUtils.isEmpty(rongToken.getToken())) {
                        SpUtils.getInstance().putString(Constant.TOKEN, rongToken.getToken());
                        // 启动融云服务
                        startCloudService();
                    }
                }


            }
        }).start();


    }


    /**
     * 启动融云服务
     */
    private void startCloudService() {
        startService(new Intent(this, RongCloudService.class));
    }

    private void initListener() {
        rg = findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_home:
                changeFragment(homeFragment);
                break;

            case R.id.rb_market:
                changeFragment(marketFragment);

                break;
            case R.id.rb_chat:
                changeFragment(chatFragment);

                break;
            case R.id.rb_my:
                changeFragment(myFragment);

                break;
        }
    }


    private void initFragment() {
        homeFragment = new HomeFragment();
        marketFragment = new MarketFragment();
        chatFragment = new ChatFragment();
        myFragment = new MyFragment();

        currentFragment = homeFragment;

        getSupportFragmentManager().beginTransaction()
                .add(R.id.mContainer, homeFragment)
                .commit();
    }


    private void changeFragment(Fragment fragment) {

        if (currentFragment != fragment) {
            if (fragment.isAdded()) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(currentFragment)
                        .show(fragment)
                        .commit();

            } else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(currentFragment)
                        .add(R.id.mContainer, fragment)
                        .commit();
            }
            currentFragment = fragment;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            // 头像上传成功
            checkToken();
        }
    }
}
