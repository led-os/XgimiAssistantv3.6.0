package com.xgimi.device.device;

public class GMDeviceThreedController {

	public static GMDeviceThreedController threedController;
	public static GMDeviceThreedController getInstance(){
		if(threedController==null){
			threedController=new GMDeviceThreedController();
		}
		return threedController;
	}
	private GMDeviceThreedController(){
		
	}
}
