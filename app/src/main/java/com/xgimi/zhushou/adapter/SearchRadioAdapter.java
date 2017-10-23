package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.SearchRadio;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
public class SearchRadioAdapter extends RecyclerView.Adapter{
    private Context mContext;
    public List<SearchRadio.data> mDatas;
    private LayoutInflater mLayoutInflater;
    private static final int ITEM_VIEW=1;
    private static final int FOOT_VIEW=2;
    public SearchRadioAdapter(Context context, List<SearchRadio.data> data){
        this.mContext=context;
        this.mDatas=data;
        mLayoutInflater=LayoutInflater.from(context);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW) {
            View view=mLayoutInflater.inflate(R.layout.search_radio_item,parent,false);
            return new ItemViewHolder(view);
        }else if (viewType == FOOT_VIEW) {
            View view = mLayoutInflater.inflate(R.layout.instance_load_more_layout, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }
    public void dataChange(List<SearchRadio.data> datas){
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
                SearchRadio.data dataBean = mDatas.get(position);
                ((ItemViewHolder)holder).title.setText(dataBean.fm_title);
                ImageLoaderUtils.display(mContext,  ((ItemViewHolder)holder).mIv,dataBean.medium_thumb);
                if(mOnClickListener!=null) {
                    ((ItemViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnClickListener.onClick(mDatas.get(position), position);
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
        public ItemViewHolder(View itemView) {
            super(itemView);
            mIv = (ImageView) itemView.findViewById(R.id.iv);
            title = (TextView) itemView.findViewById(R.id.radio_title);
        }
    }
    public interface OnClickListener {
        void onClick(Object object, int position);
    }
    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()&&getItemCount()>10 ) {
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
