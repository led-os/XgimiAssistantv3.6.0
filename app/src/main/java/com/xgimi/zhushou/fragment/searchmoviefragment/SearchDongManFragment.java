package com.xgimi.zhushou.fragment.searchmoviefragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.FilmDetailActivity;
import com.xgimi.zhushou.adapter.SearchMovieAdapter;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.util.SaveData;

/**
 * Created by Administrator on 2016/10/19.
 */
public class SearchDongManFragment extends BaseFragment {
    public SearchDongManFragment() {

    }

    private SearchMovieAdapter adapter;
    private RecyclerView mrecyclerView;
    private LinearLayoutManager mLayoutManager;
    private View view;
    private TextView mNoResourceTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_search_dongman, container, false);
            initView(view);
        }
        return view;
    }

    private void initView(View view) {
        mNoResourceTv = (TextView) view.findViewById(R.id.tv_no_resource_search_dongman);
        adapter = new SearchMovieAdapter(getActivity(), SaveData.getInstance().searchmovie, SaveData.getInstance().dm);
        mrecyclerView = (RecyclerView) view.findViewById(R.id.detail_recylerview);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mrecyclerView.setAdapter(adapter);
        mrecyclerView.setLayoutManager(mLayoutManager);
        adapter.setLisener(new SearchMovieAdapter.OnitemClick() {
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

    @Override
    public void onResume() {
        super.onResume();
        if (SaveData.getInstance().searchmovie.get(SaveData.getInstance().dm).list == null
                || SaveData.getInstance().searchmovie.get(SaveData.getInstance().dm).list.size() == 0) {
            mNoResourceTv.setVisibility(View.VISIBLE);
            mrecyclerView.setVisibility(View.GONE);
        } else {
            mNoResourceTv.setVisibility(View.GONE);
            mrecyclerView.setVisibility(View.VISIBLE);
        }
    }
}
