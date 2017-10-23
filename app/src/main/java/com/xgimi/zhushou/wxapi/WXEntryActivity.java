
package com.xgimi.zhushou.wxapi;

import android.util.Log;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.weixin.view.WXCallbackActivity;


public class WXEntryActivity extends WXCallbackActivity implements SnsPostListener {

	@Override
	public void onComplete(SHARE_MEDIA arg0, int arg1, SocializeEntity arg2) {
		// TODO Auto-generated method stub
		Log.e("info",arg1+"");
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	
}
