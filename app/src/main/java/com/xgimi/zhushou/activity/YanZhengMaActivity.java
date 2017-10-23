package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.ApplyTitleDanLi;
import com.xgimi.zhushou.bean.CodeMsg;
import com.xgimi.zhushou.bean.GimiUser;
import com.xgimi.zhushou.inter.CommonCallBack;
import com.xgimi.zhushou.netUtil.HttpRequest;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.StringUtils;

import java.util.Timer;
import java.util.TimerTask;

public class YanZhengMaActivity extends BaseActivity implements OnClickListener {

	private Button time;
	private EditText et;
	private Button next;
	private int a = 60;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yan_zheng_ma);
		initView();
		initData();
	}

	private void initView() {
		ImageView iv_remount = (ImageView) findViewById(R.id.title)
				.findViewById(R.id.iv_remount);
		setYaokongBackground(iv_remount, this, "qita");
		if (Constant.netStatus) {
			iv_remount.setImageResource(R.drawable.yaokongqi);
		} else {
			iv_remount.setImageResource(R.drawable.gimi_yaokong);
		}
		ImageView back = (ImageView) findViewById(R.id.title).findViewById(R.id.back);
		back(back);
		TextView title = (TextView) findViewById(R.id.tv_titile);
		title.setText("输入验证码");
		time = (Button) findViewById(R.id.button);
		et = (EditText) findViewById(R.id.name);
		next = (Button) findViewById(R.id.zhuce);
		time.setText("请" + a + "秒后重发");
		statrTimer();

	}

	private void initData() {
		next.setOnClickListener(this);
		time.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopTimer();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.zhuce:
			showDilog("验证中,请稍后...");
			if (StringUtils.isEmpty(et.getText().toString().trim())) {
				Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
				return;
			}
			zhuce(ApplyTitleDanLi.getInstance().phoneNumber, et.getText()
					.toString().trim());
			break;
		case R.id.button:
			getYanZhengMa(ApplyTitleDanLi.getInstance().phoneNumber);
			statrTimer();
			break;
		default:
			break;
		}
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Intent intetn = new Intent(YanZhengMaActivity.this,
						ConfirActivity.class);
				startActivity(intetn);
				finish();
				break;
			}
		}
	};

	public void zhuce(String tel, String yanzhengma) {
		HttpRequest.getInstance(this).getUserZhuCe(this, tel, yanzhengma,
				new CommonCallBack<GimiUser>() {

					@Override
					public void onSuccess(GimiUser data) {
						// TODO Auto-generated method stub
						MissDilog();
						if (data.code == 200) {
							mHandler.sendEmptyMessage(0);
						} else if (data.code == 500) {
							Toast.makeText(YanZhengMaActivity.this,
									data.message, Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onStart() {
					}

					@Override
					public void onFailed(String reason) {
						// TODO Auto-generated method stub
						MissDilog();
//						Toast.makeText(YanZhengMaActivity.this, reason, Toast.LENGTH_SHORT)
//								.show();

					}
				});
	}

	private Timer timer;
	private TimerTask task;

	public void statrTimer() {
		if (timer == null)
			timer = new Timer();
		time.setClickable(false);

		if (task == null) {
			task = new TimerTask() {
				@Override
				public void run() {
					handler.sendEmptyMessage(0);
				}
			};
			if (timer != null && task != null) {
				timer.schedule(task, 0, 1000);
			}
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				a--;
				time.setText("请" + a + "秒后重发");
				if (a <=0) {
					stopTimer();
					a = 60;
					time.setText("重新发送");
					time.setClickable(true);
				}
				break;
			default:
				break;
			}
		}
	};

	public void stopTimer() {
		time.setClickable(true);
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (task != null) {
			task.cancel();
			task = null;
		}
	}

	public void getYanZhengMa(final String tel) {
		HttpRequest.getInstance(this).getZhuCeYanZhengMa(tel,
				new CommonCallBack<CodeMsg>() {

					@Override
					public void onSuccess(CodeMsg data) {
						// TODO Auto-generated method stub
						MissDilog();
						if (data.code == 500) {
							Toast.makeText(YanZhengMaActivity.this,
									data.message, Toast.LENGTH_SHORT).show();
						}else if(data.code==200){
							Toast.makeText(YanZhengMaActivity.this,
									"验证码已发送", Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onStart() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFailed(String reason) {
						// TODO Auto-generated method stub
						MissDilog();
						Toast.makeText(YanZhengMaActivity.this,
								"服务器出错,请重新发送", Toast.LENGTH_SHORT).show();

					}
				});
	}
}
