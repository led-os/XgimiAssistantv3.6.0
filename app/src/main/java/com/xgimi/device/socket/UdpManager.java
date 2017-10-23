package com.xgimi.device.socket;


import android.os.Handler;

/**
 * udp发送集合
 */
public class UdpManager {

    public static String separator = "#";

    public static final int CONNECTED = 0x10000;

    public static final int REMOTEINPUTSHOW = 0x10005;
    public static final int REMOTEINPUTHIDE = 0x10006;

    public static final int RECEIVEDMESSAGE = 0x10007;
    public static final int RECEIVEDEVICETYPE = 0x1008;

    public static final int NEWCONNECT = 0x1009;
    public static final int FOUND_NEW_DEVICE = 1234;

    private static UdpManager mUdpManager;

    private UdpPostSender cUdpHelper;
    private UdpPostSender jUdpHelper, newUdpHelper;

    public static final int cPort = 16735;
    public static final int jPort = 16736;
    public static final int gPort = 16737;

    public static final int newPort = 16750;
    public static final int newdefaultLocalPort = 16751;

    private String IP = "";

    private static final int defaultLocalPort = 16738;

    public static UdpManager getInstance() {
        if (mUdpManager == null) {
            mUdpManager = new UdpManager();
        }
        return mUdpManager;
    }

    public String getIP() {
        return IP;
    }

    public void initUdpManager(String ip, Handler handler) {
        this.IP = ip;
        cUdpHelper = new UdpPostSender(ip, 0, cPort, null);

        jUdpHelper = new UdpPostSender(ip, defaultLocalPort, jPort, handler);
        newUdpHelper = new UdpPostSender(ip, newdefaultLocalPort, newPort, handler);
    }

    /**
     * 同屏界面的udp管理，由于是两个进程，所以不能公用
     *
     * @param ip 服务端ip
     */
    public void initYipingUdp(String ip) {
        cUdpHelper = new UdpPostSender(ip, 0, cPort, null);
    }
    /**
     * 发送操作命令
     *
     * @param command
     */
//	public void sendCCommand(String command) {
//		if (cUdpHelper != null) {
//			if(GMSdkCheck.isValide){
//				if(Consants.HeatBeatStatus){
//						if(HeartBeat.getInstance().isReceiveHeatBeat){
//						cUdpHelper.sendUDPmsg(command);
//						}
//				}
//				else{
//
//					cUdpHelper.sendUDPmsg(command);
//				}
//			}
//		}
//	}

    /**
     * 发送文件命令
     *
     * @param command
     */
//	public void sendJCommand(String command) {
//		if (jUdpHelper != null){
//			if(GMSdkCheck.isValide){
//				if(Consants.HeatBeatStatus){
//					if(HeartBeat.getInstance().isReceiveHeatBeat){
//						jUdpHelper.sendUDPmsg(command);
//					}
//				}else{
//			       jUdpHelper.sendUDPmsg(command);
//				}
//			}
//		}
//		
//		
//	}

    /**
     * 发送操作命令
     *
     * @param command
     */
    public void sendCCommand(String command) {
        if (cUdpHelper != null) {
            cUdpHelper.sendUDPmsg(command);
        }

    }

    /**
     * 发送文件命令
     *
     * @param command
     */
    public void sendJCommand(String command) {

        if (jUdpHelper != null)
            jUdpHelper.sendUDPmsg(command);

    }

    /**
     * 发送新版的命令
     */
    public void sendJCNewCommand(String command) {
        if (newUdpHelper != null)
            newUdpHelper.sendUDPmsg(command);
    }
}
