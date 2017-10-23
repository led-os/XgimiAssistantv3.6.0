package com.xgimi.zhushou.util;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;


public class YiPingUrl {

	private String yiIp = "";

	// 用于存储分辨率的高
	private int high = 640;

	// 用于存储分辨率的宽
	private int wigth = 480;

	// 协商时返回的errorcode
	private String ErrorCode = "";

	private int DefaultPort = 10080;

	private String Z3sUrlAdd = "/liveplay.123?width=640&&height=480&&cameraid=hd0&&win=allwithosd&&sub=23&&dumpvideo=0&&dumpaudio=0&&specialcase=followmain&&framerate=30";

	public YiPingUrl(String yiIp) {
		super();
		this.yiIp = yiIp;
	}

	public String getYipingUrl() {

		String url = "";

		StringBuilder sb = new StringBuilder("rtsp://");
		Constant.isZ3S=true;
		if (!Constant.isZ3S) {
			Socket socket = null;
			try {
				socket = new Socket(yiIp, DefaultPort);
				socket.setSoTimeout(5000);

				OutputStream output = socket.getOutputStream();
				String tvrequest = "getresolutionratio wigth=" + wigth
						+ "&&high=" + high + "\r\n";
				byte[] req = tvrequest.getBytes();
				output.write(req);
				output.flush();

				InputStream input = socket.getInputStream();
				byte[] res = new byte[512];
				input.read(res);

				output.close();
				input.close();
				socket.close();

				String temp = new String(res).trim();
				Log.e("temp", temp);
				String rtspport = null;

				char a = temp.charAt(temp.indexOf("&") - 1);
				ErrorCode = a + "";

				if (temp.contains("rtspport")) {
					rtspport = temp.split("rtspport=")[1].trim();
				}

				if (rtspport == null) {
					rtspport = "554";
				}

				String SecureCode = temp.split("authstring=")[1].trim();

				sb.append(yiIp).append(":").append(rtspport)
						.append("/liveplay.").append(SecureCode + ".h264");

			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} else {
			sb.append(yiIp + Z3sUrlAdd);
			
		}

		url = sb.toString().trim();

		Log.e("YiPingSer", "url=" + url);

		if (!Constant.isZ3S && !ErrorCode.contains("0")) {
			url = "";
		}

		return url;

	}

}
