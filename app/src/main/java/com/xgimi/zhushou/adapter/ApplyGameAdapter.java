package com.xgimi.zhushou.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xgimi.device.device.FeedbackInfo;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.MoreAppActivity;
import com.xgimi.zhushou.activity.NewMoreAppActivity;
import com.xgimi.zhushou.activity.RemountActivity;
import com.xgimi.zhushou.bean.ApplySearc;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.SaveTVApp;
import com.xgimi.zhushou.util.ToosUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 霍长江 on 2016/8/29.
 */
public class ApplyGameAdapter extends BaseAdapter {
    private Context mContext;
    private ApplySearc mData;
    private ApplySearc.ApplySearchItem applySearchItem;
    List<VcontrolCmd.CustomPlay.PlayList> mPlays = new ArrayList<>();
    private TextView mTv;
    private String mTitle;
    private String packgeNmae;
    private List<Integer> mListPos = new ArrayList<>();
    private int mPos = -1;
    private int status = 0;
    private boolean isLoading;
    private FeedbackInfo myInfo;
    //现在点击的title
    private String nowTitle;
    //判断是否已经刷新，让他只notify一次不然要卡
    private boolean isNoti;
    HashMap<String, TextView> mTVMaps = new HashMap<>();
    HashMap<String, Integer> mTitleMaps = new HashMap<>();
    //保存多个同时下载的时候哪些显示为排队中
    HashMap<Integer, Integer> mIntMaps = new HashMap<>();
    HashMap<Integer, TextView> hashMaps = new HashMap<>();

    public ApplyGameAdapter(Context context, ApplySearc data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData != null && mData.data != null ? mData.data.size() : 0;
    }

