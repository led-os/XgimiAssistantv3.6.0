package com.xgimi.zhushou.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.xgimi.zhushou.R;

/**
 * Created by Administrator on 2016/9/10.
 */
public class WelcomeDilog implements View.OnClickListener{
    public Dialog mDialog;
    Context mContext;

    public WelcomeDilog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        this.mContext=context;
        View view = inflater.inflate(R.layout.welcomedilog, null);
        mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        initView(view);
    }
    public void setCanceledOnTouchOutside(boolean cancel) {
        mDialog.setCanceledOnTouchOutside(cancel);
    }
    private void initView(View view) {
    }

    public void show() {
        mDialog.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
    public void dismiss() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
            // animationDrawable.stop();
        }
    }
}
