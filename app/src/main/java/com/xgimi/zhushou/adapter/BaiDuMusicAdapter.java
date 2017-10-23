package com.xgimi.zhushou.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.zhushou.R;

import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
public class BaiDuMusicAdapter extends BaseAdapter{
    private Activity mActivity;
    private List<VcontrolCmd.CustomPlay.PlayList> mDatas;
    private int mPostion;
    public BaiDuMusicAdapter( Activity activity, List<VcontrolCmd.CustomPlay.PlayList> data){
        this.mActivity=activity;
        this.mDatas=data;
//        this.mPostion=postion;
    }


    public void dataChange(List<VcontrolCmd.CustomPlay.PlayList> datas){
        this.mDatas=datas;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mDatas.size();
    }
    public void dataChange(){
//        this.mPostion=postion;
        notifyDataSetChanged();
    }

    @Override
    public VcontrolCmd.CustomPlay.PlayList getItem(int arg0) {
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
            view=View.inflate(mActivity, R.layout.radio_popu_liebiao_item, null);
            vh.tv1= (TextView) view.findViewById(R.id.mv_title);
            vh.tv2= (TextView) view.findViewById(R.id.mv_artist);
            view.setTag(vh);
        }else{
            vh=(ViewHolder) view.getTag();
        }
        vh.tv1.setText(mDatas.get(arg0).title);
        vh.tv2.setText(mDatas.get(arg0).singer);
        return view;
    }
    public class ViewHolder{
        public ImageView iv;
        public TextView tv1,tv2;
    }
}
