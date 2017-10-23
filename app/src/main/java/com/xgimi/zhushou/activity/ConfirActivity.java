package com.xgimi.zhushou.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.CodeMsg;
import com.xgimi.zhushou.inter.CommonCallBack;
import com.xgimi.zhushou.netUtil.HttpRequest;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.StringUtils;

public class ConfirActivity extends BaseActivity implements OnClickListener{


	private EditText et;
	private Button next;
	private EditText et_one;
	private EditText et_two;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confir);
		initView();
		initData();
	}
	private void initView(){
		ImageView iv_remount = (ImageView) findViewById(R.id.title).findViewById(R.id.iv_remount);
		setYaokongBackground(iv_remount,this,"qita");
		if(Constant.netStatus){
			iv_remount.setImageResource(R.drawable.yaokongqi);
		}else{
			iv_remount.setImageResource(R.drawable.gimi_yaokong);
		}
		ImageView back = (ImageView) findViewById(R.id.title).findViewById(R.id.back);
		back(back);
		TextView title = (TextView) findViewById(R.id.tv_titile);
		title.setText("输入密码");
		et = (EditText) findViewById(R.id.name);
		next = (Button) findViewById(R.id.zhuce);
		et_one = (EditText) findViewById(R.id.name_one);
		et_two = (EditText) findViewById(R.id.name_two);
	}
	private void initData(){
		next.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.zhuce:
			String pass_et = et.getText().toString().trim();
			String pass_one=et_one.getText().toString().trim();
			String pass_two=et_two.getText().toString().trim();
			if(StringUtils.isEmpty(pass_et)||StringUtils.isEmpty(pass_one)){
				return;
			}
			if(!(pass_et.equals(pass_one))){
				return;
			}
			setUserInformation(App.getContext().getLoginInfo().data.uid, pass_two, pass_one);
			break;

		default:
			break;
		}
	}
		public void setUserInformation(String uid,String username,String password){
			HttpRequest.getInstance(this).setUserInforMation(uid, username, password, new CommonCallBack<CodeMsg>() {
				@Override
				public void onSuccess(CodeMsg data) {
					// TODO Auto-generated method stub
					MissDilog();
					if(data.code==500){
						Toast.makeText(ConfirActivity.this, data.message, Toast.LENGTH_SHORT).show();
					}else{
					
//					Intent Intent=new Intent(ConfirActivity.this,FindFragment.class);
//					startActivity(Intent);
					finish();
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
//					Toast.makeText(ConfirActivity.this, reason, Toast.LENGTH_SHORT).show();

				}
			});
		}
}
