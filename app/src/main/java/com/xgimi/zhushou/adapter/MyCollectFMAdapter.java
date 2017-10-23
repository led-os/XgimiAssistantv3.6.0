package com.xgimi.zhushou.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.FMCollectList;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/8/26.
 */
public class MyCollectFMAdapter extends BaseAdapter{
    private Activity mActivity;
    private List<FMCollectList.data> mDatas;
    private int mPostion;
    public MyCollectFMAdapter(int postion, Activity activity, List<FMCollectList.data> data){
        this.mActivity=activity;
        this.mDatas=data;
        this.mPostion=postion;
    }


    public void dataChange(List<FMCollectList.data> datas){
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
    public FMCollectList.data getItem(int arg0) {
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
            view=View.inflate(mActivity, R.layout.my_collect_fm_item, null);
            vh.iv= (ImageView) view.findViewById(R.id.iv);
            vh.tv1= (TextView) view.findViewById(R.id.fm_title);
            vh.tv2= (TextView) view.findViewById(R.id.fm_artist);
            vh.tv3= (TextView) view.findViewById(R.id.add_time);
            view.setTag(vh);
        }else{
            vh=(ViewHolder) view.getTag();
        }

        ImageLoaderUtils.display(mActivity,vh.iv, mDatas.get(arg0).mf_cover);
        vh.tv1.setText(mDatas.get(arg0).mf_title);
        vh.tv2.setText(mDatas.get(arg0).mf_author);
        vh.tv3.setText(mDatas.get(arg0).add_time);
        return view;
    }
    public class ViewHolder{
        public ImageView iv;
        public TextView tv1,tv2,tv3;
    }
}
