package com.xgimi.zhushou.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.FilmDetailBean;

/**
 * Created by 霍长江 on 2016/9/12.
 */
public class FilmDilog implements View.OnClickListener{
    public Dialog mDialog;
    private AnimationDrawable animationDrawable = null;
    private inStallLisener inStallLisener;
    FilmDetailBean.DataBean.SourceBean.GmIntentBean mData;
    public void setLisener(inStallLisener lisener){
        this.inStallLisener=inStallLisener;
    }
    public FilmDilog(Context context,FilmDetailBean.DataBean.SourceBean.GmIntentBean data) {
    this.mData=data;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.film_dilog, null);
        mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(true);
        initView(view);

    }

    private void initView(View view) {
        Button cancel = (Button) view.findViewById(R.id.cancel);
        Button install = (Button) view.findViewById(R.id.install);

        cancel.setOnClickListener(this);
        install.setOnClickListener(this);

    }
    public void show() {
        mDialog.show();
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        mDialog.setCanceledOnTouchOutside(cancel);
    }

    public void dismiss() {
        if(mDialog.isShowing()) {
            mDialog.dismiss();
            animationDrawable.stop();
        }
    }
    public boolean isShowing(){
        if(mDialog.isShowing()) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                dismiss();
                break;
            case R.id.install:
                dismiss();
                inStallLisener.gmInstall(mData);

                break;
        }
    }

    public interface  inStallLisener{
        void gmInstall(FilmDetailBean.DataBean.SourceBean.GmIntentBean data);
    }
}
