package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.MyYinShiBeen;
import com.xgimi.zhushou.netUtil.NetUtil;

/**
 * Created by Administrator on 2016/12/12.
 */
public class MyHuodongAdapter extends BaseAdapter{
    private MyYinShiBeen mData;
    private Context context;
    public MyHuodongAdapter(MyYinShiBeen mData, Context context){
        this.mData=mData;
        this.context=context;
    }
    @Override
    public int getCount() {
        return mData!=null&&mData.data!=null?mData.data.size():0;
    }
    public void dataChange(MyYinShiBeen mData){
        this.mData=mData;
        notifyDataSetChanged();
    }
    @Override
    public MyYinShiBeen getItem(int position) {
        return mData;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view=convertView;
        ViewHolder vh;
        if(view==null){
            vh=new ViewHolder();
            view=View.inflate(context, R.layout.my_hudong_item, null);
            vh.time=(TextView) view.findViewById(R.id.time);
            vh.title= (TextView) view.findViewById(R.id.title);
            vh.neirong= (TextView) view.findViewById(R.id.neirong);
            vh.image= (ImageView) view.findViewById(R.id.image);
            view.setTag(vh);
        }else{
            vh=(ViewHolder) view.getTag();
        }
        vh.time.setText(mData.data.get(position).push_time);
        vh.title.setText(mData.data.get(position).title);
        vh.neirong.setText(mData.data.get(position).text);
        if(mData.data.get(position).img!=null){
            ImageLoader.getInstance().displayImage(NetUtil.IMAGEURL+mData.data.get(position).img,vh.image);
        }else {
            vh.image.setVisibility(View.GONE);
        }
        return view;
    }
    public class ViewHolder{
        public TextView time,title,neirong;
        public ImageView image;
    }
}
