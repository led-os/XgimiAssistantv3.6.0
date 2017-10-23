package com.xgimi.zhushou.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.xgimi.device.socket.UdpManager;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;

public class RemoteControlListener implements MultitouchListener {

	private static final int LOCALPORT = 15555;// 触摸默认端口
	private int MAX_DATA_PACKET_LENGTH = 40;
	private static String ip;

	private DatagramPacket dataPacket;
	private DatagramSocket udpSocket = null;

	private HandlerThread handlerThread;
	private SendHandler sendHandler;

	public RemoteControlListener(String ip) {
		this.ip = ip;
	}

	@Override
	public void touchEvent(List<Point> point, TYPE type) {
		final StringBuffer sb = new StringBuffer();
		if (type == TYPE.SINGLE_RELEASE) {
			sb.append("POINTCLICK:1");
		} else {
			sb.append("POINTEVENT:");

			for (Point p : point) {
				sb.append(p.x() * 1930 + "+" + p.y() * 1100 + ";");
			}
			sb.append(":" + point.size() + "|");
		}

		byte[] buffer = new byte[MAX_DATA_PACKET_LENGTH];
		dataPacket = new DatagramPacket(buffer, MAX_DATA_PACKET_LENGTH);
		dataPacket.setPort(UdpManager.cPort);

		byte out[] = sb.toString().trim().getBytes();
		dataPacket.setData(out);
		dataPacket.setLength(out.length);
		initData();

		if (handlerThread == null) {
			handlerThread = new HandlerThread("send_touch");
			handlerThread.start();
		}
		if (sendHandler == null) {
			sendHandler = new SendHandler(handlerThread.getLooper());
		}
		sendHandler.obtainMessage(0).sendToTarget();
	}

	private void initData() {

		try {
			if (udpSocket == null) {

				udpSocket = new DatagramSocket(null);
				udpSocket.setReuseAddress(true);
				udpSocket.bind(new InetSocketAddress(LOCALPORT));

			}
		} catch (Exception e) {
		}

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
					InetAddress broadcastAddr = InetAddress.getByName(ip);
					dataPacket.setAddress(broadcastAddr);
					udpSocket.send(dataPacket);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
