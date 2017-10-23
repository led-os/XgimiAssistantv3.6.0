package com.xgimi.zhushou.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.util.Util;

public class TiShiDilog implements View.OnClickListener{
	public Dialog mDialog;
	private Context mContxt;
	
	public TiShiDilog(Context context, String message) {
		
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.tishidilog, null);
		mContxt=context;
		Button suggess= (Button) view.findViewById(R.id.quxiao);
		Button sure= (Button) view.findViewById(R.id.queren);
		suggess.setOnClickListener(this);
		sure.setOnClickListener(this);
//		ImageView loadingImage = (ImageView) view.findViewById(R.id.progress_view);
//		loadingImage.setImageResource(R.anim.loading_animation);
//		animationDrawable = (AnimationDrawable)loadingImage.getDrawable();
//        if(animationDrawable!=null){
//            animationDrawable.setOneShot(false);
//            animationDrawable.start();
//        }
		mDialog = new Dialog(context, R.style.dialog);
		mDialog.setContentView(view);
		mDialog.setCanceledOnTouchOutside(false);
		
	}
	
	public void show() {
		mDialog.show();
	}
	
	public void setCanceledOnTouchOutside(boolean cancel) {
		mDialog.setCanceledOnTouchOutside(cancel);
	}
	
	public void dismiss() {
		if(mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}
    public boolean isShowing(){
        if(mDialog.isShowing()) {
            return true;
        }
        return false;
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.quxiao:
				Util.joinQQGroup(mContxt);
				dismiss();
				break;
			case R.id.queren:
				dismiss();
				break;
		}
	}
}
