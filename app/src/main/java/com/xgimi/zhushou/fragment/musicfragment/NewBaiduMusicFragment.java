package com.xgimi.zhushou.fragment.musicfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.baidu.music.model.AlbumList;
import com.baidu.music.model.Artist;
import com.baidu.music.model.ArtistList;
import com.baidu.music.model.MusicList;
import com.baidu.music.model.Playlist;
import com.baidu.music.model.PlaylistItems;
import com.baidu.music.model.TopList;
import com.baidu.music.model.TopListDescriptionList;
import com.baidu.music.model.TopLists;
import com.baidu.music.onlinedata.ArtistManager;
import com.baidu.music.onlinedata.OnlineManagerEngine;
import com.baidu.music.onlinedata.PlaylistManager;
import com.baidu.music.onlinedata.TopListManager;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.NewMusicHomeBean;
import com.xgimi.zhushou.bean.NewMusicMyTopListBean;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.util.LogUtil;
import com.xgimi.zhushou.util.XGIMILOG;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linzh on 2017/10/23.
 */

public class NewBaiduMusicFragment extends BaseFragment implements View.OnClickListener {

    private View mView;
    private NewMusicHomeBean mData;
    private NewBaiduMusicAdapter mAdapter;
    private RecyclerView mRcv;
    private SmartRefreshLayout mRefreshLayout;

    private boolean mSingerDataDone;
    private boolean mTopListDataDone;
    private boolean mPlayListDataDone;

    private RelativeLayout mLoadFalseRl;

    private NewBaiduMusicAdapter.OnItemClickListenr mOnItemClickListener = new NewBaiduMusicAdapter.OnItemClickListenr() {
        @Override
        public void onSingerClick(Artist artist) {
            XGIMILOG.E(new Gson().toJson(artist));
        }

        @Override
        public void onTopListClick(NewMusicMyTopListBean topListBean) {
            XGIMILOG.E(new Gson().toJson(topListBean));
        }

        @Override
        public void onPlayListClick(PlaylistItems playlist) {
            XGIMILOG.E(new Gson().toJson(playlist));
        }
    };

    private NewBaiduMusicAdapter.OnFootClickListener mOnFootClickListener = new NewBaiduMusicAdapter.OnFootClickListener() {
        @Override
        public void onFootClick(int type) {
            switch (type) {
                case NewMusicHomeBean.ITEM_TYPE_PLAY_LIST:
                    break;
                case NewMusicHomeBean.ITEM_TYPE_SINGER:
                    break;
                case NewMusicHomeBean.ITEM_TYPE_TOP_LIST:
                    break;
            }
        }
    };

    /**
     * 榜单监听
     */
    private TopListManager.TopListListener mGetTopListListener = new TopListManager.TopListListener() {
        @Override
        public void onGetTopListMusic(MusicList musicList) {

        }

        @Override
        public void onGetDescriptionList(TopListDescriptionList topListDescriptionList) {

        }

        @Override
        public void onGetTopList(TopLists topLists) {
            XGIMILOG.E("获取榜单成功");
            LogUtil.d(XGIMILOG.getTag(), new Gson().toJson(topLists));
            if (topLists != null && topLists.getItems() != null) {
                List<TopList> itemList = new ArrayList<>();
                for (int i = 0; i < topLists.getItems().size(); i++) {
                    String name = topLists.getItems().get(i).mName;
                    if ("新歌TOP100".equals(name) || "billboard".equals(name)
                            || "KTV热歌榜".equals(name) || "欧美金曲榜".equals(name)
                            || "华语金曲榜".equals(name) || "影视歌曲榜".equals(name)
                            || "情歌对唱".equals(name) || "网络歌曲".equals(name)
                            || "经典老歌".equals(name) || "舞曲榜".equals(name)
                            || "摇滚榜".equals(name) || "爵士榜".equals(name)
                            || "民谣榜".equals(name) || "叱咤歌曲榜".equals(name)
                            || "飙升中文榜".equals(name) || "飙升欧美榜".equals(name)) {
                        itemList.add(topLists.getItems().get(i));
                    }
                }
                topLists.setItems(itemList);
                mData.addData(topLists);
                mTopListDataDone = true;
                if (mSingerDataDone && mTopListDataDone && mPlayListDataDone) {
                    loadData();
                }
            } else {
                loadFalse();
            }
        }
    };

