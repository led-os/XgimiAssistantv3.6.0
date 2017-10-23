package com.xgimi.zhushou.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgimi.device.utils.FileUtils;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.GimiUser;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.DeviceUtils;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.widget.SignOutDilog;

public class SettingActivity extends BaseActivity implements OnClickListener ,OnTouchListener{

	private Intent intent;
	private SignOutDilog dilog;
	private TextView tv_size;
	public App app;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 0:
				tv_size.setText(clearImageLoaderCache() / 1024 / 1024 + "M");
				MissDilog();
				break;
			}
		}
	};
	private ImageView back;
	private ImageView iv_remount;
	private Button tuichu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initView();
	}

	public  void install(Context context, String filePath) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + filePath),
				"application/vnd.android.package-archive");
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}

	private void initView() {
		app = (App) getApplicationContext();
       iv_remount = (ImageView) findViewById(R.id.title)
				.findViewById(R.id.iv_remount);
		setYaokongBackground(iv_remount, this, "qita");
		if (Constant.netStatus) {
			iv_remount.setImageResource(R.drawable.yaokongqi);
		} else {
			iv_remount.setImageResource(R.drawable.gimi_yaokong);
		}
		back = (ImageView) findViewById(R.id.title).findViewById(R.id.back);
		back(back);
		back.setOnTouchListener(this);
		iv_remount.setOnTouchListener(this);
		TextView title = (TextView) findViewById(R.id.tv_titile);
		title.setText("设置");
		TextView gerenxinxi = (TextView) findViewById(R.id.gerenxinxi)
				.findViewById(R.id.tv);

		gerenxinxi.setText("个人信息");
		View two = findViewById(R.id.yaokongset).findViewById(R.id.view);
		View thre = findViewById(R.id.guanyu).findViewById(R.id.view);
		two.setVisibility(View.GONE);
		thre.setVisibility(View.GONE);

		TextView information = (TextView) findViewById(R.id.gerenxinxi)
				.findViewById(R.id.detail);
		GimiUser loginInfo = app.getLoginInfo();

		TextView remoteSet = (TextView) findViewById(R.id.yaokongset)
				.findViewById(R.id.tv);
		remoteSet.setText("遥控器设置");
		TextView yaokoninfor = (TextView) findViewById(R.id.yaokongset)
				.findViewById(R.id.detail);
		yaokoninfor.setVisibility(View.GONE);

		TextView huancun = (TextView) findViewById(R.id.cleanhuancun)
				.findViewById(R.id.tv);
		huancun.setText("清除应用缓存");
		tv_size = (TextView) findViewById(R.id.cleanhuancun).findViewById(
				R.id.detail);
		if(clearImageLoaderCache()/1024/1024>50){
			ImageLoader.getInstance().clearDiskCache();
			ImageLoader.getInstance().clearMemoryCache();
			tv_size.setText("0"+"M");
		}
		tv_size.setText(clearImageLoaderCache() / 1024 / 1024 + "M");
		huancun.setOnClickListener(this);

		TextView pingfen = (TextView) findViewById(R.id.pingfen).findViewById(
				R.id.tv);
		pingfen.setText("评分");
		TextView pingfeninfor = (TextView) findViewById(R.id.pingfen)
				.findViewById(R.id.detail);
		pingfeninfor.setVisibility(View.GONE);
		TextView guanyu = (TextView) findViewById(R.id.guanyu).findViewById(
				R.id.tv);
		guanyu.setText("关于");
		TextView guanyuinfor = (TextView) findViewById(R.id.guanyu)
				.findViewById(R.id.detail);
		guanyuinfor.setVisibility(View.GONE);
		tuichu = (Button) findViewById(R.id.tuichu);
		tuichu.setOnClickListener(this);
		tuichu.setOnTouchListener(this);
		View xinxi = findViewById(R.id.gerenxinxi);
		View ykset = findViewById(R.id.yaokongset);
		View clean = findViewById(R.id.cleanhuancun);
		View pf = findViewById(R.id.pingfen);
		View yu = findViewById(R.id.guanyu);
		xinxi.setOnClickListener(this);
		ykset.setOnClickListener(this);
		clean.setOnClickListener(this);
		pf.setOnClickListener(this);
		yu.setOnClickListener(this);
		if (loginInfo == null) {
			information.setText("未登录");
			tuichu.setVisibility(View.GONE);
		} else {
			information.setText(loginInfo.data.username);
		}


		
		
		TextView banben = (TextView) findViewById(R.id.banben);
		banben.setText("V " + DeviceUtils.getappVersion(this));
		LinearLayout jiaoliu= (LinearLayout) findViewById(R.id.jiaoliu);
		jiaoliu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Util.joinQQGroup(SettingActivity.this);
				Intent intent=new Intent(SettingActivity.this,WebHtmlIQiYiActivity.class);
				startActivity(intent);
			}
		});
		
	}

	private void initData() {

	}
	/****************
	 *
	 * 发起添加群流程。群号：无屏助手问题反馈群(580877255) 的 key 为： lklLAPFyDe-E0tVixLQ_RuoH4UbYdcHx
	 * 调用 joinQQGroup(lklLAPFyDe-E0tVixLQ_RuoH4UbYdcHx) 即可发起手Q客户端申请加群 无屏助手问题反馈群(580877255)
	 *
	 * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
	 ******************/
