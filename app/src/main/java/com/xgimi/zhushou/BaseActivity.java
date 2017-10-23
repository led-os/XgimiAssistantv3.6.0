package com.xgimi.zhushou;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.xgimi.device.device.GMDeviceController;
import com.xgimi.device.device.GMKeyCommand;
import com.xgimi.zhushou.activity.NewSearchDeviceActivity;
import com.xgimi.zhushou.activity.RemountActivity;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.XGIMILOG;
import com.xgimi.zhushou.widget.MyProgressDialog;

import rx.Subscription;

/**
 * Created by 霍长江 on 2016/8/8.
 */
public class BaseActivity extends FragmentActivity {
    public Subscription subscription;
    private MyProgressDialog dilog;
    public ImageView iv_remount;
    public TextView tv;
    private WindowManager wm;
    public int height;
    public int width;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushAgent.getInstance(this).onAppStart();
        wm = getWindowManager();
        height = wm.getDefaultDisplay().getHeight();
        width = wm.getDefaultDisplay().getWidth();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 替换fragment，不添加进返回栈，不添加转场动画
     */

    public void addFragement(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(R.id.layout, fragment).
                commitAllowingStateLoss();
        fragmentTransaction.hide(fragment);
    }

    public void removeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }

    public void showFragmentWithoutBackStackAndAnim(Fragment home, Fragment hideFragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        if (hideFragment != null) {
            fragmentTransaction.hide(hideFragment);
        }
        fragmentTransaction.show(home).commitAllowingStateLoss();
    }

    public void showDilog(String s) {
        if (dilog != null && dilog.isShowing()) {
            dilog.dismiss();
        }
        dilog = new MyProgressDialog(this, s);
        dilog.show();
    }
    public void showDilog(String s, boolean cancelAble) {
        dilog = new MyProgressDialog(this, s);
        dilog.setCancelAble(cancelAble);
        dilog.show();
    }

    public void MissDilog() {
        if (dilog != null) {
            if (dilog.isShowing()) {
                dilog.dismiss();
                dilog = null;
            }
        }
    }

    /**
     * 替换fragment，不添加进返回栈，不添加转场动画
     *
     * @param home
     */
    public void showFragmentWithoutBackStackAndAnim(Fragment home) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();

        fragmentTransaction.replace(R.id.layout, home).commitAllowingStateLoss();
    }

    public void transte(int a, ImageView iv) {
        ObjectAnimator.ofFloat(iv, "x", (width / 8) * (a + 1)).setDuration(0)
                .start();
    }

    private GMKeyCommand gmKeyCommand;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (Constant.netStatus && App.getContext().readVoiceContrl()) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_UP:
                    gmKeyCommand = new GMKeyCommand();
                    gmKeyCommand.setKey(GMKeyCommand.GMKeyEventVolumeUp);
                    GMDeviceController.getInstance().sendCommand(gmKeyCommand);
                    return true;
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    gmKeyCommand = new GMKeyCommand();
                    gmKeyCommand.setKey(GMKeyCommand.GMKeyEventVolumeDown);
                    GMDeviceController.getInstance().sendCommand(gmKeyCommand);
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void setYaokongBackground(ImageView iv, final Context context, final String message) {
        if (Constant.netStatus) {
            iv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(context, RemountActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(context, NewSearchDeviceActivity.class);
                    intent.putExtra("laiyuan", message);
                    startActivity(intent);
                }
            });
        }
    }

    public void back(ImageView iv) {

        iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    /**
     * 控制标题的显示于隐藏
     */

    public void controlTitle(View view, boolean back, boolean title, boolean search, boolean remount) {
        ImageView iv = (ImageView) view.findViewById(R.id.back);
        ImageView iv_search = (ImageView) view.findViewById(R.id.iv_search);
        iv_remount = (ImageView) view.findViewById(R.id.iv_remount);
        tv = (TextView) view.findViewById(R.id.tv_titile);
        if (!back) {
            iv.setVisibility(View.GONE);
        }
        if (!title) {
            tv.setVisibility(View.GONE);
        }
        if (!search) {
            iv_search.setVisibility(View.GONE);
        }
        if (!remount) {
            iv_remount.setVisibility(View.GONE);
        }
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    protected void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

    }

    public void hideInput() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
