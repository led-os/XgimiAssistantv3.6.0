package com.xgimi.zhushou.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ffmpegav.BaseUIActivity;
import com.xgimi.device.socket.UdpManager;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.util.GravitySense;
import com.xgimi.zhushou.util.MultitouchView;
import com.xgimi.zhushou.util.RemoteControlListener;
import com.xgimi.zhushou.widget.DraftButton;
import com.xgimi.zhushou.yiping.MyGravityListener;
/*import com.example.ffmpegav.BaseUIActivity;
import com.xgimi.app.MyApp;
import com.xgimi.sokect.UdpManager;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.widget.DraftButton;*/

/**
 * 同屏activity
 * 
 * @author liuyang
 *
 */
public class YiPingActivity extends BaseUIActivity {

	public static final int OPENGRAVITY = 1990;
	public static final int CLOSEGRAVITY = 1991;
	public static final int FINSHACTIVITY = 1992;

	private App myApp;

	private RelativeLayout rootLaytout;

	private String play_path = "";

	private MultitouchView controlView; // 手势监听器

	private int dw;

	private int dh;

	private String conenct_IP;

	private DraftButton btn_menu; // 可拖动的控制菜单

	public GravitySense gravitySense = null; // 重力感应

	// 用于控制屏幕的亮度
	private PowerManager.WakeLock wakelock;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Toast.makeText(this, "同屏最佳环境:\n手机和路由器在同一个区域无墙的阻隔\n路由器上无多个设备同时进行耗资源的操作", Toast.LENGTH_LONG).show();
		myApp = (App) getApplicationContext();

		dw = getWindowManager().getDefaultDisplay().getWidth();
		dh = getWindowManager().getDefaultDisplay().getHeight();

		rootLaytout = new RelativeLayout(this);

		setContentView(rootLaytout);

		play_path = getIntent().getStringExtra("path");

		conenct_IP = getIntent().getStringExtra("ip");

		UdpManager.getInstance().initYipingUdp(conenct_IP);

		initView();

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakelock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, this.getClass().getCanonicalName());

	}

	private void initView() {
		// 播放器
		glView = new GLsurfaceViewActivity(this, play_path, true);

		glView.setVisibility(View.VISIBLE);
		glView.getHolder().setFormat(PixelFormat.TRANSPARENT);

		// glView.setZOrderOnTop(true);
		glView.getHolder().setKeepScreenOn(true);

		rootLaytout.addView(glView);

		// 手势控制
		controlView = new MultitouchView(this, dw, dh);

		controlView.setOnMultitouchListener(new RemoteControlListener(conenct_IP));

		rootLaytout.addView(controlView);

		// 菜单按钮
		btn_menu = new DraftButton(this, handler, rootLaytout);
		btn_menu.setBackgroundResource(R.drawable.yiping_draftbutton_selector);
//		btn_menu.setBackgroundResource(R.drawable.R.drawable.yiping_draftbutton_selector);

		rootLaytout.addView(btn_menu);

		View view = new View(this);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		view.setLayoutParams(layoutParams);
		view.setBackgroundColor(Color.BLACK);
		rootLaytout.addView(view);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//MobclickAgent.onEvent(YiPingActivity.this, "tongping"); // 自定义统计时间
		wakelock.acquire();
		/*
		 * 启动重力感应
		 */
		if (myApp.getGravitySenseStatus() != 0) {
			openGravitySense(YiPingActivity.this, conenct_IP);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		wakelock.release();
		closeGravitySense();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// stopPlayer();
//		android.os.Process.killProcess(android.os.Process.myPid());
	}

	/**
	 * 打开游戏时重力感应
	 */
	public void openGravitySense(Context context, String ip) {

		gravitySense = new GravitySense(context);
		gravitySense.setGravitySensorListener(new MyGravityListener(ip));
	}

	/**
	 * 关闭打开游戏时重力感应
	 */
	public void closeGravitySense() {
		if (gravitySense != null) {
			gravitySense.unRegisterSensorListener();
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case OPENGRAVITY:
				openGravitySense(YiPingActivity.this, conenct_IP);
				break;
			case CLOSEGRAVITY:
				closeGravitySense();
				break;
			case FINSHACTIVITY:
				finish();
				break;
			}
		}
	};
}
