package com.xgimi.zhushou.adapter.MusicViewHolder;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.MVList;
import com.xgimi.zhushou.netUtil.HttpUrl;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;
import com.xgimi.zhushou.netUtil.NetUtil;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.ToosUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/8/16.
 */
public class AllAdapter extends RecyclerView.Adapter{
    private Context context;
    private List<MVList.data> mData;
    private LayoutInflater layoutInflater;
    private static final int ITEM_VIEW=1;
    private static final int FOOT_VIEW=2;
    public AllAdapter(List<MVList.data> data,Context context){
        this.context=context;
        this.mData=data;
        layoutInflater=LayoutInflater.from(context);
    }

    public void dataChange(List<MVList.data> data){
        if(data!=null) {
            this.mData = data;
            notifyDataSetChanged();
        }
    }
    public OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW) {
            View view=layoutInflater.inflate(R.layout.mv_tuijan_item,parent,false);
            return new ItemViewHolder(view);
        }else if (viewType == FOOT_VIEW) {
            View view = layoutInflater.inflate(R.layout.instance_load_more_layout, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
        if(mData!=null) {
            if (holder instanceof ItemViewHolder) {
                ImageLoaderUtils.display(context, ((ItemViewHolder) holder).iv, NetUtil.IMAGEURL+mData.get(position*2).mv_thumb);
                ((ItemViewHolder) holder).tv1.setText(mData.get(position*2).mv_title);
                ((ItemViewHolder) holder).tv2.setText(mData.get(position*2).mv_artist);
                ImageLoaderUtils.display(context, ((ItemViewHolder) holder).iv1, NetUtil.IMAGEURL+mData.get(position*2+1).mv_thumb);
                ((ItemViewHolder) holder).tv3.setText(mData.get(position*2+1).mv_title);
                ((ItemViewHolder) holder).tv4.setText(mData.get(position*2+1).mv_artist);
            }
            if(mOnClickListener!=null&&holder instanceof ItemViewHolder){
                ((ItemViewHolder) holder).iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Constant.netStatus) {
                            if(ToosUtil.getInstance().isInstallTvControl((Activity) context)) {
                                mOnClickListener.onClick(mData.get(position * 2), position * 2);
                            }
                        }else{
                            ToosUtil.getInstance().isConnectTv((Activity) context);
                        }
                    }
                });
                ((ItemViewHolder) holder).iv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Constant.netStatus) {
                            if(ToosUtil.getInstance().isInstallTvControl((Activity) context)) {

                                mOnClickListener.onClick(mData.get(position * 2 + 1), position * 2 + 1);
                            }
                        }else{
                            ToosUtil.getInstance().isConnectTv((Activity) context);
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData!=null?mData.size()/2:0;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView iv,iv1;
        private TextView tv1,tv3;
        private TextView tv2,tv4;

        public ItemViewHolder(View itemView) {
            super(itemView);
            iv= (ImageView) itemView.findViewById(R.id.mv_image);
            iv1 =(ImageView) itemView.findViewById(R.id.mv_image1);
            tv1= (TextView) itemView.findViewById(R.id.mv_title);
            tv2= (TextView) itemView.findViewById(R.id.mv_artist);
            tv3= (TextView) itemView.findViewById(R.id.mv_title1);
            tv4= (TextView) itemView.findViewById(R.id.mv_artist1);
        }
    }
    public interface OnClickListener {
        void onClick(Object object, int position);
    }
    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()&&getItemCount()>4&& HttpUrl.isNetworkConnected(context)) {
            return FOOT_VIEW;
        } else {
            return ITEM_VIEW;
        }
    }
    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends  RecyclerView.ViewHolder{
        private TextView foot_view_item_tv;
        public FootViewHolder(View view) {
            super(view);
            foot_view_item_tv=(TextView)view.findViewById(R.id.foot_view_item_tv);
        }
    }
}
