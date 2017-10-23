package com.xgimi.zhushou.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.xgimi.zhushou.R;

/**
 * Created by XGIMI on 2017/8/21.
 */

public class QuitMirrorPlayComfirDialog {

    private Dialog mDialog;
    private Context mContext;
    private QuitDialogListener mListener;

    public QuitMirrorPlayComfirDialog(Context mContext, QuitDialogListener listener) {
        this.mContext = mContext;
        this.mListener = listener;
        initView();

    }

    private void initView() {
        mDialog = new Dialog(mContext, R.style.dialog);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_quit_mirror_play_comfir, null);
        mDialog.setContentView(view);
        view.findViewById(R.id.btn_i_know_quit_mirror_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
                if (mListener != null) {
                    mListener.onEnterClick();
                }
            }
        });
//        Window window = mDialog.getWindow();
//
//        WindowManager.LayoutParams params = window.getAttributes();
//
//        params.gravity = Gravity.CENTER;
//        params.width = 300;
//        params.height = 200;
//        window.setAttributes(params);
    }

    public void show() {
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public interface QuitDialogListener {
        void onEnterClick();
    }

    public boolean isShowing() {
        if (mDialog != null) {
            return mDialog.isShowing();
        } else {
            return false;
        }
    }

    public void cancle() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.cancel();
        }
    }
}
