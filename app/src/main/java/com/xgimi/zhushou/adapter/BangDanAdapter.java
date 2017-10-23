package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.music.model.TopList;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgimi.zhushou.R;

import java.util.List;

public class BangDanAdapter extends BaseAdapter {

    private Context mContext;
    private List<TopList> home;
    private int itemSize;
    private String[] tupian;

    public BangDanAdapter(Context context, List<TopList> home, String[] tupian) {
        this.mContext = context;
        this.home = home;
        this.tupian = tupian;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return home != null ? home.size() : 0;
    }

    public void dataChange(List<TopList> ho) {
        this.home = ho;
        notifyDataSetChanged();
    }

    @Override
    public TopList getItem(int position) {
        // TODO Auto-generated method stub
        return home.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = convertView;
        ViewHolder vh = null;
        final int index = position;
        if (view == null) {
            vh = new ViewHolder();
            view = View.inflate(mContext, R.layout.music_adapter_sample, null);
            vh.iv = (ImageView) view.findViewById(R.id.iv);
            vh.tv = (TextView) view.findViewById(R.id.tv);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        final TopList item = home.get(position);
        ImageLoader.getInstance().displayImage(tupian[position], vh.iv);
//		vh.iv.setImageResource(tupian[position]);
//		ImageLoader.getInstance().displayImage(item.image, vh.iv);
        vh.tv.setText(item.mName);
//		vh.iv.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(mContext,
//						MusicDetailActivity.class);
//				intent.putExtra("leixing", item.mBillId);
//				intent.putExtra("class", 0);
//				intent.putExtra("index",index);
//				intent.putExtra("geName",item.mName);
//				mContext.startActivity(intent);
//			}
//		});
        return view;
    }

    public class ViewHolder {
        public ImageView iv;
        public TextView tv;
    }

}
