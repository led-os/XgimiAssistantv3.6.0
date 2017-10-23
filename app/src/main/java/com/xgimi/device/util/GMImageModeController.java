package com.xgimi.device.util;

import com.xgimi.device.device.GMDeviceController;

import org.json.JSONException;
import org.json.JSONObject;


public class GMImageModeController {
	public static final int GMImageModeLight = 0; // 明亮
	public static final int GMImageModeStandard = 1; // 标准
	public static final int GMImageModeSoft = 2; // 柔和
	public static final int GMImageModeNature = 3; // 自然
	public static final int GMImageModeUser = 4; // 自定义

	private static GMImageModeController threedController;

	public static GMImageModeController getInstance() {
		if (threedController == null) {
			threedController = new GMImageModeController();
		}
		return threedController;
	}

	private GMImageModeController() {
	}

	public void sendPictureController(int mode) {
		GMDeviceController.getInstance().SendJC(sendPicture(mode));
	}

	// 发送命令
	private String sendPicture(int mode) {
		JSONObject jsonObject = new JSONObject();
		JSONObject js2 = new JSONObject();
		try {
			jsonObject.put("mode", mode);
			js2.put("data", jsonObject);
			js2.put("action", 4);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(js2.toString());
		return js2.toString();
	}

}
