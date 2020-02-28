package com.example.meet.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.framework.bmob.BMobManager;
import com.example.framework.bmob.IMUser;
import com.example.framework.constant.Constant;
import com.example.framework.event.EventManager;
import com.example.framework.event.MessageEvent;
import com.example.framework.manager.RongCloudManager;
import com.example.meet.R;
import com.example.meet.data.ChatModel;
import com.example.meet.rongcloud.TextMsgBean;
import com.example.meet.ui.adapter.CommonAdapter;
import com.example.meet.ui.adapter.base.CommonViewHolder;
import com.example.meet.ui.base.BaseActivity;
import com.example.meet.utils.GlideUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.invoke.CallSite;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

/**
 * 聊天页面
 */
public class ChatActivity extends BaseActivity {

    private String TAG = "== ChatActivity";

    // 左边的消息
    private final int TYPE_LEFT_TEXT = 0;
    private final int TYPE_LEFT_IMG = 1;
    private final int TYPE_LEFT_LOCATION = 2;

    // 右边的消息
    private final int TYPE_RIGHT_TEXT = 3;
    private final int TYPE_RIGHT_IMG = 4;
    private final int TYPE_RIGHT_LOCATION = 5;

    private RecyclerView mRecyclerView;
    private EditText mMsgEt;
    private TextView mBackTv;
    private TextView mTitleTv;


    private String userId;
    private String userNick;
    private String userHeader;

    private IMUser myUser;


    private CommonAdapter<ChatModel> adapter;
    private List<ChatModel> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        initWidgets();
        getBundle();

        queryHistoryMessage();
    }


    private void getBundle() {

        Intent intent = getIntent();
        userId = intent.getStringExtra(Constant.USER_ID);
        userNick = intent.getStringExtra(Constant.USER_NICK);
        userHeader = intent.getStringExtra(Constant.USER_HEADER);


        myUser = BMobManager.getInstance().getUser();


        mTitleTv.setText(userNick);

    }


    private void initWidgets() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mMsgEt = findViewById(R.id.mMsgEt);

        mBackTv = findViewById(R.id.mBackTv);
        mTitleTv = findViewById(R.id.title);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter = new CommonAdapter<ChatModel>(mList, new CommonAdapter.OnMultiTypeBindDataInterface<ChatModel>() {
            @Override
            public int getItemViewType(int position) {
                return mList.get(position).getType();
            }

            @Override
            public void onBindData(ChatModel model, CommonViewHolder holder,
                                   int type, int position) {
                switch (model.getType()) {
                    case TYPE_LEFT_TEXT:
                        holder.setText(R.id.mLeftTextTv, model.getContent());
                        holder.setImageUrl(ChatActivity.this,
                                R.id.mLeftTextIv, userHeader);
                        break;
                    case TYPE_RIGHT_TEXT:
                        holder.setText(R.id.mRightTextTv, model.getContent());
                        holder.setImageUrl(ChatActivity.this,
                                R.id.mRightTextIv, myUser.getHead());
                        break;
                }
            }

            @Override
            public int getItemLayoutId(int viewType) {
                int layoutId = 0;

                switch (viewType) {

                    case TYPE_LEFT_TEXT:        // 左边 文字
                        layoutId = R.layout.adapter_chat_left_text;
                        break;

                    case TYPE_LEFT_IMG:
                        break;
                    case TYPE_LEFT_LOCATION:
                        break;
                    case TYPE_RIGHT_TEXT:       // 右边 文字
                        layoutId = R.layout.adapter_chat_right_text;
                        break;
                    case TYPE_RIGHT_IMG:
                        break;

                    case TYPE_RIGHT_LOCATION:
                        break;
                }
                return layoutId;
            }
        });

        mRecyclerView.setAdapter(adapter);


    }

    /**
     * 点击事件
     *
     * @param view
     */
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.mSendMsgBtn:

                sendTextMessage();

                break;
        }
    }

    // 发送文本消息
    private void sendTextMessage() {
        String content = mMsgEt.getText().toString().trim();
        if (!TextUtils.isEmpty(content)) {

            RongCloudManager.getInstance().sendTextMessage(content,
                    RongCloudManager.TYPE_MSG_TEXT, userId);

            // 清空消息框
            mMsgEt.setText("");

            addText(1, content);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        switch (event.getType()) {
            case EventManager.TAG_SEND_TEXT_MSG:        // 接收普通文本消息
                String content = event.getContent();
                String sendUserId = event.getUserId();

                if (sendUserId.equals(userId))
                    addText(0, content);

                break;
        }

    }


    private void queryHistoryMessage() {
        RongCloudManager.getInstance().getHistoryMessage(userId, new RongIMClient.ResultCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> list) {
                if (list != null) {
                    if (list.size() > 0) {

                        parseHistoryMessage(list);
                    } else {
                        // 再去查找远程记录；
                    }
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e(TAG, "onError: ");
            }
        });
    }


    private void parseHistoryMessage(List<Message> list) {

        // 倒叙
        Collections.reverse(list);


        for (int i = 0; i < list.size(); i++) {
            Message message = list.get(i);
            String senderUserId = message.getSenderUserId();

            TextMessage textMessage = (TextMessage) message.getContent();
            String content = textMessage.getContent();
            TextMsgBean textMsgBean = new Gson().fromJson(content, TextMsgBean.class);
            String msg_content = textMsgBean.getMsg_content();

            // 如果 id 相同
            if (senderUserId.equals(userId)) {
                addText(0, msg_content);
            } else {
                addText(1, msg_content);
            }

        }
    }

    /**
     * 添加 聊天内容到 UI
     *
     * @param index
     * @param msg
     */
    private void addText(int index, String msg) {
        ChatModel model = new ChatModel();

        if (index == 0) {
            model.setType(TYPE_LEFT_TEXT);
        } else {
            model.setType(TYPE_RIGHT_TEXT);
        }

        model.setContent(msg);
        baseAdd(model);

    }

    private void baseAdd(ChatModel model) {
        mList.add(model);
        adapter.notifyDataSetChanged();

        mRecyclerView.scrollToPosition(mList.size() - 1);

    }


}
