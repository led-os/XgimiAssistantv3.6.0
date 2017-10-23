package com.xgimi.gmsdk.connect;

import com.google.gson.Gson;
import com.xgimi.gmsdk.bean.device.GMDevice;
import com.xgimi.gmsdk.bean.send.GMKeyCommand;
import com.xgimi.gmsdk.bean.send.VcontrolCmd;
import com.xgimi.gmsdk.callback.GMConnectListeners;
import com.xgimi.gmsdk.callback.IGMDeviceConnectedListener;
import com.xgimi.gmsdk.callback.IGMDeviceFoundListener;
import com.xgimi.gmsdk.callback.IGMReceivedScreenShotResultListener;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by XGIMI on 2017/9/6.
 */

public class DeviceProxy implements IGMDeviceProxy {

    private ConnectManager mManager;

    private static DeviceProxy mInstance;

    public synchronized static DeviceProxy getInstance() {
        if (mInstance == null) {
            mInstance = new DeviceProxy();
        }
        return mInstance;
    }

    private DeviceProxy() {
        this.mManager = ConnectManager.getConnectManager();
    }

    @Override
    public GMDevice getConnectedDevice() throws Exception {
        GMDevice device = mManager.getConnectedDevice();
        if (!mManager.isConnectedToDevice() || device == null) {
            throw new Exception("Not Connected To Any Device");
        }
        return device;
    }

    @Override
    public boolean isConnectedToDevice() {
        return mManager.isConnectedToDevice();
    }

    @Override
    public boolean initAuthentication(String appid, String secrete) {
        return mManager.initAuthentication(appid, secrete);
    }

    @Override
    public void searchDevice(IGMDeviceFoundListener listener) throws Exception {
        mManager.setOnDeviceFoundListener(listener);
        mManager.startSearchDevice();
    }

    @Override
    public void searchDevice(IGMDeviceFoundListener listener, int timeOut) throws Exception {
        mManager.setOnDeviceFoundListener(listener);
        mManager.startSearchDevice(timeOut);
    }

    @Override
    public void stopSearchDevice() {
        mManager.stopSearchDevice();
    }

    @Override
    public void connectDevice(String ip, IGMDeviceConnectedListener listener) throws Exception {
        if (!CommonUtil.isIpAddress(ip)) {
            throw new Exception("Illegal parameter");
        }
        mManager.setOnDeviceConnectListener(listener);
        mManager.connectDevice(ip);
    }

    @Override
    public void connectDevice(String ip, IGMDeviceConnectedListener listener, int timeOut) throws Exception {
        if (!CommonUtil.isIpAddress(ip)) {
            throw new Exception("Illegal parameter");
        }
        mManager.setOnDeviceConnectListener(listener);
        mManager.connectDevice(ip, timeOut);
    }

    @Override
    public void sendKeyCommand(GMKeyCommand command) throws Exception {
        mManager.sendKeyCommand(command);
    }

    @Override
    public void sendVideo(String url) throws Exception {
        if (url == null || !url.startsWith("http")) {
            throw new Exception("Illegal parameter");
        } else {
            ArrayList<VcontrolCmd.CustomPlay.PlayList> sendList = new ArrayList<>();
            sendList.add(new VcontrolCmd.CustomPlay.PlayList(null
                    , null, null, null, null, url, null));
            mManager.sendNormalCommand(
                    new Gson().toJson(
                            new VcontrolCmd(30200, "2", null, null,
                                    new VcontrolCmd.CustomPlay(0, 0, null, sendList, 0),
                                    null, null, null)
                    )
            );
        }
    }

    @Override
    public void sendImage(String url) throws Exception {
        if (url == null || !url.startsWith("http")) {
            throw new Exception("Illegal parameter");
        } else {
            ArrayList<VcontrolCmd.CustomPlay.PlayList> sendList = new ArrayList<>();
            sendList.add(new VcontrolCmd.CustomPlay.PlayList(url));
            mManager.sendNormalCommand(
                    new Gson().toJson(
                            new VcontrolCmd(30200, "2", null, null,
                                    new VcontrolCmd.CustomPlay(2, sendList, 0, 0),
                                    null, null, null)
                    )
            );
        }
    }

    @Override
    public void sendMusic(String url, String artist, String title) throws Exception {
        if (url == null || !url.startsWith("http")) {
            throw new Exception("Illegal parameter");
        } else {
            ArrayList<VcontrolCmd.CustomPlay.PlayList> sendList = new ArrayList<>();
            sendList.add(new VcontrolCmd.CustomPlay.PlayList(null, null,
                    null, title, artist, url, null));
            mManager.sendNormalCommand(
                    new Gson().toJson(
                            new VcontrolCmd(30200, "2",
                                    null, null,
                                    new VcontrolCmd.CustomPlay(1, 0, null, sendList, 0),
                                    null, null, null)
                    )
            );
        }
    }

