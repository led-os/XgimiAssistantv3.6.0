package com.xgimi.zhushou.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.AppCollectBeen;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/8/27.
 */
public class MyCollectAppAdapter extends BaseAdapter{
    private Activity mActivity;
    private List<AppCollectBeen.data> mDatas;
    private int mPostion;
    public MyCollectAppAdapter(int postion, Activity activity, List<AppCollectBeen.data> data){
        this.mActivity=activity;
        this.mDatas=data;
        this.mPostion=postion;
    }


    public void dataChange(List<AppCollectBeen.data> datas){
        this.mDatas=datas;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mDatas.size();
    }
    public void dataChange(int postion){
        this.mPostion=postion;
        notifyDataSetChanged();
    }

    @Override
    public AppCollectBeen.data getItem(int arg0) {
        // TODO Auto-generated method stub
        return mDatas.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        // TODO Auto-generated method stub
        View view=arg1;
        ViewHolder vh;
        if(view==null){
            vh=new ViewHolder();
            view=View.inflate(mActivity, R.layout.my_collect_app_item, null);
            vh.iv= (ImageView) view.findViewById(R.id.iv);
            vh.tv1= (TextView) view.findViewById(R.id.title);
            vh.tv2= (TextView) view.findViewById(R.id.size);
            vh.tv3= (TextView) view.findViewById(R.id.add_time);
            view.setTag(vh);
        }else{
            vh=(ViewHolder) view.getTag();
        }

        ImageLoaderUtils.display(mActivity,vh.iv, mDatas.get(arg0).icon);
        vh.tv1.setText(mDatas.get(arg0).title);
        vh.tv2.setText(mDatas.get(arg0).file_size);
        vh.tv3.setText(mDatas.get(arg0).version);
        return view;
    }
    public class ViewHolder{
        public ImageView iv;
        public TextView tv1,tv2,tv3;
    }

}
