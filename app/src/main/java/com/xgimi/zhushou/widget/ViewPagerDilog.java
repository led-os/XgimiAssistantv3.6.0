package com.xgimi.zhushou.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.SimpleAdapter;

import java.util.ArrayList;

public class ViewPagerDilog {
	Dialog mDialog;
	Context contetn;
	private ArrayList<String> sds;
	
	private TextView tv;
	private ImageView iv;
	public ViewPagerDilog(Context context, ArrayList<String> sds, int postion) {
		LayoutInflater inflater = LayoutInflater.from(context);
		this.contetn=context;
		View view = inflater.inflate(R.layout.activity_phono_view_pager, null);
		mDialog = new Dialog(context, R.style.dialog);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(true);
		this.sds=sds;
		iv = (ImageView) view.findViewById(R.id.iv);
		iv.setVisibility(View.GONE);
		Window window = mDialog.getWindow();
		LayoutParams windowparams = window.getAttributes();
		window.setGravity(Gravity.BOTTOM);
		windowparams.width = LayoutParams.FILL_PARENT;
		window.setAttributes(windowparams);
		Rect rect = new Rect();
		View view1 = window.getDecorView();
		view1.getWindowVisibleDisplayFrame(rect);
		
		initView(view,postion);
		
	}

	private void initView(View view,int postion) {
		tv = (TextView) view.findViewById(R.id.tv);
		tv.setText((postion+1)+"/"+sds.size());
	
		PhonoViewPager viewpager = (PhonoViewPager) view.findViewById(R.id.viewpager);
		viewpager.setAdapter(new SimpleAdapter(sds,mDialog));
		viewpager.setCurrentItem(postion);
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				tv.setText((arg0+1)+"/"+sds.size());
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
	}
	public void show() {
		mDialog.show();
	}
	public void setCanceledOnTouchOutside(boolean cancel) {
		mDialog.setCanceledOnTouchOutside(cancel);
	}
	public void dismiss() {
		if (mDialog.isShowing()) {
			mDialog.dismiss();
			// animationDrawable.stop();
		}
	}
	public boolean isShowing() {
		if (mDialog.isShowing()) {
			return true;
		}
		return false;
	}

}
