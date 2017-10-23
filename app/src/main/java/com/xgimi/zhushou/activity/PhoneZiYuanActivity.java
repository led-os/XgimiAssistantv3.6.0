package com.xgimi.zhushou.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.FragmentAdapter;
import com.xgimi.zhushou.indictor.FileFragment;
import com.xgimi.zhushou.indictor.ImageListFragment;
import com.xgimi.zhushou.indictor.LocalMusicFragment;
import com.xgimi.zhushou.indictor.VideoFragment;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.UnderlinePageIndicator;

import java.util.ArrayList;

public class PhoneZiYuanActivity extends BaseActivity implements OnTouchListener{
	private FragmentAdapter mAdapter;
	private UnderlinePageIndicator indicator;

	private Button imgButton;
	private Button audioButton;
	private Button videoButton;
	private Button folderButton;

	private Fragment photoFragment = null;
	private LocalMusicFragment musicFragment = null;
	private Fragment videoFragment = null;
	private Fragment fileFragment = null;
	private ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();

	private ViewPager mPager;
	public View view;
	private ImageView back;
	private ImageView iv_remount;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = View.inflate(this, R.layout.local_fragment_total, null);
		setContentView(view);
		initView();
	}
	private void initView(){
		initFragment();
		iv_remount = (ImageView) findViewById(R.id.titile).findViewById(R.id.iv_remount);
		setYaokongBackground(iv_remount,this,"qita");
		if(Constant.netStatus){
			iv_remount.setImageResource(R.drawable.yaokongqi);
		}else{
			iv_remount.setImageResource(R.drawable.gimi_yaokong);
		}
		back = (ImageView) findViewById(R.id.titile).findViewById(R.id.back);
		back(back);
		back.setOnTouchListener(this);
		iv_remount.setOnTouchListener(this);
		TextView title = (TextView) findViewById(R.id.titile).findViewById(R.id.tv_titile);
		title.setText("本地资源");
		imgButton = (Button) findViewById(R.id.multiPhoto);
		audioButton = (Button) findViewById(R.id.multiMusic);
		videoButton = (Button) findViewById(R.id.multiVideo);
		folderButton = (Button) findViewById(R.id.multiFolder);
		MultiOnclickListener listener = new MultiOnclickListener();
		imgButton.setOnClickListener(listener);
		audioButton.setOnClickListener(listener);
		videoButton.setOnClickListener(listener);
		folderButton.setOnClickListener(listener);
		mPager = (ViewPager) findViewById(R.id.multiContent);
		indicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
		mAdapter = new FragmentAdapter(getSupportFragmentManager(), fragmentList);
		mPager.setAdapter(mAdapter);
		mPager.setOffscreenPageLimit(4);
		indicator.setViewPager(mPager);
		indicator.setSelectedColor(0xff4ba5ea);
		indicator.setBackgroundColor(0xFFFFFFFF);
		indicator.setCurrentItem(0);

		indicator.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	private void initFragment(){
		photoFragment = new ImageListFragment();
		musicFragment = new LocalMusicFragment();
		videoFragment = new VideoFragment();
		fileFragment = new FileFragment();

		fragmentList.add(photoFragment);
		fragmentList.add(musicFragment);
		fragmentList.add(videoFragment);
		fragmentList.add(fileFragment);
	}
	/**
	 * 设置当前界面
	 * @param item
	 */
	public void setCurrentPager(int item){
		indicator.setCurrentItem(item);
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				indicator.setCurrentItem(0);
				break;
			case 1:
				indicator.setCurrentItem(1);
				break;
			case 2:
				indicator.setCurrentItem(2);
				break;
			case 3:
				indicator.setCurrentItem(3);
				break;
			}
		}
	}

	private class MultiOnclickListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.multiPhoto:
				indicator.setCurrentItem(0);
				break;
			case R.id.multiMusic:
				indicator.setCurrentItem(1);
				break;
			case R.id.multiVideo:
				indicator.setCurrentItem(2);
				break;
			case R.id.multiFolder:
				indicator.setCurrentItem(3);
				break;
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
//		case R.id.iv_user:
//			switch (event.getAction()) {
//			case MotionEvent.ACTION_DOWN:
//				back.setAlpha(0.6f);
//				break;
//			case MotionEvent.ACTION_UP:
//				back.setAlpha(1.0f);
//				break;
//			}
//			break;
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
