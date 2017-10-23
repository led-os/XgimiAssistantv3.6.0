package com.xgimi.zhushou.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class YaokongTitle extends LinearLayout {

	private TextView tv;
	private ImageView heImageView;
	private Context mconte;
	public YaokongTitle(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.setOrientation(LinearLayout.VERTICAL);
		this.setGravity(Gravity.CENTER);
		this.setClickable(true);
		this.requestFocus();
		LayoutParams layoutParams=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		heImageView = new ImageView(context);
//		this.setBackgroundColor(Color.parseColor("#282A32"));
		heImageView.setLayoutParams(layoutParams);
		addView(heImageView);
		LayoutParams layoutParams1=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		tv = new TextView(context);
		tv.setTextColor(Color.parseColor("#ffffff"));
		tv.setTextSize(12);
		tv.setLayoutParams(layoutParams1);
		addView(tv);
		mconte=context;
		setNoColor();
//		setTextColor();
		
	}
	
	public void setDrable(int id){
		heImageView.setImageResource(id);
	}
	public void setText(String text){
		tv.setText(text);
	}
	public void setTextColor(){
		tv.setTextColor(Color.parseColor("#ffffff"));
		tv.setAlpha(1.0f);
		heImageView.setAlpha(1.0f);
	}
	public void setNoColor(){
//		tv.setTextColor(Color.parseColor("#ffffff"));
		tv.setAlpha(0.7f);
		heImageView.setAlpha(0.6f);
	}
}
