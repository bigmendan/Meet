package com.example.meet.ui.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.framework.bmob.BMobManager;
import com.example.framework.bmob.IMUser;
import com.example.framework.constant.Constant;
import com.example.framework.manager.RongCloudManager;
import com.example.meet.R;
import com.example.meet.data.ChatRecordModel;
import com.example.meet.rongcloud.TextMsgBean;
import com.example.meet.ui.ChatActivity;
import com.example.meet.ui.adapter.CommonAdapter;
import com.example.meet.ui.adapter.base.CommonViewHolder;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

/**
 * 聊天记录
 */
public class ChatRecordFragment extends Fragment {


    private String TAG = "== ChatRecordFragment";
    private RecyclerView mRecyclerView;
    private CommonAdapter<ChatRecordModel> adapter;
    private List<ChatRecordModel> mList = new ArrayList<>();

    private Disposable disposable;

    public ChatRecordFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chat_record, container, false);
        initWidgets(v);
        return v;
    }


    private void initWidgets(View v) {

        mRecyclerView = v.findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CommonAdapter<ChatRecordModel>(mList, new CommonAdapter.OnBindDataInterface<ChatRecordModel>() {
            @Override
            public void onBindData(final ChatRecordModel model, CommonViewHolder holder, int type, int position) {
                if (model != null) {

                    holder.setImageUrl(getContext(), R.id.mHeaderIv, model.getHeaderUrl());
                    holder.setText(R.id.mNickTv, model.getName());
                    holder.setText(R.id.mUnReadMsgNum, model.getUnReadMessNum() + "");

                    holder.setText(R.id.mLastMsgTv, model.getEndMsg());
                    holder.setText(R.id.mTimeTv, model.getTime());

                    // 点击事件；
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Intent intent = new Intent();
                            intent.setClass(getContext(), ChatActivity.class);
                            intent.putExtra(Constant.USER_ID, model.getUserId());
                            intent.putExtra(Constant.USER_NICK, model.getName());
                            intent.putExtra(Constant.USER_HEADER, model.getHeaderUrl());
                            startActivity(intent);

                        }
                    });
                }
            }

            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.adapter_chat_record;
            }
        });


        mRecyclerView.setAdapter(adapter);
        getConversation();

    }


    private void getConversation() {
        RongCloudManager.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> list) {
                mList.clear();
                if (list != null && list.size() > 0) {

                    for (int i = 0; i < list.size(); i++) {

                        final ChatRecordModel model = new ChatRecordModel();

                        final Conversation c = list.get(i);
                        String senderUserId = c.getSenderUserId();

                        BMobManager.getInstance().queryUserId(senderUserId, new FindListener<IMUser>() {
                            @Override
                            public void done(List<IMUser> list, BmobException e) {
                                if (e == null) {
                                    if (list != null && list.size() > 0) {
                                        IMUser imUser = list.get(0);

                                        model.setUserId(imUser.getObjectId());
                                        model.setHeaderUrl(imUser.getHead());
                                        model.setName(imUser.getNick());
                                        model.setUnReadMessNum(c.getUnreadMessageCount());
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                        model.setTime(simpleDateFormat.format(new Date(c.getSentTime())));

                                        String objectName = c.getObjectName();

                                        // 会话列表 文本消息
                                        if (objectName.equals(RongCloudManager.TYPE_MSG_TEXT)) {
                                            TextMessage textMessage = (TextMessage) c.getLatestMessage();
                                            String content = textMessage.getContent();
                                            TextMsgBean bean = new Gson().fromJson(content, TextMsgBean.class);

                                            String msg_type = bean.getMsg_type();
                                            String msg_content = bean.getMsg_content();

                                            if (msg_type.equals(bean.getMsg_type())) {
                                                model.setEndMsg(msg_content);
                                            }

                                        }


                                        mList.add(model);
                                        adapter.notifyDataSetChanged();
                                    }

                                }
                            }
                        });


                    }
                }
            }


            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e(TAG, "onError: " + errorCode);
            }

        });
    }

}
