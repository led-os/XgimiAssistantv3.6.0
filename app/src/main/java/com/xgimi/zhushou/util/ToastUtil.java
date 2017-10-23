package com.xgimi.zhushou.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by XGIMI on 2017/8/30.
 */

public class ToastUtil {
    private static Toast mToast;

    public static Toast getToast(String text, Context context) {

        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);

        return mToast;
    }
}
