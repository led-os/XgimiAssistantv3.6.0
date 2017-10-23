package com.xgimi.zhushou.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.RecommendViewHolder.AppFooterViewHolder;
import com.xgimi.zhushou.adapter.RecommendViewHolder.AppHeaderViewHolder;
import com.xgimi.zhushou.bean.NewMvList;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;
import com.xgimi.zhushou.netUtil.NetUtil;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.ToosUtil;

/**
 * Created by Administrator on 2016/12/13.
 */
public class MVNewAdapter extends SectionedRecyclerViewAdapter<AppHeaderViewHolder, NewMVItemViewHolder,AppFooterViewHolder>{
    private NewMvList mDatas;
    private Context context;
    private LayoutInflater mLayoutInflater;
    public MVNewAdapter(Context context,NewMvList mDatas){
        this.context=context;
        mLayoutInflater=LayoutInflater.from(context);
        this.mDatas=mDatas;
    }
    public void dataChange(NewMvList mDatas){
        this.mDatas=mDatas;
        notifyDataSetChanged();
    }
    public OnFooterClick mFooterLisener;
    public void setmFooterLisener(OnFooterClick footerLisener){
        this.mFooterLisener=footerLisener;
    }
    public interface OnFooterClick {
        void clickLisener(int area_id,int section);
    }
    public OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }
    public interface OnClickListener {
        void onClick( int position,int section);
    }
    @Override
    protected int getSectionCount() {
        if(mDatas!=null&&mDatas.data!=null){
            Log.e("cishu",mDatas.data.size()+"");
        }
        return mDatas!=null&&mDatas.data!=null?mDatas.data.size():0;
    }

    @Override
    protected int getItemCountForSection(int section) {
        if(section==0){
            return mDatas!=null&&mDatas.data!=null&&mDatas.data.size()>0&&
                    mDatas.data.get(0).info!=null&&mDatas.data.get(0).info.size()/2>2?3:mDatas.data.get(0).info.size()/2;
        }else if(section>0&&section<mDatas.data.size()){
            return mDatas!=null&&mDatas.data!=null&&mDatas.data.size()>0&&
                    mDatas.data.get(section).info!=null&&mDatas.data.get(section).info.size()/1>2?2:mDatas.data.get(section).info.size()/2;
        }
        return mDatas!=null&&mDatas.data!=null&&mDatas.data.size()>0&&
                mDatas.data.get(section).info!=null&&mDatas.data.get(section).info.size()/2>1?2:mDatas.data.get(section).info.size()/2;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return true;
    }

    @Override
    protected boolean hasHeaderInSection(int section) {
        return true;
    }

    @Override
    protected AppHeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View view=mLayoutInflater.inflate(R.layout.app_lei_header_one,parent,false);
        return new AppHeaderViewHolder(view);
    }

    @Override
    protected AppFooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.animation_footer, parent, false);
        return new AppFooterViewHolder(view);
    }

    @Override
    protected NewMVItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.new_mv_count_item, parent, false);
        return new NewMVItemViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(AppHeaderViewHolder holder, int section) {
        if(mDatas!=null&&mDatas.data!=null&&mDatas.data.size()>0){
            holder.tv.setText(mDatas.data.get(section).area_name);
        }
    }

    @Override
    protected void onBindSectionFooterViewHolder(AppFooterViewHolder holder, final int section) {
        if(mDatas!=null&&mDatas.data!=null&&mDatas.data.size()>0){
            holder.tv_more.setText("更多"+mDatas.data.get(section).area_name+" >");
        }
        if(holder instanceof AppFooterViewHolder){
            if(mFooterLisener!=null){
                ((AppFooterViewHolder) holder).ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFooterLisener.clickLisener(mDatas.data.get(section).area_id,section);
                    }
                });
            }
        }
    }

    @Override
    protected void onBindItemViewHolder(NewMVItemViewHolder holder, final int section, final int position) {
        if(mDatas!=null&&mDatas.data!=null&&mDatas.data.size()>0&&mDatas.data.get(section).info!=null) {
            if(holder instanceof NewMVItemViewHolder){
                ImageLoaderUtils.display(context, ((NewMVItemViewHolder) holder).iv, NetUtil.IMAGEURL+mDatas.data.get(section).info.get(position*2).mv_thumb);
                ((NewMVItemViewHolder) holder).tv1.setText(mDatas.data.get(section).info.get(position*2).mv_title);
                ((NewMVItemViewHolder) holder).tv2.setText(mDatas.data.get(section).info.get(position*2).mv_artist);
                ImageLoaderUtils.display(context, ((NewMVItemViewHolder) holder).iv1, NetUtil.IMAGEURL+mDatas.data.get(section).info.get(position*2+1).mv_thumb);
                ((NewMVItemViewHolder) holder).tv3.setText(mDatas.data.get(section).info.get(position*2+1).mv_title);
                ((NewMVItemViewHolder) holder).tv4.setText(mDatas.data.get(section).info.get(position*2+1).mv_artist);
//                holder.tv1.setText(mDatas.data.get(section).info.get(position*2).mv_title);
//                holder.tv2.setText(mDatas.data.get(section).info.get(position*2+1).mv_title);
//                holder.tv_detail_two.setText(mDatas.data.get(section).info.get(position*2).mv_artist);
//                holder.tv_detail_three.setText(mDatas.data.get(section).info.get(position*2+1).mv_artist);
//                ImageLoaderUtils.display(context,holder.iv1, NetUtil.IMAGEURL+mDatas.data.get(section).info.get(position*2).mv_thumb);
//                ImageLoaderUtils.display(context,holder.iv1,NetUtil.IMAGEURL+mDatas.data.get(section).info.get(position*2+1).mv_thumb);
            }
            if(holder instanceof NewMVItemViewHolder){
                ((NewMVItemViewHolder) holder).iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Constant.netStatus) {
                            if(ToosUtil.getInstance().isInstallTvControl((Activity) context)) {
                                mOnClickListener.onClick( position * 2,section);
                            }
                        }else{
                            ToosUtil.getInstance().isConnectTv((Activity) context);
                        }
                    }
                });
                ((NewMVItemViewHolder) holder).iv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Constant.netStatus) {
                            if(ToosUtil.getInstance().isInstallTvControl((Activity) context)) {
                                mOnClickListener.onClick( position * 2 + 1,section);
                            }
                        }else{
                            ToosUtil.getInstance().isConnectTv((Activity) context);
                        }
                    }
                });
            }
        }
    }
}
