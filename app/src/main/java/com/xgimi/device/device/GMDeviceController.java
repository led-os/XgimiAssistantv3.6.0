package com.xgimi.device.device;

import com.xgimi.device.callback.GMDeviceCommandListener;
import com.xgimi.device.socket.UdpManager;
import com.xgimi.device.socket.UdpThreadHelper;
import com.xgimi.device.util.JsonData;
import com.xgimi.device.utils.Consants;

import java.util.HashMap;


/**
 * 设备设备控制类
 * class GMDeviceController
 */
public class GMDeviceController {
    public static HashMap<Integer, String> hashMap;
    public GMDeviceCommandListener gmdclistener;
    public static GMDeviceController gmdcontrlller;
    private UdpThreadHelper udphelper;

    static {
        hashMap = new HashMap<Integer, String>();
        hashMap.put(GMKeyCommand.GMKeyEventLeft, Consants.GMKeyEventLeft);
        hashMap.put(GMKeyCommand.GMKeyEventRight, Consants.GMKeyEventRight);
        hashMap.put(GMKeyCommand.GMKeyEventUp, Consants.GMKeyEventUp);
        hashMap.put(GMKeyCommand.GMKeyEventDown, Consants.GMKeyEventDown);
        hashMap.put(GMKeyCommand.GMKeyEventOk, Consants.GMKeyEventOk);
        hashMap.put(GMKeyCommand.GMKeyEventBack, Consants.GMKeyEventBack);
        hashMap.put(GMKeyCommand.GMKeyEventFocusLeft_down, Consants.COM_FOCUS_ADD_DOWN);
        hashMap.put(GMKeyCommand.GMKeyEventFocusLeft_up, Consants.COM_FOCUS_ADD_UP);
        hashMap.put(GMKeyCommand.GMKeyEventFocusRight_down, Consants.COM_FOCUS_REDUCE_DOWN);
        hashMap.put(GMKeyCommand.GMKeyEventFocusRight_up, Consants.COM_FOCUS_REDUCE_UP);
        hashMap.put(GMKeyCommand.GMKeyEventHome, Consants.GMKeyEventHome);
        hashMap.put(GMKeyCommand.GMKeyEventMenu, Consants.GMKeyEventMenu);
        hashMap.put(GMKeyCommand.GMKeyEventVolumeDown, Consants.GMKeyEventVolumeDown);
        hashMap.put(GMKeyCommand.GMKeyEventVolumeUp, Consants.GMKeyEventVolumeUp);
        hashMap.put(GMKeyCommand.GMKeyEventFocusTurnOFF, Consants.GMKeyEventTurnOff);
        hashMap.put(GMKeyCommand.GMKeyEventFocusThreeD, Consants.GMKeyEventThreeD);
        hashMap.put(GMKeyCommand.GMKeyEventFunction, Consants.GMKeyEventFunction);
        hashMap.put(GMKeyCommand.GMkeyOpen, Consants.GMKeyOpen);
        hashMap.put(GMKeyCommand.GMKeyGamel, Consants.GMkeyEVentl);
        hashMap.put(GMKeyCommand.GMKeyGamer, Consants.GMkeyEVentr);
        hashMap.put(GMKeyCommand.GMKeyGamea, Consants.GMkeyEVenta);
        hashMap.put(GMKeyCommand.GMKeyGameb, Consants.GMkeyEVentb);
        hashMap.put(GMKeyCommand.GMKeyGamex, Consants.GMkeyEVentx);
        hashMap.put(GMKeyCommand.GMKeyGameY, Consants.GMkeyEVentY);
        hashMap.put(GMKeyCommand.GMKeyGameSelecter, Consants.GMkeyEVentselector);
        hashMap.put(GMKeyCommand.GMKeyGameleft, Consants.GMkeyEVentLeft);
        hashMap.put(GMKeyCommand.GMKeyGameright, Consants.GMkeyEVentRight);
        hashMap.put(GMKeyCommand.GMKeyGameup, Consants.GMkeyEVentUp);
        hashMap.put(GMKeyCommand.GMKeyGamedown, Consants.GMkeyEVentDown);

        hashMap.put(GMKeyCommand.GMKeyLeftDown, Consants.GMKeyStatusLeft);
        hashMap.put(GMKeyCommand.GMKeyLeftup, Consants.GMKeyPressLeft);

        hashMap.put(GMKeyCommand.GMKeyrightdonw, Consants.GMKeyStatusRight);
        hashMap.put(GMKeyCommand.GMKeyrightup, Consants.GMKeyPressRight);

        hashMap.put(GMKeyCommand.GMKeyupdonw, Consants.GMKeyStatusup);
        hashMap.put(GMKeyCommand.GMKeyupUp, Consants.GMKeyPressup);

        hashMap.put(GMKeyCommand.GMKeyDowndonw, Consants.GMKeyStatusdown);
        hashMap.put(GMKeyCommand.GMKeyDownup, Consants.GMKeyPressdown);

        hashMap.put(GMKeyCommand.GMKeyOkDown, Consants.GMKeyOkDown);
        hashMap.put(GMKeyCommand.GMKeyOkUp, Consants.GMKeyOkUp);
        hashMap.put(GMKeyCommand.GMKeyHomeDown, Consants.GMKeyEventHomeDOWN);
        hashMap.put(GMKeyCommand.GMKeyHomeUp, Consants.GMKeyEventHomeUP);
    }