    public void dataChange(ApplySearc data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void setText(FeedbackInfo info) {
        if (info.downloadInfo != null) {
            //获得这个集合的迭代器，保存在iter里
            Iterator iter = mTVMaps.entrySet().iterator();
            Iterator iter1 = mTitleMaps.entrySet().iterator();
            while (iter1.hasNext()) {
                //能获得map中的每一个键值对了
                Map.Entry entry1 = (Map.Entry) iter1.next();
                Object key1 = entry1.getKey();
                Object value = entry1.getValue();
                if (info.downloadInfo.filename.equals(key1.toString())) {
                    mPos = mTitleMaps.get(key1);
                    Log.e("mpos", "前面个pos" + mPos);
                    if (info.downloadInfo.progress == 0) {
                        if (!isNoti) {
                            notifyDataSetChanged();
                            isNoti = true;
                            mTv.setText("排队中");
                        }
                    } else {
                        isNoti = false;
                    }
                }
            }
            while (iter.hasNext()) {
                //能获得map中的每一个键值对了
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object value = entry.getValue();
                if (info.downloadInfo.filename.equals(key.toString())) {

                    myInfo = info;
                    status = 0;
                    Message message = mhanHandler.obtainMessage();
                    message.obj = info;
                    message.what = 0;
                    myInfo = info;
                    mhanHandler.sendMessage(message);
                }
            }
        }
        if (info.installInfo != null && mTv != null) {
            if (packgeNmae.equals(info.installInfo.packagename)) {
                Message message = mhanHandler.obtainMessage();
                isLoading = false;
                mListPos.add(mPos);
                message.obj = info;
                message.what = 0;
                status = 1;
                mhanHandler.sendMessage(message);
            }
        }
    }

    @Override
    public ApplySearc.ApplySearchItem getItem(int position) {
        return mData.data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        View view = convertView;
        vh = new ViewHolder();
        view = View.inflate(mContext, R.layout.apply_game_item, null);
        vh.iv = (ImageView) view.findViewById(R.id.iv);
        vh.size = (TextView) view.findViewById(R.id.size);
        vh.kind = (TextView) view.findViewById(R.id.kind);
        vh.ll = (LinearLayout) view.findViewById(R.id.more);
        vh.title = (TextView) view.findViewById(R.id.title);
        vh.open = (TextView) view.findViewById(R.id.open);
        vh.many = (TextView) view.findViewById(R.id.much);
        vh.tv_install = (TextView) view.findViewById(R.id.tv_install);
        ImageLoaderUtils.displayTwo(mContext, vh.iv, mData.data.get(position).icon);
        vh.size.setText(mData.data.get(position).file_size);
        vh.title.setText(mData.data.get(position).title);
        vh.many.setText(mData.data.get(position).plays + "");
        vh.tv_install.setText(" 人玩过");

        if (position == mData.data.size() - 1) {
            vh.ll.setVisibility(View.VISIBLE);
        }
        vh.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToosUtil.getInstance().addEventUmeng(mContext, "event_game_more");
                Intent intent = new Intent(mContext, NewMoreAppActivity.class);
                intent.putExtra("type", "1");
                mContext.startActivity(intent);
            }
        });
        if (SaveTVApp.getInstance(mContext).mTvApp != null && SaveTVApp.getInstance(mContext).mTvApp.appList != null && SaveTVApp.getInstance(mContext).mTvApp.appList.size() > 0) {
            for (int i = 0; i < SaveTVApp.getInstance(mContext).mTvApp.appList.size(); i++) {
                String appitem = SaveTVApp.getInstance(mContext).mTvApp.appList.get(i).packageName;
                if (appitem.equals(mData.data.get(position).package_name)) {
                    vh.open.setBackgroundResource(R.drawable.app_install_open);
                    vh.open.setTextColor(Color.parseColor("#00CC33"));
                    vh.open.setText("打开游戏");
                }
            }
        }
        for (int i = 0; i < mListPos.size(); i++) {
            if (position == mListPos.get(i)) {
                vh.open.setText("打开游戏");
                vh.open.setBackgroundResource(R.drawable.app_install_open);
                vh.open.setTextColor(Color.parseColor("#00CC33"));
            }
        }
        if (mIntMaps != null) {
            Iterator iter1 = mIntMaps.entrySet().iterator();
            while (iter1.hasNext()) {
                //能获得map中的每一个键值对了
                Map.Entry entry1 = (Map.Entry) iter1.next();
                Object key1 = entry1.getKey();
                if (mIntMaps.get(key1) == position) {
                    vh.open.setText("排队中");
                }
            }
        }
        if (mPos == position) {
            Log.e("mpos", mPos + "");
            mTv = vh.open;
            convertView.setFocusable(true);
            convertView.setFocusableInTouchMode(true);
            if (status == 0) {
                if (myInfo != null) {
                    if (myInfo.downloadInfo.progress == -2) {
//                        vh.open.setText("排队中");
                    } else if (myInfo.downloadInfo.progress == -1) {
                        vh.open.setText("下载失败");
                    } else if (myInfo.downloadInfo != null && myInfo.downloadInfo.progress > 0) {
                        vh.open.setText("正在下载" + myInfo.downloadInfo.progress + "%");
                    } else {
                    }
                }
            } else if (status == 1) {
                vh.open.setText("正在安装");
            } else if (status == 2) {
                vh.open.setText("打开游戏");
                vh.open.setBackgroundResource(R.drawable.app_install_open);
                vh.open.setTextColor(Color.parseColor("#00CC33"));
            }
        }
        vh.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.netStatus) {
                    if (ToosUtil.getInstance().isInstallTvControl((Activity) mContext)) {


                        if (vh.open.getText().toString().trim().equals("打开游戏")) {
//                            GMDeviceController.getInstance().senddOpen(detail.data.package_name);
                            ToosUtil.getInstance().addEventUmeng(mContext, "event_app_open");
                            VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(20000, "2", GMSdkCheck.appId, null,
                                    null, new VcontrolCmd.ControlCmd(7, 1, 0, mData.data.get(position).package_name, null, null), null, null)));

                            Intent intent = new Intent(mContext, RemountActivity.class);
                            mContext.startActivity(intent);
                        } else if (vh.open.getText().toString().trim().equals("安装到TV") || vh.open.getText().toString().trim().equals("下载失败")) {
//                            GMDeviceController.getInstance().SendJC(sendJson(detail.data.download_url, detail.data.title+".apk",detail.data.package_name,detail.data.icon));
                            mPlays.clear();
                            vh.open.setText("排队中");
                            mTitle = mData.data.get(position).title;
                            nowTitle = mData.data.get(position).title;
                            mTVMaps.put(mTitle, vh.open);
                            mTitleMaps.put(mTitle, position);
                            mIntMaps.put(position, position);
                            mPos = position;
//                            mTitle = mData.data.get(position).title;
                            packgeNmae = mData.data.get(position).package_name;
                            VcontrolCmd.CustomPlay.PlayList playList = new VcontrolCmd.CustomPlay.PlayList(
                                    ".apk", mData.data.get(position).package_name, mData.data.get(position).icon, mData.data.get(position).download_url, mData.data.get(position).title, null);
                            mPlays.add(playList);
                            ToosUtil.getInstance().addEventUmeng(mContext, "event_game_install");
                            VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200,
                                    "2", GMSdkCheck.appId, null, new VcontrolCmd.CustomPlay(3, 0, null, mPlays, 0), null, null, null)));
                        }
                    }
                } else {
                    ToosUtil.getInstance().isConnectTv((Activity) mContext);
                }
            }
        });
        return view;
    }

    public class ViewHolder {
        ImageView iv;
        TextView size, kind, title, open, many, tv_install;
        LinearLayout ll;
    }

    Handler mhanHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            if (mTv != null) {
                mTv.setFocusable(true);
                mTv.setFocusableInTouchMode(true);
                FeedbackInfo info = (FeedbackInfo) msg.obj;
                myInfo = info;
                if (info.downloadInfo != null && info.downloadInfo.progress == -2) {
//                    mTv.setText("排队中");
                } else if (info.downloadInfo != null && info.downloadInfo.progress == -1) {
                    mTv.setText("下载失败");
                } else if (info.downloadInfo != null && info.downloadInfo.progress < 99) {
                    if (mIntMaps.get(mPos) != null) {
                        mIntMaps.remove(mPos);
                    }
                    mTv.setText("正在下载" + info.downloadInfo.progress + "%");
                    Log.e("key", "handle里面的" + "mTv的值" + mTv.getText().toString() + "---" + "value值" + mPos + "" + "---" + "进度" + info.downloadInfo.filename + info.downloadInfo.progress + "%");
                    isLoading = true;
                } else if (info.downloadInfo != null && info.downloadInfo.progress == 99) {
                    mTv.setText("正在安装");
                } else {

                }
                if (info.installInfo != null && info.installInfo.stat == 1) {
                    mTv.setText("打开游戏");
                    mTv.setBackgroundResource(R.drawable.app_install_open);
                    mTv.setTextColor(Color.parseColor("#00CC33"));
                    status = 2;
                }
//                    if(info.downloadInfo != null&&info.downloadInfo.progress==100){
//                        mTv.setText("打开应用");
//                        status=2;
//                        mTv.setBackgroundResource(R.drawable.app_install_open);
//                        mTv.setTextColor(Color.parseColor("#00CC33"));
//                    }
            }
        }
    };

}
