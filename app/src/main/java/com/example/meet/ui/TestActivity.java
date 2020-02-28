package com.example.meet.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.framework.bmob.BmobTestData;
import com.example.meet.R;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:
                saveData();
                break;
            case R.id.delete:
                break;

            case R.id.update:
                break;

            case R.id.query:
                break;
        }
    }

    private void saveData() {
        BmobTestData data = new BmobTestData();
        data.setName("小王");
        data.setAge(13);
        data.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(TestActivity.this, "数据添加成功", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("==== ", "done: " + e.getMessage());

                }
            }
        });
    }
}
