package com.xgimi.zhushou.util;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.FindPassActivity;
import com.xgimi.zhushou.activity.PhonePasswordActivity;

/**
 * 找回密码对话框
 */
public class FindPassWord implements OnClickListener {

	public Dialog mDialog;
	private Context context;
	private Intent intent;

	public FindPassWord(Context context, String message) {
		this.context=context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.findpass, null);
		mDialog = new Dialog(context, R.style.dialog);
		TextView youxiang = (TextView) view.findViewById(R.id.youxiang);
		TextView shoujihao = (TextView) view.findViewById(R.id.shoujihao);
		TextView quxiao = (TextView) view.findViewById(R.id.quxiao);
		youxiang.setOnClickListener(this);
		shoujihao.setOnClickListener(this);
		quxiao.setOnClickListener(this);

		 Window window = mDialog.getWindow();  
		    window.setGravity(Gravity.BOTTOM); 
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(true);
	}

	public void show() {
		mDialog.show();
	}

	public void setCanceledOnTouchOutside(boolean cancel) {
		mDialog.setCanceledOnTouchOutside(cancel);
	}

	public void dismiss() {
		if (mDialog.isShowing()) {
			mDialog.dismiss();
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
		case R.id.youxiang:
			intent = new Intent(context,FindPassActivity.class);
			context.startActivity(intent);
			dismiss();
			break;
		case R.id.shoujihao:
			 intent=new Intent(context,PhonePasswordActivity.class);
			 context.startActivity(intent);
			  dismiss();
			break;
		case R.id.quxiao:
			dismiss();
			break;

		default:
			break;
		}
	}
}
