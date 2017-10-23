package com.xgimi.gmsdk.bean.reply;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tommy on 2017/7/26.
 */

public class PacketFactory {

    private static final int SEARCH_REPLY_ACTION = 9999;
    private static final int CONNECT_SUCCEED_ACTION = 10001;
    private static final int APP_LIST_ACTION = 30236;
    private static final int DEVICE_OPEN_KEYBOARD = 1;
    private static final int DEVICE_CLOSE_KEYBOARD = 0;
    private static final int SCREEN_SHOT = 30235;

    private static Gson mGson;

    public static Packet genaratePacket(Packet packet) {
        if (mGson == null) {
            mGson = new Gson();
        }
        Packet resPacket = null;
        try {
            JSONObject jsonObject = new JSONObject(packet.getMsg());
            int action = jsonObject.getInt("action");
            switch (action) {
                case SEARCH_REPLY_ACTION:
                    resPacket = mGson.fromJson(jsonObject.toString(), SearchReplyPacket.class);
                    ((SearchReplyPacket)resPacket).getDeviceInfo().setDeviceip(packet.getRealIP());
                    break;
                case CONNECT_SUCCEED_ACTION:
                    resPacket = mGson.fromJson(jsonObject.toString(), ConnectReplyPacket.class);
                    ((ConnectReplyPacket)resPacket).getDeviceInfo().setIpaddress(packet.getRealIP());
                    break;
                case APP_LIST_ACTION:
                    resPacket = mGson.fromJson(jsonObject.toString(), AppListReply.class);
                    break;
                case DEVICE_OPEN_KEYBOARD:
                    resPacket = mGson.fromJson(jsonObject.toString(), DeviceKeyboardStateChangePacket.class);
                    break;
                case DEVICE_CLOSE_KEYBOARD:
                    resPacket = mGson.fromJson(jsonObject.toString(), DeviceKeyboardStateChangePacket.class);
                    break;
                case SCREEN_SHOT:
                    resPacket = mGson.fromJson(jsonObject.toString(), ScreenShotPacket.class);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resPacket;
    }
}
