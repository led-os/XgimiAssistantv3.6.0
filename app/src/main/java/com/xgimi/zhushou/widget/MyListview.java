package com.xgimi.zhushou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;


import com.xgimi.zhushou.callback.ScroHuaLisener;


public class MyListview extends ListView {

	public MyListview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyListview(Context context) {
		super(context);
	}

	public MyListview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}


	public void setScroolview(MyScrollview mys) {
		this.my = mys;
	}

	private MyScrollview my;


	  /** 
     * 设置不滚动 
     */  
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)  
    {  
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
                            MeasureSpec.AT_MOST);  
            super.onMeasure(widthMeasureSpec, expandSpec);  

    }  


	public void setHuaDong(ScroHuaLisener sc) {
		this.scro = sc;
	}

	public ScroHuaLisener scro;

	private float raw;
	/*
	 * @Override protected void onMeasure(int widthMeasureSpec, int
	 * heightMeasureSpec) { int expandSpec =
	 * MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
	 * super.onMeasure(widthMeasureSpec, expandSpec); }
	 */

	/*
	 * @Override public boolean onTouchEvent(MotionEvent ev) { // TODO
	 * Auto-generated method stub if (ev.getAction() == MotionEvent.ACTION_DOWN
	 * || ev.getAction() == MotionEvent.ACTION_MOVE) {
	 * my.requestDisallowInterceptTouchEvent(false); return false; } return
	 * false; }
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if(ev.getAction()==MotionEvent.ACTION_MOVE){
			return false;
		}
		return super.onTouchEvent(ev);
	}
	
	/*public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
}*/
}
