package com.xgimi.zhushou.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
 * Created by Administrator on 2016/10/20.
 */
public class SearchMovieAllAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<SearchMovieResult.DataBean.ListBean> mDatas;

    public SearchMovieAllAdapter(Context context, List<SearchMovieResult.DataBean.ListBean> datas) {
        this.mContext = context;
        this.mDatas = datas;
        mLayoutInflater = LayoutInflater.from(context);
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
                ((ItemViewHolder) holder).tv.setText(mDatas.get(position).name);
                // ((ItemViewHolder)holder).kind.setText(mDatas.get(position).category.equals("")?"未知":mDatas.get(position).category);
                ((ItemViewHolder) holder).area.setText(mDatas.get(position).area.equals("") ? "未知" : mDatas.get(position).area);
                ((ItemViewHolder) holder).time.setText(mDatas.get(position).year.equals("") ? "未知" : mDatas.get(position).year);
                ((ItemViewHolder) holder).actor.setText(mDatas.get(position).actors.equals("") ? "未知" : mDatas.get(position).actors);
                ((ItemViewHolder) holder).tv_laiyuan.setText(mDatas.get(position).category.equals("") ? "未知" : mDatas.get(position).category);
//                if(mDatas.get(position).source.size()>0&&mDatas.get(position).source.get(0).gm_intent!=null&&!mDatas.get(position).source.get(0).gm_intent.n.equals("")){
//                    String a=mDatas.get(position).source.get(0).gm_intent.n;
//                    ((ItemViewHolder)holder).tv_laiyuan.setText(mDatas.get(position).source.get(0).gm_intent.n.equals("")?"未知":mDatas.get(position).source.get(0).gm_intent.n);
//                }


//                ImageLoaderUtils.display(mContext, ((ItemViewHolder) holder).iv, mDatas.get(position).image);

                ImageLoader.getInstance().displayImage(mDatas.get(position).image, ((ItemViewHolder) holder).iv);
                if (mLisener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mLisener.onItemCliceLisener(mDatas.get(position).video_id, "item");

                        }
                    });
                }
                if (mLisener != null) {
                    ((ItemViewHolder) holder).ll_play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mLisener.onItemCliceLisener(mDatas.get(position).video_id, "play");
                            liJiBoFang(mDatas, position);
                        }
                    });
                }
            }
        }
    }

    public void dataChange(List<SearchMovieResult.DataBean.ListBean> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDatas != null && mDatas.size() > 0 ? mDatas.size() : 0;
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

    private boolean isInstall;
    private boolean mIsInstall = true;

    public void liJiBoFang(List<SearchMovieResult.DataBean.ListBean> mDatas, int position) {
        if (mDatas != null) {
            isInstall = false;
            if (Constant.netStatus) {
                if (ToosUtil.getInstance().isInstallTvControl((Activity) mContext)) {
                    ToosUtil.getInstance().addEventUmeng(mContext, "event_movie_play");
                    if (mDatas != null && mDatas.get(position).source != null && mDatas.get(position).source.size() > 0) {
                        for (int i = 0; i < ToosUtil.getInstance().mInstallPacageNames.size(); i++) {
                            if (mDatas.get(position).source.get(0).gm_intent.p.equals(ToosUtil.getInstance().mInstallPacageNames.get(i))) {
                                isInstall = true;
//                                addSql(mId);
                                if (mDatas.get(position).source.get(0).gm_intent != null && mDatas.get(position).source.get(0).gm_intent.gm_is != null && !mDatas.get(position).source.get(0).gm_intent.gm_is.equals("") && mDatas.get(position).source.get(0).gm_intent.gm_is != null) {
                                    VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30000, "2", GMSdkCheck.appId, DeviceUtils.getappVersion(mContext), App.getContext().PACKAGENAME, new VcontrolCmd.ThirdPlay(mDatas.get(position).source.get(0).gm_intent.n,
                                            mDatas.get(position).source.get(0).gm_intent.o,
                                            mDatas.get(position).source.get(0).gm_intent.u,
                                            mDatas.get(position).source.get(0).gm_intent.p,
                                            mDatas.get(position).source.get(0).gm_intent.gm_is.i,
                                            mDatas.get(position).source.get(0).gm_intent.gm_is.m + ""), null, null, null, null)));
                                }
//                                    Toast.makeText(FilmDetailActivity.this, "正在无屏电视上打开" + mDatas.data.source.get(0).gm_intent.n, Toast.LENGTH_SHORT).show();
                            }
                        }
                        Intent inten = new Intent(mContext, RemountActivity.class);
                        mContext.startActivity(inten);
//                        if (isInstall) {
//                            Toast.makeText(mContext, "正在无屏电视上打开" + mDatas.get(position).source.get(0).gm_intent.n, Toast.LENGTH_SHORT).show();
//                        }
//                        else {
//                                Toast.makeText(FilmDetailActivity.this, "正在无屏电视上安装" + mDatas.data.source.get(0).gm_intent.n, Toast.LENGTH_SHORT).show();
                        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30000, "2", GMSdkCheck.appId, DeviceUtils.getappVersion(mContext), App.getContext().PACKAGENAME, new VcontrolCmd.ThirdPlay(mDatas.get(position).source.get(0).gm_intent.n,
                                mDatas.get(position).source.get(0).gm_intent.o,
                                mDatas.get(position).source.get(0).gm_intent.u,
                                mDatas.get(position).source.get(0).gm_intent.p,
                                mDatas.get(position).source.get(0).gm_intent.gm_is.i,
                                mDatas.get(position).source.get(0).gm_intent.gm_is.m + ""), null, null, null, null)));
                        Toast.makeText(mContext, "正在打开", Toast.LENGTH_SHORT).show();
//                            if (mIsInstall) {
                        //Toast.makeText(mContext, "正在无屏电视上打开" + mDatas.get(position).source.get(0).gm_intent.n, Toast.LENGTH_SHORT).show();
                        // Intent inten = new Intent(mContext, RemountActivity.class);
                        //  mContext.startActivity(inten);
//                            } else {
                        //Toast.makeText(mContext, "正在无屏电视上安装" + mDatas.get(position).source.get(0).gm_intent.n, Toast.LENGTH_SHORT).show();
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
