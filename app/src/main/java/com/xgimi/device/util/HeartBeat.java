package com.xgimi.device.util;

import android.os.Handler;

import com.xgimi.device.callback.GMDeviceHeartLisener;
import com.xgimi.device.callback.HeartBeatLisener;
import com.xgimi.device.callback.HeartBit;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.utils.Consants;
import com.xgimi.device.vcontrolcmd.VcontrolControl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class HeartBeat implements GMDeviceHeartLisener {

	public static HeartBeat heart;
	private int cishu=0;
	public boolean isSuppot;
	public boolean isReceiveHeatBeat=true;
	
	public HeartBit heartbit;
	private HeartBeatLisener healisener;
	public void setHeartBeatLisener(HeartBeatLisener lisener){
		this.healisener=lisener;
	}
	public static HeartBeat getInstance(){
		if(heart==null){
			heart=new HeartBeat();
		}
		return heart;
	}
	private HeartBeat(){
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
					if(Consants.constatus){
						VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(10002,"2","2")));
					}
				}
			};
			
			if(timer!=null&&task!=null){
				timer.schedule(task, 0, 2000);
			}
			
		}
	}
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				break;

			default:
				break;
			}
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
			jsonObject.put("action", 10002);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject.toString();
	}
	long lastTime;
	@Override
	public void deviceLost(HeartBit bean) {
		// TODO Auto-generated method stub
		isSuppot=true;
		heartbit=bean;
		cishu=0;
		isReceiveHeatBeat=true;
		lastTime =System.currentTimeMillis();
		
	}
}
