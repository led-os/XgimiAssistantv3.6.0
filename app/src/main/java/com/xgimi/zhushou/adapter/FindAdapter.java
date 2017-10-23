package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.XgimiBean;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;
import com.xgimi.zhushou.netUtil.NetUtil;

/**
 * Created by 霍长江 on 2016/9/2.
 */
public class FindAdapter extends BaseAdapter {
    public XgimiBean mData;
    public Context mContext;

    public FindAdapter(Context context, XgimiBean data) {
        this.mData = data;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mData.data != null ? mData.data.size() : 0;
    }

    public void dataChange(XgimiBean data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public XgimiBean.DataBean getItem(int position) {
        return mData.data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        View view = convertView;
        if (view == null) {
            vh = new ViewHolder();
            view = View.inflate(mContext, R.layout.find_fragment_item, null);
            vh.tv = (TextView) view.findViewById(R.id.tv);
            vh.tv1 = (TextView) view.findViewById(R.id.tv_name);
            vh.iv = (ImageView) view.findViewById(R.id.iv);
            vh.view_xian = view.findViewById(R.id.view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        if (position == mData.data.size() - 1) {
            vh.view_xian.setVisibility(View.GONE);
        } else {
            vh.view_xian.setVisibility(View.VISIBLE);
        }
        ImageLoaderUtils.display(mContext, vh.iv, NetUtil.IMAGEURL + mData.data.get(position).cate_icon);
        vh.tv.setText(mData.data.get(position).cate_name);
        vh.tv1.setText(mData.data.get(position).link_title);
        return view;
    }

    public class ViewHolder {
        public TextView tv;
        public TextView tv1;
        public ImageView iv;
        public View view_xian;
    }
}
