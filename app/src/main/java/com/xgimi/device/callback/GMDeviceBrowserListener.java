package com.xgimi.device.callback;


import com.xgimi.device.device.GMDevice;

/**
* 扫描设备的回调 
* interface GMDeviceBrowserListener 
* 
*/
public interface GMDeviceBrowserListener {
	/**
	 * 
	 * @param gmdevices 设备
	 */
	void GMDevice(GMDevice gmdevices, boolean isByUdp);
}
