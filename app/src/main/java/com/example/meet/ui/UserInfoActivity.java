package com.example.meet.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.framework.bmob.BMobManager;
import com.example.framework.bmob.Friend;
import com.example.framework.bmob.IMUser;
import com.example.framework.constant.Constant;
import com.example.framework.manager.DialogManager;
import com.example.framework.manager.RongCloudManager;
import com.example.framework.views.DialogView;
import com.example.meet.R;
import com.example.meet.ui.base.BaseActivity;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 用户信息 页面 ;
 */
public class UserInfoActivity extends BaseActivity {


    private String TAG = "=== UserInfoActivity = ";

    private ImageView mHeaderIv;
    private TextView mNickTv;

    private TextView mAddTv;
    private LinearLayout isFriendL;
    private TextView mSendMsgTv;
    private TextView mSendVoiceTv;
    private TextView mSendVideoTv;

    private String userId;
    private IMUser imUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);


        initWidgets();

        Intent intent = getIntent();
        userId = intent.getStringExtra(Constant.USER_ID);


        if (!TextUtils.isEmpty(userId)) {
            BMobManager.getInstance().queryUserId(userId, new FindListener<IMUser>() {
                @Override
                public void done(List<IMUser> list, BmobException e) {
                    if (e == null) {
                        if (list != null && list.size() > 0) {
                            imUser = list.get(0);

                            if (imUser != null) {
                                Glide.with(UserInfoActivity.this).load(imUser.getHead()).into(mHeaderIv);
                                mNickTv.setText(imUser.getNick());
                            }
                        }
                    } else {
                    }
                }
            });

            // 怎么判断是不是好友 ？？？
            BMobManager.getInstance().queryMyFriend(new FindListener<Friend>() {
                @Override
                public void done(List<Friend> list, BmobException e) {
                    if (e == null) {
                        // 是我的好友 ，
                        if (list != null && list.size() > 0) {
                            mAddTv.setVisibility(View.GONE);
                            isFriendL.setVisibility(View.VISIBLE);

                        } else {
                            mAddTv.setVisibility(View.VISIBLE);
                            isFriendL.setVisibility(View.GONE);
                        }
                    } else {
                        Log.e(TAG, "判断好友 " +
                                "error " + e.toString());
                    }
                }
            });
        }


    }


    private void initWidgets() {

        mHeaderIv = findViewById(R.id.mHeaderIv);
        mNickTv = findViewById(R.id.mNickTv);
        mAddTv = findViewById(R.id.mAddTv);
        isFriendL = findViewById(R.id.isFriendL);
        mSendMsgTv = findViewById(R.id.mSendMsgTv);
        mSendVoiceTv = findViewById(R.id.mSendVoiceTv);
        mSendVideoTv = findViewById(R.id.mSendVideoTv);


    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mAddTv:            // 添加好友

                // 弹个窗吧
                initAddFriendDialog();

                break;
            case R.id.mSendMsgTv:        // 发送文本消息

                Intent intent = new Intent();

                intent.setClass(this, ChatActivity.class);
                intent.putExtra(Constant.USER_ID, userId);
                intent.putExtra(Constant.USER_NICK, imUser.getNick());
                intent.putExtra(Constant.USER_HEADER, imUser.getHead());
                startActivity(intent);

                break;
            case R.id.mSendVoiceTv:      // 发送语音消息

                RongCloudManager.getInstance().startAudioCall(userId);
                break;
            case R.id.mSendVideoTv:      // 发送视频聊天；
                RongCloudManager.getInstance().startVideoCall(userId);
                break;
        }
    }


    private void initAddFriendDialog() {
        final DialogView dialogView = DialogManager.getInstance()
                .initView(this, R.layout.dialog_add_friend, Gravity.CENTER);

        DialogManager.getInstance().show(dialogView);


        final EditText mAddContentEt = dialogView.findViewById(R.id.mAddContentEt);
        TextView mSendTv = dialogView.findViewById(R.id.mSendTv);
        TextView mCancelTv = dialogView.findViewById(R.id.mCancelTv);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.mSendTv:

                        String content = mAddContentEt.getText().toString().trim();
                        String sendContent = "";
                        if (TextUtils.isEmpty(content)) {
                            sendContent = "你好，我是 " + BMobManager.getInstance().getUser().getNick();
                        } else {
                            sendContent = content;
                        }

                        RongCloudManager.getInstance()
                                .sendTextMessage(sendContent,
                                        RongCloudManager.TYPE_MSG_ADD_FRIEND,
                                        userId);

                        DialogManager.getInstance().hide(dialogView);
                        break;
                    case R.id.mCancelTv:

                        DialogManager.getInstance().hide(dialogView);

                        break;
                }
            }
        };

        mSendTv.setOnClickListener(listener);
        mCancelTv.setOnClickListener(listener);


    }


}
