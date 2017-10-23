package com.xgimi.zhushou.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 获取设备的基本信息
 * 
 * @author liuyang
 *
 */
public class DeviceUtils {

	/**
	 * 获取本机ip
	 * 
	 * @return
	 */
	public static String getLocalIPAddress() {

		String ip = "";

		try {

			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& (inetAddress instanceof Inet4Address)) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}

		} catch (Exception e) {

			ip = "";
		}

		return ip;
	}

	/**
	 * 获取机器imei设备号
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		String imei = ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId()
				+ "";

		return imei;
	}

	/**
	 * 获取app版本信息
	 * 
	 * @param context
	 * @return
	 */
	public static String getappVersion(Context context) {

		String appVersion = "0.0.0";

		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			appVersion = info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return appVersion;

	}

	/**
	 * 获取app版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getappVersionCode(Context context) {

		int appVersionCode = 999;

		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			appVersionCode = info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return appVersionCode;

	}

	/**
	 * 获取android的版本号
	 * 
	 * @return
	 */
	public static String getandroidVersion() {
		String androidVersion = Build.VERSION.RELEASE + "";

		return androidVersion;
	}
}
