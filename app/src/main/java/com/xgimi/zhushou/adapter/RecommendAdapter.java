package com.xgimi.zhushou.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.waxrain.droidsender.SenderService;
import com.xgimi.device.device.GMDevice;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.FileTransferActivity;
import com.xgimi.zhushou.activity.IntelligenceRecomendActivity;
import com.xgimi.zhushou.activity.PhoneZiYuanActivity;
import com.xgimi.zhushou.activity.PlayHostoryActivity;
import com.xgimi.zhushou.activity.SearceWebVideoActivity;
import com.xgimi.zhushou.activity.WallpaperActivity;
import com.xgimi.zhushou.adapter.RecommendViewHolder.AppCountItemFirstViewHolder;
import com.xgimi.zhushou.adapter.RecommendViewHolder.AppFooterViewHolder;
import com.xgimi.zhushou.adapter.RecommendViewHolder.AppHeaderViewHolder;
import com.xgimi.zhushou.adapter.RecommendViewHolder.AppItemSecondViewHolder;
import com.xgimi.zhushou.adapter.RecommendViewHolder.AppItemViewHolder;
import com.xgimi.zhushou.bean.MovieRecommend;
import com.xgimi.zhushou.inter.OnFooterClick;
import com.xgimi.zhushou.inter.OnitemClick;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;
import com.xgimi.zhushou.netUtil.NetUtil;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.SaveTVApp;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.WOL;
import com.xgimi.zhushou.util.XGIMILOG;
import com.xgimi.zhushou.widget.ImageDilog;
import com.xgimi.zhushou.widget.ViewFlow;

import de.greenrobot.event.EventBus;

/**
 * Created by 霍长江 on 2016/8/8.
 */
