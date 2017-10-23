package com.xgimi.zhushou.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.util.Constant;

public class RemountSetActivity extends BaseActivity implements OnTouchListener{

	private ToggleButton snake;
	private ToggleButton phoneVoice;
	private ToggleButton ykjianpan;
	private ImageView iv_remount;
	private ImageView back;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remount_set);
		initView();
		initData();
	}
	private void initView(){
		iv_remount = (ImageView) findViewById(R.id.title).findViewById(R.id.iv_remount);
		setYaokongBackground(iv_remount,this,"qita");
		if(Constant.netStatus){
			iv_remount.setImageResource(R.drawable.yaokongqi);
		}else{
			iv_remount.setImageResource(R.drawable.gimi_yaokong);
		}
		back = (ImageView) findViewById(R.id.back);
		back(back);
		back.setOnTouchListener(this);
		iv_remount.setOnTouchListener(this);
		TextView title = (TextView) findViewById(R.id.tv_titile);
		title.setText("遥控器设置");
		TextView shenyin=(TextView) findViewById(R.id.shenying).findViewById(R.id.tv_name);
		shenyin.setText("手机音量键控制声音");
		TextView jianpan=(TextView) findViewById(R.id.yaokongqi).findViewById(R.id.tv_name);
		jianpan.setText("遥控器小键盘模式");
		snake = (ToggleButton) findViewById(R.id.anjian).findViewById(R.id.tp);
		phoneVoice = (ToggleButton) findViewById(R.id.shenying).findViewById(R.id.tp);
		ykjianpan = (ToggleButton) findViewById(R.id.yaokongqi).findViewById(R.id.tp);
		View line = findViewById(R.id.yaokongqi).findViewById(R.id.view);
		line.setVisibility(View.INVISIBLE);
		
		if(!App.getContext().readZhuangtai()){
			snake.setChecked(false);
		}
		if(!App.getContext().readVoiceContrl()){
			phoneVoice.setChecked(false);
		}
		if(!App.getContext().readXiaoJianPan()){
			ykjianpan.setChecked(false);
		}
	}
	private void initData(){
		snake.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					App.getContext().saveZhuangtai(true);
				}else{
					App.getContext().saveZhuangtai(false);
				}
			}
		});
		
		phoneVoice.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					App.getContext().saveVoiceContrl(true);
				}else{
					App.getContext().saveVoiceContrl(false);
				}
			}
		});
		
		ykjianpan.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					App.getContext().saveXiaoJianPan(true);
				}else{
					App.getContext().saveXiaoJianPan(false);

				}
			}
		});
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
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
