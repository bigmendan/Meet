package com.example.meet.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.framework.bmob.BMobManager;
import com.example.framework.bmob.IMUser;
import com.example.framework.constant.Constant;
import com.example.meet.R;
import com.example.meet.ui.adapter.AddFriendAdapter;
import com.example.meet.ui.base.BaseActivity;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 添加 Friend ;
 */
public class AddFriendActivity extends BaseActivity implements View.OnClickListener {


    private EditText mPhoneEt;
    private Button mSearchBtn;
    private RecyclerView mRecyclerView;

    private AddFriendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);


        mPhoneEt = findViewById(R.id.mPhoneEt);
        mSearchBtn = findViewById(R.id.mSearchBtn);
        mSearchBtn.setOnClickListener(this);


        initRecyclerView();


    }


    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddFriendAdapter(this);
        mRecyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new AddFriendAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

                IMUser imUser = adapter.getItemPosition(position);

                Intent intent = new Intent(AddFriendActivity.this,
                        UserInfoActivity.class);
                intent.putExtra(Constant.USER_ID, imUser.getObjectId());


                startActivity(intent);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mSearchBtn:
                String phone = mPhoneEt.getText().toString().trim();
                BMobManager.getInstance().queryPhoneNumber(phone, new FindListener<IMUser>() {
                    @Override
                    public void done(List<IMUser> list, BmobException e) {
                        if (e == null) {
                            adapter.setList(list);

                        } else {
                            Toast.makeText(AddFriendActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                            Log.e("== AddFriend = ", "queryFail: " + e);

                        }
                    }
                });

                break;
        }
    }
}