public class RecommendAdapter extends SectionedRecyclerViewAdapter<AppHeaderViewHolder,
        AppItemViewHolder,
        AppFooterViewHolder> {

    private static final String TAG = "RecommendAdapter";
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private MovieRecommend mDatas;
    private static final int ITEM_FIRST = 1;
    private static final int ITEM_SECOND = 2;
    private int FOOTER_CLASS = 3;
    private RecyclerView mRecyclerView;
    private int isHasRecommend = 0;
    private String[] more = new String[]{"更多电影", "更多电视剧", "更多综艺", "更多动漫", "更多纪录片"};


    private ImageDilog.OnNoDeviceListener mOnNoDeviceListener = new ImageDilog.OnNoDeviceListener() {
        @Override
        public void onNoDevice() {
            Toast.makeText(mContext, "镜像失败，请稍后再试", Toast.LENGTH_SHORT).show();
        }
    };

    public RecommendAdapter(Context context, MovieRecommend data, RecyclerView recyclerView) {
        this.mContext = context;
        this.mDatas = data;
        mLayoutInflater = LayoutInflater.from(context);
        this.mRecyclerView = recyclerView;
    }

    @Override
    protected int getSectionCount() {
        return (mDatas != null && mDatas.data != null) ? mDatas.data.subject.size() + mDatas.data.recommend.size() + isHasRecommend + 1 : 0;
    }

    public void dataChange(MovieRecommend datas) {
        this.mDatas = datas;
        isHasRecommend = 0;
//        if (datas.data.individualitys != null && datas.data.individualitys.data.size() > 0) {
//            isHasRecommend = 1;
//        }
        notifyDataSetChanged();
    }

    public OnFooterClick mFooterLisener;

    public void setmFooterLisener(OnFooterClick footerLisener) {
        this.mFooterLisener = footerLisener;
    }

    public OnitemClick mLisener;

    public void setLisener(OnitemClick lisener) {
        this.mLisener = lisener;
    }

    @Override
    protected int getItemCountForSection(int section) {
        if (section == 0) {
            return 1;
        }
        if (isHasRecommend == 1 && section == 1) {
            return mDatas.data.individualitys.data.size() / 3;
        }
        if (isHasRecommend < section && section <= mDatas.data.subject.size() + isHasRecommend) {
            return mDatas.data.subject.get(section - 1 - isHasRecommend).contents.size() / 2;
        }
        return mDatas.data.recommend != null ? 2 : 0;
    }

    @Override
    protected int getSectionItemViewType(int section, int position) {
        if (section == 0) {
            return ITEM_FIRST;
        } else if (isHasRecommend < section && section <= mDatas.data.subject.size() + isHasRecommend) {
            return ITEM_SECOND;
        }
        return super.getSectionItemViewType(section, position);
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        if (section == 0) {
            if (mDatas != null && mDatas.data.live != null && mDatas.data.live.size() > 0) {
                return true;

            } else {
                return false;

            }
        }
//        if(0<section&&section<=mDatas.data.subject.size()){
//            return false;
//        }
        return true;
    }

    @Override
    protected boolean hasHeaderInSection(int section) {
        if (section == 0) {
            return false;
        }
        return true;
    }

    @Override
    protected AppHeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.app_lei_header_one, parent, false);
        return new AppHeaderViewHolder(view);
    }

    @Override
    protected AppFooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.animation_footer, parent, false);
        return new AppFooterViewHolder(view);
    }


    @Override
    protected AppItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_FIRST) {
            View view = mLayoutInflater.inflate(R.layout.recommend_item_first, parent, false);
            return new AppCountItemFirstViewHolder(view);
        } else if (viewType == ITEM_SECOND) {
            View view = mLayoutInflater.inflate(R.layout.app_count_item, parent, false);
            return new AppItemViewHolder(view);
        } else {
            View view = mLayoutInflater.inflate(R.layout.app_count_second_item, parent, false);
            return new AppItemSecondViewHolder(view);
        }
    }

    @Override
    protected void onBindSectionHeaderViewHolder(AppHeaderViewHolder holder, int section) {
        if (mDatas != null && mDatas.data != null) {
            if (isHasRecommend == 1 && section == 1) {
                holder.tv.setText(mDatas.data.individualitys.title);
            }
            for (int i = 0; i < mDatas.data.subject.size(); i++) {
                if (section == i + 1 + isHasRecommend) {
                    holder.tv.setText(mDatas.data.subject.get(i).title);
                }
            }
            for (int i = 0; i < mDatas.data.recommend.size(); i++) {
                if (section == mDatas.data.subject.size() + i + 1 + isHasRecommend) {
                    holder.tv.setText(mDatas.data.recommend.get(i).name);
                }
            }
        }
    }

    @Override
    protected void onBindSectionFooterViewHolder(AppFooterViewHolder holder, final int section) {
        if (holder instanceof AppFooterViewHolder) {

            if (mFooterLisener != null) {

                ((AppFooterViewHolder) holder).ll.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (isHasRecommend == 1 && section == 1) {
                            Intent intent = new Intent(mContext, IntelligenceRecomendActivity.class);
                            intent.putExtra("mtitle", mDatas.data.individualitys.title);
                            mContext.startActivity(intent);
                            SaveData.getInstance().mSouceInsight = "1";
                            SaveData.getInstance().mSoucePage = "1";
                            SaveData.getInstance().mSourceInsightLocation = "更多";
                        } else {
                            SaveData.getInstance().mSoucePage = "1";
                            SaveData.getInstance().mSouceInsight = "1";
                            SaveData.getInstance().mSourceInsightLocation = "更多";
                            mFooterLisener.clickLisener(section - mDatas.data.subject.size() - isHasRecommend, null);
                        }
                    }
                });
            }
            if (0 < section && section <= mDatas.data.subject.size()) {
//                ((AppFooterViewHolder) holder).tv_more.setText(more[section-mDatas.data.subject.size()-1]+" >");
            }
            if (section == 0) {
                if (mDatas != null && mDatas.data.live != null && mDatas.data.live.size() > 0) {
                    if (mDatas.data.live.get(0).list.size() == 1) {
                        ((AppFooterViewHolder) holder).tv_title.setText(mDatas.data.live.get(0).parent_title);
                        ImageLoaderUtils.display(mContext, ((AppFooterViewHolder) holder).iv, NetUtil.IMAGEURL + mDatas.data.live.get(0).list.get(0).img_url);
                        ((AppFooterViewHolder) holder).tv_detail_one.setText(mDatas.data.live.get(0).list.get(0).text2);
                        isShowDisPlay(holder, true, false, true, false, false);
                        ((AppFooterViewHolder) holder).tv.setText(mDatas.data.live.get(0).list.get(0).text1);
                        ((AppFooterViewHolder) holder).iv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mFooterLisener.clickLisener(-1, mDatas.data.live.get(0).list.get(0));
                            }
                        });

                    } else if (mDatas.data.live.get(0).list.size() == 2) {
                        ((AppFooterViewHolder) holder).tv_title.setText(mDatas.data.live.get(0).parent_title);
                        ImageLoaderUtils.display(mContext, ((AppFooterViewHolder) holder).iv, NetUtil.IMAGEURL + mDatas.data.live.get(0).list.get(0).img_url);
                        ImageLoaderUtils.display(mContext, ((AppFooterViewHolder) holder).iv1, NetUtil.IMAGEURL + mDatas.data.live.get(0).list.get(1).img_url);
                        ((AppFooterViewHolder) holder).tv_detail_two.setText(mDatas.data.live.get(0).list.get(0).text2);
                        ((AppFooterViewHolder) holder).tv_detail_three.setText(mDatas.data.live.get(0).list.get(1).text2);
                        ((AppFooterViewHolder) holder).tv1.setText(mDatas.data.live.get(0).list.get(0).text1);
                        ((AppFooterViewHolder) holder).tv2.setText(mDatas.data.live.get(0).list.get(1).text1);

                        isShowDisPlay(holder, true, false, true, false, true);

                        ((AppFooterViewHolder) holder).iv1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mFooterLisener.clickLisener(-1, mDatas.data.live.get(0).list.get(0));
                            }
                        });
                        ((AppFooterViewHolder) holder).iv2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mFooterLisener.clickLisener(-1, mDatas.data.live.get(0).list.get(1));
                            }
                        });

                    }
                }
            }
            if (mDatas != null && mDatas.data != null) {

                if (isHasRecommend < section && section <= mDatas.data.subject.size()) {
                    if (mDatas.data.subject.get(section - 1 - isHasRecommend).live != null && mDatas.data.subject.get(section - 1 - isHasRecommend).live.size() > 0) {
                        if (mDatas.data.subject.get(section - 1 - isHasRecommend).live.get(0).list.size() == 1) {
                            isShowDisPlay(holder, true, true, true, false, false);

                            ((AppFooterViewHolder) holder).tv_title.setText(mDatas.data.subject.get(section - 1 - isHasRecommend).live.get(0).parent_title);
                            ImageLoaderUtils.display(mContext, ((AppFooterViewHolder) holder).iv, NetUtil.IMAGEURL + mDatas.data.subject.get(section - 1 - isHasRecommend).live.get(0).list.get(0).img_url);
                            ((AppFooterViewHolder) holder).tv_detail_one.setText(mDatas.data.subject.get(section - 1 - isHasRecommend).live.get(0).list.get(0).text2);

                            ((AppFooterViewHolder) holder).tv.setText(mDatas.data.subject.get(section - 1 - isHasRecommend).live.get(0).list.get(0).text1);

                            ((AppFooterViewHolder) holder).iv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mFooterLisener.clickLisener(-1, mDatas.data.subject.get(section - 1 - isHasRecommend).live.get(0).list.get(0));
                                }
                            });

                        } else if (mDatas.data.subject.get(section - 1 - isHasRecommend).live.get(0).list.size() == 2) {
                            isShowDisPlay(holder, true, true, true, false, true);
                            ((AppFooterViewHolder) holder).tv_title.setText(mDatas.data.subject.get(section - 1 - isHasRecommend).live.get(0).parent_title);
                            ImageLoaderUtils.display(mContext, ((AppFooterViewHolder) holder).iv, NetUtil.IMAGEURL + mDatas.data.subject.get(section - 1 - isHasRecommend).live.get(0).list.get(0).img_url);
                            ImageLoaderUtils.display(mContext, ((AppFooterViewHolder) holder).iv1, NetUtil.IMAGEURL + mDatas.data.subject.get(section - 1 - isHasRecommend).live.get(0).list.get(1).img_url);
                            ((AppFooterViewHolder) holder).tv_detail_two.setText(mDatas.data.subject.get(section - 1 - isHasRecommend).live.get(0).list.get(0).text2);
                            ((AppFooterViewHolder) holder).tv_detail_three.setText(mDatas.data.subject.get(section - 1 - isHasRecommend).live.get(0).list.get(1).text2);

                            ((AppFooterViewHolder) holder).tv1.setText(mDatas.data.subject.get(section - 1 - isHasRecommend).live.get(0).list.get(0).text1);
                            ((AppFooterViewHolder) holder).tv2.setText(mDatas.data.subject.get(section - 1 - isHasRecommend).live.get(0).list.get(1).text1);


                            ((AppFooterViewHolder) holder).iv1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mFooterLisener.clickLisener(-1, mDatas.data.subject.get(section - 1 - isHasRecommend).live.get(0).list.get(0));
                                }
                            });
                            ((AppFooterViewHolder) holder).iv2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mFooterLisener.clickLisener(-1, mDatas.data.subject.get(section - 1 - isHasRecommend).live.get(0).list.get(1));
                                }
                            });
                        }
                    } else {
                        isShowDisPlay(holder, false, false, false, true, false);
                    }
                }
                if (section > mDatas.data.subject.size() + isHasRecommend) {
                    if (mDatas.data.recommend.get(section - mDatas.data.subject.size() - 1 - isHasRecommend).live != null && mDatas.data.recommend.get(section - mDatas.data.subject.size() - 1).live.size() > 0) {
                        if (mDatas.data.recommend.get(section - mDatas.data.subject.size() - 1 - isHasRecommend).live.get(0).list.size() == 1) {

                            ((AppFooterViewHolder) holder).tv_title.setText(mDatas.data.recommend.get(section - mDatas.data.subject.size() - 1 - isHasRecommend).live.get(0).parent_title);
                            ImageLoaderUtils.display(mContext, ((AppFooterViewHolder) holder).iv, NetUtil.IMAGEURL + mDatas.data.recommend.get(section - mDatas.data.subject.size() - 1 - isHasRecommend).live.get(0).list.get(0).img_url);
                            ((AppFooterViewHolder) holder).tv_detail_one.setText(mDatas.data.recommend.get(section - mDatas.data.subject.size() - 1 - isHasRecommend).live.get(0).list.get(0).text2);
                            ((AppFooterViewHolder) holder).tv.setText(mDatas.data.recommend.get(section - mDatas.data.subject.size() - 1 - isHasRecommend).live.get(0).list.get(0).text1);

                            isShowDisPlay(holder, true, true, true, false, false);

                            ((AppFooterViewHolder) holder).iv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mFooterLisener.clickLisener(-1, mDatas.data.recommend.get(section - mDatas.data.subject.size() - 1 - isHasRecommend).live.get(0).list.get(0));
                                }
                            });
                        } else if (mDatas.data.recommend.get(section - mDatas.data.subject.size() - 1 - isHasRecommend).live.get(0).list.size() == 2) {

                            ((AppFooterViewHolder) holder).tv_title.setText(mDatas.data.recommend.get(section - mDatas.data.subject.size() - 1).live.get(0).parent_title);
                            ImageLoaderUtils.display(mContext, ((AppFooterViewHolder) holder).iv, NetUtil.IMAGEURL + mDatas.data.recommend.get(section - mDatas.data.subject.size() - 1).live.get(0).list.get(0).img_url);
                            ImageLoaderUtils.display(mContext, ((AppFooterViewHolder) holder).iv1, NetUtil.IMAGEURL + mDatas.data.recommend.get(section - mDatas.data.subject.size() - 1).live.get(0).list.get(1).img_url);
                            ((AppFooterViewHolder) holder).tv_detail_two.setText(mDatas.data.recommend.get(section - mDatas.data.subject.size() - 1).live.get(0).list.get(0).text2);
                            ((AppFooterViewHolder) holder).tv_detail_three.setText(mDatas.data.recommend.get(section - mDatas.data.subject.size() - 1).live.get(0).list.get(1).text2);

                            ((AppFooterViewHolder) holder).tv1.setText(mDatas.data.recommend.get(section - mDatas.data.subject.size() - 1).live.get(0).list.get(0).text1);
                            ((AppFooterViewHolder) holder).tv2.setText(mDatas.data.recommend.get(section - mDatas.data.subject.size() - 1).live.get(0).list.get(1).text1);

                            isShowDisPlay(holder, true, true, true, false, true);


                            ((AppFooterViewHolder) holder).iv1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mFooterLisener.clickLisener(-1, mDatas.data.recommend.get(section - mDatas.data.subject.size() - 1 - isHasRecommend).live.get(0).list.get(0));
                                }
                            });
                            ((AppFooterViewHolder) holder).iv2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mFooterLisener.clickLisener(-1, mDatas.data.recommend.get(section - mDatas.data.subject.size() - 1 - isHasRecommend).live.get(0).list.get(1));
                                }
                            });

                        }
                    } else {
                        isShowDisPlay(holder, true, true, false, false, false);
                    }
                }
            }
            if (section == 1 && isHasRecommend == 1) {
                isShowDisPlay(holder, true, true, false, false, false);
            }
        }
    }

    @Override
    protected void onBindItemViewHolder(final AppItemViewHolder holder, final int section, final int position) {
        if (mDatas != null && mDatas.data != null) {
            if (section == 0) {
                ((AppCountItemFirstViewHolder) holder).ll_dian.removeAllViews();
                ((AppCountItemFirstViewHolder) holder).viewFlow.setAdapter(new AppAdviceAdapter(mContext, mRecyclerView, mDatas.data).setInfiniteLoop(true));
                ((AppCountItemFirstViewHolder) holder).ll_rukou.setVisibility(View.VISIBLE);
                for (int i = 0; i < mDatas.data.ad.size(); i++) {
                    createImageView(((AppCountItemFirstViewHolder) holder).ll_dian);
                }

                ((AppCountItemFirstViewHolder) holder).viewFlow.setLiserner(new ViewFlow.postion() {
                    @Override
                    public void postion(int postin) {
                        for (int i = 0; i < ((AppCountItemFirstViewHolder) holder).ll_dian.getChildCount(); i++) {
                            if (postin % ((AppCountItemFirstViewHolder) holder).ll_dian.getChildCount() == i) {
                                ImageView childAt = (ImageView) ((AppCountItemFirstViewHolder) holder).ll_dian.getChildAt(i);
                                childAt.setImageResource(R.drawable.baidian);
                            } else {
                                ImageView childAt = (ImageView) ((AppCountItemFirstViewHolder) holder).ll_dian.getChildAt(i);
                                childAt.setImageResource(R.drawable.hongdian);
                            }
                        }
                    }
                });
                ((AppCountItemFirstViewHolder) holder).viewFlow.setmSideBuffer(mDatas.data.ad.size()); // 实际图片张数，
                ((AppCountItemFirstViewHolder) holder).viewFlow.setSelection(mDatas.data.ad.size() * 1000); // 设置初始位置
                ((AppCountItemFirstViewHolder) holder).ll_file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Constant.netStatus) {

                            Intent intent = new Intent(mContext, FileTransferActivity.class);
                            mContext.startActivity(intent);
                        } else {
                            ToosUtil.getInstance().isConnectTv((Activity) mContext);
                        }
                    }
                });
                ((AppCountItemFirstViewHolder) holder).ll_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Constant.netStatus) {
                            if (Integer.valueOf(Build.VERSION.SDK) >= 21) {
                                Log.d(TAG, "onClick: " + SaveTVApp.getInstance(mContext).isWirelessScreen);
                                if (SaveTVApp.getInstance(mContext).isWirelessScreen) {
                                    if (!SenderService.mirrorServStarted) {
                                        SenderService.requestRestartMirrorService();
                                    }
                                    ImageDilog imageDilog = new ImageDilog(mContext, "", mOnNoDeviceListener);
                                    imageDilog.show();
                                    SaveData.getInstance().path = 0;
                                } else {
                                    if (SaveTVApp.instance.mTvApp == null) {
                                        Toast.makeText(mContext, "应用列表还未更新，请尝试重新连接投影", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(mContext, "功能即将上线，请耐心等待。", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(mContext, "本功能仅支持Android5.1及以上版本", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            ToosUtil.getInstance().isConnectTv((Activity) mContext);
                        }
                    }
                });
                ((AppCountItemFirstViewHolder) holder).ll_phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, PhoneZiYuanActivity.class);
                        mContext.startActivity(intent);
                    }
                });
                ((AppCountItemFirstViewHolder) holder).ll_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToosUtil.getInstance().addEventUmeng(mContext, "event_movie_history");
                        Intent intent2 = new Intent(mContext, PlayHostoryActivity.class);
                        mContext.startActivity(intent2);

                        //WIFI开机
