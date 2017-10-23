package com.xgimi.zhushou.bean;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.xgimi.device.callback.GMDeviceHeartLisener;
import com.xgimi.device.callback.HeartBit;
import com.xgimi.device.device.GMDeviceController;
import com.xgimi.zhushou.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class Heartbeat implements GMDeviceHeartLisener {

	public static Heartbeat heart;
	public Context context;
	private int cishu=0;
	public boolean isSuppot;
	public static Heartbeat getInstance(Context  context){
		if(heart==null){
			heart=new Heartbeat(context);
		}
		return heart;
	}
	private Heartbeat(Context context){
		this.context=context;
//		statrTimer();
	}
	Timer timer;
	TimerTask task;
	
	public void statrTimer(){
		if(timer==null)
		timer = new Timer();
		
		if(task==null){
			task=new TimerTask() {
				@Override
				public void run() {
					long currentTime=System.currentTimeMillis();
					if(Constant.netStatus){
						if(currentTime-lastTime>8000&&lastTime!=0){
							Log.e("info",currentTime-lastTime+"--ssss" );
//							stopTimer();
							if(cishu>10){
							lastTime=0;
							handler.sendEmptyMessage(0);
							stopTimer();
							cishu=0;
							}else{
								GMDeviceController.getInstance().SendJC(sendHeartJson());
								Log.e("info", "fasongchaoshi");
							}
							cishu++;
							
						}else{
							GMDeviceController.getInstance().SendJC(sendHeartJson());
						}
					}
				}
			};
			
			if(timer!=null&&task!=null){
				timer.schedule(task, 0, 6000);
			}
			
		}
	}
	
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			Toast.makeText(context, "设备断开连接", Toast.LENGTH_SHORT).show();
			Constant.netStatus=false;
		}
	};
	
	public void stopTimer(){
		if(timer!=null){
			timer.cancel();
			timer=null;
		}
		if(task!=null){
			task.cancel();
			task=null;
		}
	}
	
	
	
	public String sendHeartJson(){
		JSONObject jsonObject=new JSONObject();
		JSONObject js2=new JSONObject();
		try {
			jsonObject.put("data", js2);
			jsonObject.put("action", 1000);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject.toString();
	}
	
	long lastTime;

	@Override
	public void deviceLost(HeartBit heartBit) {
		// TODO Auto-generated method stub
		ApplyTitleDanLi.getInstance().heartbean=heartBit;
		isSuppot=true;
		cishu=0;
		lastTime =System.currentTimeMillis();
		Log.e("info", "fasongchaoshi"+lastTime);
	}
}
