package com.xgimi.device.socket;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.xgimi.zhushou.util.XGIMILOG;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * udp循环线程发送命令 现在主要是触摸和重力用到
 *
 */
public class UdpThreadHelper {

	private static final int CLOCALPORT = 15555;// 触摸默认端口

	private static final int GLOCALPORT = 15557;

	public static final int COMMAND = 0;
	public static final int TOUCHGRIVATY = 1;

	private int PORT;

	private int MAX_DATA_PACKET_LENGTH = 40;
	private String ip;
	private int type;

	private DatagramPacket dataPacket;
	private DatagramSocket udpSocket = null;

	private HandlerThread handlerThread;
	private SendHandler sendHandler;

	public UdpThreadHelper(String ip, int type) {
		this.ip = ip;
		this.type = type;
	}

	public String getIP() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip=ip;
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
//					XGIMILOG.E("local port = " + udpSocket.getLocalPort() + ", remote port = " + udpSocket.getPort() + ", msg = " + new String(dataPacket.getData(), 0, dataPacket.getData().length));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void initData(int type) {

		try {
			if (udpSocket == null) {

				udpSocket = new DatagramSocket(null);
				udpSocket.setReuseAddress(true);
				if (type == COMMAND) {
					udpSocket.bind(new InetSocketAddress(CLOCALPORT)); // 绑定本地端口
					PORT = UdpManager.cPort;
				} else if (type == TOUCHGRIVATY) {
					udpSocket.bind(new InetSocketAddress(GLOCALPORT));
					PORT = UdpManager.gPort;
				}
			}
		} catch (Exception e) {

		}

	}

	public void sendMessage(String message) {

		byte[] buffer = new byte[MAX_DATA_PACKET_LENGTH];
		dataPacket = new DatagramPacket(buffer, MAX_DATA_PACKET_LENGTH);

		dataPacket.setPort(PORT);

		byte out[] = message.getBytes();

		dataPacket.setData(out);

		dataPacket.setLength(out.length);

		initData(type);

		if (handlerThread == null) {
			handlerThread = new HandlerThread("send_udpmsg");
			handlerThread.start();
		}
		if (sendHandler == null) {
			sendHandler = new SendHandler(handlerThread.getLooper());
		}
		sendHandler.obtainMessage(0).sendToTarget();
	}
}
