package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.ChannelActivity;
import com.xgimi.zhushou.bean.AllMovie;
import com.xgimi.zhushou.bean.ChannelBean;
import com.xgimi.zhushou.bean.MovieByCategory;
import com.xgimi.zhushou.inter.OnitemClick;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by 霍长江 on 2016/8/10.
 */
public class AllMovieAdapter extends RecyclerView.Adapter {
    //普通ITEM
    private static final int ITEM_VIEW = 1;
    private static final int FIRST_ITEM = 0;
    private static final int SECOND_ITEM = 3;
    private boolean isFirst;

    //FOOT ITEM
    private static final int FOOT_VIEW = 2;
    private Context mContext;
    private LayoutInflater mlLayoutInflater;
    private List<AllMovie.DataBean> mDatas;
    //    private List<MovieByCategory.DataBean> mIds;
    private MovieByCategory.DataBean mMovieByCategoryDateBean;
    private boolean isHome;
    private int mIndex;
    private List<String> mString = new ArrayList<>();
    private List<String> mString1 = new ArrayList<>();
    private List<String> mString2 = new ArrayList<>();
    private View mFootView;

    private List<MovieByCategory.DataBean.TypeBean> mTypeList;
    private List<MovieByCategory.DataBean.YearBean> mYearList;
    private List<MovieByCategory.DataBean.AreaBean> mAreaList;


    private String mCategory;

    public AllMovieAdapter(Context context, List<AllMovie.DataBean> data, boolean isHome, String category) {
        this.mContext = context;
        this.mDatas = data;
        this.isHome = isHome;
        mlLayoutInflater = LayoutInflater.from(context);
        this.mCategory = category;
    }

    public void dataChange(int pos, MovieByCategory.DataBean movieByCategoryDateBean) {
        XGIMILOG.D("mMovieByCategoryDateBean == null ?? " + (mMovieByCategoryDateBean == null));
        this.mIndex = pos;
        this.mMovieByCategoryDateBean = movieByCategoryDateBean;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FIRST_ITEM) {
            View view = mlLayoutInflater.inflate(R.layout.channel_first, parent, false);
            return new FistChannelViewHolder(view);
        }
        if (viewType == SECOND_ITEM) {
            View view = mlLayoutInflater.inflate(R.layout.home_item, parent, false);
            return new FistHomeViewHolder(view);
        }
        if (viewType == ITEM_VIEW) {
            View view = mlLayoutInflater.inflate(R.layout.all_movie_item, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == FOOT_VIEW) {
            View view = mlLayoutInflater.inflate(R.layout.instance_load_more_layout, parent, false);
            mFootView = view;
            return new FootViewHolder(view);
        }
        return null;
    }

    public void dismissFootView() {
        if (mFootView != null) {
            mFootView.setVisibility(View.GONE);
        }
    }

    public void showFootView() {
        if (mFootView != null) {
            mFootView.setVisibility(View.VISIBLE);
        }
    }

    public OnitemClick mLisener;

    public void setLisener(OnitemClick lisener) {
        this.mLisener = lisener;
    }

    public void dataChange(List<AllMovie.DataBean> data) {
//        XGIMILOG.D("AllMovie datachange");
        if (data != null) {
            this.mDatas = data;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position == 0 && holder instanceof FistHomeViewHolder) {
            ((FistHomeViewHolder) holder).tv_channel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ChannelActivity.class);
                    intent.putExtra("title", mCategory);
                    mContext.startActivity(intent);
                    ToosUtil.getInstance().addEventUmeng(mContext, "event_movie_condition_search_click");
                }
            });
        }
        if (mString != null && position == 0 && holder instanceof FistChannelViewHolder) {
//            isFirst = true;
            if (mMovieByCategoryDateBean != null && mString.size() == 0) {
                mString.add("全部");
                mString1.add("全部");
                mString2.add("全部");
                for (int i = 0; i < mMovieByCategoryDateBean.getType().size(); i++) {
                    mString.add(mMovieByCategoryDateBean.getType().get(i).getName());
                }

                for (int i = 0; i < mMovieByCategoryDateBean.getYear().size(); i++) {
                    mString1.add(mMovieByCategoryDateBean.getYear().get(i).getName());
                }

                for (int i = 0; i < mMovieByCategoryDateBean.getArea().size(); i++) {
                    mString2.add(mMovieByCategoryDateBean.getArea().get(i).getName());
                }
                ((FistChannelViewHolder) holder).addView(0, mString);
                ((FistChannelViewHolder) holder).addView(1, mString1);
                ((FistChannelViewHolder) holder).addView(2, mString2);
            }
        }
        if (mDatas != null) {
            if (holder instanceof ItemViewHolder && mDatas.size() > 0) {
//                XGIMILOG.D("position = " + position);
//                XGIMILOG.D("" + mDatas.get(position - 1).getName());
                ((ItemViewHolder) holder).tvName.setVisibility(View.GONE);
                ((ItemViewHolder) holder).iv.setVisibility(View.GONE);
                ((ItemViewHolder) holder).tvName1.setVisibility(View.GONE);
                ((ItemViewHolder) holder).iv1.setVisibility(View.GONE);
                ((ItemViewHolder) holder).tvName2.setVisibility(View.GONE);
                ((ItemViewHolder) holder).iv2.setVisibility(View.GONE);

                if (mDatas.size() > (position - 1) * 3) {
                    ((ItemViewHolder) holder).tvName.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).iv.setVisibility(View.VISIBLE);

//                    ImageLoaderUtils.display(mContext, ((ItemViewHolder) holder).iv, mDatas.get((position - 1) * 3).cover);

                    ImageLoader.getInstance().displayImage(mDatas.get((position - 1) * 3).cover, ((ItemViewHolder) holder).iv);

                    ((ItemViewHolder) holder).tvName.setText(mDatas.get((position - 1) * 3).name);
                    ((ItemViewHolder) holder).iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mLisener.onClickLisener(mDatas.get((position - 1) * 3).name, mDatas.get((position - 1) * 3).video_id, null);
                        }
                    });
                }

                if (mDatas.size() > (position - 1) * 3 + 1) {
                    ((ItemViewHolder) holder).tvName1.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).iv1.setVisibility(View.VISIBLE);

