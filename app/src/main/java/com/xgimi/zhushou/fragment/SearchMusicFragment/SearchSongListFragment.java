package com.xgimi.zhushou.fragment.SearchMusicFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baidu.music.model.Music;
import com.baidu.music.model.SearchSceneResult;
import com.baidu.music.onlinedata.OnlineManagerEngine;
import com.baidu.music.onlinedata.SceneManager;
import com.google.gson.Gson;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.MusicDetailActivity;
import com.xgimi.zhushou.activity.RadioBoFangActivity;
import com.xgimi.zhushou.adapter.SearchMusicAdapter;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.DeviceUtils;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/13.
 */
public class SearchSongListFragment extends BaseFragment {
    private View view;
    private ListView listview;
    private SceneManager mSceneManager;
    private ListView mListView;
    List<Music> musics = new ArrayList<Music>();
    private SearchMusicAdapter adapter;
    private int index = -1;
    private View nodata;

    public SearchSongListFragment() {

    }

    private List<VcontrolCmd.CustomPlay.PlayList> mPlayLists;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_songlist, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mPlayLists = new ArrayList<>();
        nodata = view.findViewById(R.id.nodata);
        mListView = (ListView) view.findViewById(R.id.listview);
        mSceneManager = OnlineManagerEngine
                .getInstance(getActivity()).getSceneManager(getActivity());
        adapter = new SearchMusicAdapter(musics, getActivity());
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Constant.netStatus) {
                    if (ToosUtil.getInstance().isInstallTvControl(getActivity())) {
                        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200, "2",
                                GMSdkCheck.appId, DeviceUtils.getappVersion(getActivity()), App.getContext().PACKAGENAME, null,
                                new VcontrolCmd.CustomPlay(1, 0, null, mPlayLists, position),
                                null, null, null)));



                        SaveData.getInstance().position = position;
                        if (SaveData.getInstance().mRadioShow != null) {
                            SaveData.getInstance().mRadioShow.clear();
                            SaveData.getInstance().mRadioShow = null;
                        }
                        SaveData.getInstance().fenlei = "2";
                        SaveData.getInstance().index = position + "";

                        Intent intent = new Intent(getContext(), RadioBoFangActivity.class);
                        intent.putExtra("type", "2");
                        intent.putExtra("index", position + "");
                        intent.putExtra("geName", mPlayLists.get(position).name);
                        intent.putExtra("title", mPlayLists.get(position).title);
                        intent.putExtra("singer", mPlayLists.get(position).singer);
                        getContext().startActivity(intent);

                    }
                } else {
                    ToosUtil.getInstance().isConnectTv(getActivity());
                }
            }
        });
    }


    public void search(String scene, String song, String singer, String rawText) {
        if (rawText.length() != 0) {
            showDilog("加载中");
            mListView.setVisibility(View.VISIBLE);
            mSceneManager.searchScenesAsync(scene, song, singer, rawText,
                    new SceneManager.SearchSceneCallBackListener() {
                        @Override
                        public void onResult(SearchSceneResult searchSceneResult) {
                            MissDilog();
                            if (searchSceneResult != null && searchSceneResult.mMusics != null && searchSceneResult.mMusics.size() > 0
                                    && searchSceneResult.isAvailable()) {
                                if (searchSceneResult.mAlbums != null
                                        || searchSceneResult.mArtists != null
                                        || searchSceneResult.mMusics != null
                                        || searchSceneResult.mSceneInfos != null) {
                                    musics = searchSceneResult.mMusics;
                                    adapter.dataChange(searchSceneResult.mMusics);
                                    nodata.setVisibility(View.GONE);
                                    mListView.setVisibility(View.VISIBLE);
                                    if (mPlayLists != null) {
                                        mPlayLists.clear();
                                    }
                                    for (int i = 0; i < musics.size(); i++) {
                                        VcontrolCmd.CustomPlay.PlayList playList = new VcontrolCmd.CustomPlay.PlayList(null, musics.get(i).mId,
                                                null, musics.get(i).mTitle, musics.get(i).mArtist, null, null, 0, null,
                                                App.getContext().BAIDUYINYUE);
                                        mPlayLists.add(playList);
//                                        SaveData.getInstance().mPlayLists = mPlayLists;
                                    }
                                    SaveData.getInstance().mPlayLists = mPlayLists;
//                                    for (int i = 0; i < mPlayLists.size(); i++) {
//                                        XGIMILOG.D("搜索结果: " + new Gson().toJson(mPlayLists.get(i)));
//                                    }
                                } else {
                                    nodata.setVisibility(View.VISIBLE);
                                    mListView.setVisibility(View.GONE);
                                }
                            } else {
                                nodata.setVisibility(View.VISIBLE);
                                mListView.setVisibility(View.GONE);
                            }
                        }
                    });
        } else {
            mListView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
