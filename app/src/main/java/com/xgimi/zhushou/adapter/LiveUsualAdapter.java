package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.UrlBean;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;

/**
 * Created by 霍长江 on 2016/8/24.
 */
public class LiveUsualAdapter extends BaseAdapter{


    public Context mContext;
    public UrlBean mDatas;
    public LiveUsualAdapter(Context context,UrlBean data){
        this.mDatas=data;
        this.mContext=context;
    }
    @Override
    public int getCount() {
        return mDatas.data!=null?mDatas.data.recommend.size():0;
    }

    @Override
    public UrlBean.DataBean.RecommendBean getItem(int position) {
        return mDatas.data.recommend.get(position);
    }
    public void dataChange(UrlBean data){
        this.mDatas=data;
        notifyDataSetChanged();
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        View view=convertView;
        if(view==null){
            view=View.inflate(mContext, R.layout.live_grdiview_item,null);
            vh=new ViewHolder();
            vh.tv= (TextView) view.findViewById(R.id.tv);
            vh.iv= (ImageView) view.findViewById(R.id.imageview);
            view.setTag(vh);
        }else{
            vh= (ViewHolder) view.getTag();
        }
        vh.tv.setText(mDatas.data.recommend.get(position).getName());
        ImageLoaderUtils.display(mContext,vh.iv, mDatas.data.recommend.get(position).icon);
        return view;
    }
    public class ViewHolder{
        public ImageView iv;
        public TextView tv;
    }
}