//                    ImageLoaderUtils.display(mContext, ((ItemViewHolder) holder).iv1, mDatas.get((position - 1) * 3 + 1).cover);
//
                    ImageLoader.getInstance().displayImage(mDatas.get((position - 1) * 3 + 1).cover, ((ItemViewHolder) holder).iv1);
                    ((ItemViewHolder) holder).tvName1.setText(mDatas.get((position - 1) * 3 + 1).name);
                    ((ItemViewHolder) holder).iv1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mLisener.onClickLisener(mDatas.get((position - 1) * 3 + 1).name, mDatas.get((position - 1) * 3 + 1).video_id, null);
                        }
                    });
                }

                if (mDatas.size() > (position - 1) * 3 + 2) {
                    ((ItemViewHolder) holder).tvName2.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).iv2.setVisibility(View.VISIBLE);

//                    ImageLoaderUtils.display(mContext, ((ItemViewHolder) holder).iv2, mDatas.get((position - 1) * 3 + 2).cover);

                    ImageLoader.getInstance().displayImage(mDatas.get((position - 1) * 3 + 2).cover, ((ItemViewHolder) holder).iv2);

                    ((ItemViewHolder) holder).tvName2.setText(mDatas.get((position - 1) * 3 + 2).name);

                    ((ItemViewHolder) holder).iv2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mLisener.onClickLisener(mDatas.get((position - 1) * 3 + 1).name, mDatas.get((position - 1) * 3 + 2).video_id, null);
                        }
                    });
                }

            }
        }
    }

    @Override
    public int getItemCount() {
//        int res = mDatas != null && mDatas.size() / 3 < 1 ? 1 : mDatas.size() / 3 + 1;
//        XGIMILOG.D("size = " + mDatas.size() + ", res = " + res);
//        if (mDatas.size() == 6) {
//            return res + 1;
//        }
//        if (mDatas != null && mDatas.size() != 0 && res == 1) {
//            res++;
//        }

//        if (mDatas != null && mDatas.size() > 0 && mDatas.size() % 3 != 0) {
//            res++;
//        }

        int res = 1;
        if (mDatas != null) {
            if (mDatas.size() == 0) {
                res = 1;
            } else if (mDatas.size() > 0 && mDatas.size() < 3) {
                res = 2;
            } else {
                if (mDatas.size() % 3 == 0) {
                    res = mDatas.size() / 3 + 1;
                } else {
                    res = mDatas.size() / 3 + 2;
                }
            }
        }
        return res;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            if (isHome) {
                return SECOND_ITEM;
            } else {
                return FIRST_ITEM;
            }
        }
        if (position + 1 == getItemCount() && getItemCount() > 6) {
            return FOOT_VIEW;
        } else {
            return ITEM_VIEW;
        }
    }


    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public final TextView tvName, tvName1, tvName2;
        public final ImageView iv, iv1, iv2;

        public ItemViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.iv);
            iv1 = (ImageView) itemView.findViewById(R.id.iv1);
            iv2 = (ImageView) itemView.findViewById(R.id.iv2);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvName1 = (TextView) itemView.findViewById(R.id.tv_name1);
            tvName2 = (TextView) itemView.findViewById(R.id.tv_name2);
        }
    }

    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends RecyclerView.ViewHolder {
        private TextView foot_view_item_tv;

        public FootViewHolder(View view) {
            super(view);
            foot_view_item_tv = (TextView) view.findViewById(R.id.foot_view_item_tv);
        }
    }


    public static class FistHomeViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_channel;

        public FistHomeViewHolder(View itemView) {
            super(itemView);
            tv_channel = (TextView) itemView.findViewById(R.id.tv_channel);
        }
    }

    public class FistChannelViewHolder extends RecyclerView.ViewHolder {
        List<LinearLayout> linears = new ArrayList<LinearLayout>();
        ChannelBean mChannes = new ChannelBean();
        int shangbiao = 0;
        int xiabiao = 0;
        private List<HorizontalScrollView> hors = new ArrayList<>();

        public FistChannelViewHolder(View itemView) {
            super(itemView);
            HorizontalScrollView mHori_one = (HorizontalScrollView) itemView.findViewById(R.id.horscroll_one);
            HorizontalScrollView mHori_two = (HorizontalScrollView) itemView.findViewById(R.id.horscroll_two);
            HorizontalScrollView mHori_three = (HorizontalScrollView) itemView.findViewById(R.id.horscroll_three);
            LinearLayout linearLayout1 = new LinearLayout(mContext);
            linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout linearLayout2 = new LinearLayout(mContext);
            linearLayout2.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout linearLayout3 = new LinearLayout(mContext);
            linearLayout2.setOrientation(LinearLayout.HORIZONTAL);

            linears.add(linearLayout1);
            linears.add(linearLayout2);
            linears.add(linearLayout3);

            hors.add(mHori_one);
            hors.add(mHori_two);
            hors.add(mHori_three);
        }

        public void addView(int i, List<String> a) {
            hors.get(i).removeAllViews();
            if (a != null) {
                for (int j = 0; j < a.size(); j++) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.FILL_PARENT);
                    final TextView tv = new TextView(mContext);
                    tv.setText(a.get(j));
                    tv.setTextSize(13);
                    linears.get(i).addView(tv);
                    if (j == 0 && (i == 0 || i == 1 || i == 2)) {
                        tv.setBackgroundResource(R.drawable.app_installation);
                        tv.setTextColor(Color.parseColor("#4392F3"));
                    } else {
                        tv.setBackgroundResource(R.drawable.white_background);
                        tv.setTextColor(Color.parseColor("#333333"));
                    }
                    tv.setLayoutParams(layoutParams);
                    if (!(j == 0)) {
                        layoutParams.leftMargin = 20;
                        tv.setGravity(Gravity.CENTER);
                    }
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
//                    setAnimation();
                            getIndex(tv);
                            changeColor(shangbiao, xiabiao, false);
                            if (shangbiao == 0) {
                                if (xiabiao == 0) {
                                    mChannes.kind = "";
                                } else {
                                    for (int i = 0; i < mMovieByCategoryDateBean.getType().size(); i++) {
                                        if (tv.getText().toString().trim().equals(mMovieByCategoryDateBean.getType().get(i).getName())) {
                                            mChannes.id = mMovieByCategoryDateBean.getType().get(i).getCategory_id() + "";
                                        }
                                    }
                                    mChannes.kind = tv.getText().toString().trim();
                                }
                            }
                            if (shangbiao == 1) {
                                if (xiabiao == 0) {
                                    mChannes.setYear("");
                                } else {
                                    mChannes.setYear(tv.getText().toString());
                                }
                            }
                            if (shangbiao == 2) {
                                if (xiabiao == 0) {
                                    mChannes.setArea("");
                                } else {
                                    for (int i = 0; i < mMovieByCategoryDateBean.getArea().size(); i++) {
                                        if (tv.getText().toString().trim().equals(mMovieByCategoryDateBean.getType().get(i).getName())) {
                                            mChannes.id = mMovieByCategoryDateBean.getType().get(i).getCategory_id() + "";
                                        }
                                    }
                                    mChannes.setArea(tv.getText().toString());
                                }
                            }
                            EventBus.getDefault().post(mChannes);
                        }
                    });
                }
                hors.get(i).addView(linears.get(i));
            }
        }

        private void getIndex(TextView tv) {
            for (int i = 0; i < linears.size(); i++) {
                for (int j = 0; j < linears.get(i).getChildCount(); j++) {
                    TextView tv1 = (TextView) linears.get(i).getChildAt(j);
                    if (tv.equals(tv1)) {
                        shangbiao = i;
                        xiabiao = j;
                    }
                }
            }
        }

        private void changeColor(int a, int b, boolean isfirst) {
            LinearLayout linearLayout3 = linears.get(a);
            for (int i = 0; i < linearLayout3.getChildCount(); i++) {
                TextView tv = (TextView) linearLayout3.getChildAt(i);
                if (b == i) {
                    tv.setBackgroundResource(R.drawable.app_installation);
                    tv.setTextColor(Color.parseColor("#4392F3"));
                } else {
                    tv.setBackgroundResource(R.drawable.white_background);
                    tv.setTextColor(Color.parseColor("#333333"));
                }
            }

        }
    }

}
