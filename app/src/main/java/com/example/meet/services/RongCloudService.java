package com.example.meet.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.framework.bmob.BMobManager;
import com.example.framework.db.LitePalManager;
import com.example.framework.db.NewFriend;
import com.example.framework.event.EventManager;
import com.example.framework.event.MessageEvent;
import com.example.framework.manager.RongCloudManager;
import com.example.framework.constant.Constant;
import com.example.meet.rongcloud.TextMsgBean;
import com.example.meet.utils.SpUtils;
import com.google.gson.Gson;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.rong.calllib.IRongReceivedCallListener;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallSession;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;


/**
 * RongCloud Service
 */
public class RongCloudService extends Service {
    private String TAG = "== RongCloudService = ";

    private Disposable disposable;

    public RongCloudService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String token = SpUtils.getInstance().getString(Constant.TOKEN);

        // 连接服务
        RongCloudManager.getInstance().connect(token);

        // 接收消息
        RongCloudManager.getInstance().setReceiveListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(final Message message, int i) {

                Conversation.ConversationType conversationType = message.getConversationType();
                // 文本消息
                if (conversationType == Conversation.ConversationType.PRIVATE) {
                    TextMessage textMessage = (TextMessage) message.getContent();
                    String content = textMessage.getContent();

                    final TextMsgBean textMsgBean = new Gson().fromJson(content, TextMsgBean.class);

                    // 请求添加好友
                    if (textMsgBean.getMsg_type().equals(RongCloudManager.TYPE_MSG_ADD_FRIEND)) {

                        //把 发送好友请求的 信息保存本地； 只添加一次
                        disposable = Observable.create(new ObservableOnSubscribe<List<NewFriend>>() {
                            @Override
                            public void subscribe(ObservableEmitter<List<NewFriend>> emitter) throws Exception {

                                emitter.onNext(LitePalManager.getInstance().queryNewFriend());
                                emitter.onComplete();

                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<List<NewFriend>>() {
                                    @Override
                                    public void accept(List<NewFriend> list) throws Exception {

                                        Log.e(TAG, "accept:  添加对方好友的请求 存储到 本地 " + list.size());
                                        if (list != null && list.size() > 0) {
                                            boolean have = false;
                                            for (int j = 0; j < list.size(); j++) {
                                                NewFriend friend = list.get(0);

                                                // id 不同的时候在添加 ,  即使有多次发送添加好友的消息， have 只会一次等于 false ;
                                                if (message.getSenderUserId().equals(friend.getUserId())) {
                                                    have = true;
                                                    break;
                                                }
                                            }
                                            if (!have) {
                                                // 保存本地
                                                LitePalManager.getInstance()
                                                        .saveNewFriend(textMsgBean.getMsg_content(),
                                                                message.getSenderUserId());
                                            }
                                        } else {

                                            LitePalManager.getInstance()
                                                    .saveNewFriend(textMsgBean.getMsg_content(),
                                                            message.getSenderUserId());
                                        }


                                    }
                                });


                        // 普通文本消息
                    } else if (textMsgBean.getMsg_type().equals(RongCloudManager.TYPE_MSG_TEXT)) {

                        String msg_content = textMsgBean.getMsg_content();
                        MessageEvent event = new MessageEvent(EventManager.TAG_SEND_TEXT_MSG);
                        event.setContent(msg_content);
                        event.setUserId(message.getSenderUserId());

                        EventManager.post(event);


                        // 同意添加好友
                    } else if (textMsgBean.getMsg_type().equals(RongCloudManager.TYPE_MSG_AGREE_FRIEND)) {

                        /**
                         *  对方同意好友请求
                         *  1，将对方添加到好友列表
                         *  2，刷新好友列表
                         */
                        Log.e(TAG, "对方同意好友请求");
                        BMobManager.getInstance().addFriend(message.getSenderUserId(), new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    EventManager.post(EventManager.TAG_REFRESH_FRIEND_LIST);
                                }
                            }
                        });


                    }

                }

                return false;
            }
        });


        //监听音视频通话
        RongCloudManager.getInstance().setReceivedCallListener(new IRongReceivedCallListener() {
            @Override
            public void onReceivedCall(RongCallSession rongCallSession) {
                String s = new Gson().toJson(rongCallSession);
                Log.e(TAG, "onReceivedCall: " + s);

            }

            @Override
            public void onCheckPermission(RongCallSession rongCallSession) {
                Log.e(TAG, "onCheckPermission: ");
            }
        });


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable.isDisposed()) {
            disposable.dispose();

        }
    }
}
