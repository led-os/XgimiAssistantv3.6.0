package com.xgimi.zhushou.yiping;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.xgimi.device.socket.UdpManager;
import com.xgimi.zhushou.util.GravitySenseListener;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class MyGravityListener implements GravitySenseListener {

	private String IP = null;

	private int MAX_DATA_PACKET_LENGTH = 40;
	private int DEFAULT_PORT = 15554;

	private DatagramPacket dataPacket;
	private DatagramSocket udpSocket = null;

	private HandlerThread handlerThread;
	private SendHandler sendHandler;

	public MyGravityListener(String ip) {
		super();
		this.IP = ip;
	}

	private void initData() {

		try {
			if (udpSocket == null) {

				udpSocket = new DatagramSocket(null);
				udpSocket.setReuseAddress(true);
				udpSocket.bind(new InetSocketAddress(DEFAULT_PORT));
			}
		} catch (Exception e) {
		}

	}

	@Override
	public void gravitySenseListener(Point point, TYPE type) {

		String content = "GSENSORVAL:" + point.getX() + "+" + point.getY()
				+ "+" + point.getZ();

		byte[] buffer = new byte[MAX_DATA_PACKET_LENGTH];

		dataPacket = new DatagramPacket(buffer, MAX_DATA_PACKET_LENGTH);
		dataPacket.setPort(UdpManager.gPort);

		byte out[] = content.getBytes();
		dataPacket.setData(out);
		dataPacket.setLength(out.length);

		initData();

		if (handlerThread == null) {
			handlerThread = new HandlerThread("send_gravity");
			handlerThread.start();
		}
		if (sendHandler == null) {
			sendHandler = new SendHandler(handlerThread.getLooper());
		}

		sendHandler.obtainMessage(0).sendToTarget();

	}

	private class SendHandler extends Handler {
		public SendHandler() {

		}

		public SendHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				try {
					InetAddress broadcastAddr = InetAddress.getByName(IP);
					dataPacket.setAddress(broadcastAddr);
					udpSocket.send(dataPacket);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
