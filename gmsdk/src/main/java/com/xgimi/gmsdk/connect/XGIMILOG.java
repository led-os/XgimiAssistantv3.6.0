/*
 * @Author: hailong.qiu
 * @Maintainer: hailong.qiu
 * @Date: 16-10-17 下午4:20
 * @Copyright: 2016 www.xgimi.com Inc. All rights reserved.
 */

package com.xgimi.gmsdk.connect;

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

    private static String sTag = "xgimiui";
    private static boolean sDebug = false;

    public static void initTag(String tag) {
        initTag(tag, false);
    }

    public static void initTag(boolean debug) {
        initTag(sTag, debug);
    }

    public static void initTag(String tag, boolean debug) {
        sTag = tag;
        sDebug = debug;
    }

    public static void D(String str, Object... args) {
        if (isDebug()) {
            Log.d(getTag(), buildLogString(str, args));
        }
    }

    public static void V(String str, Object... args) {
        if (isDebug()) {
            Log.v(getTag(), buildLogString(str, args));
        }
    }

    public static void E(Throwable e) {
        Log.e(getTag(), "", e);
    }

    public static void E(String str, Object... args) {
        Log.e(getTag(), buildLogString(str, args));
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
        //
        StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];
        StringBuilder stringBuilder = new StringBuilder();
        //
//        if (TextUtils.isEmpty(sTag)) {
//            stringBuilder.append(caller.getMethodName())
//                    .append("():")
//                    .append(caller.getLineNumber())
//                    .append(":")
//                    .append(str);
//        } else {
        stringBuilder
                .append("(")
                .append(caller.getFileName())
                .append(":")
                .append(caller.getLineNumber())
                .append(").")
                .append(caller.getMethodName())
                .append("():")
                .append(str);
//        }
        return stringBuilder.toString();
    }

    private static boolean isDebug() {
        return sDebug || Log.isLoggable(getTag(), Log.DEBUG);
    }

}