//                        if (App.getContext().readLastConnectedDevice() != null) {
//                            GMDevice device = App.getContext().readLastConnectedDevice();
//                            if (device.getIp() != null && !device.getIp().equals("") && device.getMac() != null && !device.getMac().equals("")) {
//                                WOL.sendMagicPackage(device.getMac(), device.getIp());
//                                App.mOpenDeviceByMagicPacket = true;
//                            }
//                        }




//                        EventBus.getDefault().post(new String("tv"));
                        //电视台点击
//                        Intent intent = new Intent(mContext, WallpaperActivity.class);
//                        mContext.startActivity(intent);
                    }
                });
                ((AppCountItemFirstViewHolder) holder).ll_video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent inten = new Intent(mContext, SearceWebVideoActivity.class);
                        mContext.startActivity(inten);
                    }
                });

            } else if (isHasRecommend < section && section <= mDatas.data.subject.size() + isHasRecommend) {
                if (holder instanceof AppItemViewHolder) {
                    final MovieRecommend.DataBean.SubjectBean subjectBean = mDatas.data.subject.get(section - 1 - isHasRecommend);
//                    if (subjectBean.layout.equals("1")) {
                    if ((subjectBean.layout + "").equals("1")) {
                        if (position == 0) {
                            if (position < mDatas.data.subject.get(section - 1 - isHasRecommend).contents.size() / 2) {
                                ((AppItemViewHolder) holder).xianview.setVisibility(View.GONE);

                            }
                            if (position == mDatas.data.subject.get(section - 1 - isHasRecommend).contents.size() / 2 - 1) {
//                                    ((AppItemViewHolder) holder).xianview.setVisibility(View.VISIBLE);

                            }
                            ((AppItemViewHolder) holder).ll.setVisibility(View.VISIBLE);
                            ((AppItemViewHolder) holder).tv.setText(subjectBean.contents.get(position).name);
                            ((AppItemViewHolder) holder).tv1.setText(subjectBean.contents.get(position + 1).name);
                            ((AppItemViewHolder) holder).tv2.setText(subjectBean.contents.get(position + 2).name);

                            ((AppItemViewHolder) holder).tv_detail_one.setText(subjectBean.contents.get(position).description);
                            ((AppItemViewHolder) holder).tv_detail_two.setText(subjectBean.contents.get(position + 1).description);
                            ((AppItemViewHolder) holder).tv_detail_three.setText(subjectBean.contents.get(position + 2).description);

//                            ImageLoaderUtils.display(mContext, ((AppItemViewHolder) holder).iv, NetUtil.IMAGEURL + subjectBean.contents.get(position).img);
//                            ImageLoaderUtils.display(mContext, ((AppItemViewHolder) holder).iv1, NetUtil.IMAGEURL + subjectBean.contents.get(position + 1).img);
//                            ImageLoaderUtils.display(mContext, ((AppItemViewHolder) holder).iv2, NetUtil.IMAGEURL + subjectBean.contents.get(position + 2).img);

                            ImageLoader.getInstance().displayImage(NetUtil.IMAGEURL + subjectBean.contents.get(position).img, ((AppItemViewHolder) holder).iv);
                            ImageLoader.getInstance().displayImage(NetUtil.IMAGEURL + subjectBean.contents.get(position + 1).img, ((AppItemViewHolder) holder).iv1);
                            ImageLoader.getInstance().displayImage(NetUtil.IMAGEURL + subjectBean.contents.get(position + 2).img, ((AppItemViewHolder) holder).iv2);

                            XGIMILOG.D("加载三张" + subjectBean.contents.get(position).img);
                            ((AppItemViewHolder) holder).iv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mLisener.onClickLisener(subjectBean.contents.get(position).name, subjectBean.contents.get(position).content_id, subjectBean.contents.get(position).type);
                                    XGIMILOG.D("section = " + section + ", position = " + position);
                                    SaveData.getInstance().mSouceInsight = "2";
                                    SaveData.getInstance().mSoucePage = "1";
                                    SaveData.getInstance().mSourceInsightLocation = section + "";

                                }
                            });
                            ((AppItemViewHolder) holder).iv1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mLisener.onClickLisener(subjectBean.contents.get(position + 1).name, subjectBean.contents.get(position + 1).content_id, subjectBean.contents.get(position + 1).type);
                                    XGIMILOG.D("section = " + section + ", position = " + position);
                                    SaveData.getInstance().mSouceInsight = "2";
                                    SaveData.getInstance().mSoucePage = "1";

                                    SaveData.getInstance().mSourceInsightLocation = section + "";


                                }
                            });
                            ((AppItemViewHolder) holder).iv2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mLisener.onClickLisener(subjectBean.contents.get(position + 2).name, subjectBean.contents.get(position + 2).content_id, subjectBean.contents.get(position + 2).type);
                                    XGIMILOG.D("section = " + section + ", position = " + position);
                                    SaveData.getInstance().mSouceInsight = "2";
                                    SaveData.getInstance().mSoucePage = "1";
                                    SaveData.getInstance().mSourceInsightLocation = section + "";

                                }
                            });
                        } else {
                            if (position < mDatas.data.subject.get(section - 1 - isHasRecommend).contents.size() / 2) {
                                ((AppItemViewHolder) holder).xianview.setVisibility(View.GONE);
                            }
                            if (position == mDatas.data.subject.get(section - 1 - isHasRecommend).contents.size() / 2 - 1) {
//                                    ((AppItemViewHolder) holder).xianview.setVisibility(View.VISIBLE);
                            }
                            if (subjectBean.contents.size() > 0) {
//                                WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                                ((AppItemViewHolder) holder).ll.setVisibility(View.GONE);
                                ((AppItemViewHolder) holder).tv1.setText(subjectBean.contents.get(position * 2 + 1).name);
                                ((AppItemViewHolder) holder).tv2.setText(subjectBean.contents.get(position * 2 + 2).name);
                                ((AppItemViewHolder) holder).tv_detail_two.setText(subjectBean.contents.get(position * 2 + 1).description);
                                ((AppItemViewHolder) holder).tv_detail_three.setText(subjectBean.contents.get(position * 2 + 2).description);

//                                ImageLoaderUtils.display(mContext, ((AppItemViewHolder) holder).iv1, subjectBean.contents.get(position * 2 + 1).img);
//                                ImageLoaderUtils.display(mContext, ((AppItemViewHolder) holder).iv2, subjectBean.contents.get(position * 2 + 2).img);

                                ImageLoader.getInstance().displayImage(subjectBean.contents.get(position * 2 + 1).img, ((AppItemViewHolder) holder).iv1);
                                ImageLoader.getInstance().displayImage(subjectBean.contents.get(position * 2 + 2).img, ((AppItemViewHolder) holder).iv2);

                                ((AppItemViewHolder) holder).iv1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mLisener.onClickLisener(subjectBean.contents.get(position * 2 + 1).name, subjectBean.contents.get(position * 2 + 1).content_id, subjectBean.contents.get(position * 2 + 1).type);
                                        XGIMILOG.D("section = " + section + ", position = " + position);
                                        SaveData.getInstance().mSouceInsight = "2";
                                        SaveData.getInstance().mSoucePage = "1";
                                        SaveData.getInstance().mSourceInsightLocation = section + "";

                                    }
                                });
                                ((AppItemViewHolder) holder).iv2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mLisener.onClickLisener(subjectBean.contents.get(position * 2 + 2).name, subjectBean.contents.get(position * 2 + 2).content_id, subjectBean.contents.get(position * 2 + 2).type);
                                        XGIMILOG.D("section = " + section + ", position = " + position);
                                        SaveData.getInstance().mSouceInsight = "2";
                                        SaveData.getInstance().mSoucePage = "1";
                                        SaveData.getInstance().mSourceInsightLocation = section + "";

                                    }
                                });
                            }
                        }
                    } else {
                        if (position < mDatas.data.subject.get(section - 1 - isHasRecommend).contents.size() / 2) {
                            ((AppItemViewHolder) holder).xianview.setVisibility(View.GONE);
                        }
                        if (position == mDatas.data.subject.get(section - 1 - isHasRecommend).contents.size() / 2 - 1) {
//                                ((AppItemViewHolder) holder).xianview.setVisibility(View.VISIBLE);
                        }
                        if (subjectBean.contents.size() > 0) {
                            ((AppItemViewHolder) holder).ll.setVisibility(View.GONE);
                            ((AppItemViewHolder) holder).tv1.setText(subjectBean.contents.get(position * 2).name);
                            ((AppItemViewHolder) holder).tv2.setText(subjectBean.contents.get(position * 2 + 1).name);
                            ((AppItemViewHolder) holder).tv_detail_two.setText(subjectBean.contents.get(position * 2).description);
                            ((AppItemViewHolder) holder).tv_detail_three.setText(subjectBean.contents.get(position * 2 + 1).description);

//                            ImageLoaderUtils.display(mContext, ((AppItemViewHolder) holder).iv1, subjectBean.contents.get(position * 2).img);
//                            ImageLoaderUtils.display(mContext, ((AppItemViewHolder) holder).iv2, subjectBean.contents.get(position * 2 + 1).img);

                            ImageLoader.getInstance().displayImage(subjectBean.contents.get(position * 2).img, ((AppItemViewHolder) holder).iv1);
                            ImageLoader.getInstance().displayImage(subjectBean.contents.get(position * 2 + 1).img, ((AppItemViewHolder) holder).iv2);

                            ((AppItemViewHolder) holder).iv1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (0 < section && section <= mDatas.data.subject.size()) {
                                        ToosUtil.getInstance().addEventUmeng(mContext, "event_movie_topic_click" + section);
                                    }
                                    mLisener.onClickLisener(subjectBean.contents.get(position * 2).name, subjectBean.contents.get(position * 2).content_id, subjectBean.contents.get(position * 2).type);
                                    XGIMILOG.D("section = " + section + ", position = " + position);
                                    SaveData.getInstance().mSouceInsight = "2";
                                    SaveData.getInstance().mSoucePage = "1";
                                    SaveData.getInstance().mSourceInsightLocation = section + "";
                                }
                            });
                            ((AppItemViewHolder) holder).iv2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (0 < section && section <= mDatas.data.subject.size()) {
                                        ToosUtil.getInstance().addEventUmeng(mContext, "event_movie_topic_click" + section);
                                    }
                                    mLisener.onClickLisener(subjectBean.contents.get(position * 2 + 1).name, subjectBean.contents.get(position * 2 + 1).content_id, subjectBean.contents.get(position * 2 + 1).type);
                                    XGIMILOG.D("section = " + section + ", position = " + position);
                                    SaveData.getInstance().mSouceInsight = "2";
                                    SaveData.getInstance().mSoucePage = "1";
                                    SaveData.getInstance().mSourceInsightLocation = section + "";

                                }
                            });
                        }
                    }
                }
            } else {
                if (holder instanceof AppItemSecondViewHolder) {
                    if (isHasRecommend == 1 && section == 1) {
                        ((AppItemSecondViewHolder) holder).image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (int i = 0; i < mDatas.data.individualitys.data.size(); i++) {
                                    XGIMILOG.D(i + " = " + mDatas.data.individualitys.data.get(i).getVideo_name()
                                            + ", " + mDatas.data.individualitys.data.get(i).getVideo_img()
                                            + ", " + mDatas.data.individualitys.data.get(i).getVideo_id()
                                    );
                                }
                                mLisener.onClickLisener(mDatas.data.individualitys.data.get(position * 3).video_name, mDatas.data.individualitys.data.get(position * 3).video_id, null);
                                XGIMILOG.D("section = " + section + ", position = " + position);//太空旅客
                                SaveData.getInstance().mSouceInsight = "1";
                                SaveData.getInstance().mSoucePage = "1";
                                SaveData.getInstance().mSourceInsightLocation = section + "";
                                ToosUtil.getInstance().addEventUmeng(mContext, "machinerecmend_event");
                            }
                        });
                        ((AppItemSecondViewHolder) holder).image1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mLisener.onClickLisener(mDatas.data.individualitys.data.get(position * 3 + 1).video_name, mDatas.data.individualitys.data.get(position * 3 + 1).video_id, null);
                                XGIMILOG.D("section = " + section + ", position = " + position);//刀剑神域
                                SaveData.getInstance().mSouceInsight = "1";
                                SaveData.getInstance().mSoucePage = "1";
                                SaveData.getInstance().mSourceInsightLocation = section + "";
                                ToosUtil.getInstance().addEventUmeng(mContext, "machinerecmend_event");

                            }
                        });

                        ((AppItemSecondViewHolder) holder).image2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mLisener.onClickLisener(mDatas.data.individualitys.data.get(position * 3 + 2).video_name, mDatas.data.individualitys.data.get(position * 3 + 2).video_id, null);
                                XGIMILOG.D("section = " + section + ", position = " + position);//游戏规则
                                SaveData.getInstance().mSouceInsight = "1";
                                SaveData.getInstance().mSoucePage = "1";
                                SaveData.getInstance().mSourceInsightLocation = section + "";
                                ToosUtil.getInstance().addEventUmeng(mContext, "machinerecmend_event");
                            }
                        });
                        ((AppItemSecondViewHolder) holder).name.setText(mDatas.data.individualitys.data.get(position * 3).video_name);
                        ((AppItemSecondViewHolder) holder).name1.setText(mDatas.data.individualitys.data.get(position * 3 + 1).video_name);
                        ((AppItemSecondViewHolder) holder).name2.setText(mDatas.data.individualitys.data.get(position * 3 + 2).video_name);

