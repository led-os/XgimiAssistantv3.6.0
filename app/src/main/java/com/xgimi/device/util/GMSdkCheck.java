package com.xgimi.device.util;

public class GMSdkCheck {
    public static final String key = "9a2d2c55986665dd0d3b384343acc4db";
    public static String appId;
    public static String appSecret;
    public static boolean isValide;

    public static void checkMd5() {
        if (appId == null || appSecret == null) {
            isValide = false;
            return;
        }
        if (appSecret.equals(MD5Util.getMD5String(appId + key))) {
            isValide = true;
        } else {
            isValide = false;
        }
    }
}
