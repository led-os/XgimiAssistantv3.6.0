package com.xgimi.zhushou.widget;


import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.xgimi.device.socket.UdpManager;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.YiPingActivity;
import com.xgimi.zhushou.util.Constant;

/**
 * 同屏时可拖动的控制菜单，先写成这样，后面有时间再重新写
 * 
 * 可处理的逻辑都在这里面处理了，不提交到yipingactivity
 * 
 * 2014.10.25
 * 
 */
public class DraftButton extends Button {

	private App App;

	private Context mContext;

	private Handler mHandler;

	private int screenWidth, screenHeight;

	private int lastX, lastY, lastDisX, lastDisY;
	private int top = 0;
	private int left = 0;

	private boolean isLeft = true;
	private boolean ismovehide = false;

	private TranslateAnimation animation;
	private TranslateAnimation animation_layout;
	private TranslateAnimation animation_hide;
	private ScaleAnimation scaleAnimation; // 点击重力感应按钮是的动画

	private long time;

	private RelativeLayout rootLaytout;

	private RelativeLayout menuLayout;

	private Button btn_finish_left, btn_finish_right;

	private Button vol_decreast, vol_add;
	private Button Gravtiy, Menu, Home, Back;

	private MyClickListener myClickListener;
	private MyLongClickListener myLongClickListener;

	// 标记长按
	private boolean volLongpress = false;
	private String long_command; // 长按的命令拷贝
	private static final int VOLUMEHANDLER = 199; // 长按消息

	public DraftButton(Context context, Handler handler, RelativeLayout layout) {
		super(context);
		this.mContext = context;
		this.mHandler = handler;
		this.rootLaytout = layout;
		initView(context);
	}

	public void initView(Context context) {

		App = (App) context.getApplicationContext();

		DisplayMetrics dm = new DisplayMetrics();
		dm = mContext.getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;

		menuLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.yiping_menu, null);

		btn_finish_left = (Button) menuLayout.findViewById(R.id.yiping_menu_close);
		btn_finish_right = (Button) menuLayout.findViewById(R.id.yiping_menu_close_right);

		vol_decreast = (Button) menuLayout.findViewById(R.id.yiping_menu_volume_decreast);
		vol_add = (Button) menuLayout.findViewById(R.id.yiping_menu_volume_add);
		Gravtiy = (Button) menuLayout.findViewById(R.id.yiping_menu_gravity);
		Menu = (Button) menuLayout.findViewById(R.id.yiping_menu_menu);
		Home = (Button) menuLayout.findViewById(R.id.yiping_menu_home);
		Back = (Button) menuLayout.findViewById(R.id.yiping_menu_back);

		myClickListener = new MyClickListener();
		myLongClickListener = new MyLongClickListener();

		vol_decreast.setOnClickListener(myClickListener);
		vol_add.setOnClickListener(myClickListener);
		Gravtiy.setOnClickListener(myClickListener);
		Menu.setOnClickListener(myClickListener);
		Home.setOnClickListener(myClickListener);
		Back.setOnClickListener(myClickListener);
		btn_finish_left.setOnClickListener(myClickListener);
		btn_finish_right.setOnClickListener(myClickListener);
		vol_decreast.setOnLongClickListener(myLongClickListener);
		vol_add.setOnLongClickListener(myLongClickListener);

