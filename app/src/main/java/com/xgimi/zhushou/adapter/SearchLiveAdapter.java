package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.SearchLiveTv;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;

import java.util.List;

/**
 * Created by 霍长江 on 2016/8/13.
 */
public class SearchLiveAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mlLayoutInflater;
    private List<SearchLiveTv.DataBean> mDatas;
    public SearchLiveAdapter(Context context, List<SearchLiveTv.DataBean> datas){
        this.mContext=context;
        this.mDatas=datas;
        mlLayoutInflater=LayoutInflater.from(context);
    }
    public void dataChange( List<SearchLiveTv.DataBean> data){
        this.mDatas=data;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mDatas!=null?mDatas.size():0;
    }

    @Override
    public SearchLiveTv.DataBean getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view=convertView;
        ViewHolder vh;
        if(view==null){
            vh=new ViewHolder();
            view=mlLayoutInflater.inflate(R.layout.live_right_channel,parent,false);
            vh.iv= (ImageView) view.findViewById(R.id.iv_image);
            vh.tv= (TextView) view.findViewById(R.id.tv_channel);
            view.setTag(vh);
        }else{
            vh= (ViewHolder) view.getTag();
        }
        ImageLoaderUtils.display(mContext,vh.iv,mDatas.get(position).icon);
        vh.tv.setText(mDatas.get(position).name);
        return view;
    }

    public class ViewHolder{
        public ImageView iv;
        public TextView tv;

    }
}
