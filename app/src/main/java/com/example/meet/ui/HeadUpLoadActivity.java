package com.example.meet.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.framework.bmob.BMobManager;
import com.example.framework.manager.DialogManager;
import com.example.framework.utils.FileHelper;
import com.example.framework.views.DialogView;
import com.example.meet.R;
import com.example.meet.ui.base.BaseActivity;

import java.io.File;


import cn.bmob.v3.exception.BmobException;

/**
 * 用户资料上传
 */
public class HeadUpLoadActivity extends BaseActivity {


    private ImageView iv_header;
    private EditText et_nick;

    private DialogView dialogView;
    private File uploadFile = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_up_load);


        iv_header = findViewById(R.id.header);
        et_nick = findViewById(R.id.nick);

    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header:
                createDialogView();
                break;


            case R.id.uploadBtn:
                uploadPhoto();
                break;


        }
    }

    /**
     * 上传头像
     */
    private void uploadPhoto() {
        String nickName = et_nick.getText().toString().trim();
        BMobManager.getInstance().uploadPhoto(uploadFile, nickName, new BMobManager.UploadListener() {
            @Override
            public void upLoadSuccess() {
                Log.e("== 上传成功", "upLoadSuccess: ");
                // 成功以后 finish()
                setResult(RESULT_OK);
                finish();

            }

            @Override
            public void upLoadFail(BmobException e) {
                Log.e("=== ", "upLoadFail: " + e.getErrorCode() + ":" + e.getMessage());
            }
        });

    }

    private void createDialogView() {
        dialogView = DialogManager.getInstance().initView(this,
                R.layout.upload_header, Gravity.BOTTOM);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.camera:            // 记得添加动态请求权限;

                        DialogManager.getInstance().hide(dialogView);
                        FileHelper.getInstance().toCamera(HeadUpLoadActivity.this);

                        break;
                    case R.id.album:
                        DialogManager.getInstance().hide(dialogView);
                        FileHelper.getInstance().toAlbum(HeadUpLoadActivity.this);
                        break;
                    case R.id.cancel:
                        DialogManager.getInstance().hide(dialogView);

                        break;

                }

            }
        };


        TextView camera = dialogView.findViewById(R.id.camera);
        TextView album = dialogView.findViewById(R.id.album);
        TextView cancel = dialogView.findViewById(R.id.cancel);


        camera.setOnClickListener(listener);
        album.setOnClickListener(listener);
        cancel.setOnClickListener(listener);


        dialogView.setCanceledOnTouchOutside(false);
        dialogView.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            Log.e("=== ", "onActivityResult: " + requestCode);

            switch (requestCode) {

                case FileHelper.CAMERA_REQUEST_CODE:

                    uploadFile = FileHelper.getInstance().getTempFile();
                    break;

                case FileHelper.ALBUM_REQUEST_CODE:
                    String realImagePath = FileHelper.getInstance().getRealImagePath(this, data.getData());

                    Log.e("===", " 相册图片地址 =  " + realImagePath);

                    if (!TextUtils.isEmpty(realImagePath)) {
                        uploadFile = new File(realImagePath);
                    }

                    break;
            }


            if (uploadFile != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(uploadFile.getPath());
                iv_header.setImageBitmap(bitmap);

            }

        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
