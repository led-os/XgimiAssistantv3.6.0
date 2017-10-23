package com.xgimi.zhushou.adapter;

import android.app.Dialog;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;

public class SimpleAdapter extends PagerAdapter {

//	private static final int[] sDrawables = { R.drawable.wallpaper,
//			R.drawable.wallpaper, R.drawable.wallpaper, R.drawable.wallpaper,
//			R.drawable.wallpaper, R.drawable.wallpaper };

    private ArrayList<String> sDrawables;
    private Dialog acti;
    public SimpleAdapter(ArrayList<String> sds,Dialog activity){
        this.sDrawables=sds;
        this.acti=activity;
    }


    @Override
    public int getCount() {
        return sDrawables.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        PhotoView photoView = new PhotoView(container.getContext());
//		photoView.setImageResource(sDrawables[position]);
        ImageLoader.getInstance().displayImage(sDrawables.get(position), photoView);



        PhotoViewAttacher attacher=new PhotoViewAttacher(photoView);

        attacher.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                // TODO Auto-generated method stub
                acti.dismiss();
            }
        });
        attacher.setOnViewTapListener(new OnViewTapListener() {

            @Override
            public void onViewTap(View view, float x, float y) {
                // TODO Auto-generated method stub
                acti.dismiss();

            }
        });
        // Now just add PhotoView to ViewPager and return it
        container.addView(photoView, LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        return photoView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
