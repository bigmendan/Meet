package com.example.framework.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件类
 */
public class FileHelper {

    private static volatile FileHelper INSTANCE = null;

    private SimpleDateFormat simpleDateFormat = null;
    // 相机
    public final static int CAMERA_REQUEST_CODE = 1003;
    // 相册
    public final static int ALBUM_REQUEST_CODE = 1004;


    private FileHelper() {

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-DD-hh:mm:ss");

    }


    public static FileHelper getInstance() {

        if (INSTANCE == null) {
            synchronized (FileHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FileHelper();
                }
            }
        }

        return INSTANCE;
    }


    private File tempFile;
    private Uri imgUrl;

    /**
     * 跳转到相机     根本记不住啊 (灬ꈍ ꈍ灬) 😭😭😭
     */
    public void toCamera(Activity mActivity) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String fileName = simpleDateFormat.format(new Date()) + ".jpg";

        tempFile = new File(Environment.getExternalStorageDirectory(), fileName);

        // 适配 Android 7.0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            imgUrl = Uri.fromFile(tempFile);
        } else {
            imgUrl = FileProvider.getUriForFile(mActivity,
                    mActivity.getPackageName() + ".fileprovider", tempFile);

            // 添加权限
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // 并且需要配置xml 文件， 它是ContentProvider的子类，也要在清单文件中注册;
        }

        // 输出结果
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUrl);
        mActivity.startActivityForResult(intent, CAMERA_REQUEST_CODE);

    }


    /**
     * 跳转到相册
     *
     * @param mActivity
     */
    public void toAlbum(Activity mActivity) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        mActivity.startActivityForResult(intent, ALBUM_REQUEST_CODE);
    }

    public File getTempFile() {
        return tempFile;
    }


    /**
     * 通过 Uri 获取到图片的真实地址   完全不懂 ;
     *
     * @param context
     * @param uri
     * @return
     */
    public String getRealImagePath(Context context, Uri uri) {

        String[] s = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(context, uri, s,
                null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(index);


    }


}
