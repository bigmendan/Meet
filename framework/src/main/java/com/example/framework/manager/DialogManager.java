package com.example.framework.manager;

import android.content.Context;

import com.example.framework.R;
import com.example.framework.views.DialogView;

/**
 * 单例 Dialog
 */
public class DialogManager {

    private static DialogManager INSTANCE = null;

    private DialogManager() {
    }


    public static DialogManager getInstance() {
        if (INSTANCE == null) {
            synchronized (DialogManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DialogManager();
                }
            }
        }

        return INSTANCE;
    }

    public DialogView initView(Context mContext, int layout, int gravity) {
        return new DialogView(mContext, layout, R.style.Dialog_View_Theme, gravity);

    }


    /**
     * 显示
     *
     * @param view
     */
    public void show(DialogView view) {
        if (view != null) {
            if (!view.isShowing()) {
                view.show();
            }
        }
    }


    /**
     * 隐藏
     *
     * @param view
     */
    public void hide(DialogView view) {
        if (view != null) {
            if (view.isShowing()) {
                view.dismiss();
            }
        }
    }
}
