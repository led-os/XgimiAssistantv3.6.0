package com.xgimi.gmsdk.connect;

/**
 * Created by XGIMI on 2017/9/7.
 */

public class GMCheckAuthentication {
    private static final String KEY1 = "9a2d2c55986665dd0";
    private static final String KEY2 = "d3b384343acc4db";
    static boolean check(String appid, String secrete) {
        if (MD5Util.getMD5String(appid + KEY1 + KEY2).equals(secrete)) {
            return true;
        } else {
            return false;
        }
    }
}
