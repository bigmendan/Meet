package com.example.framework.manager;

import android.content.Context;
import android.util.Log;

import com.example.framework.constant.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.rong.calllib.IRongReceivedCallListener;
import io.rong.calllib.RongCallClient;
import io.rong.calllib.RongCallCommon;
import io.rong.calllib.RongCallSession;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * 融云 Manager
 */
public class RongCloudManager {

    private String TAG = "== RongCloudManager";

    public static final String RONG_CLOUD_APP_KEY = "0vnjpoad0i3tz";
    public static final String RONG_CLOUD_APP_SECRET = "IM1jKPjBa5ymF";

    // TYPE
    public static final String MSG_TXT_NAME = "TxtMsg";
    public static final String MSG_IMG_NAME = "TxtMsg";
    public static final String MSG_LOCATION_NAME = "TxtMsg";

    // 普通文本消息
    public static final String TYPE_MSG_TEXT = "TYPE_MSG_TEXT";
    public static final String TYPE_MSG_ADD_FRIEND = "TYPE_MSG_ADD_FRIEND";
    public static final String TYPE_MSG_AGREE_FRIEND = "TYPE_MSG_AGREE_FRIEND";


    private static RongCloudManager INSTANCE = null;

    private RongCloudManager() {
    }


    public static RongCloudManager getInstance() {
        if (INSTANCE == null) {
            synchronized (RongCloudManager.class) {
                if (INSTANCE == null)
                    INSTANCE = new RongCloudManager();
            }
        }
        return INSTANCE;
    }

    /**
     * 初始化 RongCloud
     *
     * @param context
     */
    public void init(Context context) {
        RongIMClient.init(context);
    }

    /**
     * 融云连接
     */
    public void connect(String token) {
        RongIMClient.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                Log.e(TAG, " 连接 onTokenIncorrect: ");
            }

            @Override
            public void onSuccess(String s) {
                Log.e(TAG, " 连接 onSuccess: " + s);

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e(TAG, " 连接 onError: " + errorCode);

            }
        });
    }

    /**
     * 融云断开连接
     * 在断开和融云的连接后，有新消息时，仍然能够收到推送通知，调用 disconnect() 方法。
     */
    public void disconnect() {
        RongIMClient.getInstance().disconnect();
    }

    /**
     * 断开连接后，不想收到任何推送通知，调用 logout() 方法。
     */
    public void logout() {
        RongIMClient.getInstance().logout();
    }


    /**
     * 接收消息的监听 ;
     *
     * @param listener
     */
    public void setReceiveListener(RongIMClient.OnReceiveMessageListener listener) {
        RongIMClient.setOnReceiveMessageListener(listener);

    }

    // 发送文本消息的监听回调
    IRongCallback.ISendMessageCallback iSendMessageCallback = new IRongCallback.ISendMessageCallback() {
        @Override
        public void onAttached(Message message) {
            //消息本地数据库存储成功的回调
        }

        @Override
        public void onSuccess(Message message) {
            //消息通过网络发送成功的回调
            Log.e(TAG, "消息发送 onSuccess: " + message);

        }

        @Override
        public void onError(Message message, RongIMClient.ErrorCode errorCode) {
            //消息发送失败的回调
            Log.e(TAG, "消息发送 onError: " + errorCode);
        }
    };

    /**
     * 发送文本消息
     *
     * @param msg
     * @param targetId
     */
    private void sendTextMessage(String msg, String targetId) {

        TextMessage textMessage = new TextMessage(msg);
        RongIMClient.getInstance().sendMessage(
                Conversation.ConversationType.PRIVATE,
                targetId,
                textMessage,
                null,
                null,
                iSendMessageCallback);

    }

    /**
     * 发送文本消息
     *
     * @param msg
     * @param type     添加好友， 普通文本消息， 同意添加好友
     * @param targetId
     */
    public void sendTextMessage(String msg, String type, String targetId) {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put(Constant.MSG_TYPE, type);
            jsonObject.put(Constant.MSG_CONTENT, msg);
            String message = jsonObject.toString();

            sendTextMessage(message, targetId);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询本地会话列表
     *
     * @param callback
     */
    public void getConversationList(RongIMClient.ResultCallback<List<Conversation>> callback) {
        RongIMClient.getInstance().getConversationList(callback);
    }


    /**
     * 获取本地历史消息
     *
     * @param targetId
     * @param callback
     */
    public void getHistoryMessage(String targetId, RongIMClient.ResultCallback<List<Message>> callback) {
        RongIMClient.getInstance().getHistoryMessages(Conversation.ConversationType.PRIVATE,
                targetId, -1, 1000, callback);
    }


    /**
     * 获取 远程 历史消息
     *
     * @param targetId
     * @param callback
     */
    public void getRemoteHistoryMessage(String targetId, RongIMClient.ResultCallback<List<Message>> callback) {
        RongIMClient.getInstance().getRemoteHistoryMessages(Conversation.ConversationType.PRIVATE,
                targetId, 0, 1000, callback);
    }

    //  ----------------------------------------   音视频 ------------------------------------

    /**
     * 开启 音频 / 视频
     *
     * @param targetId
     * @param mediaType
     */
    private void startCall(String targetId, RongCallCommon.CallMediaType mediaType) {
        List<String> list = new ArrayList<>();
        list.add(targetId);

        RongCallClient.getInstance().startCall(Conversation.ConversationType.PRIVATE,
                targetId,
                list,
                null,
                mediaType,
                null);
    }


    /**
     * 音频通话
     *
     * @param targetId
     */
    public void startAudioCall(String targetId) {
        Log.e(TAG, "startAudioCall: " + targetId);
        startCall(targetId, RongCallCommon.CallMediaType.AUDIO);
    }


    /**
     * 视频通话
     *
     * @param targetId
     */
    public void startVideoCall(String targetId) {
        startCall(targetId, RongCallCommon.CallMediaType.VIDEO);

    }


    /**
     * 监听音视频电话
     *
     * @param listener
     */
    public void setReceivedCallListener(IRongReceivedCallListener listener) {
        RongCallClient.getInstance().setReceivedCallListener(listener);
    }

    /**
     * 接听电话
     *
     * @param callId
     */
    public void acceptCall(String callId) {
        RongCallClient.getInstance().acceptCall(callId);
    }


    /**
     * 挂断电话
     *
     * @param callId
     */
    public void hangUpCall(String callId) {
        RongCallClient.getInstance().hangUpCall(callId);
    }
}
