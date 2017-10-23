package com.xgimi.zhushou.fragment.searchmoviefragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.FilmDetailActivity;
import com.xgimi.zhushou.adapter.SearchMovieAllAdapter;
import com.xgimi.zhushou.bean.SearchMovieResult;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.XGIMILOG;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/19.
 */
public class SearchAllFragment extends BaseFragment {

    private SearchMovieAllAdapter adapter;
    private RecyclerView mrecyclerView;
    private LinearLayoutManager mLayoutManager;
    private View view;
    private List<SearchMovieResult.DataBean.ListBean> movieList = new ArrayList<>();

    public SearchAllFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_all, container, false);
        initView(view);
        initData();
        return view;

    }


    private void initView(View view) {
        if (SaveData.getInstance().searchmovie != null && SaveData.getInstance().searchmovie.size() > 0) {
            for (int i = 0; i < SaveData.getInstance().searchmovie.size(); i++) {
                movieList.addAll(SaveData.getInstance().searchmovie.get(i).list);
            }
        }
        adapter = new SearchMovieAllAdapter(getActivity(), movieList);
        mrecyclerView = (RecyclerView) view.findViewById(R.id.detail_recylerview);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mrecyclerView.setAdapter(adapter);
        mrecyclerView.setLayoutManager(mLayoutManager);
    }

    public void dataChange() {
        movieList.clear();
//        for (int i = 0; i < SaveData.getInstance().searchmovie.size(); i++) {
//            movieList.addAll(SaveData.getInstance().searchmovie.get(i).list);
//            for (int j = 0; j < SaveData.getInstance().searchmovie.get(i).getList().size(); j++) {
//                XGIMILOG.D("" + new Gson().toJson(SaveData.getInstance().searchmovie.get(i).getList().get(j)));
//            }
//        }
        adapter.dataChange(movieList);
    }

    private void initData() {
        adapter.setLisener(new SearchMovieAllAdapter.OnitemClick() {
            @Override
            public void onItemCliceLisener(String id, String type) {
                if (type.equals("item")) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), FilmDetailActivity.class);
                    intent.putExtra("id", id);
                    SaveData.getInstance().mSouceInsight = "4";
                    SaveData.getInstance().mSoucePage = null;
                    SaveData.getInstance().mSourceInsightLocation = null;
                    startActivity(intent);
                }
            }
        });
    }
}
