package com.xgimi.zhushou.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.utils.FileUtils;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.TvApp;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;
import com.xgimi.zhushou.util.ToastUtil;
import com.xgimi.zhushou.util.ToosUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/6/21.
 */
public class AppManageAdapter extends RecyclerView.Adapter {

    private static final String TAG = "AppManageAdapter";
    public Context mContext;
    public List<TvApp.Appitem> data;
    public String ip;
    public LayoutInflater mlaLayoutInflater;
    String url = "http://%s:16741/data/data/com.xgimi.vcontrol/app_appDatas/%s";

    public AppManageAdapter(Context context, List<TvApp.Appitem> data, String ip) {
        this.mContext = context;
        this.data = data;
        this.ip = ip;
        mlaLayoutInflater = LayoutInflater.from(context);
    }

    public void dataChange(List<TvApp.Appitem> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mlaLayoutInflater.inflate(R.layout.app_manage_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (data != null && data.size() > 0) {
            ((ItemViewHolder) holder).name.setText(data.get(position).appName);
            ((ItemViewHolder) holder).size.setText("大小:" + FileUtils.formatFileSize(data.get(position).length));
            ImageLoaderUtils.display(mContext, ((ItemViewHolder) holder).icon, String.format(url, ip, data.get(position).packageName + ".xgimi"));
            ((ItemViewHolder) holder).open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    GMAppController.getInstance().openApp(data.get(position).packageName);
                    if (ToosUtil.getInstance().isInstallTvControl((Activity) mContext)) {
                        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(20000, "2", GMSdkCheck.appId, null,
                                null, new VcontrolCmd.ControlCmd(7, 1, 0, data.get(position).packageName, null, null), null, null)));
                        Log.d(TAG, "onClick: " + data.get(position).packageName);
                    }
                }
            });
            ((ItemViewHolder) holder).close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    GMAppController.getInstance().uninstallApp(data.get(position).packageName);
                    if (ToosUtil.getInstance().isInstallTvControl((Activity) mContext)) {

                        if (!data.get(position).systemApp) {

                            VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(20000, "2", GMSdkCheck.appId, null,
                                    null, new VcontrolCmd.ControlCmd(7, 2, 0, data.get(position).packageName, null, null), null, null)));
                            data.remove(position);
                            notifyDataSetChanged();
                        } else {
//                            Toast.makeText(mContext, "系统应用不能卸载", Toast.LENGTH_SHORT).show();
                            ToastUtil.getToast("系统应用不能卸载", mContext).show();
                        }
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size() > 0 ? data.size() : 0;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public final ImageView icon;
        public final TextView name;
        public final TextView size;
        public final TextView open;
        public final TextView close;

        public ItemViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.app_icon);
            name = (TextView) itemView.findViewById(R.id.app_name);
            size = (TextView) itemView.findViewById(R.id.app_size);
            open = (TextView) itemView.findViewById(R.id.open);
            close = (TextView) itemView.findViewById(R.id.close);
        }
    }
}
