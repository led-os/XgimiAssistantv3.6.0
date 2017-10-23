package com.xgimi.gmsdk.connect;


import com.xgimi.gmsdk.bean.device.GMDevice;
import com.xgimi.gmsdk.bean.send.GMKeyCommand;
import com.xgimi.gmsdk.callback.GMConnectListeners;
import com.xgimi.gmsdk.callback.IGMDeviceConnectedListener;
import com.xgimi.gmsdk.callback.IGMDeviceFoundListener;
import com.xgimi.gmsdk.callback.IGMReceivedScreenShotResultListener;

/**
 * Created by Tommy on 2017/9/6.
 */

public interface IGMDeviceProxy {


    GMDevice getConnectedDevice() throws Exception;
    /**
     * 判断是否已经连接到设备
     * @return
     */
    boolean isConnectedToDevice();

    boolean initAuthentication(String appid, String secrete);

    /**
     * 搜索设备
     * @param listener 每搜到一个设备的监听
     * @throws Exception 鉴权未通过
     */
    void searchDevice(IGMDeviceFoundListener listener) throws Exception;

    void searchDevice(IGMDeviceFoundListener listener, int timeOut) throws  Exception;

    /**
     * 停止搜索
     */
    void stopSearchDevice();

    /**
     * 向某一设备发起连接
     * @param ip 设备ip
     * @param listener 连接成功的监听
     * @throws Exception 鉴权未通过
     */
    void connectDevice(String ip, IGMDeviceConnectedListener listener) throws Exception;

    void connectDevice(String ip, IGMDeviceConnectedListener listener, int timeOut) throws Exception;



    /**
     * 发送按键
     * @param command
     * @throws Exception
     */
    void sendKeyCommand(GMKeyCommand command) throws Exception;

    /**
     *发送视频
     * @param url
     * @throws Exception
     */
    void sendVideo(String url) throws Exception;

    /**
     * 发送图片
     * @param url
     * @throws Exception
     */
    void sendImage(String url) throws Exception;

    /**
     * 发送音乐
     * @param url
     * @param artist
     * @param title
     * @throws Exception
     */
    void sendMusic(String url, String artist, String title) throws Exception;

    /**
     * 发送文件
     * @param url
     * @param name
     * @param type  0: doc 1:txt, 2:pdf, 3:ppt, 4:xls
     * @throws Exception
     */
    void sendFile(String url, String name, int type) throws Exception;

    /**
     * 发送apk文件
     * @param url
     * @param name
     * @param packageName 包名
     * @throws Exception
     */
    void sendApk(String url, String name, String packageName) throws Exception;

    /**
     * 发送关机选项
     * @param option 0 : 关机， 1：重启， 2：开光机， 3：关光机
     * @throws Exception
     */
    void sendPowerOption(int option) throws Exception;


    /**
     * 发送3D模式
     * @param option 0:取消, 1:蓝光， 2:上下， 3:左右， 4:自动, 5:2D转3D， 6:上下3D转2D， 7:左右3D转2D
     * @throws Exception
     */
    void send3DModeOption(int option) throws Exception;

    /**
     * 发送触摸板数据
     * @param tempX
     * @param tempY
     * @throws Exception
     */
    void sendTouchMousePosition(float tempX, float tempY) throws Exception;

    /**
     * 发送左键点击
     * @throws Exception
     */
    void sendTouchMouseClick() throws Exception;

    /**
     * 发送用户输入的文字
     * @param text
     * @throws Exception 设备未打开输入键盘
     */
    void sendUserText(String text) throws Exception;

    /**
     * 无极调焦命令
     * @param value 0-100
     * @throws Exception
     */
    void sendSmoothFocus(int value) throws Exception;

    /**
     * 发送截图命令
     * @param listener
     * @throws Exception
     */
    void sendScreenShot(IGMReceivedScreenShotResultListener listener) throws Exception;

    /**
     * 图像模式
     * @param value 0: 标准 1：明亮 2:柔和 3:自然  4:自定义
     * @throws Exception
     */
    void sendImageMode(int value) throws Exception;

    /**
     * 清理内存
     * @throws Exception
     */
    void sendCleanCache() throws Exception;

//    String getConnectedDeviceIp();
}
