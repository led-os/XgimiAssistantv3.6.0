package com.xgimi.device.util;

import com.xgimi.device.device.GMDeviceController;


public class GMScreenShotController {
	
	private  String startScreenShot="SCREENSHOT";
	
	private static GMScreenShotController threedController;
	public static screenShop screenshops;
	public void setScreenShopLisener(screenShop Lisener){
		this.screenshops=Lisener;
	}
	public static GMScreenShotController getInstance(){
		if(threedController==null){
			threedController=new GMScreenShotController();
//			GMDeviceConnector.getInstance().setGmRecivceListener(new GMDeviceReceiveListener() {
//
//				@Override
//				public void receive(Message arg0) {
//					// TODO Auto-generated method stub
//					String message=(String) arg0.obj;
//					String[] strs = message.split(":", 2);
//					String image_url = "http://%s:16740%s";
//
//					if (strs.length == 2) {
//						image_url = String.format(image_url, GMDeviceStorage.getInstance().getConnectedDevice().ip,
//								strs[1]);
//						if(screenshops!=null){
//							screenshops.receiveUrl(image_url);
//						}
//					}
//				}
//			});

		}
		return threedController;
	}
	
	
	
	private GMScreenShotController(){
		
	}
	
	public void startScrrenShop(){
		 GMDeviceController.getInstance().SendJC(startScreenShot);
	}
	
	
	public interface screenShop{
		void receiveUrl(String url);
	}
	
	
}
