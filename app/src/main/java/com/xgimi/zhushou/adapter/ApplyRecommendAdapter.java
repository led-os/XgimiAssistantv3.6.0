package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.ApplyRecommendViewHolder.ApplyFooterViewHolder;
import com.xgimi.zhushou.adapter.ApplyRecommendViewHolder.ApplyHeaderItemViewHolder;
import com.xgimi.zhushou.adapter.ApplyRecommendViewHolder.ApplyHeaderViewHolder;
import com.xgimi.zhushou.adapter.ApplyRecommendViewHolder.ApplyItemViewHolder;
import com.xgimi.zhushou.bean.ApplyHomeBean;
import com.xgimi.zhushou.inter.ApplyFooterCLick;
import com.xgimi.zhushou.inter.OnApplyItemClick;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;
import com.xgimi.zhushou.widget.ViewFlow;

/**
 * Created by 霍长江 on 2016/8/17.
 */
public class ApplyRecommendAdapter extends SectionedRecyclerViewAdapter<ApplyHeaderViewHolder,
        ApplyItemViewHolder,
        ApplyFooterViewHolder> {

    private static final String TAG = "ApplyRecommendAdapter";
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private static final int ITEM_FIRST = 1;
    private ApplyHomeBean mDatas;
    private RecyclerView mRecyclerView;
    private int mPos;
    public ApplyRecommendAdapter(Context context,ApplyHomeBean data,RecyclerView recyclerView){
        this.mContext=context;
        this.mRecyclerView=recyclerView;
        this.mDatas=data;
        mLayoutInflater=LayoutInflater.from(context);

    }
    @Override
    protected int getSectionCount() {
        return (mDatas!=null&&mDatas.data!=null)?mDatas.data.subject.size()+3:0;
    }
    @Override
    protected int getItemCountForSection(int section) {
        if (section == 0) {
            return 1;
        }
        if (0 < section && section < mDatas.data.subject.size()+1)
            return mDatas.data.subject.get(section - 1).content.size() / 4>2?2:mDatas.data.subject.get(section - 1).content.size()/4;
        if (section==mDatas.data.subject.size()+1) {
            return mDatas.data.apps.size() / 4>3?3:mDatas.data.apps.size()/4;
        }
        if (section==mDatas.data.subject.size()+2) {
            return mDatas.data.games.size() /4>3?3:mDatas.data.games.size()/4;
        }
        return 1;
    }
    public void dataChange(ApplyHomeBean data,int pos){
        this.mDatas=data;
        this.mPos=pos;
        notifyDataSetChanged();
    }
    @Override
    protected int getSectionItemViewType(int section, int position) {
        if (section == 0) {
            return ITEM_FIRST;
        }
        return super.getSectionItemViewType(section, position);
    }


    @Override
    protected boolean hasFooterInSection(int section) {
        if(section==0){
            return false;
        }
        if(mPos==1){
            if(1 < section && section < mDatas.data.subject.size()+1){
                return false;
            }else{
                return true;
            }
        }else if(0 < section && section < mDatas.data.subject.size()+1){
            return false;
        }
        return true;
    }
    private ApplyFooterCLick mFooterClick;
    public void setmFooterClickLisener(ApplyFooterCLick lisener){
        this.mFooterClick=lisener;

    }
    private OnApplyItemClick onApplyItemClick;
    public void setmOnitemClick(OnApplyItemClick lisener){
        this.onApplyItemClick=lisener;
    }
    @Override
    protected boolean hasHeaderInSection(int section) {
        if (section == 0 ) {
            return false;
        }
        return true;
    }

    @Override
    protected ApplyHeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View view=mLayoutInflater.inflate(R.layout.app_lei_header_one,parent,false);

        return new ApplyHeaderViewHolder(view);
    }

    @Override
    protected ApplyFooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        View view=mLayoutInflater.inflate(R.layout.animation_footer,parent,false);

        return new ApplyFooterViewHolder(view);
    }
    @Override
    protected ApplyItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_FIRST) {
            View view = mLayoutInflater.inflate(R.layout.recommend_item_first, parent, false);
            return new ApplyHeaderItemViewHolder(view);
        }else {
            View view = mLayoutInflater.inflate(R.layout.apply_recommend_item, parent, false);
            return new ApplyItemViewHolder(view);
        }
    }
    @Override
    protected void onBindSectionHeaderViewHolder(ApplyHeaderViewHolder holder, int section) {
        if(mDatas!=null&&mDatas.data!=null){
            for (int i = 0; i <mDatas.data.subject.size() ; i++) {
                if(section==i+1) {
                    ((ApplyHeaderViewHolder) holder).tv.setText(mDatas.data.subject.get(i).title);
                }
            }
            if(section==mDatas.data.subject.size()+1){
                ((ApplyHeaderViewHolder) holder).tv.setText("热门应用");
            }
            if(section==mDatas.data.subject.size()+2){
                ((ApplyHeaderViewHolder) holder).tv.setText("热门游戏");
            }
        }
    }
    @Override
    protected void onBindSectionFooterViewHolder(ApplyFooterViewHolder holder, final int section) {
    if(mDatas!=null&&mDatas.data!=null){
        if(mFooterClick!=null){
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mFooterClick.OnFooterClickLisener(section,mPos);
                        }
                    });
                }
    }
    }
    @Override
    protected void onBindItemViewHolder(final ApplyItemViewHolder holder, final int section, final int position) {
        Log.d(TAG, "onBindItemViewHolder: section = " + section + ", position = " + position);
        if(mDatas!=null&&mDatas.data!=null) {
            if (section == 0) {
                ((ApplyHeaderItemViewHolder) holder).ll_dian.removeAllViews();
                ((ApplyHeaderItemViewHolder) holder).viewFlow.setAdapter(new ApplyRecommendFirstAdapter(mContext, mRecyclerView, mDatas.data).setInfiniteLoop(true));
                for (int i = 0; i < mDatas.data.ad.size(); i++) {
                    createImageView(((ApplyHeaderItemViewHolder) holder).ll_dian);
                }
                ((ApplyHeaderItemViewHolder) holder).viewFlow.setLiserner(new ViewFlow.postion() {
                    @Override
                    public void postion(int postin) {
                        for (int i = 0; i < ((ApplyHeaderItemViewHolder) holder).ll_dian.getChildCount(); i++) {
                            if (postin % ((ApplyHeaderItemViewHolder) holder).ll_dian.getChildCount() == i) {
                                ImageView childAt = (ImageView) ((ApplyHeaderItemViewHolder) holder).ll_dian.getChildAt(i);
                                childAt.setImageResource(R.drawable.baidian);
                            } else {
                                ImageView childAt = (ImageView) ((ApplyHeaderItemViewHolder) holder).ll_dian.getChildAt(i);
                                childAt.setImageResource(R.drawable.hongdian);
                            }
                        }
                    }
                });
                ((ApplyHeaderItemViewHolder) holder).viewFlow.setmSideBuffer(mDatas.data.ad.size()); // 实际图片张数，
                ((ApplyHeaderItemViewHolder) holder).viewFlow.setSelection(mDatas.data.ad.size() * 1000); // 设置初始位置
            }
            if (0 < section && section <= mDatas.data.subject.size()) {
                ((ApplyItemViewHolder) holder).name.setText(mDatas.data.subject.get(section - 1).content.get(position * 4).name);
                ((ApplyItemViewHolder) holder).name1.setText(mDatas.data.subject.get(section - 1).content.get(position * 4 + 1).name);
                ((ApplyItemViewHolder) holder).name2.setText(mDatas.data.subject.get(section - 1).content.get(position * 4 + 2).name);
                ((ApplyItemViewHolder) holder).name3.setText(mDatas.data.subject.get(section - 1).content.get(position * 4 + 3).name);
                ImageLoaderUtils.displayTwo(mContext, ((ApplyItemViewHolder) holder).image, mDatas.data.subject.get(section - 1).content.get(position * 4).icon);
                ImageLoaderUtils.displayTwo(mContext, ((ApplyItemViewHolder) holder).image1, mDatas.data.subject.get(section - 1).content.get(position * 4 + 1).icon);
                ImageLoaderUtils.displayTwo(mContext, ((ApplyItemViewHolder) holder).image2, mDatas.data.subject.get(section - 1).content.get(position * 4 + 2).icon);
                ImageLoaderUtils.displayTwo(mContext, ((ApplyItemViewHolder) holder).image3, mDatas.data.subject.get(section - 1).content.get(position * 4 + 3).icon);
                if (mPos == 1 && section == 1) {
                    ((ApplyItemViewHolder) holder).views.setVisibility(View.GONE);
                } else {
                    ((ApplyItemViewHolder) holder).views.setVisibility(View.VISIBLE);
                }
            }
            if (section == mDatas.data.subject.size() + 1) {
                ((ApplyItemViewHolder) holder).name.setText(mDatas.data.apps.get(position * 4).title);
                ((ApplyItemViewHolder) holder).name1.setText(mDatas.data.apps.get(position * 4 + 1).title);
                ((ApplyItemViewHolder) holder).name2.setText(mDatas.data.apps.get(position * 4 + 2).title);
                ((ApplyItemViewHolder) holder).name3.setText(mDatas.data.apps.get(position * 4 + 3).title);
                ImageLoaderUtils.displayTwo(mContext, ((ApplyItemViewHolder) holder).image, mDatas.data.apps.get(position * 4).icon);
                ImageLoaderUtils.displayTwo(mContext, ((ApplyItemViewHolder) holder).image1, mDatas.data.apps.get(position * 4 + 1).icon);
                ImageLoaderUtils.displayTwo(mContext, ((ApplyItemViewHolder) holder).image2, mDatas.data.apps.get(position * 4 + 2).icon);
                ImageLoaderUtils.displayTwo(mContext, ((ApplyItemViewHolder) holder).image3, mDatas.data.apps.get(position * 4 + 3).icon);
                ((ApplyItemViewHolder) holder).views.setVisibility(View.GONE);
            }
            if (section == mDatas.data.subject.size() + 2) {
                ((ApplyItemViewHolder) holder).name.setText(mDatas.data.games.get(position * 4).title);
                ((ApplyItemViewHolder) holder).name1.setText(mDatas.data.games.get(position * 4 + 1).title);
                ((ApplyItemViewHolder) holder).name2.setText(mDatas.data.games.get(position * 4 + 2).title);
                ((ApplyItemViewHolder) holder).name3.setText(mDatas.data.games.get(position * 4 + 3).title);
                ImageLoaderUtils.displayTwo(mContext, ((ApplyItemViewHolder) holder).image, mDatas.data.games.get(position * 4).icon);
                ImageLoaderUtils.displayTwo(mContext, ((ApplyItemViewHolder) holder).image1, mDatas.data.games.get(position * 4 + 1).icon);
                ImageLoaderUtils.displayTwo(mContext, ((ApplyItemViewHolder) holder).image2, mDatas.data.games.get(position * 4 + 2).icon);
                ImageLoaderUtils.displayTwo(mContext, ((ApplyItemViewHolder) holder).image3, mDatas.data.games.get(position * 4 + 3).icon);
                ((ApplyItemViewHolder) holder).views.setVisibility(View.GONE);
            }
            if (section > 0) {
                ((ApplyItemViewHolder) holder).image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (0 < section && section <= mDatas.data.subject.size()) {
                            Log.d(TAG, "1 (section = " + section + ", postion = " + position);
                            onApplyItemClick.onItemClickLisene(mDatas.data.subject.get(section - 1).content.get(position * 4).pacagename, mDatas.data.subject.get(section - 1).content.get(position * 4).app_id, section, mPos);
                        }
                        if (section == mDatas.data.subject.size() + 1) {
                            onApplyItemClick.onItemClickLisene(null, mDatas.data.apps.get(position * 4).id, section, mPos);
                        }
                        if (section == mDatas.data.subject.size() + 2) {
                            onApplyItemClick.onItemClickLisene(null, mDatas.data.games.get(position * 4).id, section, mPos);

                        }
                    }
                });
                ((ApplyItemViewHolder) holder).image1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (0 < section && section <= mDatas.data.subject.size()) {
                            Log.d(TAG, "2 (section = " + section + ", postion = " + position);
                            onApplyItemClick.onItemClickLisene(mDatas.data.subject.get(section - 1).content.get(position * 4 + 1).pacagename, mDatas.data.subject.get(section - 1).content.get(position * 4 + 1).app_id, section, mPos);
                        }
                        if (section == mDatas.data.subject.size() + 1) {
                            onApplyItemClick.onItemClickLisene(null, mDatas.data.apps.get(position * 4 + 1).id, section, mPos);

                        }
                        if (section == mDatas.data.subject.size() + 2) {
                            onApplyItemClick.onItemClickLisene(null, mDatas.data.games.get(position * 4 + 1).id, section, mPos);

                        }
                    }
                });
                ((ApplyItemViewHolder) holder).image2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (0 < section && section <= mDatas.data.subject.size()) {
                            Log.d(TAG, "3 (section = " + section + ", postion = " + position);
                            onApplyItemClick.onItemClickLisene(mDatas.data.subject.get(section - 1).content.get(position * 4 + 2).pacagename, mDatas.data.subject.get(section - 1).content.get(position * 4 + 2).app_id, section, mPos);
                        }
                        if (section == mDatas.data.subject.size() + 1) {
                            onApplyItemClick.onItemClickLisene(null, mDatas.data.apps.get(position * 4 + 2).id, section, mPos);
                        }
                        if (section == mDatas.data.subject.size() + 2) {
                            onApplyItemClick.onItemClickLisene(null, mDatas.data.games.get(position * 4 + 2).id, section, mPos);

                        }
                    }
                });
                ((ApplyItemViewHolder) holder).image3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (0 < section && section <= mDatas.data.subject.size()) {
                            onApplyItemClick.onItemClickLisene(mDatas.data.subject.get(section - 1).content.get(position * 4 + 3).pacagename, mDatas.data.subject.get(section - 1).content.get(position * 4 + 3).app_id, section, mPos);
                        }
                        if (section == mDatas.data.subject.size() + 1) {
                            onApplyItemClick.onItemClickLisene(null, mDatas.data.apps.get(position * 4 + 3).id, section, mPos);
                        }
                        if (section == mDatas.data.subject.size() + 2) {
                            onApplyItemClick.onItemClickLisene(null, mDatas.data.games.get(position * 4 + 3).id, section, mPos);
                        }
                    }
                });
            }
        }
    }
    /**
     * 动态创建
     */
    public void createImageView(LinearLayout ll_dian){
        ImageView iv=new ImageView(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        iv.setLayoutParams(layoutParams);
        layoutParams.leftMargin=10;
        iv.setImageResource(R.drawable.hongdian);
        ll_dian.addView(iv);
    }
}