    /**
     * 歌手监听
     */
    private ArtistManager.ArtistListener mGetHotSingerListener = new ArtistManager.ArtistListener() {
        @Override
        public void onGetHotArtistList(ArtistList artistList) {
            XGIMILOG.E("获取歌手成功");
            LogUtil.e(XGIMILOG.getTag(), new Gson().toJson(artistList));
            if (artistList != null && artistList.getItems() != null) {
                mData.addData(artistList);
                mSingerDataDone = true;
                if (mSingerDataDone && mTopListDataDone && mPlayListDataDone) {
                    loadData();
                }
            } else {
                loadFalse();
            }
        }

        @Override
        public void onGetArtistAlbumList(AlbumList albumList) {
            XGIMILOG.E("");
        }

        @Override
        public void onGetArtistMusicList(MusicList musicList) {
            XGIMILOG.E("");
        }

        @Override
        public void onGetArtist(Artist artist) {
            XGIMILOG.E("");
        }

        @Override
        public void onGetArtistInArea(ArtistList artistList) {
            XGIMILOG.E("");
        }
    };



    /**
     * 歌单监听
     */
    private PlaylistManager.PlayListInterface.onGetPlayListListener mGetPlayListListener = new PlaylistManager.PlayListInterface.onGetPlayListListener() {
        @Override
        public void onGetPlayList(Playlist playlist) {
            XGIMILOG.E("获取歌单成功");
            LogUtil.e(XGIMILOG.getTag(), new Gson().toJson(playlist));
            if (playlist != null && playlist.getItems() != null) {
                mData.addData(playlist);
                mPlayListDataDone = true;
                if (mSingerDataDone && mTopListDataDone && mPlayListDataDone) {
                    loadData();
                }
            } else {
                loadFalse();
            }
        }
    };

    private OnRefreshListener mOnRefreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(RefreshLayout refreshlayout) {
            if (!refreshlayout.isRefreshing()) {
                loadData();
            }
        }
    };


    private void loadFalse() {
        mRcv.setVisibility(View.GONE);
        mLoadFalseRl.setVisibility(View.VISIBLE);
        mRefreshLayout.finishRefresh();
    }

    private void loadData() {
        mRcv.setVisibility(View.VISIBLE);
        if (mAdapter == null) {
            mAdapter = new NewBaiduMusicAdapter(mData.getDataList(), getContext());
            mAdapter.setItemClickListener(mOnItemClickListener);
            mAdapter.setFootClickListener(mOnFootClickListener);
            mRcv.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        mRefreshLayout.finishRefresh();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_new_baidu_music_layout, container, false);
            initView();
            initData();
        }
        return mView;
    }

    private void initData() {
        if (mData == null) {
            mData = new NewMusicHomeBean();
        } else {
            mData.getDataList().clear();
        }
        mTopListDataDone = false;
        mSingerDataDone = false;
        mPlayListDataDone = false;
//        if (!mRefreshLayout.isRefreshing()) {
//            mRefreshLayout.autoRefresh();
//        }
        OnlineManagerEngine.getInstance(getContext()).getTopListManager(getContext())
                .getTopListAsync(getContext(), mGetTopListListener);
        OnlineManagerEngine.getInstance(getContext()).getArtistManager(getContext())
                .getHotArtistListAsync(getContext(), 1, 9, mGetHotSingerListener);
        OnlineManagerEngine.getInstance(getContext()).getPlayListManager(getContext())
                .getPlayList(getContext(), 1, 9, "", mGetPlayListListener);
    }


    private void initView() {
        mLoadFalseRl = (RelativeLayout) mView.findViewById(R.id.rl_load_false_new_music);
        mRefreshLayout = (SmartRefreshLayout) mView.findViewById(R.id.srf_new_music);
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mRcv = (RecyclerView) mView.findViewById(R.id.rcv_new_music);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRcv.setLayoutManager(layoutManager);
        mRcv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE: // The RecyclerView is not currently scrolling.
                        //当屏幕停止滚动，加载图片
                        try {
                            if (getContext() != null) Glide.with(getContext()).resumeRequests();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING: // The RecyclerView is currently being dragged by outside input such as user touch input.
                        //当屏幕滚动且用户使用的触碰或手指还在屏幕上，停止加载图片
                        try {
                            if (getContext() != null) Glide.with(getContext()).pauseRequests();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING: // The RecyclerView is currently animating to a final position while not under outside control.
                        //由于用户的操作，屏幕产生惯性滑动，停止加载图片
                        try {
                            if (getContext() != null) Glide.with(getContext()).pauseRequests();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
