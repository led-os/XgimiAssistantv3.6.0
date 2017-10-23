package com.xgimi.device.util;


import com.xgimi.device.device.GMDeviceController;

public class GMInputController {
	private static GMInputController threedController;
	public static GMInputController getInstance(){
		if(threedController==null){
			threedController=new GMInputController();
		}
		return threedController;
	}
	private GMInputController(){
	}
	public void sendInput(String s){
		GMDeviceController.getInstance().SendJC("inputstring:" + s);
	}
}
