package com.example.meet.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.framework.bmob.BMobManager;
import com.example.framework.db.LitePalManager;
import com.example.framework.db.NewFriend;
import com.example.framework.event.EventManager;
import com.example.framework.manager.RongCloudManager;
import com.example.meet.R;
import com.example.meet.ui.adapter.NewFriendAdapter;
import com.example.meet.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 新朋友
 */
public class NewFriendActivity extends BaseActivity {


    private String TAG = "== NewFrienActivity = ";

    private TextView noView;
    private RecyclerView mRecyclerView;

    private Disposable disposable;
    private NewFriendAdapter adapter;

    private List<NewFriend> newList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend);


        initWidgets();

        queryNewFriend();


    }


    private void initWidgets() {
        noView = findViewById(R.id.noView);
        mRecyclerView = findViewById(R.id.mRecyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewFriendAdapter(this, newList);


        mRecyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new NewFriendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String userId) {

            }

            @Override
            public void onStatus(NewFriendAdapter.ViewHolder holder, int position, final String userId) {
                // 如果是等待点击，直接同意吧
                /**
                 * 1, 更改UI
                 * 2, 添加好友列表
                 *
                 */

                NewFriend newFriend = newList.get(position);
                newFriend.setAgree(0);  // 同意

                LitePalManager.getInstance().updateNewFriend(newFriend.getUserId(), 0);

                newList.set(position, newFriend);
                adapter.notifyDataSetChanged();


                /**
                 *    添加好友成成功 ， 只有成功，没有失败；
                 *    通知对方
                 */

                BMobManager.getInstance().addFriend(userId, new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Log.e(TAG, "同意添加对方好友 = " + s);
                            RongCloudManager.getInstance().sendTextMessage("我已经同意你的好友申请",
                                    RongCloudManager.TYPE_MSG_AGREE_FRIEND,
                                    userId);

                            // 刷新好友列表
                            EventManager.post(EventManager.TAG_REFRESH_FRIEND_LIST);

                        }
                    }
                });

            }
        });


    }

    /**
     * 查找新的好友 ;
     */
    private void queryNewFriend() {
        /**
         *  RxJava 线程调度 ;
         */
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
                    public void accept(List<NewFriend> newFriends) throws Exception {

                        if (newFriends != null && newFriends.size() > 0) {
                            newList.addAll(newFriends);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable.isDisposed()) {
            disposable.dispose();

        }
    }
}
