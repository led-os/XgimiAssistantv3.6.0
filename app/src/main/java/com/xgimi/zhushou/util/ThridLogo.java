package com.xgimi.zhushou.util;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.GimiUser;
import com.xgimi.zhushou.inter.CommonCallBack;
import com.xgimi.zhushou.netUtil.HttpRequest;

public class ThridLogo implements OnClickListener{
	public Dialog mDialog;
	private Context mcontext;
	private EditText et;
	private String type,openid,unid,username;

	public ThridLogo(Context context, String type, String opemid, String unid, String userName) {
		mcontext = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.thridlogo, null);
		mDialog = new Dialog(context, R.style.dialog);
		mDialog.setContentView(view);
		this.type=type;
		this.openid=opemid;
		this.unid=unid;
		this.username=userName;
		mDialog.setCanceledOnTouchOutside(true);
		initView(view);
	}

	private void initView(View view) {
		et = (EditText) view.findViewById(R.id.et_search);
		Button sure = (Button) view.findViewById(R.id.send);
		Button cancel = (Button) view.findViewById(R.id.quxxiaofasong);
		sure.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}

	public void show() {
		mDialog.show();
	}

	public void setCanceledOnTouchOutside(boolean cancel) {
		mDialog.setCanceledOnTouchOutside(cancel);
	}
private RegistLisener registLiener;
	public void setLisener(RegistLisener liserner){
		this.registLiener=liserner;
	}
	
	public void dismiss() {
		if (mDialog.isShowing()) {
			mDialog.dismiss();
			// animationDrawable.stop();
		}
	}

	public boolean isShowing() {
		if (mDialog.isShowing()) {
			return true;
		}
		return false;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.send:
			String pass=et.getText().toString().trim();
			if(TextUtils.isEmpty(pass)){
				Toast.makeText(mcontext, "请输入密码", Toast.LENGTH_SHORT).show();
				return ;
			}
		HttpRequest.getInstance(mcontext).thridBangDing(type, openid, unid, username, pass, new CommonCallBack<GimiUser>() {
			
			@Override
			public void onSuccess(GimiUser data) {
				// TODO Auto-generated method stub
				if(data!=null){
				registLiener.succes();
				}
			}
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				registLiener.onstart();
			}
			@Override
			public void onFailed(String reason) {
				// TODO Auto-generated method stub
				registLiener.fail(reason);
			}
		});
		break;
case R.id.quxxiaofasong:
		dismiss();
		break;
		}
	}
	public interface RegistLisener{
		void onstart();
		void succes();
		void fail(String reason);
	}
}
