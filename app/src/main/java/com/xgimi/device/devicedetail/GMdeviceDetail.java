package com.xgimi.device.devicedetail;


import com.xgimi.device.device.GMDevice;

public class GMdeviceDetail extends GMDevice {

	public String type;
	public String version;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public GMdeviceDetail(String type, String version) {
		this.type = type;
		this.version = version;
	}
	public GMdeviceDetail(){
		
	}
	
}
