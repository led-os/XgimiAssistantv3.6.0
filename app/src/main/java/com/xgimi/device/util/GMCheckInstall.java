package com.xgimi.device.util;

import com.xgimi.device.device.GMDeviceController;

import org.json.JSONException;
import org.json.JSONObject;


public class GMCheckInstall {
	private static GMCheckInstall threedController;
	public static GMCheckInstall getInstance(){
		if(threedController==null){
			threedController=new GMCheckInstall();
		}
		return threedController;
	}
	private GMCheckInstall(){
	}
	public void checkInstalled(String packagename){
		GMDeviceController.getInstance().SendJC(sendJson(packagename));

	}
	public String sendJson(String packagename){
		// 发送命令
		JSONObject jsonObject = new JSONObject();
			JSONObject js2 = new JSONObject();
			try {
				jsonObject.put("packageName", packagename);
				js2.put("data", jsonObject);
				js2.put("action", 9);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(js2.toString());
			return js2.toString();
	}
}