//                        ImageLoaderUtils.display(mContext, ((AppItemSecondViewHolder) holder).image, mDatas.data.individualitys.data.get(position * 3).video_img);
//                        ImageLoaderUtils.display(mContext, ((AppItemSecondViewHolder) holder).image1, mDatas.data.individualitys.data.get(position * 3 + 1).video_img);
//                        ImageLoaderUtils.display(mContext, ((AppItemSecondViewHolder) holder).image2, mDatas.data.individualitys.data.get(position * 3 + 2).video_img);

                        ImageLoader.getInstance().displayImage(mDatas.data.individualitys.data.get(position * 3).video_img, ((AppItemSecondViewHolder) holder).image);
                        ImageLoader.getInstance().displayImage(mDatas.data.individualitys.data.get(position * 3 + 1).video_img, ((AppItemSecondViewHolder) holder).image1);
                        ImageLoader.getInstance().displayImage(mDatas.data.individualitys.data.get(position * 3 + 2).video_img, ((AppItemSecondViewHolder) holder).image2);
                    } else {
                        final MovieRecommend.DataBean.RecommendBean recommendBean = mDatas.data.recommend.get(section - mDatas.data.subject.size() - 1 - isHasRecommend);
                        ((AppItemSecondViewHolder) holder).image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mLisener.onClickLisener(recommendBean.videos.get(position * 3).name, recommendBean.videos.get(position * 3).video_id, null);
                                XGIMILOG.D("");
                                SaveData.getInstance().mSouceInsight = "2";
                                SaveData.getInstance().mSoucePage = "1";
                                SaveData.getInstance().mSourceInsightLocation = section + "";

                            }
                        });
                        ((AppItemSecondViewHolder) holder).image1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mLisener.onClickLisener(recommendBean.videos.get(position * 3 + 1).name, recommendBean.videos.get(position * 3 + 1).video_id, null);
                                XGIMILOG.D("");
                                SaveData.getInstance().mSouceInsight = "2";
                                SaveData.getInstance().mSoucePage = "1";
                                SaveData.getInstance().mSourceInsightLocation = section + "";

                            }
                        });

                        ((AppItemSecondViewHolder) holder).image2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mLisener.onClickLisener(recommendBean.videos.get(position * 3 + 2).name, recommendBean.videos.get(position * 3 + 2).video_id, null);
                                XGIMILOG.D("section = " + section + ", position = " + position);
                                SaveData.getInstance().mSouceInsight = "2";
                                SaveData.getInstance().mSoucePage = "1";
                                SaveData.getInstance().mSourceInsightLocation = section + "";
                            }
                        });
