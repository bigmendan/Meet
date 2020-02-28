package com.example.framework.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;


public class DialogView extends Dialog {


    public DialogView(Context context, int layout, int themeResId, int gravity) {
        super(context, themeResId);
        setContentView(layout);

        Window window = getWindow();
        WindowManager.LayoutParams layoutPrams = window.getAttributes();

        layoutPrams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutPrams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        layoutPrams.gravity = gravity;


        window.setAttributes(layoutPrams);

    }


}
