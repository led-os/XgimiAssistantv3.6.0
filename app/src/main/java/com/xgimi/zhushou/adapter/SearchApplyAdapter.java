package com.xgimi.zhushou.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgimi.device.callback.FeedBackInforInterface;
import com.xgimi.device.device.FeedbackInfo;
import com.xgimi.device.device.GMDeviceConnector;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.ApplyDetailActivity;
import com.xgimi.zhushou.activity.NewSearchDeviceActivity;
import com.xgimi.zhushou.bean.ApplySearc;
import com.xgimi.zhushou.bean.TvApp;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.SaveTVApp;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;
import com.xgimi.zhushou.widget.CircleImageView;
import com.xgimi.zhushou.widget.SignOutDilog;
import com.xgimi.zhushou.widget.ViewPagerDilog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchApplyAdapter extends BaseAdapter implements OnClickListener,
        FeedBackInforInterface {

    private Context mContext;
    private ApplySearc mSearch;
    private int heig;
    private int width;
    private Button button;
    private Button mTv;
    List<VcontrolCmd.CustomPlay.PlayList> mPlays = new ArrayList<>();

    public SearchApplyAdapter(Context context, ApplySearc search, int height,
                              int width) {
        this.mContext = context;
        this.mSearch = search;
        this.width = width;
        this.heig = height;
        GMDeviceConnector.getInstance().setFeedBackInforInterface(this);
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mSearch.data != null ? mSearch.data.size() : 0;
    }

    public void dataChange(ApplySearc sea) {
        this.mSearch = sea;
        notifyDataSetChanged();
    }

    @Override
    public ApplySearc.ApplySearchItem getItem(int position) {
        // TODO Auto-generated method stub
        return mSearch.data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = convertView;
        final ApplySearc.ApplySearchItem applySearchItem = mSearch.data.get(position);
        final ViewHolder vh = new ViewHolder();
        view = View.inflate(mContext, R.layout.searc_apply_item, null);
        vh.hor = (HorizontalScrollView) view.findViewById(R.id.horscroview);
        vh.iv = (CircleImageView) view.findViewById(R.id.app_icon);
        vh.name = (TextView) view.findViewById(R.id.app_name);
        vh.banben = (TextView) view.findViewById(R.id.app_banben);
        vh.size = (TextView) view.findViewById(R.id.app_size);
        vh.iv_anzhuang = (Button) view.findViewById(R.id.iv_anzhuang);
        addApplyShoot(vh.hor, applySearchItem);
        if (SaveTVApp.getInstance(mContext).mTvApp != null && SaveTVApp.getInstance(mContext).mTvApp != null
                && SaveTVApp.getInstance(mContext).mTvApp.appList.size() > 0) {
            for (int i = 0; i < SaveTVApp.getInstance(mContext).mTvApp.appList
                    .size(); i++) {
                String appitem = SaveTVApp.getInstance(mContext).mTvApp.appList
                        .get(i).packageName;
//                XGIMILOG.D("" + appitem);
                if (appitem.equals(applySearchItem.package_name)) {
                    vh.iv_anzhuang.setText("打开");
                }
            }
        }
        vh.iv_anzhuang.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (Constant.netStatus) {
                    if (Constant.netStatus) {
                        if (ToosUtil.getInstance().isInstallTvControl((Activity) mContext)) {

                            if (vh.iv_anzhuang.getText().toString().trim()
                                    .equals("打开")) {
                                VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(20000, "2", GMSdkCheck.appId, null,
                                        null, new VcontrolCmd.ControlCmd(7, 1, 0, mSearch.data.get(position).package_name, null, null), null, null)));
                            } else if (vh.iv_anzhuang.getText().toString().trim()
                                    .equals("远程安装")) {
                                mPlays.clear();
                                mTv = vh.iv_anzhuang;
                                VcontrolCmd.CustomPlay.PlayList playList = new VcontrolCmd.CustomPlay.PlayList(
                                        ".apk", mSearch.data.get(position).package_name, mSearch.data.get(position).icon, mSearch.data.get(position).download_url, mSearch.data.get(position).title, null);
                                mPlays.add(playList);
                                VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200,
                                        "2", GMSdkCheck.appId, null, new VcontrolCmd.CustomPlay(3, 0, null, mPlays, 0), null, null, null)));
                                vh.iv_anzhuang.setText("正在下载");

//							button=vh.iv_anzhuang;
                            }
                        }
                    }
                } else {
                    SignOutDilog singDilog = new SignOutDilog(mContext,
                            "是否现在连接无屏电视");
                    singDilog.show();
                    singDilog.setOnLisener(new SignOutDilog.onListern() {

                        @Override
                        public void send() {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(mContext,
                                    NewSearchDeviceActivity.class);
                            mContext.startActivity(intent);
                        }
                    });
                }

            }
        });
        ImageLoader.getInstance().displayImage(applySearchItem.icon, vh.iv);
        vh.iv_anzhuang.setTag(applySearchItem);
        vh.name.setText(applySearchItem.title);
        vh.banben.setText("版本号 " + applySearchItem.version);
        vh.size.setText("大小 " + applySearchItem.file_size);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplySearc.ApplySearchItem item = getItem(position);
                Intent intent = new Intent(mContext, ApplyDetailActivity.class);
                intent.putExtra("id", item.id);
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    // 应用推荐的horscrollview
    private void addApplyShoot(HorizontalScrollView horscroview,
                               final ApplySearc.ApplySearchItem app) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        for (int i = 0; i < app.screenshots.size(); i++) {
            LinearLayout itemLinearlayout = new LinearLayout(mContext);
            itemLinearlayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    width / 2, heig / 5);
            final int mpos = i;
            ImageView iv = new ImageView(mContext);
            layoutParams.leftMargin = 10;
            ImageLoader.getInstance().displayImage(app.screenshots.get(i), iv);
            iv.setLayoutParams(layoutParams);
            iv.setScaleType(ScaleType.FIT_XY);
            itemLinearlayout.addView(iv);
            linearLayout.addView(itemLinearlayout);
            iv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    ViewPagerDilog vp = new ViewPagerDilog(mContext, (ArrayList<String>) app.screenshots, mpos);
                    vp.show();
                }
            });
        }
        horscroview.addView(linearLayout);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            default:
                break;
        }
    }

    // 发送json数据给投影一
    public String sendJson(String url, String title, String packageName,
                           String iconUrl) {
        JSONObject jsonObject = new JSONObject();
        JSONObject js2 = new JSONObject();
        try {
            jsonObject.put("title", title);
            jsonObject.put("url", url);
            jsonObject.put("packageName", packageName);
            jsonObject.put("iconUrl", iconUrl);
            js2.put("data", jsonObject);
            js2.put("action", 1);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(js2.toString());
        return js2.toString();
    }

//	@Override
//	public void musicInfor(String arg0) {
//
//		// TODO Auto-generated method stub
//
//
//
//		if (arg0.startsWith("APP")) {
//			button.setText("打开");
//		} else {
//			JSONObject josnJsonObject;
//			try {
//				josnJsonObject = new JSONObject(arg0);
//				int action = josnJsonObject.getInt("action");
//				if (action == 200) {
//					handler.sendEmptyMessage(2);
//				} else if (action == 201) {
//					handler.sendEmptyMessage(3);
//					handler.sendEmptyMessageDelayed(1, 600);
//				} else if (action == 202) {
//
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//	}


    @Override
    public void receive(FeedbackInfo info) {
        XGIMILOG.D("安装app : " + new Gson().toJson(info));
        Message message = mhanHandler.obtainMessage();
        message.obj = info;
        message.what = 0;
        mhanHandler.sendMessage(message);
    }

    private String mCurrentInstallAppName = null;
    Handler mhanHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            if (mTv != null) {
                FeedbackInfo info = (FeedbackInfo) msg.obj;
                if (info.downloadInfo != null && info.downloadInfo.progress < 99) {
                    mTv.setText("正在下载" + info.downloadInfo.progress + "%");
                    mCurrentInstallAppName = info.downloadInfo.filename;
                } else if (info.downloadInfo != null && info.downloadInfo.progress == 99) {
                    mTv.setText("正在安装");
                } else {
                    if (info.installInfo != null) {
                        if (info.installInfo.stat == 1) {
                            mTv.setText("打开");
                            VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(null,"2",
                                    new VcontrolCmd.ControlCmd(7,3,0), 20000)));
                        }
                    }
                }
            }
        }
    };

}

class ViewHolder {
    public HorizontalScrollView hor;
    public CircleImageView iv;
    public TextView name;
    public TextView size;
    public TextView banben;
    public Button iv_anzhuang;

}
