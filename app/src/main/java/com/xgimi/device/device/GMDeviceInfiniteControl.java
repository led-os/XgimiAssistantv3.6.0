package com.xgimi.device.device;

public class GMDeviceInfiniteControl {
	public static GMDeviceInfiniteControl infiniteController;
	public static GMDeviceInfiniteControl getInstance(){
		if(infiniteController==null){
			infiniteController=new GMDeviceInfiniteControl();
		}
		return infiniteController;
	}
	private GMDeviceInfiniteControl(){
		
	}
}
