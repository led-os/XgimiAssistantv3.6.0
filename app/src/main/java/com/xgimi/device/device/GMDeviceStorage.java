package com.xgimi.device.device;


public class GMDeviceStorage {
	 public GMDevice gmdevice;

	public static GMDeviceStorage gmdevicestorage;
	public static GMDeviceStorage getInstance(){
		if(gmdevicestorage==null){
			gmdevicestorage=new GMDeviceStorage();
		}
		return gmdevicestorage;
	}
	public GMDevice getConnectedDevice() {
		return gmdevice;
	}
}
