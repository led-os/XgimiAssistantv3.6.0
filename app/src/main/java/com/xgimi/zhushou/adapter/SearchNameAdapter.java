package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.SearchBean;

public class SearchNameAdapter extends BaseAdapter{

	private Context mContext;
	private SearchBean mSearchs;
	public SearchNameAdapter(Context context,SearchBean searchs){
		this.mContext=context;
		this.mSearchs=searchs;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mSearchs.data!=null?mSearchs.data.size():0;
	}
	public void dataChange(SearchBean sea){
		this.mSearchs=sea;
		notifyDataSetChanged();
	}
	
	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return mSearchs.data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view=convertView;
		ViewHolder vh;
		if(view==null){
			vh=new ViewHolder();
			view=View.inflate(mContext, R.layout.pop_search_item, null);
			vh.tv=(TextView) view.findViewById(R.id.search_tv);
			view.setTag(vh);
		}else{
			vh=(ViewHolder) view.getTag();
		}
		vh.tv.setText(mSearchs.data.get(position));
		return view;
	}

	class ViewHolder{
		public TextView tv;
		
	}
}
