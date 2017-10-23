package com.xgimi.device.util;

public class GMAuthorize {
	private static GMAuthorize threedController;
	public static GMAuthorize getInstance(){
		if(threedController==null){
			threedController=new GMAuthorize();
		}
		return threedController;
	}
	private GMAuthorize(){
	}
	public void init(String appId,String appSecret)
	{
		GMSdkCheck.appId=appId;
		GMSdkCheck.appSecret=appSecret;
		GMSdkCheck.checkMd5();
	}
}
