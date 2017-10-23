package com.xgimi.zhushou.adapter.MusicViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.ClassToRadio;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
public class ClassToRadioAdapter extends RecyclerView.Adapter{
    private Context context;
    private List<ClassToRadio.data> data;
    private LayoutInflater layoutInflater;
    private static final int ITEM_VIEW=1;
    private static final int FOOT_VIEW=2;
    public ClassToRadioAdapter(List<ClassToRadio.data> data, Context context){
        this.data=data;
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
    }
    public void dataChange(List<ClassToRadio.data> data){
        if(data!=null) {
            this.data = data;
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
            View view=layoutInflater.inflate(R.layout.radio_all_class,parent,false);
            return new ItemViewHolder(view);
        }else if (viewType == FOOT_VIEW) {
            View view = layoutInflater.inflate(R.layout.instance_load_more_layout, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(data!=null&&data.size()>0){
            if (holder instanceof ItemViewHolder) {
                ImageLoaderUtils.display(context, ((ItemViewHolder) holder).image1, data.get(position*3).medium_thumb);
                ((ItemViewHolder) holder).tv1.setText(data.get(position*3).fm_title);
                ImageLoaderUtils.display(context, ((ItemViewHolder) holder).image2, data.get(position*3 + 1).medium_thumb);
                ((ItemViewHolder) holder).tv2.setText(data.get(position*3 + 1).fm_title);
                ImageLoaderUtils.display(context, ((ItemViewHolder) holder).image3, data.get(position*3 + 2).medium_thumb);
                ((ItemViewHolder) holder).tv3.setText(data.get(position*3 + 2).fm_title);
                if(mOnClickListener!=null){
                    ((ItemViewHolder) holder).image1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnClickListener.onClick(data.get(position*3),position);
                        }
                    });
                    ((ItemViewHolder) holder).image2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnClickListener.onClick(data.get(position*3+1),position);
                        }
                    });
                    ((ItemViewHolder) holder).image3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnClickListener.onClick(data.get(position*3+2),position);
                        }
                    });
                }
            }
        }

    }

    @Override
    public int getItemCount() {
//        return data!=null?data.size()/3:0;
        return data!=null&&data.size()/3<1?1:data.size()/3;
    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView image1,image2,image3;
        private TextView tv1,tv2,tv3;

        public ItemViewHolder(View itemView) {
            super(itemView);
            image1= (ImageView) itemView.findViewById(R.id.image1);
            image2= (ImageView) itemView.findViewById(R.id.image2);
            image3= (ImageView) itemView.findViewById(R.id.image3);
            tv1= (TextView) itemView.findViewById(R.id.text1);
            tv2= (TextView) itemView.findViewById(R.id.text2);
            tv3= (TextView) itemView.findViewById(R.id.text3);
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
