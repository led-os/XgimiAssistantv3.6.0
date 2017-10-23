package com.xgimi.device.callback;

/** 
* 发送命令的回调
* interface GMDeviceBrowserListener 
* 
*/
public interface GMDeviceCommandListener {
	/**
	 * 
	 * @param code 返回发送命令的状态码
	 */
	void commandDidSend();
	void commandDidNotSend(int code);
}
