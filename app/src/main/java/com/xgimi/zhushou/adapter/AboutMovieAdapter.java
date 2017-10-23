package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.FilmDetailBean;

import java.util.List;

public class AboutMovieAdapter extends BaseAdapter{

	private Context mContext;
	private List<FilmDetailBean.DataBean.RecommendBean> home;

	public AboutMovieAdapter(Context context, List<FilmDetailBean.DataBean.RecommendBean> home) {
		this.mContext = context;
		this.home = home;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return home != null ? home.size() : 0;
	}

	public void dataChange(List<FilmDetailBean.DataBean.RecommendBean> ho){
		this.home=ho;
		notifyDataSetChanged();
	}
	@Override
	public FilmDetailBean.DataBean.RecommendBean getItem(int position) {
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
		if (view == null) {
			vh=new ViewHolder();
			view = View.inflate(mContext, R.layout.movie_adapter, null);
			vh.iv = (ImageView) view.findViewById(R.id.iv);
			vh.tv = (TextView) view.findViewById(R.id.tv);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		FilmDetailBean.DataBean.RecommendBean item=home.get(position);
		ImageLoader.getInstance().displayImage(item.cover, vh.iv);
		vh.tv.setText(item.name);
		return view;
	}

	public class ViewHolder {
		public ImageView iv;
		public TextView tv;
	}

}
