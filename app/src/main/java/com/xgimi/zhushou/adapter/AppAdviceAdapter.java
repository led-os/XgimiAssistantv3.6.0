package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.FilmDetailActivity;
import com.xgimi.zhushou.activity.FilmZhuanTiActivity;
import com.xgimi.zhushou.activity.XgimiCommunityActivity;
import com.xgimi.zhushou.bean.MovieRecommend;
import com.xgimi.zhushou.netUtil.NetUtil;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.Statics;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;


/**
 * Created by 霍长江 on 2016/6/27.
 */
public class AppAdviceAdapter extends BaseAdapter {

    private static final String TAG = "AppAdviceAdapter";

    private Context context;
    private int size;
    private boolean isInfiniteLoop;
    private RecyclerView scrool;
    MovieRecommend.DataBean mHomePage;

    public AppAdviceAdapter(Context context, RecyclerView myscro, MovieRecommend.DataBean homepage) {
        this.scrool = myscro;
        this.context = context;
        this.mHomePage = homepage;
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
    public View getView(final int position, View view, final ViewGroup container) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(context, R.layout.viewflowertext, null);
            holder.tv = (TextView) view.findViewById(R.id.tv);
            holder.imageView = (ImageView) view.findViewById(R.id.iv);
//            view = holder.imageView = new ImageView(context);
//            holder.imageView
//                    .setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
//            holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
//		ImageLoaderUtils.display(context,holder.imageView, NetUtil.IMAGEURL+mHomePage.ad.get(getPosition(position)).image);
        holder.imageView.setBackgroundResource(R.drawable.apphome);
        if (mHomePage.ad != null && mHomePage.ad.get(getPosition(position)).title != null) {
            holder.tv.setText(mHomePage.ad.get(getPosition(position)).title);
        }
//        ImageLoader.getInstance().displayImage(
//                NetUtil.IMAGEURL + mHomePage.ad.get(getPosition(position)).image,
//                holder.imageView);
        ImageLoader.getInstance().displayImage(mHomePage.ad.get(getPosition(position)).image,
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
                XGIMILOG.D("");
                MovieRecommend.DataBean.AdBean item = mHomePage.ad.get(getPosition(position));
                SaveData.getInstance().mSouceInsight = "6";
                Statics.getInstance().sendStatics(context, item.title, item.ad_id, SaveData.getInstance().mSouceInsight, SaveData.getInstance().mSoucePage, null);
                ToosUtil.getInstance().addEventUmeng(context, "event_movie_adver_click");
                if (item.type == null || item.type.equals("") || item.type.equals("video")) {
                    XGIMILOG.D("viewFlow go to FilmDetailActivity");
                    Intent intent = new Intent(context, FilmDetailActivity.class);
                    intent.putExtra("id", item.ad_id);
                    SaveData.getInstance().mSouceInsight = "6";
                    SaveData.getInstance().mSoucePage = "1";
                    SaveData.getInstance().mSourceInsightLocation = null;
                    context.startActivity(intent);
                } else if (item.type.equals("url")) {
                    XGIMILOG.D("viewFlow go to FilmDetailActivity");
                    Intent intent = new Intent(context, XgimiCommunityActivity.class);
                    intent.putExtra("url", item.url);
                    intent.putExtra("name", item.title);
                    SaveData.getInstance().mSouceInsight = "6";
                    SaveData.getInstance().mSoucePage = "1";
                    SaveData.getInstance().mSourceInsightLocation = null;
                    context.startActivity(intent);
                } else {
                    XGIMILOG.D("viewFlow go to FilmDetailActivity");
                    Intent intent = new Intent(context, FilmZhuanTiActivity.class);
                    intent.putExtra("id", item.ad_id);
                    SaveData.getInstance().mSouceInsight = "6";
                    SaveData.getInstance().mSoucePage = "1";
                    SaveData.getInstance().mSourceInsightLocation = null;
                    context.startActivity(intent);
                }

            }

        });

        return view;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView tv;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public AppAdviceAdapter setInfiniteLoop(boolean isInfiniteLoop) {
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