    private GMDeviceController() {

    }

    /**
     * @param gmd 极米设备控制监听回调
     */
    public void setGMDeviceCommandListener(GMDeviceCommandListener gmd) {
        this.gmdclistener = gmd;
    }

    /**
     * 得到极米控制类
     *
     * @return
     */
    public static GMDeviceController getInstance() {
        if (gmdcontrlller == null) {
            gmdcontrlller = new GMDeviceController();
        }
        return gmdcontrlller;
    }


    /**
     * @param gmkcommand 控制命令类
     */
    public void sendCommand(GMKeyCommand gmkcommand) {
        if (Consants.constatus == false) {
            if (gmdclistener != null) {
                gmdclistener.commandDidNotSend(Consants.GM_ERROR_CODE_NOT_CONNECTED);
            }
            return;
        }
        if (!hashMap.containsKey(gmkcommand.getKey())) {
            if (gmdclistener != null) {
                gmdclistener.commandDidNotSend(Consants.GM_ERROR_CODE_NOT_CONNECTED);
            }
            return;
        }
        String command = hashMap.get(gmkcommand.getKey());
        Send(command);
    }

    //直接发送命令字符串
    public void sendCommand(String com) {
        Send(com);
    }

    private void Send(String com) {
        if (!GMDeviceStorage.getInstance().getConnectedDevice().ip.equals("")) {
            UdpManager.getInstance().sendCCommand(com);
            if (gmdclistener != null) {
                gmdclistener.commandDidSend();
            }
        } else {
            if (gmdclistener != null)
                gmdclistener.commandDidNotSend(Consants.GM_ERROR_CODE_NOT_CONNECTED);
        }
    }

    public void senddOpen(String s) {
        SendOpen(hashMap.get(GMKeyCommand.GMkeyOpen) + s);
    }

    public void sendXieZai(String s) {
        SendOpen(Consants.GMKeyXieZai + s);

    }

    public void SendOpen(String com) {
        UdpManager.getInstance().sendJCommand(com);
    }

    public void SendJC(String com) {
        UdpManager.getInstance().sendJCommand(com);
    }

    public void SendNewJC(String com) {
        UdpManager.getInstance().sendJCNewCommand(com);
    }

    //发送触摸控制
    public void sendChuMo(String s) {
        if (GMDeviceStorage.getInstance().gmdevice != null && GMDeviceStorage.getInstance().gmdevice.ip != null) {
            if (udphelper == null) {
                udphelper = new UdpThreadHelper(GMDeviceStorage.getInstance().gmdevice.ip, UdpThreadHelper.TOUCHGRIVATY);
            }
            udphelper.setIp(GMDeviceStorage.getInstance().gmdevice.ip);
            udphelper.sendMessage(s);
        }
    }

    //传送文件
    public void pushFile(String url, int type) {
        if (type != 4 || type != 0) {
            SendJC(JsonData.getInstance().sendFileJson(url, type));
        }
    }

    //推送应用
    private void sendApk(String url, String title, String packageName, String iconUrl) {
        SendJC(JsonData.getInstance().sendJson(url, title, packageName, iconUrl));
    }
}



