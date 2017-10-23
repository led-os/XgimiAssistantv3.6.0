package com.xgimi.device.util;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonData {

	private static JsonData threedController;
	private static final String cleanMeory="CLEARMEMORY";
	public static JsonData getInstance(){
		if(threedController==null){
			threedController=new JsonData();
		}
		return threedController;
	}
	private JsonData(){
	}
	/**
	 * 打开flyme视频
	 */
	public String getFlymeData(String url,String appId){
		JSONObject jsonObject=new JSONObject();
		JSONObject js2=new JSONObject();
				try {
			jsonObject.put("url", url);
			js2.put("data", jsonObject);
			js2.put("action", 11);
			js2.put("appID", GMSdkCheck.appId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				return js2.toString();
	}
	
	/**
	 * 发送关机命令
	 */
	
	public String sendTurnOff(){
		JSONObject objecet=new JSONObject();
		JSONObject js=new JSONObject();
		try {
			js.put("type", 0);
			objecet.put("data", js);
			objecet.put("action", 6);
			js.put("appID", GMSdkCheck.appId);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return objecet.toString();
	}
	
	/**
	 * 安装应用
	 * 
	 */
	public String sendJson(String url,String title,String packageName,String iconUrl){
		JSONObject jsonObject=new JSONObject();
		JSONObject js2=new JSONObject();
				try {
			jsonObject.put("title",title);
			jsonObject.put("url", url);
			jsonObject.put("packageName", packageName);
			jsonObject.put("iconUrl", iconUrl);
			js2.put("data", jsonObject);
			js2.put("action", 1);
			js2.put("appID", GMSdkCheck.appId);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(js2.toString());
		return js2.toString();
}
	/**
	 * 传送文件到投影上
	 * @param url
	 * @param type
	 * @return
	 */
	public String sendFileJson(String url,int type){
		JSONObject jsonObject=new JSONObject();
		JSONObject js2=new JSONObject();
				try {
			jsonObject.put("url", url);
			jsonObject.put("type", type);
			js2.put("data", jsonObject);
			js2.put("action", 7);
			js2.put("appID", GMSdkCheck.appId);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return js2.toString();
}
	
	/**
	 * 发送得到mac的命令
	 */
	
	
	public String sendMac(){
		JSONObject objecet=new JSONObject();
		JSONObject js=new JSONObject();
		try {
			objecet.put("data", js);
			objecet.put("action", 12);
			js.put("appID", GMSdkCheck.appId);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return objecet.toString();
	}


}
