package com.xgimi.zhushou.fragment.musicfragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.baidu.music.model.MusicList;
import com.baidu.music.model.TopList;
import com.baidu.music.model.TopListDescriptionList;
import com.baidu.music.model.TopLists;
import com.baidu.music.onlinedata.TopListManager;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.MusicDetailActivity;
import com.xgimi.zhushou.adapter.BangDanAdapter;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.util.SaveTVApp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/16.
 */
public class SongListFragment extends BaseFragment implements TopListManager.TopListListener {
    private View view;
    private List<TopList> home = new ArrayList<>();
    private BangDanAdapter mAdapter;

    public SongListFragment() {
        // Required empty public constructor
    }

    public static SongListFragment fragment;

    public static SongListFragment getInstance() {
        if (fragment == null) {
            fragment = new SongListFragment();
        }
        return fragment;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BaiDuMusicList.getInstance().getBangDanList(getActivity(), SongListFragment.this);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.song_list_fragment, container, false);
        initView(view);
        handler.sendEmptyMessageDelayed(1, 200);
        return view;
    }

    private boolean isFisrts;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isFisrts) {
            isFisrts = true;
        }
    }

    private void initView(View view) {
        GridView mGridView = (GridView) view.findViewById(R.id.gridview);
        mAdapter = new BangDanAdapter(getActivity(), home, SaveTVApp.getInstance(getActivity()).BangDanTubiao);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),
                        MusicDetailActivity.class);
                intent.putExtra("leixing", mAdapter.getItem(position).mBillId);
                intent.putExtra("class", 0);
                intent.putExtra("index", position);
                intent.putExtra("geName", mAdapter.getItem(position).mName);
                getActivity().startActivity(intent);
            }
        });
    }


    @Override
    public void onGetTopListMusic(MusicList musicList) {

    }

    @Override
    public void onGetDescriptionList(TopListDescriptionList topListDescriptionList) {

    }

    @Override
    public void onGetTopList(TopLists topLists) {
        mAdapter.dataChange(topLists.mItems);
    }
}
