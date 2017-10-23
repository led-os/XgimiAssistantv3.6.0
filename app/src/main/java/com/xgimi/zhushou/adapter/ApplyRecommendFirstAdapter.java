package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.ApplyDetailActivity;
import com.xgimi.zhushou.bean.ApplyHomeBean;
import com.xgimi.zhushou.netUtil.NetUtil;
import com.xgimi.zhushou.util.ToosUtil;

/**
 * Created by 霍长江 on 2016/8/17.
 */
public class ApplyRecommendFirstAdapter extends BaseAdapter {

    private Context context;
    private int size;
    private boolean isInfiniteLoop;
    private RecyclerView scrool;
    ApplyHomeBean.DataBean mHomePage;
    public ApplyRecommendFirstAdapter(Context context, RecyclerView myscro,  ApplyHomeBean.DataBean homepage) {
        this.scrool=myscro;
        this.context = context;
        this.mHomePage=homepage;
//		this.film = film;
        if (homepage != null) {
            this.size = homepage.ad.size();
        }
        isInfiniteLoop = false;

    }

    @Override
    public int getCount() {
        // Infinite loop
        return isInfiniteLoop ? Integer.MAX_VALUE : 3;
    }

    /**
     * get really position
     *
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop ? position % size : position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup container) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = holder.imageView = new ImageView(context);
            holder.imageView
                    .setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
//		ImageLoaderUtils.display(context,holder.imageView, NetUtil.IMAGEURL+mHomePage.ad.get(getPosition(position)).image);
        holder.imageView.setBackgroundResource(R.drawable.apphome);
        ImageLoader.getInstance().displayImage(
                NetUtil.IMAGEURL+mHomePage.ad.get(getPosition(position)).image,
                holder.imageView);
        holder.imageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        scrool.requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        scrool.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ToosUtil.getInstance().addEventUmeng(context,"event_app_adver");
                ApplyHomeBean.DataBean.AdBean item = mHomePage.ad.get(getPosition(position));
				Intent intent = new Intent(context, ApplyDetailActivity.class);
				intent.putExtra("id", item.getId());
				context.startActivity(intent);
            }
        });

        return view;
    }

    private static class ViewHolder {

        ImageView imageView;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop
     *            the isInfiniteLoop to set
     */
    public ApplyRecommendFirstAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

}

