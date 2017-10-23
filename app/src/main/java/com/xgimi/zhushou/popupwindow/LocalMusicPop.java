package com.xgimi.zhushou.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.Update;
import com.xgimi.zhushou.bean.Vctrolbean;
import com.xgimi.zhushou.bean.XgimiBean;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/11/1.
 */
public class LocalMusicPop extends PopupWindow{
    public View mMenuView;
    public TextView cancel,poptime,popzimu;
    public LocalMusicPop(Activity context){
        super(context);
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView=inflater.inflate(R.layout.local_music_pop,null);
        cancel= (TextView) mMenuView.findViewById(R.id.btn_cancel);
        poptime= (TextView) mMenuView.findViewById(R.id.pop_time);
        popzimu= (TextView) mMenuView.findViewById(R.id.pop_zimu);
        //取消按钮
        cancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //销毁弹出框
                EventBus.getDefault().post(new Vctrolbean());
                dismiss();
            }
        });
        poptime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new Update());
                //销毁弹出框
                dismiss();
            }
        });
        popzimu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new XgimiBean());
                //销毁弹出框
                dismiss();
            }
        });
        //设置按钮监听
//        listView.setOnClickListener(itemsOnClick);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setAnimationStyle(R.style.PopupAnimation);
    }
}