    @Override
    public void sendFile(String url, String name, int type) throws Exception {
        if (url == null || name == null || type < 0 || type > 4 || !url.startsWith("http")) {
            throw new Exception("Illegal parameter");
        } else {
            String fileType = ConnectManager.FILE_TYPE_ARR[type];
            List<VcontrolCmd.CustomPlay.PlayList> mPlyLists = new ArrayList<>();
            VcontrolCmd.CustomPlay.PlayList playList =
                    new VcontrolCmd.CustomPlay.PlayList(fileType, null, null, url, name, null);
            mPlyLists.add(playList);
            mManager.sendNormalCommand(new Gson().toJson(
                    new VcontrolCmd(30200, "2", null, null,
                            new VcontrolCmd.CustomPlay(3, 0, null, mPlyLists, 0),
                            null, null, null)
            ));
        }
    }

    @Override
    public void sendApk(String url, String name, String packageName) throws Exception {
        if (url == null || name == null || packageName == null || !url.startsWith("http")) {
            throw new Exception("Illegal parameter");
        } else {
            List<VcontrolCmd.CustomPlay.PlayList> mPlyLists = new ArrayList<>();
            VcontrolCmd.CustomPlay.PlayList playList =
                    new VcontrolCmd.CustomPlay.PlayList(".apk", packageName, null, url, name, null);
            mPlyLists.add(playList);
            mManager.sendNormalCommand(new Gson().toJson(
                    new VcontrolCmd(30200, "2", null, null,
                            new VcontrolCmd.CustomPlay(3, 0, null, mPlyLists, 0), null,
                            null, null)
            ));
        }
    }

    @Override
    public void sendPowerOption(int option) throws Exception {
        if (option < 0 || option > 3) {
            throw new Exception("Illegal parameter");
        } else {
            switch (option) {
                case 0://关机
                    mManager.sendNormalCommand(
                            new Gson().toJson(
                                    new VcontrolCmd(20000, "2", null, null, null,
                                            new VcontrolCmd.ControlCmd(6, 0, null),
                                            null, null)
                            )
                    );
                    break;
                case 1://重启
                    mManager.sendNormalCommand(
                            new Gson().toJson(
                                    new VcontrolCmd(20000, "2", null, null, null,
                                            new VcontrolCmd.ControlCmd(6, 1, null),
                                            null, null)
                            )
                    );
                    break;
                case 2://开光机
                    mManager.sendNormalCommand(
                            new Gson().toJson(
                                    new VcontrolCmd(20000, "2", null, null, null,
                                            new VcontrolCmd.ControlCmd(6, 4, null),
                                            null, null)
                            )
                    );
                    break;
                case 3://关光机
                    mManager.sendNormalCommand(
                            new Gson().toJson(
                                    new VcontrolCmd(20000, "2", null, null, null,
                                            new VcontrolCmd.ControlCmd(6, 2, null),
                                            null, null)
                            )
                    );
                    break;
            }
        }
    }

    @Override
    public void send3DModeOption(int option) throws Exception {
        if (option < 0 || option > 7) {
            throw new Exception("Illegal parameter");
        } else {
            mManager.sendNormalCommand(new Gson().toJson(
                    new VcontrolCmd(20000, "2", null, null, null,
                            new VcontrolCmd.ControlCmd(3, option, null),
                            null, null)
            ));
        }
    }

    @Override
    public void sendTouchMousePosition(float tempX, float tempY) throws Exception {
        String touchMsg = "TOUCHEVENT:" + tempX + "+" + tempY;
        mManager.sendTouchMouse(touchMsg);
    }

    @Override
    public void sendTouchMouseClick() throws Exception {
        mManager.sendTouchMouse("MOUSELEFTS:3");
    }

    @Override
    public void sendUserText(String text) throws Exception {
        if (!mManager.isKeyboardOpen()) {
            throw new Exception("Device Keyboard Not Open");
        } else {
            String sendText = new Gson().toJson(
                    new VcontrolCmd(20000, "2", null, null,
                            null, new VcontrolCmd.ControlCmd(9, 3, text),
                            null, null));
            mManager.sendNormalCommand(sendText);
        }
    }

    @Override
    public void sendSmoothFocus(int value) throws Exception {
        if (value < 0 || value > 100) {
            throw new Exception("Illegal parameter, value within 0-100");
        }
        String sendText = new Gson().toJson(new VcontrolCmd(20000, "2", null, null,
                null, new VcontrolCmd.ControlCmd(2, 1, 0, null, null, new VcontrolCmd.ControlCmd.ZoomFocus(1, value)), null, null));
        mManager.sendNormalCommand(sendText);
    }

    @Override
    public void sendScreenShot(IGMReceivedScreenShotResultListener listener) throws Exception {
        String msg = new Gson().toJson(new VcontrolCmd(20000, "2", null, null,
                null, new VcontrolCmd.ControlCmd(9, 1, null), null, null));
        mManager.setOnReceivedScreenShotResultListener(listener);
        mManager.sendNormalCommand(msg);
    }

    @Override
    public void sendImageMode(int i) throws Exception {
        if (i < 0 || i > 4) {
            throw new Exception("Illegal parameter, value within 0-4");
        } else {
            String msg = new Gson().toJson(new VcontrolCmd(20000, "2", null, null, null, new VcontrolCmd.ControlCmd(4, i, null), null, null));
            mManager.sendNormalCommand(msg);
        }
    }

    @Override
    public void sendCleanCache() throws Exception {
        String msg = new Gson().toJson( new VcontrolCmd(20000, "2", null, null,
                null, new VcontrolCmd.ControlCmd(9, 2, null), null, null));
        mManager.sendNormalCommand(msg);
    }

}
