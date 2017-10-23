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
import com.xgimi.zhushou.netUtil.NetUtil;

/**
 * Created by 霍长江 on 2016/8/19.
 */
public class FindUrlAdapter extends BaseAdapter {
    public Context mContext;
    public UrlBean mdatas;

    public FindUrlAdapter(Context context, UrlBean data) {
        this.mdatas = data;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mdatas.data != null ? mdatas.data.game_live.size() : 0;
    }

    @Override
    public UrlBean.DataBean.GameLiveBean getItem(int position) {
        return mdatas.data.game_live.get(position);
    }

    public void dataChange(UrlBean data) {
        this.mdatas = data;
        notifyDataSetChanged();
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
            view = View.inflate(mContext, R.layout.find_url_item, null);
            vh.tv = (TextView) view.findViewById(R.id.tv);
            vh.tv1 = (TextView) view.findViewById(R.id.tv1);
            vh.iv = (ImageView) view.findViewById(R.id.iv);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }

        vh.tv.setText(mdatas.data.game_live.get(position).getName());
        vh.tv1.setText(mdatas.data.game_live.get(position).getDesc());
        ImageLoaderUtils.display(mContext, vh.iv, NetUtil.IMAGEURL + mdatas.data.game_live.get(position).img);
        return view;
    }

    public class ViewHolder {
        public TextView tv, tv1;
        public ImageView iv;
    }
}
