package com.xgimi.zhushou.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.RadioShow;

import java.util.List;

public class RadioShowListAdapter extends BaseAdapter{

	private Activity mActivity;
	private List<RadioShow.data> mDatas;
	private int mPostion;
	public RadioShowListAdapter(int postion, Activity activity, List<RadioShow.data> data){
		this.mActivity=activity;
		this.mDatas=data;
		this.mPostion=postion;
	}


	public void dataChange(List<RadioShow.data> datas){
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
	public RadioShow.data getItem(int arg0) {
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
			view=View.inflate(mActivity, R.layout.radio_show_list_item, null);
			vh.tv=(TextView) view.findViewById(R.id.tv_name);
			vh.tv1= (TextView) view.findViewById(R.id.tv_time);
			vh.tv2= (TextView) view.findViewById(R.id.update);
			vh.xian=view.findViewById(R.id.xian);
			view.setTag(vh);
		}else{
			vh=(ViewHolder) view.getTag();
		}
		if(arg0==mPostion){
			vh.xian.setVisibility(View.VISIBLE);
		}else{
			vh.xian.setVisibility(View.GONE);
		}
		vh.tv.setText(mDatas.get(arg0).program_title);
		vh.tv1.setText(mDatas.get(arg0).program_play_time);
		vh.tv2.setText(mDatas.get(arg0).program_update_time);
		return view;
	}
    public class ViewHolder{
	    public TextView tv;
		public TextView tv1,tv2;
		public View xian;
}
}
