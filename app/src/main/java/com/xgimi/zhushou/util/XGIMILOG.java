/*
 * @Author: hailong.qiu
 * @Maintainer: hailong.qiu
 * @Date: 16-10-17 下午4:20
 * @Copyright: 2016 www.xgimi.com Inc. All rights reserved.
 */

package com.xgimi.zhushou.util;

import android.os.Build;
import android.support.design.BuildConfig;
import android.text.TextUtils;
import android.util.Log;

/**
 * http://angeldevil.me/2014/07/24/Android-SQLite-Debug/ 如果要查看LOG信息， 请在终端输入
 * --(输入)--> setprop log.tag.xgimiui DEBUG --(输入)--> start
 * 使用 logcat | grep "xgimiui" 来过滤查看打印.
 *
 * @author hailong.qiu
 */
public final class XGIMILOG {

    public static boolean mHasInit;


    private static String sTag = "xgimiui";
    private static boolean sDebug = false;


    public static void initTag(String tag) {
        sTag = tag;
        sDebug = true;
        mHasInit = true;
    }

    public static void initTag(String tag, boolean debug) {
        sTag = tag;
        sDebug = debug;
        mHasInit = true;
    }

    public static void D(String str, Object... args) {
        if (sDebug) {
            Log.d(getTag(), buildLogString(str, args));
        }
    }

    public static void V(String str, Object... args) {
        if (sDebug) {
            Log.v(getTag(), buildLogString(str, args));
        }
    }

    public static void E(Throwable e) {
        if (sDebug) {
            Log.e(getTag(), "", e);
        }

    }

    public static void E(String str, Object... args) {
        if (sDebug) {
            Log.e(getTag(), buildLogString(str, args));
        }
    }

    /**
     * 如果sTAG是空则自动从StackTrace中取TAG
     */
    public static String getTag() {
        if (!TextUtils.isEmpty(sTag)) {
            return sTag;
        }
        StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];
        return caller.getFileName();
    }

    private static String buildLogString(String str, Object... args) {
        if (args.length > 0) {
            str = String.format(str, args);
        }

        StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append("(")
                .append(caller.getFileName())
                .append(":")
                .append(caller.getLineNumber())
                .append(").")
                .append(caller.getMethodName())
                .append("():")
                .append(str);

        return stringBuilder.toString();
    }

}
