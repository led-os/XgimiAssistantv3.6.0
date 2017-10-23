package com.xgimi.zhushou.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ViewPagerAdapter1<T> extends PagerAdapter {

	private ArrayList<T> views;

	public ViewPagerAdapter1(ArrayList<T> views) {
		if (views == null) {
			this.views = new ArrayList<T>();
		} else {
			this.views = views;
		}
	}
	
 
	 public void dataChanged(ArrayList<T> photos){
	        this.views = photos;
	        notifyDataSetChanged();
	    }
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = (View) views.get(position);

		container.addView(view);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		View view = (View) views.get(position);

		container.removeView(view);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return views.size();
	}

}
