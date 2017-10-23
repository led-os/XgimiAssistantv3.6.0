package com.xgimi.device.util;

import com.xgimi.device.device.GMDeviceController;

import org.json.JSONException;
import org.json.JSONObject;


public class GMInfiniteController {
	public static GMInfiniteController infiniteController;
	public static GMInfiniteController getInstance(){
		if(infiniteController==null){
			infiniteController=new GMInfiniteController();
		}
		return infiniteController;
	}
	private GMInfiniteController(){
		
	}
	
	public void sendInfiniteController(int value){
		GMDeviceController.getInstance().SendJC(InfiniteController(value));
	}
	
	//发送命令
	private String InfiniteController(int value){
		JSONObject jsonObject = new JSONObject();
		
		JSONObject js2 = new JSONObject();
		JSONObject js4=new JSONObject();
		
		try {
//			jsonObject.put("mode", js2);
			js2.put("data", jsonObject);
			jsonObject.put("zoomfocus", js4);
			jsonObject.put("type", 1);
			js4.put("scale", null);
			js4.put("value", value);
			js4.put("vvalue", null);
			js4.put("hvalue", null);
			js2.put("action", 2);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(js2.toString());
		return js2.toString();
	}
}
