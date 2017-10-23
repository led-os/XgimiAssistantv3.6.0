package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.BaiBao;

import java.util.ArrayList;
import java.util.List;

public class BaiBaoAdatper extends BaseAdapter{

	private List<BaiBao> mBaibaos;
	private Context context;
	public BaiBaoAdatper(Context conte, ArrayList<BaiBao> baibaos){
		this.mBaibaos=baibaos;
		this.context=conte;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mBaibaos.size();
	}

	@Override
	public BaiBao getItem(int position) {
		// TODO Auto-generated method stub
		return mBaibaos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh;
		View view=convertView;
		if(view==null){
			vh=new ViewHolder();
			view=View.inflate(context, R.layout.baibaoitem, null);
			vh.iv=(ImageView) view.findViewById(R.id.iv);
			vh.tv=(TextView) view.findViewById(R.id.tv);
			view.setTag(vh);
		}else{
			vh=(ViewHolder) view.getTag();
		}
		vh.iv.setImageResource(mBaibaos.get(position).getId());
		vh.tv.setText(mBaibaos.get(position).getName());
		return view;
	}
class ViewHolder{
	public ImageView iv;
	public TextView tv;
}
}
