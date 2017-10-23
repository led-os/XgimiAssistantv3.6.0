package com.xgimi.zhushou.activity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.DeviceUtils;

public class AboutActivity extends BaseActivity implements OnTouchListener{
	private TextView Version;
	private ImageView iv_remount;
	private ImageView back;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		iv_remount = (ImageView) findViewById(R.id.title).findViewById(R.id.iv_remount);

		setYaokongBackground(iv_remount,this,"qita");
		iv_remount.setOnTouchListener(this);
		back = (ImageView) findViewById(R.id.iv_user);
		back(back);
		back.setOnTouchListener(this);
		TextView title = (TextView) findViewById(R.id.tv_titile);
		title.setText("关于");
		Version = (TextView) findViewById(R.id.about_version);
		Version.setText("V " + DeviceUtils.getappVersion(this));
		if(Constant.netStatus){
			iv_remount.setImageResource(R.drawable.yaokongqi);
		}else{
			iv_remount.setImageResource(R.drawable.gimi_yaokong);
		}
	}
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_user:
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				back.setAlpha(0.6f);
				break;
			case MotionEvent.ACTION_UP:
				back.setAlpha(1.0f);
				break;
			}
			break;
		case R.id.iv_remount:
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				iv_remount.setAlpha(0.6f);
				break;
			case MotionEvent.ACTION_UP:
				iv_remount.setAlpha(1.0f);
				break;
			}
			break;
		}
		return false;
	}
}
