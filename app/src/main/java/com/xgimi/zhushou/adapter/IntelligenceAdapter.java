package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.IntelligenceBean;

import java.util.List;

/**
 * Created by 霍长江 on 2016/10/12.
 */
public class IntelligenceAdapter extends BaseAdapter {

    private Context mContext;
    private List<IntelligenceBean> mDatas;
    public IntelligenceAdapter(Context context,List<IntelligenceBean> datas){
        this.mContext=context;
        this.mDatas=datas;
    }


    public void dataChange(List<IntelligenceBean> datas){
        this.mDatas=datas;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mDatas!=null&&mDatas.size()>0?mDatas.size():0;
    }

    @Override
    public IntelligenceBean getItem(int position) {
        return mDatas.get(position);
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
            view = View.inflate(mContext, R.layout.deviceadapter, null);
            vh.tv = (TextView) view.findViewById(R.id.tv);
            vh.iv = (ImageView) view.findViewById(R.id.iv);

            vh.tv1 = (TextView) view.findViewById(R.id.tv_ip);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        IntelligenceBean gmDevice = mDatas.get(position);
        vh.tv.setText(gmDevice.name);
        vh.tv1.setText(gmDevice.ip);
        return view;
    }
    class ViewHolder {
        public ImageView iv;
        public TextView tv;
        public TextView tv1;
    }
}
