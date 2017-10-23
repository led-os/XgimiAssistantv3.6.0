package com.xgimi.device.callback;

public class HeartBit {

	public int action;
	public int version;
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}

	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public HeartBit(int action, int version) {
		this.action = action;
		this.version = version;
	}
	
}
