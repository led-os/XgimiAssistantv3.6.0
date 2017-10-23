package com.xgimi.device.util;


import com.xgimi.device.device.GMDeviceController;

public class GMGetSerialNumber {
	private static GMGetSerialNumber threedController;
	public static GMGetSerialNumber getInstance(){
		if(threedController==null){
			threedController=new GMGetSerialNumber();
		}
		return threedController;
	}
	
	public void getSerialNumber(){
		GMDeviceController.getInstance().SendJC(JsonData.getInstance().sendMac());
	}
	
}
