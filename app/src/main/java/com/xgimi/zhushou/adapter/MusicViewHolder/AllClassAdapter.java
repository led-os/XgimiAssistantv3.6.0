package com.xgimi.zhushou.adapter.MusicViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.AllRadioClass;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
public class AllClassAdapter extends RecyclerView.Adapter{
    private Context context;
    private List<AllRadioClass.data> data;
    private LayoutInflater layoutInflater;

    public AllClassAdapter(List<AllRadioClass.data> data, Context context){
        this.data=data;
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
    }
    public void dataChange(List<AllRadioClass.data> data){
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
        View view=layoutInflater.inflate(R.layout.radio_all_class,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(data!=null){
            if (holder instanceof ItemViewHolder) {
                ImageLoaderUtils.display(context, ((ItemViewHolder) holder).image1, data.get(position*3).category_thumb);
                ((ItemViewHolder) holder).tv1.setText(data.get(position*3).category_name);
                ImageLoaderUtils.display(context, ((ItemViewHolder) holder).image2, data.get(position*3 + 1).category_thumb);
                ((ItemViewHolder) holder).tv2.setText(data.get(position*3 + 1).category_name);
                ImageLoaderUtils.display(context, ((ItemViewHolder) holder).image3, data.get(position*3 + 2).category_thumb);
                ((ItemViewHolder) holder).tv3.setText(data.get(position*3 + 2).category_name);
            }
        }
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

    @Override
    public int getItemCount() {
        return data!=null?data.size()/3:0;
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
}
