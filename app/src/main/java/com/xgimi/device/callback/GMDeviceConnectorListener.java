package com.xgimi.device.callback;


import com.xgimi.device.device.GMDevice;

/**
* 连接设备的回调
* interface GMDeviceBrowserListener 
* 
*/
public interface GMDeviceConnectorListener {
	/**
	 * 
	 * @param gm 返回的设备
	 */
	void deviceConnected(GMDevice gm);
	
	/**
	 * 
	 * @param gm 返回的设备
	 * @param code 连接失败的错误码
	 */
	void deviceNotConnected(GMDevice gm, int code);

	int getVersion();
}
