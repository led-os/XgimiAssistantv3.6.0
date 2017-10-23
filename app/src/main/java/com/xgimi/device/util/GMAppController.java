package com.xgimi.device.util;


import com.xgimi.device.device.GMDeviceController;

public class GMAppController {
	private static GMAppController threedController;
	private static final String cleanMeory="CLEARMEMORY";
	public static GMAppController getInstance(){
		if(threedController==null){
			threedController=new GMAppController();
		}
		return threedController;
	}
	private GMAppController(){
	}
	public void openApp(String pacageName){
		GMDeviceController.getInstance().senddOpen(pacageName);
	}
	public void uninstallApp(String pacageName){
		GMDeviceController.getInstance().sendXieZai(pacageName);
	}
	public void cleanAppMemory(){
		GMDeviceController.getInstance().SendJC(cleanMeory);
	}
	private void installApp(String url,String title,String packageName,String iconUrl){
		GMDeviceController.getInstance().SendJC(JsonData.getInstance().sendJson(url, title, packageName, iconUrl));
	}
	public void openFlyme(String url){
		GMDeviceController.getInstance().SendJC(JsonData.getInstance().getFlymeData(url, GMSdkCheck.appId));
	}
	public void installFlyme(){
		installApp("http://lifekit-apk.b0.upaiyun.com/apk/Videolifekit.apk", "flyme" + ".apk",
				"com.meizu.media.video", null);
	}

	public void installFilFly(String url,String packageName){
		GMDeviceController.getInstance().SendJC(JsonData.getInstance().sendJson(url,"filefly"+".apk",packageName,null));
	}
	
}
