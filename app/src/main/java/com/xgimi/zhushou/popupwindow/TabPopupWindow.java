package com.xgimi.zhushou.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xgimi.zhushou.R;

/**
 * Created by Administrator on 2016/10/24.
 */
public class TabPopupWindow extends PopupWindow {

    private View mMenuView;
    private TextView textView;
    public TabPopupWindow(Activity context,String type){
        super(context);
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView=inflater.inflate(R.layout.popwindow_table,null);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置匡高
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        textView= (TextView) mMenuView.findViewById(R.id.tab_pop_text);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
//        this.setAnimationStyle(R.style.PopupAnimation);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
//        实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(1);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
//        mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        if(type.equals("left")){
            textView.setBackgroundResource(R.drawable.tabpop_left);
        }else {
            textView.setBackgroundResource(R.drawable.tabpop_left);
        }
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.tab_pop_text).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

}