//                        XGIMILOG.D("recommendBean.videos.size() = " + recommendBean.videos.size());
                        ((AppItemSecondViewHolder) holder).name.setText(recommendBean.videos.get(position * 3).name);
                        ((AppItemSecondViewHolder) holder).name1.setText(recommendBean.videos.get(position * 3 + 1).name);
                        ((AppItemSecondViewHolder) holder).name2.setText(recommendBean.videos.get(position * 3 + 2).name);

//                        ImageLoaderUtils.display(mContext, ((AppItemSecondViewHolder) holder).image, recommendBean.videos.get(position * 3).cover);
//                        ImageLoaderUtils.display(mContext, ((AppItemSecondViewHolder) holder).image1, recommendBean.videos.get(position * 3 + 1).cover);
//                        ImageLoaderUtils.display(mContext, ((AppItemSecondViewHolder) holder).image2, recommendBean.videos.get(position * 3 + 2).cover);

                        ImageLoader.getInstance().displayImage(recommendBean.videos.get(position * 3).cover, ((AppItemSecondViewHolder) holder).image);
                        ImageLoader.getInstance().displayImage(recommendBean.videos.get(position * 3 + 1).cover, ((AppItemSecondViewHolder) holder).image1);
                        ImageLoader.getInstance().displayImage(recommendBean.videos.get(position * 3 + 2).cover, ((AppItemSecondViewHolder) holder).image2);

                    }
                }
            }
        }
    }

    /**
     * 动态创建
     */
    public void createImageView(LinearLayout ll_dian) {
        ImageView iv = new ImageView(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        iv.setLayoutParams(layoutParams);
        layoutParams.leftMargin = 10;
        iv.setImageResource(R.drawable.hongdian);
        ll_dian.addView(iv);
    }

    private void isShowDisPlay(AppFooterViewHolder holder, boolean ll, boolean more, boolean lltwo, boolean xian, boolean isEvenNumber) {
        if (ll) {
            ((AppFooterViewHolder) holder).ll_all_one.setVisibility(View.VISIBLE);
        } else {
            ((AppFooterViewHolder) holder).ll_all_one.setVisibility(View.GONE);
        }
        if (lltwo) {
            ((AppFooterViewHolder) holder).ll_all.setVisibility(View.VISIBLE);
        } else {
            ((AppFooterViewHolder) holder).ll_all.setVisibility(View.GONE);
        }
        if (xian) {
            ((AppFooterViewHolder) holder).view.setVisibility(View.VISIBLE);
        } else {
            ((AppFooterViewHolder) holder).view.setVisibility(View.GONE);
        }
        if (more) {
            ((AppFooterViewHolder) holder).ll.setVisibility(View.VISIBLE);

        } else {
            ((AppFooterViewHolder) holder).ll.setVisibility(View.GONE);

        }
        if (isEvenNumber) {
            ((AppFooterViewHolder) holder).ll_one.setVisibility(View.GONE);
            ((AppFooterViewHolder) holder).ll_two.setVisibility(View.VISIBLE);

        } else {
            ((AppFooterViewHolder) holder).ll_one.setVisibility(View.VISIBLE);
            ((AppFooterViewHolder) holder).ll_two.setVisibility(View.GONE);
        }
    }


}