//	public static  boolean joinQQGroup(Context context) {
//		Intent intent = new Intent();
//		intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + "lklLAPFyDe-E0tVixLQ_RuoH4UbYdcHx"));
//		// 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//		try {
//			context.startActivity(intent);
//			return true;
//		} catch (Exception e) {
//			// 未安装手Q或安装的版本不支持
//			return false;
//		}
//	}
	 
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tuichu:
			if (dilog == null)
				dilog = new SignOutDilog(this, "确认退出?", app);
			dilog.show();

			break;
		case R.id.gerenxinxi:
			if (app.getLoginInfo() == null) {
				intent = new Intent(this, LogoActivity.class);
			} else {
				intent = new Intent(this, UserInfoActivity.class);
			}
			startActivity(intent);
			break;
		case R.id.yaokongset:
			intent = new Intent(this, RemountSetActivity.class);
			startActivity(intent);
			break;
		case R.id.cleanhuancun:
			showDilog("清除中，请稍后...");
			ToosUtil.getInstance().addEventUmeng(SettingActivity.this,"event_clear_cache");
			handler.sendEmptyMessageDelayed(0, 600);
			ImageLoader.getInstance().clearDiskCache();
			ImageLoader.getInstance().clearMemoryCache();
//			MobclickAgent.onEvent(this, "clean_huancun");

			break;
		case R.id.pingfen:
			ComponentName toActivity = new ComponentName("com.hpplay.happycast","com.hpplay.happycast.ActMain");
        	Intent intent2 = new Intent();
        	intent2.setComponent(toActivity);
        	intent2.setAction("android.intent.action.VIEW"); 
            startActivity(intent2);
			break;
		case R.id.guanyu:
			intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
			break;
		}
	}

	/**
	 * 检查缓存大小，超过50M就删除
	 */
	public Long clearImageLoaderCache() {
		long size = FileUtils.getDirSize(getDir(App.CachePath, 0)
				.getAbsoluteFile());
		return size;
		// if (size > (1024 * 1024 * 50)) {
		// ImageLoader.getInstance().clearDiskCache();
		// ImageLoader.getInstance().clearMemoryCache();
		// }
	}

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
		case R.id.tuichu:
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				tuichu.setAlpha(0.6f);
				break;
			case MotionEvent.ACTION_UP:
				tuichu.setAlpha(1.0f);
				break;
			}
			break;
		}
		return false;
	}

}
