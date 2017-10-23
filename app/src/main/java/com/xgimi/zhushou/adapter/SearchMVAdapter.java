package com.xgimi.zhushou.adapter;

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
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;
import com.xgimi.zhushou.netUtil.NetUtil;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.ToosUtil;

import java.util.List;

/**
 * Created by 霍长江 on 2016/6/30.
 */
public class SearchMVAdapter extends RecyclerView.Adapter{
    private Context mContext;
    public List<MVList.data> mDatas;
    private LayoutInflater mLayoutInflater;
    private static final int ITEM_VIEW=1;
    private static final int FOOT_VIEW=2;
    public SearchMVAdapter(Context context, List<MVList.data> data){
        this.mContext=context;
        this.mDatas=data;
        mLayoutInflater=LayoutInflater.from(context);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ITEM_VIEW) {
            View view=mLayoutInflater.inflate(R.layout.search_mv_item,parent,false);
            return new ItemViewHolder(view);
        }else if (viewType == FOOT_VIEW) {
            View view = mLayoutInflater.inflate(R.layout.instance_load_more_layout, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }
    public void dataChange(List<MVList.data> datas){
        if(datas!=null) {
            this.mDatas = datas;
            notifyDataSetChanged();
        }
    }
    public OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(mDatas!=null&&mDatas.size()>0){
            if(holder instanceof ItemViewHolder){
                MVList.data dataBean = mDatas.get(position);
                ((ItemViewHolder)holder).title.setText(dataBean.mv_title);
                ((ItemViewHolder)holder).decript.setText(dataBean.mv_artist);
                ImageLoaderUtils.display(mContext,  ((ItemViewHolder)holder).mIv, NetUtil.IMAGEURL+dataBean.mv_thumb);
                if(mOnClickListener!=null){
                    ((ItemViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(Constant.netStatus) {
                                if(ToosUtil.getInstance().isInstallTvControl((Activity) mContext)) {

                                    mOnClickListener.onClick(mDatas.get(position), position);
                                }
                            }else{
                                ToosUtil.getInstance().isConnectTv((Activity) mContext);
                            }

                        }
                    });
                }
            }

        }
    }
    @Override
    public int getItemCount() {
        return mDatas!=null?mDatas.size():0;
    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        public final ImageView mIv;
        public final TextView title;
        public final TextView decript;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mIv = (ImageView) itemView.findViewById(R.id.iv);
            title = (TextView) itemView.findViewById(R.id.mv_title);
            decript = (TextView) itemView.findViewById(R.id.mv_artist);
        }
    }
    public interface OnClickListener {
        void onClick(Object object, int position);
    }
    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()&&getItemCount()>8) {
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
