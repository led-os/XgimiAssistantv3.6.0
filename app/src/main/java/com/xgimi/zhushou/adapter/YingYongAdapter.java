package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.ApplySearc;

import java.util.List;


public class YingYongAdapter extends BaseAdapter {

    private Context mcontext;
    private List<ApplySearc.ApplySearchItem> mSearch;
    private DisplayImageOptions options;

    public YingYongAdapter(Context context, List<ApplySearc.ApplySearchItem> search) {
        this.mcontext = context;
        this.mSearch = search;
    }

    @Override
    public int getCount() {
        return mSearch != null ? mSearch.size() : 0;
    }

    public void dataChange(List<ApplySearc.ApplySearchItem> sea) {
        this.mSearch = sea;
        notifyDataSetChanged();
    }

    @Override
    public ApplySearc.ApplySearchItem getItem(int position) {
        return mSearch.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        View view = convertView;
        ApplySearc.ApplySearchItem applySearchItem = mSearch.get(position);
        if (view == null) {
            vh = new ViewHolder();
            view = View.inflate(mcontext, R.layout.yingyong_adapter, null);
            vh.iv = (ImageView) view.findViewById(R.id.iv_tubiao);
            vh.tv = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        vh.tv.setText(applySearchItem.title);
        ImageLoader.getInstance().displayImage(applySearchItem.icon, vh.iv);
        return view;
    }

    class ViewHolder {
        public ImageView iv;
        public TextView tv;
    }
}
