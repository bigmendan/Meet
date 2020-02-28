package com.example.framework.event;

import org.greenrobot.eventbus.EventBus;

/**
 * EventBus 管理类
 */
public class EventManager {


    // 刷新好友列表 ；
    public static final int TAG_REFRESH_FRIEND_LIST = 10000001;


    // 发送文本消息
    public static final int TAG_SEND_TEXT_MSG = 10000002;

    /**
     * 注册
     *
     * @param o
     */
    public static void register(Object o) {
        EventBus.getDefault().register(o);

    }


    /**
     * 注销
     *
     * @param o
     */
    public static void unregister(Object o) {
        EventBus.getDefault().unregister(o);
    }


    /**
     * 发送事件
     *
     * @param type
     */
    public static void post(int type) {
        MessageEvent event = new MessageEvent(type);
        EventBus.getDefault().post(event);
    }


    /**
     * 发送事件
     *
     * @param event
     */
    public static void post(MessageEvent event) {
        EventBus.getDefault().post(event);
    }
}
