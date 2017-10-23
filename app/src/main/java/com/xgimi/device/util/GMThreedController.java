package com.xgimi.device.util;

import com.xgimi.device.device.GMDeviceController;

import org.json.JSONException;
import org.json.JSONObject;



public class GMThreedController {

	public static final int GMThreeDModeNone =0;//取消3D
	public static final int GMThreeDModeBlueLight  =1;//蓝光3D
	public static final int GMThreeDModeUpDown  =2;//上下3D
	public static final int GMThreeDModeLeftRight  =3;//左右3D
	public static final int GMThreeDModeAuto  =4;//自动3D
	public static final int GMThreeDMode2DTo3D  =5;//2D转3D
	public static final int GMThreeDModeUpDown3DTo2D  =6;//上下3D转2D
	public static final int GMThreeDModeLeftRight3DTo2D  =7;//左右3D转2D
	
	
	
	private static GMThreedController threedController;
	public static GMThreedController getInstance(){
		if(threedController==null){
			threedController=new GMThreedController();
		}
		return threedController;
	}
	private GMThreedController(){
	}
	public void send3DController(int mode){
		GMDeviceController.getInstance().SendJC(send3D(mode));
	}
	// 发送命令
	private String send3D(int mode) {
		JSONObject jsonObject = new JSONObject();
		JSONObject js2 = new JSONObject();
		try {
			jsonObject.put("mode", mode);
			js2.put("data", jsonObject);
			js2.put("action", 3);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(js2.toString());
		return js2.toString();
	}
	
}
