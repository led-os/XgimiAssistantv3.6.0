package com.xgimi.zhushou.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.RemountActivity;
import com.xgimi.zhushou.bean.SearchMovieResult;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.DeviceUtils;
import com.xgimi.zhushou.util.ToosUtil;

import java.util.List;

/**
 * Created by 霍长江 on 2016/8/13.
 */
public class SearchMovieAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private int num;
    private List<SearchMovieResult.DataBean> mDatas;

    public SearchMovieAdapter(Context context, List<SearchMovieResult.DataBean> datas, int num) {
        this.mContext = context;
        this.mDatas = datas;
        this.num = num;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void dataChange(List<SearchMovieResult.DataBean> datas, int num) {
        this.mDatas = datas;
        this.num = num;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.search_movie_item, parent, false);
        return new ItemViewHolder(view);
    }

    public OnitemClick mLisener;

    public void setLisener(OnitemClick lisener) {
        this.mLisener = lisener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (mDatas != null) {
            if (holder instanceof ItemViewHolder) {
                ((ItemViewHolder) holder).tv.setText(mDatas.get(num).list.get(position).name);
                // ((ItemViewHolder) holder).kind.setText(mDatas.get(num).list.get(position).category.equals("") ? "未知" : mDatas.get(num).list.get(position).category);
                ((ItemViewHolder) holder).area.setText(mDatas.get(num).list.get(position).area.equals("") ? "未知" : mDatas.get(num).list.get(position).area);
                ((ItemViewHolder) holder).time.setText(mDatas.get(num).list.get(position).year.equals("") ? "未知" : mDatas.get(num).list.get(position).year);
                ((ItemViewHolder) holder).actor.setText(mDatas.get(num).list.get(position).actors.equals("") ? "未知" : mDatas.get(num).list.get(position).actors);
                if (mDatas.get(num).list.get(position).source.size() > 0 && mDatas.get(num).list.get(position).source.get(0).gm_intent != null && !mDatas.get(num).list.get(position).source.get(0).gm_intent.n.equals("")) {
                    String a = mDatas.get(num).list.get(position).source.get(0).gm_intent.n;
                    Log.e("main", a);
                    ((ItemViewHolder) holder).tv_laiyuan.setText(mDatas.get(num).list.get(position).source.get(0).gm_intent.n.equals("") ? "未知" : mDatas.get(num).list.get(position).source.get(0).gm_intent.n);
                }
//                ImageLoaderUtils.display(mContext, ((ItemViewHolder) holder).iv, mDatas.get(num).list.get(position).image);
                ImageLoader.getInstance().displayImage(mDatas.get(num).list.get(position).image, ((ItemViewHolder) holder).iv);

                if (mLisener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mLisener.onItemCliceLisener(mDatas.get(num).list.get(position).video_id, "item");
                        }
                    });
                }
                if (mLisener != null) {
                    ((ItemViewHolder) holder).ll_play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mLisener.onItemCliceLisener(mDatas.get(num).list.get(position).video_id, "play");
                            liJiBoFang(mDatas, num, position);
                        }
                    });
                }
            }
        }
    }

    public void dataChange(List<SearchMovieResult.DataBean> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDatas != null && mDatas.get(num).list.size() > 0 ? mDatas.get(num).list.size() : 0;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv;
        public TextView tv;
        public TextView kind;
        public TextView time;
        public TextView area;
        public TextView actor;
        public TextView tv_laiyuan;
        public LinearLayout ll_play;

        public ItemViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            tv = (TextView) itemView.findViewById(R.id.tv);
            kind = (TextView) itemView.findViewById(R.id.kind);
            time = (TextView) itemView.findViewById(R.id.time);
            area = (TextView) itemView.findViewById(R.id.area);
            actor = (TextView) itemView.findViewById(R.id.actor);
            tv_laiyuan = (TextView) itemView.findViewById(R.id.tv_laiyuan);
            ll_play = (LinearLayout) itemView.findViewById(R.id.ll_play);
        }
    }

    public interface OnitemClick {
        void onItemCliceLisener(String id, String type);
    }

    private boolean isInstall = true;
    private boolean mIsInstall = true;

    public void liJiBoFang(List<SearchMovieResult.DataBean> mDatas, int num, int position) {
        if (mDatas != null) {
            //  isInstall=false;
            if (Constant.netStatus) {
                if (ToosUtil.getInstance().isInstallTvControl((Activity) mContext)) {
                    ToosUtil.getInstance().addEventUmeng(mContext, "event_movie_play");
                    if (mDatas.get(num).list != null && mDatas.get(num).list.get(position).source != null && mDatas.get(num).list.get(position).source.size() > 0) {
                        for (int i = 0; i < ToosUtil.getInstance().mInstallPacageNames.size(); i++) {
                            if (mDatas.get(num).list.get(position).source.get(0).gm_intent.p.equals(ToosUtil.getInstance().mInstallPacageNames.get(i))) {
                                isInstall = true;
//                                addSql(mId);
                                if (mDatas.get(num).list.get(position).source.get(0).gm_intent != null && mDatas.get(num).list.get(position).source.get(0).gm_intent.gm_is != null && !mDatas.get(num).list.get(position).source.get(0).gm_intent.gm_is.equals("") && mDatas.get(num).list.get(position).source.get(0).gm_intent.gm_is != null) {
                                    VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30000, "2", GMSdkCheck.appId, DeviceUtils.getappVersion(mContext), App.getContext().PACKAGENAME, new VcontrolCmd.ThirdPlay(mDatas.get(num).list.get(position).source.get(0).gm_intent.n,
                                            mDatas.get(num).list.get(position).source.get(0).gm_intent.o,
                                            mDatas.get(num).list.get(position).source.get(0).gm_intent.u,
                                            mDatas.get(num).list.get(position).source.get(0).gm_intent.p,
                                            mDatas.get(num).list.get(position).source.get(0).gm_intent.gm_is.i,
                                            mDatas.get(num).list.get(position).source.get(0).gm_intent.gm_is.m + ""), null, null, null, null)));
                                }
//                                    Toast.makeText(FilmDetailActivity.this, "正在无屏电视上打开" + mDatas.data.source.get(0).gm_intent.n, Toast.LENGTH_SHORT).show();
                            }
                        }
                        Intent inten = new Intent(mContext, RemountActivity.class);
                        mContext.startActivity(inten);
//                        if (isInstall) {
//                            Toast.makeText(mContext, "正在无屏电视上打开" + mDatas.get(num).list.get(position).source.get(0).gm_intent.n, Toast.LENGTH_SHORT).show();
//                        }
//                        else {
//                                Toast.makeText(FilmDetailActivity.this, "正在无屏电视上安装" + mDatas.data.source.get(0).gm_intent.n, Toast.LENGTH_SHORT).show();
                        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30000, "2", GMSdkCheck.appId, DeviceUtils.getappVersion(mContext), App.getContext().PACKAGENAME, new VcontrolCmd.ThirdPlay(mDatas.get(num).list.get(position).source.get(0).gm_intent.n,
                                mDatas.get(num).list.get(position).source.get(0).gm_intent.o,
                                mDatas.get(num).list.get(position).source.get(0).gm_intent.u,
                                mDatas.get(num).list.get(position).source.get(0).gm_intent.p,
                                mDatas.get(num).list.get(position).source.get(0).gm_intent.gm_is.i,
                                mDatas.get(num).list.get(position).source.get(0).gm_intent.gm_is.m + ""), null, null, null, null)));
                        Toast.makeText(mContext, "请稍等", Toast.LENGTH_SHORT).show();
