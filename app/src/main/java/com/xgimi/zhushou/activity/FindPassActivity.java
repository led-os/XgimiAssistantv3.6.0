package com.xgimi.zhushou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xgimi.device.utils.StringUtils;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.netUtil.HttpRequest;

public class FindPassActivity extends BaseActivity implements OnTouchListener{

	private EditText et;
	public static final int FORGETPWDSENDOK = 0; // 找回密码邮件发送成功
	public static final int FORGETPWDSENDFAIL = 1; // 找回密码邮件发送失败
	
	
	// 网络请求的消息处理
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {

			case FORGETPWDSENDOK:

				Toast.makeText(FindPassActivity.this,
						R.string.forgetpwd_sendemailok, Toast.LENGTH_SHORT).show();

				finish();

				break;

			case FORGETPWDSENDFAIL:

				String tip = (String) msg.obj;

				if (StringUtils.isEmpty(tip)) {
					Toast.makeText(FindPassActivity.this,
							R.string.login_forgetpwd_error, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(FindPassActivity.this, tip, Toast.LENGTH_SHORT).show();
				}

				break;

			}
		};
	};
	private ImageView back;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_pass);
		initView();
		initData();
	}
	private void initView(){
		back = (ImageView) findViewById(R.id.iv_user);
		back(back);
		back.setOnTouchListener(this);
		TextView title = (TextView) findViewById(R.id.tv_titile);
		title.setText("密码找回");
	}
	private void initData(){
		et = (EditText) findViewById(R.id.forgetpwd_text);
		Button send = (Button) findViewById(R.id.send);
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String email = et.getText().toString();

				if (StringUtils.isEmail(email)) {
					HttpRequest.getInstance(FindPassActivity.this).ForgetPwd(FindPassActivity.this, handler, email);
				} else {
					Toast.makeText(FindPassActivity.this,
							R.string.forgetpwd_emailerror, Toast.LENGTH_SHORT).show();

				}
			}
		});
		
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
		}
		return false;
	}

}
