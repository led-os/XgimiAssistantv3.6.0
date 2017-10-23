package com.xgimi.zhushou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

import com.xgimi.zhushou.bean.ContrlSHow;
import com.xgimi.zhushou.callback.ScroHuaLisener;

import de.greenrobot.event.EventBus;


public class MyScrollview extends ScrollView {

	private boolean isUphuadong=true;
	//private  EventBus;

	public MyScrollview(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
	}
	public ScroHuaLisener jinating;

	public void setDown(ScroHuaLisener jt) {
		this.jinating = jt;
	}
	private ListView listveiw;
	private boolean isfirst=true;
	
	private float rawdown=0.0f;
	private int computeVerticalScrollOffset;
	
	@Override
	public  boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_CANCEL:
			break;
		case MotionEvent.ACTION_DOWN:
			rawdown = ev.getRawY();
			computeVerticalScrollOffset = computeVerticalScrollOffset();
			Log.i("info",rawdown+"-----"+ev.getRawY());
		break;
		case MotionEvent.ACTION_MOVE:
			if(isfirst){
			rawdown = ev.getRawY();
			computeVerticalScrollOffset=computeVerticalScrollOffset();
			isfirst=false;
			}
			Log.i("info", rawdown+"huochangijang");
			break;
		case MotionEvent.ACTION_UP:
			Log.i("info",rawdown+"----2222-"+ev.getRawY());
			isfirst=true;
			if (ev.getRawY()-rawdown < 20) {
				Log.i("info",rawdown+"-----"+ev.getRawY());
				if(isUphuadong){
				jinating.setMove(120);
				isUphuadong=false;
				}
			} else if(Math.abs(ev.getRawY()-rawdown)>0||Math.abs(ev.getRawY()-rawdown)<20){
				
			}
			else {
				isUphuadong=true;
				jinating.setMove(0);
			}
			if(this.computeVerticalScrollOffset()-computeVerticalScrollOffset>0){
				if(isUphuadong){
					jinating.setMove(120);
					isUphuadong=false;
					}
			}else if(this.computeVerticalScrollOffset()-computeVerticalScrollOffset==0){
				
			}
			else
			{
				isUphuadong=true;
				jinating.setMove(0);
			}
			break;
		}
		return super.onTouchEvent(ev);
		
	}
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		View view = (View)getChildAt(getChildCount()-1);
        int d = view.getBottom();
        d -= (getHeight()+getScrollY());
        if(d==0)
        {
           EventBus.getDefault().post(new ContrlSHow(1));
        }
        else{
            super.onScrollChanged(l,t,oldl,oldt);
    }
	}

}
