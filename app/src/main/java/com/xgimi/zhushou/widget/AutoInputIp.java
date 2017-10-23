package com.xgimi.zhushou.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.util.RegexUtils;

public class AutoInputIp {

	public Dialog mDialog;
	private Context mcontext;
	private Button liangjie;
	private EditText one;
	private EditText two;
	private EditText three;
	private EditText four;
	private String ip;

	public AutoInputIp(Context context) {
		mcontext = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.input, null);
		mDialog = new Dialog(context, R.style.dialog);
		mcontext = context;
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(false);
		initVIew(view);
		Window window = mDialog.getWindow();
		LayoutParams windowparams = window.getAttributes();
		window.setGravity(Gravity.BOTTOM);
		windowparams.width = LayoutParams.FILL_PARENT;
		window.setAttributes(windowparams);
		Rect rect = new Rect();
		View view1 = window.getDecorView();
		view1.getWindowVisibleDisplayFrame(rect);
		initData();
	}

	private void initVIew(View view) {
		one = (EditText) view.findViewById(R.id.one);
		two = (EditText) view.findViewById(R.id.two);
		three = (EditText) view.findViewById(R.id.three);
		four = (EditText) view.findViewById(R.id.four);
		liangjie = (Button) view.findViewById(R.id.denglu);
		four.requestFocus();
		InputMethodManager imm = (InputMethodManager) mcontext
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(four, InputMethodManager.RESULT_SHOWN);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	private void initData() {
		liangjie.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String one_text = one.getText().toString().trim();
				String two_text = two.getText().toString().trim();
				String three_text = three.getText().toString().trim();
				String four_text = four.getText().toString().trim();
				ip = one_text + "." + two_text + "." + three_text + "."
						+ four_text;
				if (RegexUtils.isIPAddress(ip)) {
					InputMethodManager inputMethodManager = (InputMethodManager) mcontext.getSystemService(Context.INPUT_METHOD_SERVICE);
					if (inputMethodManager.isActive()) {
						inputMethodManager.hideSoftInputFromWindow(
								v.getApplicationWindowToken(), 0);
					}
					
					listenr.onSend(ip);
					dismiss();
				} else {
					Toast.makeText(mcontext, "ip地址不合法", 0).show();
				}
			}
		});
	}

	public void show() {
		mDialog.show();
	}

	public void setCanceledOnTouchOutside(boolean cancel) {
		mDialog.setCanceledOnTouchOutside(cancel);
	}

	public void setLisener(onListener lis) {
		this.listenr = lis;
	}

	private onListener listenr;

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

	public interface onListener {
		void onSend(String ip);
	}

}