//                            if (mIsInstall) {
//                                Toast.makeText(mContext, "正在无屏电视上打开" + mDatas.get(num).list.get(position).source.get(0).gm_intent.n, Toast.LENGTH_SHORT).show();
//                                Intent inten = new Intent(mContext, RemountActivity.class);
//                                mContext.startActivity(inten);
//                            } else {
//                                Toast.makeText(mContext, "正在无屏电视上安装" + mDatas.get(num).list.get(position).source.get(0).gm_intent.n, Toast.LENGTH_SHORT).show();
//                            }
//                                Intent inten = new Intent(FilmDetailActivity.this, RemountActivity.class);
//                                startActivity(inten);
//                        }
//                            else{
//                                VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30000, "2", GMSdkCheck.appId, new VcontrolCmd.ThirdPlay(mDatas.data.source.get(0).gm_intent.n,
//                                        mDatas.data.source.get(0).gm_intent.o,
//                                        mDatas.data.source.get(0).gm_intent.u,
//                                        mDatas.data.source.get(0).gm_intent.p,
//                                        mDatas.data.source.get(0).gm_intent.gm_is.get(0).i,
//                                        mDatas.data.source.get(0).gm_intent.gm_is.get(0).m), null, null, null, null)));

//                            }
                    }
                }

            } else {
                ToosUtil.getInstance().isConnectTv((Activity) mContext);
            }
        }
    }
}
