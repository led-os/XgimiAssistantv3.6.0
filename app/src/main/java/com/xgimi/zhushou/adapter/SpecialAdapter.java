package com.xgimi.zhushou.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.ZhuanTiBean;

public class SpecialAdapter extends BaseAdapter {

	public Context mActivity;
	public ZhuanTiBean mMovieSpecials;
	private View view;
	private boolean isxian;

	public SpecialAdapter(Context context, ZhuanTiBean moviespecial) {
		this.mActivity = context;
		this.mMovieSpecials = moviespecial;

	}
	public void dataChange(ZhuanTiBean film){
		if(film!=null){
		this.mMovieSpecials=film;
		notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mMovieSpecials.data!=null?mMovieSpecials.data.contents.size():0;
	}

	@Override
	public ZhuanTiBean.DataBean.ContentsBean getItem(int position) {
		// TODO Auto-generated method stub
		return mMovieSpecials.data.contents.get(position);
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
		
		ViewHolder vh;
		if (view == null) {
			vh = new ViewHolder();
			view = View.inflate(mActivity, R.layout.movie_special_item, null);
			vh.iv=(ImageView) view.findViewById(R.id.iv);
			vh.tv=(TextView) view.findViewById(R.id.tv);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		ZhuanTiBean.DataBean.ContentsBean content = mMovieSpecials.data.contents.get(position);
			ImageLoader.getInstance().displayImage(
					content.img, vh.iv);
			vh.tv.setText(content.name);
		return view;
	}
	class ViewHolder {
		public ImageView iv;
		public TextView tv;
		public TextView content;

	}
}
