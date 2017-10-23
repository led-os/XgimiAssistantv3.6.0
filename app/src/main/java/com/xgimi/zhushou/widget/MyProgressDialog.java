package com.xgimi.zhushou.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;

public class MyProgressDialog {

    public Dialog mDialog;
    private AnimationDrawable animationDrawable = null;

    public MyProgressDialog(Context context, String message) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.progress_view, null);

        TextView text = (TextView) view.findViewById(R.id.progress_message);
        text.setText(message);
        ImageView loadingImage = (ImageView) view.findViewById(R.id.progress_view);
        loadingImage.setImageResource(R.drawable.loading_animation);
        animationDrawable = (AnimationDrawable) loadingImage.getDrawable();
        if (animationDrawable != null) {
            animationDrawable.setOneShot(false);
            animationDrawable.start();
        }
        mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);

    }

    public void setCancelAble(boolean cancleAble) {
        mDialog.setCancelable(cancleAble);
    }

    public void show() {
        mDialog.show();
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        mDialog.setCanceledOnTouchOutside(cancel);
    }

    public void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.cancel();
            animationDrawable.stop();
        }
    }

    public boolean isShowing() {
        if (mDialog.isShowing()) {
            return true;
        }
        return false;
    }
}