		scaleAnimation = new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f, Gravtiy.getMeasuredWidth(), Gravtiy.getMeasuredHeight());
		scaleAnimation.setDuration(50);

		setGravityStatus();

	}

	/**
	 * 初始化界面的重力感应图标
	 */
	private void setGravityStatus() {
		switch (App.getGravitySenseStatus()) {
		case 0:
			Gravtiy.setBackgroundResource(R.drawable.gravity_colse);
			break;
		case 1:
			Gravtiy.setBackgroundResource(R.drawable.gravity_low);
			break;
		case 2:
			Gravtiy.setBackgroundResource(R.drawable.gravity_mid);
			break;
		case 3:
			Gravtiy.setBackgroundResource(R.drawable.gravity_high);
			break;
		}
	}

	/**
	 * 改变界面的重力感应图标，需要控制操作
	 */
	private void changeGravityStatus() {
		switch (App.getGravitySenseStatus()) {
		case 0:
			mHandler.sendEmptyMessage(YiPingActivity.OPENGRAVITY);
			Gravtiy.setBackgroundResource(R.drawable.gravity_low);
			App.setGravitySenseStatus(1);
			break;
		case 1:
			Gravtiy.setBackgroundResource(R.drawable.gravity_mid);
			App.setGravitySenseStatus(2);
			break;
		case 2:
			Gravtiy.setBackgroundResource(R.drawable.gravity_high);
			App.setGravitySenseStatus(3);
			break;
		case 3:
			mHandler.sendEmptyMessage(YiPingActivity.CLOSEGRAVITY);
			Gravtiy.setBackgroundResource(R.drawable.gravity_colse);
			App.setGravitySenseStatus(0);
			break;
		}
	}

	private class MyClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {

			volLongpress = false;

			switch (arg0.getId()) {
			case R.id.yiping_menu_gravity:
				changeGravityStatus();
				Gravtiy.clearAnimation();
				Gravtiy.startAnimation(scaleAnimation);
				break;
			case R.id.yiping_menu_menu:
				Send(Constant.COM_Menu);
				break;
			case R.id.yiping_menu_home:
				Send(Constant.COM_Home);
				break;
			case R.id.yiping_menu_back:
				Send(Constant.COM_Back);
				break;
			case R.id.yiping_menu_volume_decreast:
				Send(Constant.COM_DecreaseVolume);
				break;
			case R.id.yiping_menu_volume_add:
				Send(Constant.COM_IncreaseVolume);
				break;
			case R.id.yiping_menu_close:
				mHandler.sendEmptyMessage(YiPingActivity.FINSHACTIVITY);
				break;
			case R.id.yiping_menu_close_right:
				mHandler.sendEmptyMessage(YiPingActivity.FINSHACTIVITY);
				break;
			}
		}

	}

	private class MyLongClickListener implements OnLongClickListener {

		@Override
		public boolean onLongClick(View v) {
			switch (v.getId()) {
			case R.id.yiping_menu_volume_add:
				long_command = Constant.COM_IncreaseVolume;
				break;
			case R.id.yiping_menu_volume_decreast:
				long_command = Constant.COM_DecreaseVolume;
				break;
			}
			volLongpress = true;
			handler.sendEmptyMessage(VOLUMEHANDLER);
			return false;
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case VOLUMEHANDLER:
				Send(long_command);
				if (volLongpress) {
					msg = obtainMessage(VOLUMEHANDLER);
					sendMessageDelayed(msg, 100);
				}
				break;
			}
		};
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:

			time = System.currentTimeMillis();

			lastX = (int) event.getRawX();
			lastY = (int) event.getRawY();

			lastDisX = (int) getLeft();
			lastDisY = (int) getTop();

			break;

		case MotionEvent.ACTION_MOVE:

			int dx = (int) event.getRawX() - lastX;
			int dy = (int) event.getRawY() - lastY;

			int disx = (int) Math.abs(getLeft() - lastDisX);
			int disy = (int) Math.abs(getTop() - lastDisY);

			if (disx > getWidth() || disy > getWidth()) {
				hideLayout();
			}

			top = getTop() + dy;

			left = getLeft() + dx;

			if (top <= 0) {
				top = 0;
			}
			if (top >= screenHeight - getHeight()) {
				top = screenHeight - getHeight();
			}
			if (left >= screenWidth - getWidth()) {
				left = screenWidth - getWidth();
			}

			if (left <= 0) {
				left = 0;
			}

			layout(left, top, left + getWidth(), top + getHeight());

			lastX = (int) event.getRawX();
			lastY = (int) event.getRawY();

			break;
		case MotionEvent.ACTION_UP:

			if ((System.currentTimeMillis() - time) < 150) {

				handlerLayout();
			}

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -2);

			if (lastX < (screenWidth / 2.0)) {

				isLeft = true;

				params.setMargins(0, top, 0, 0);

				animation = new TranslateAnimation(left, 0, 0, 0);

			} else {

				isLeft = false;

				params.setMargins(screenWidth - getWidth(), top, 0, 0);

				animation = new TranslateAnimation(-(screenWidth - left - getWidth()), 0, 0, 0);

			}

			setLayoutParams(params);

			animation.setDuration(500);

			startAnimation(animation);

			break;

		}

		return true;
	}

	// 针对button处理布局的显示或者隐藏
	private void handlerLayout() {

		if (menuLayout.getParent() == null) {

			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);

			if (isLeft) {
				btn_finish_right.setVisibility(View.VISIBLE);

				layoutParams.setMargins((ismovehide ? getWidth() : 0), getTop(), 0, 0);

				animation_layout = new TranslateAnimation(0 - screenWidth, 0, 0, 0);

			} else {

				btn_finish_left.setVisibility(View.VISIBLE);

				layoutParams.setMargins(0, getTop(), (ismovehide ? getWidth() : 0), 0);

				animation_layout = new TranslateAnimation(screenWidth, 0, 0, 0);

			}

			ismovehide = false;

			rootLaytout.addView(menuLayout, layoutParams);

			animation_layout.setDuration(300);
			menuLayout.startAnimation(animation_layout);

		} else {

			if (isLeft) {

				animation_hide = new TranslateAnimation(0, 0 - menuLayout.getWidth(), 0, 0);

				animation_hide.setDuration(300);
				animation_hide.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation arg0) {

						btn_finish_left.setVisibility(View.INVISIBLE);
						btn_finish_right.setVisibility(View.INVISIBLE);

						rootLaytout.removeView(menuLayout);

					}
				});

			} else {

				animation_hide = new TranslateAnimation(0, menuLayout.getWidth(), 0, 0);

				animation_hide.setDuration(300);
				animation_hide.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation arg0) {

						btn_finish_left.setVisibility(View.INVISIBLE);
						btn_finish_right.setVisibility(View.INVISIBLE);

						rootLaytout.removeView(menuLayout);

					}
				});

			}

			menuLayout.startAnimation(animation_hide);

		}
	}

	// 隐藏布局文件
	private void hideLayout() {

		if (menuLayout.getParent() != null) {

			ismovehide = true;

			rootLaytout.removeView(menuLayout);

			btn_finish_left.setVisibility(View.GONE);
			btn_finish_right.setVisibility(View.GONE);
		}

	}

	/**
	 * 发送命令给服务端
	 * 
	 * @param
	 */
	private void Send(final String command) {
		UdpManager.getInstance().sendCCommand(command);
	}

}
