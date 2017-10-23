package com.xgimi.zhushou.fragment.SearchMusicFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.RadioDetailActivity;
import com.xgimi.zhushou.adapter.SearchRadioAdapter;
import com.xgimi.zhushou.bean.SearchRadio;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.netUtil.Api;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/13.
 */
public class SearchRadioFragment extends BaseFragment {
    private View view;
    private RecyclerView recyclerView;
    private Subscription subscription;
    private SearchRadioAdapter adapter;
    private List<SearchRadio.data> mData = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private String key;
    private int page = 1;
    private int lastVisibleItem;
    //是否正在加载更多的标志
    private boolean isMoreLoading = false;
    private View nodata;

    public SearchRadioFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search_radio, container, false);
        initView(view);
        initData();
        return view;
    }


    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyleview);
        nodata = view.findViewById(R.id.nodata);
        adapter = new SearchRadioAdapter(getActivity(), mData);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    //跳转等逻辑
    private void initData() {
        adapter.setOnClickListener(new SearchRadioAdapter.OnClickListener() {
            @Override
            public void onClick(Object object, int position) {
                Intent intent = new Intent(getActivity(), RadioDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("id", ((SearchRadio.data) object).fm_id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    if (!isMoreLoading) {
                        isMoreLoading = true;
                        page++;
                        getSearchRadio(key, page);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
    }



    //传入key
    public void getSearchRadio(String keywords, int mpage) {
        if (!keywords.equals(key)) {
            mData.clear();
            page = 1;
            isMoreLoading = false;
            recyclerView = new RecyclerView(getActivity());
        }
        if (keywords.length() != 0) {
            key = keywords;
            recyclerView.setVisibility(View.VISIBLE);
            subscription = Api.getMangoApi().getSearchRadio(keywords, mpage).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        } else {
            isMoreLoading = false;
            recyclerView.setVisibility(View.GONE);
        }

    }

    Observer<SearchRadio> observer = new Observer<SearchRadio>() {
        @Override
        public void onCompleted() {
            unRegist(subscription);
            MissDilog();

        }

        @Override
        public void onError(Throwable e) {
            isMoreLoading = false;
            e.printStackTrace();
            MissDilog();
            if (page == 1) {
                nodata.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }

        }

        @Override
        public void onNext(SearchRadio searchRadio) {
            // mData.clear();

            if (searchRadio != null && searchRadio.data.size() > 0) {
                mData.addAll(searchRadio.data);
                adapter.dataChange(mData);
                nodata.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                if (page == 1) {
                    nodata.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
            isMoreLoading = false;
            MissDilog();
        }
    };

    private void unRegist(Subscription sub) {
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
