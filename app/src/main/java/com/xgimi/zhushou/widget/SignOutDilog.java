package com.xgimi.zhushou.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;

/**
 * 退出登陆对话框
 */

public class SignOutDilog implements  OnClickListener{
	public Dialog mDialog;
	private Activity mContext;
	private int position;
    App app;
	public SignOutDilog(Activity context, String message, App app) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.tuichu_dilog, null);
		mDialog = new Dialog(context, R.style.dialog);
		mContext = context;
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(true);
		initView(view,message);
		this.app=app;
		mContext=context;
	}
	private Context con;
	public SignOutDilog(Context context, String message) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.tuichu_dilog, null);
		mDialog = new Dialog(context, R.style.dialog);
		con = context;
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(true);
		initView(view,message);
		con=context;
	}


	private void initView(View view,String message) {
		Button queren = (Button) view.findViewById(R.id.queren);
		Button quxiao = (Button) view.findViewById(R.id.quxiao);
		tishi = (TextView) view.findViewById(R.id.tishi);
		tishi.setText(message);
		queren.setOnClickListener(this);
		quxiao.setOnClickListener(this);
		
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
			// animationDrawable.stop();
		}
	}

	public boolean isShowing() {
		if (mDialog.isShowing()) {
			return true;
		}
		return false;
	}

	int index;
	private TextView tishi;
	private onListern onlistern;
	public void setOnLisener(onListern onliListern){
		this.onlistern=onliListern;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.queren:
			if(tishi.getText().toString().equals("确认退出?")){
			app.clianLogoInfor();
			dismiss();
			mContext.finish();
			}
			else if(tishi.getText().toString().endsWith("确定删除记录么?")){
				if(onlistern!=null)
					onlistern.send();
					dismiss();
			}
			else{
				if(onlistern!=null)
				onlistern.send();
				dismiss();
			}
			
			
			break;
		case R.id.quxiao:
			dismiss();
			break;
		}
	}
	
	public interface onListern{
		void send();
	}
}
