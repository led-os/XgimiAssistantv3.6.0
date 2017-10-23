package com.xgimi.zhushou.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.RemountActivity;
import com.xgimi.zhushou.adapter.SourceAdapter;
import com.xgimi.zhushou.bean.FilmDetailBean;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.DeviceUtils;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.ToosUtil;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by 霍长江 on 2016/8/16.
 */

/**
 * 投屏源对话框
 */
public class SourceDilog {

    public Dialog mDialog;
    private SourceAdapter mAdapter;
    private Activity mconte;
    private boolean isInstall = false;
    private ItemClick mOnItemClickerListener;

    public SourceDilog(final Activity context, final FilmDetailBean mDatas, List<FilmDetailBean.DataBean.SourceBean> mDatas1) {
        LayoutInflater inflater = LayoutInflater.from(context);
        this.mconte = context;
        View view = inflater.inflate(R.layout.sorurce_number, null);
        ListView listview = (ListView) view.findViewById(R.id.lisetview);
        mAdapter = new SourceAdapter(context, mDatas1);
        listview.setAdapter(mAdapter);
        mDialog = new Dialog(context, R.style.dialog);
        mDialog.setContentView(view);
        mDialog.getWindow().setGravity(Gravity.BOTTOM);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (Constant.netStatus) {
                    isInstall = false;
                    FilmDetailBean.DataBean.SourceBean item = mAdapter.getItem(position);
                    if (item.gm_intent != null && item.gm_intent.gm_is != null) {

                        for (int i = 0; i < ToosUtil.getInstance().mInstallPacageNames.size(); i++) {
//                            if ("com.hunantv.market".equals(item.gm_intent.p) && "com.hunantv.license".equals(ToosUtil.getInstance().mInstallPacageNames.get(i))) {
//                                if (item.gm_intent != null && item.gm_intent.gm_is != null && !item.gm_intent.gm_is.equals("") && item.gm_intent.gm_is != null) {
//                                    isInstall = true;
//                                    String type = mDatas.data.kind + "." + mDatas.data.category;
//                                    VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30000, "2", GMSdkCheck.appId, DeviceUtils.getappVersion(context), App.getContext().PACKAGENAME, new VcontrolCmd.ThirdPlay(
//                                            type, mDatas.data.title, mDatas.data.id, 0, SaveData.getInstance().mSoucePage, SaveData.getInstance().mSouceInsight, SaveData.getInstance().mSourceInsightLocation,
//                                            item.gm_intent.n,
//                                            item.gm_intent.o,
//                                            item.gm_intent.u,
//                                            item.gm_intent.p,
//                                            item.gm_intent.gm_is.i, item.gm_intent.gm_is.m), null, null, null, null)));
//                                    Log.e("banben", DeviceUtils.getappVersion(context) + App.getContext().PACKAGENAME);
//                                }
//                            }
                            if (item.gm_intent.p.equals(ToosUtil.getInstance().mInstallPacageNames.get(i))) {
//                                    FilmDetailBean data = new Gson().fromJson(readHomeJson, FilmDetailBean.class);
                                if (item.gm_intent != null && item.gm_intent.gm_is != null && !item.gm_intent.gm_is.equals("") && item.gm_intent.gm_is != null) {
                                    isInstall = true;
                                    String type = mDatas.data.kind + "." + mDatas.data.category;
                                    if (mOnItemClickerListener != null) {
                                        mOnItemClickerListener.onItemClick();
                                    }
                                    VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30000, "2", GMSdkCheck.appId, DeviceUtils.getappVersion(context), App.getContext().PACKAGENAME, new VcontrolCmd.ThirdPlay(
                                            type, mDatas.data.title, mDatas.data.id, 0, SaveData.getInstance().mSoucePage, SaveData.getInstance().mSouceInsight, SaveData.getInstance().mSourceInsightLocation,
                                            item.gm_intent.n,
                                            item.gm_intent.o,
                                            item.gm_intent.u,
//                                            item.gm_intent.p.equals("com.hunantv.license") ? "com.hunantv.market" : item.gm_intent.p,
                                            item.gm_intent.p,
                                            item.gm_intent.gm_is.i, item.gm_intent.gm_is.m), null, null, null, null)));
                                    Log.e("banben", DeviceUtils.getappVersion(context) + App.getContext().PACKAGENAME);
                                }
                            }
                        }
                    }
                    mDialog.dismiss();
                    mDialog.cancel();
                    if (isInstall) {
                        Intent inten = new Intent(mconte, RemountActivity.class);
                        mconte.startActivity(inten);
                        Toast.makeText(mconte, "正在无屏电视上打开" + item.gm_intent.n, Toast.LENGTH_SHORT).show();
                    } else {
                        //检测到未安装，发送下载安装命令
                        Toast.makeText(mconte, "正在无屏电视上安装" + item.gm_intent.n, Toast.LENGTH_SHORT).show();
                        String type1 = mDatas.data.kind + "." + mDatas.data.category;
                        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30000, "2", GMSdkCheck.appId, new VcontrolCmd.ThirdPlay(
                                type1, mDatas.data.title, mDatas.data.id, 0, null, null,
                                DeviceUtils.getappVersion(context),
                                item.gm_intent.n,
                                item.gm_intent.o,
                                item.gm_intent.u,
                                item.gm_intent.p,
                                item.gm_intent.gm_is.i, item.gm_intent.gm_is.m), null, null, null, null)));
                    }


                    if (item.from.equals("iqiyi")) {
                        EventBus.getDefault().post(item);
                    }
                } else {
                    ToosUtil.getInstance().isConnectTv(mconte);
                }
            }
        });
    }

    public void setOnItemClickerListener(ItemClick mOnItemClickerListener) {
        this.mOnItemClickerListener = mOnItemClickerListener;
    }

    public void show() {
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        WindowManager windowManager = mconte.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        mDialog.getWindow().setAttributes(lp);
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        mDialog.setCanceledOnTouchOutside(cancel);
    }

    public void dismiss() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public boolean isShowing() {
        if (mDialog.isShowing()) {
            return true;
        }
        return false;
    }

    public interface ItemClick {
        void onItemClick();
    }

}
