package com.xgimi.device.device;

import android.util.Log;

import com.xgimi.device.devicedetail.GMdeviceDetail;
import com.xgimi.device.utils.Consants;


/**
 * 设备对象 class GMDevice
 * 
 */
public class GMDevice {

	/**
	 * ip: 设备ip
	 */
	public String ip;
	/**
	 * name: 设备名字
	 */
	public String name;
	public String phoneIp;

	/**
	 * 设备版本号
	 * @return
     */
	public int version;

	/**
	 * 设备类型
	 * @return
     */
	public String type;

	/**
	 * 机器的具体型号
	 * @return
     */
	public String devicetype;

	/**
	 * 机器的mac地址
	 * @return
     */
	public String mac;

	public String getMac(){
		return mac;
	}
	public void setMac(String mac){
		this.mac=mac;
	}
	public String getDevicetype() {
		return devicetype;
	}

	public void setDevicetype(String devicetype) {
		this.devicetype = devicetype;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getVersions() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getPhoneIp() {
		return phoneIp;
	}

	public void setPhoneIp(String phoneIp) {
		this.phoneIp = phoneIp;
	}

	public String getZiShengip() {
		return ziShengip;
	}

	public void setZiShengip(String ziShengip) {
		this.ziShengip = ziShengip;
	}

	public String ziShengip;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GMDevice(String ip, String gmdevice) {
		this.ip = ip;
		this.name = gmdevice;
	}

	public GMDevice() {

	}

	public boolean isZhiChi = false;

	public boolean isFocusAble() {
		if ("full_mstara3".equals(type) || "mango".equals(type) || "mstara3".equals(type) ||
				"full_mango".equals(type) || "mstarnapoli".equals(type) || "full_mstarnapoli".equals(type) ||
				"full_z4air_business".equals(type) || "z4air".equals(type) || "full_z4air".equals(type) || "z4air_business".equals(type)) {
			return false;
		} else {
			return true;
		}
	}


	/**
	 * 
	 * @return ture 该设备可以调焦 false 该设备不可以调焦
	 * 
	 */
//	public boolean isFocusEnabled() {
//		for (int i = 0; i < Consants.gmdevicedetails.size(); i++) {
//			GMdeviceDetail gMdeviceDetail = Consants.gmdevicedetails.get(i);
//			if (gMdeviceDetail.getIp() != null && gMdeviceDetail.type != null) {
//				if (getIp().equals(gMdeviceDetail.getIp())) {
//					// if(gMdeviceDetail.type.equals("full_mstarnapoli_z4x")||gMdeviceDetail.type.equals("z4x")){
//					// return true;
//					// }else {
//					// return false;
//					// }
//					Log.e("info", "gMdeviceDetail.type000000000"
//							+ gMdeviceDetail.type);
//					if (getIp().equals(gMdeviceDetail.getIp())) {
//						Log.e("info", "gMdeviceDetail.type"
//								+ gMdeviceDetail.type);
//						if (gMdeviceDetail.type.equals("full_mstara3")
//								|| gMdeviceDetail.type.equals("mango")
//								|| gMdeviceDetail.type.equals("mstara3")
//								|| gMdeviceDetail.type.equals("full_mango")
//								|| gMdeviceDetail.type.equals("mstarnapoli")
//								|| gMdeviceDetail.type.equals("full_mstarnapoli")
//								|| gMdeviceDetail.type.equals("full_z4air_business")
//								|| gMdeviceDetail.type.equals("z4air")
//								|| gMdeviceDetail.type.equals("full_z4air")||gMdeviceDetail.type.equals("z4air_business")) {
//							return false;
//						} else {
//							return true;
//						}
//					}
//				}
//			}
//
//		}
//		return true;
//	}

	/**
	 * 
	 * @return ture 该设备支持一键3D false 该设备不支持一键3D
	 * 
	 */
//	public boolean isThreeDEnbaled() {
//		for (int i = 0; i < Consants.gmdevicedetails.size(); i++) {
//			GMdeviceDetail gMdeviceDetail = Consants.gmdevicedetails.get(i);
//			if (getIp() != null && gMdeviceDetail.type != null) {
//				if (getIp().equals(gMdeviceDetail.getIp())) {
//					if (gMdeviceDetail.type.equals("full_mstara3")
//							|| gMdeviceDetail.type.equals("mango")
//							|| gMdeviceDetail.type.equals("full_mango")
//							|| gMdeviceDetail.type.equals("mstarnapoli")) {
//						return true;
//					} else {
//						return false;
//					}
//				}
//			}
//		}
//		return false;
//	}

	@Override
	public String toString() {
		return "GMDevice{" +
				"ip='" + ip + '\'' +
				", name='" + name + '\'' +
				", phoneIp='" + phoneIp + '\'' +
				", version=" + version +
				", type='" + type + '\'' +
				", devicetype='" + devicetype + '\'' +
				", mac='" + mac + '\'' +
				", ziShengip='" + ziShengip + '\'' +
				", isZhiChi=" + isZhiChi +
				", isFocusAble = " + isFocusAble() +
				'}';
	}
}
