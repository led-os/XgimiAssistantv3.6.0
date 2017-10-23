package com.xgimi.zhushou.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.SearchMV;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;
import com.xgimi.zhushou.netUtil.NetUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/8/22.
 */
public class MVSearchPoPListAdapter extends BaseAdapter {
    private Activity mActivity;
    private List<SearchMV.data> mDatas;
    private int mPostion;
    public MVSearchPoPListAdapter(Activity activity, List<SearchMV.data> data){
        this.mActivity=activity;
        this.mDatas=data;
//        this.mPostion=postion;
    }


    public void dataChange(List<SearchMV.data> datas){
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
    public SearchMV.data getItem(int arg0) {
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
            view=View.inflate(mActivity, R.layout.mv_popu_liebiao_item, null);
            vh.iv=(ImageView) view.findViewById(R.id.iv);
            vh.tv1= (TextView) view.findViewById(R.id.mv_title);
            vh.tv2= (TextView) view.findViewById(R.id.mv_artist);
            view.setTag(vh);
        }else{
            vh=(ViewHolder) view.getTag();
        }
        ImageLoaderUtils.display(mActivity,vh.iv, NetUtil.IMAGEURL+mDatas.get(arg0).mv_thumb);
        vh.tv1.setText(mDatas.get(arg0).mv_title);
        vh.tv2.setText(mDatas.get(arg0).mv_artist);
        return view;
    }
    public class ViewHolder{
        public ImageView iv;
        public TextView tv1,tv2;
    }
}
