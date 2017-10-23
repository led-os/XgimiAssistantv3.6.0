package com.xgimi.gmsdk.callback;

import android.view.MotionEvent;
import android.view.View;

import com.xgimi.gmsdk.connect.IGMDeviceProxy;

/**
 * Created by linzhixin on 2017/9/6.
 */

public class OnXGimiTouchListener implements View.OnTouchListener {

    private float mLastX;
    private float mLastY;
    private IGMDeviceProxy mDeviceProxy;
    private long mOnTouchDownTime;

    public OnXGimiTouchListener(IGMDeviceProxy mDeviceProxy) {
        this.mDeviceProxy = mDeviceProxy;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            mOnTouchDownTime = System.currentTimeMillis();
        } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            if (x > mLastX && x - mLastX > 50) {
                x = mLastX + 1;
            }

            if (x < mLastX && mLastX - x > 50) {
                x = mLastX - 1;
            }

            if (y > mLastY && y - mLastY > 50) {
                y = mLastY + 1;
            }

            if (y < mLastY && mLastY - y > 50) {
                y = mLastY - 1;
            }

            boolean bContinue = (Math.abs(mLastX - x) > 0.2 || Math.abs(mLastY - y) > 0.2);
            if (bContinue) {
                float tempX = (mLastX - x);
                float tempY = (mLastY - y);
                if (Math.abs(tempX / tempY) > 2.0) {
                    tempY = 0;
                } else if (Math.abs(tempY / tempX) > 2.0) {
                    tempX = 0;
                }
                try {
                    mDeviceProxy.sendTouchMousePosition(-tempX, -tempY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mLastX = motionEvent.getX();
            mLastY = motionEvent.getY();

        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if (mOnTouchDownTime != 0 && System.currentTimeMillis() - mOnTouchDownTime < 200) {
                try {
                    mDeviceProxy.sendTouchMouseClick();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;

    }

}
