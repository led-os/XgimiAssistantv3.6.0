package com.xgimi.zhushou.fragment.radiodetailfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xgimi.zhushou.Interface.EndLessOnScrollListener;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.RadioDetailActivity;
import com.xgimi.zhushou.adapter.MusicViewHolder.ClassToRadioAdapter;
import com.xgimi.zhushou.bean.ClassToRadio;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.util.ToosUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/18.
 */
public class SameFragment extends BaseFragment{
    private View view;
    private RecyclerView recyclerView;
    private ClassToRadioAdapter adapter;
    private List<ClassToRadio.data> mData=new ArrayList<>();
    private Subscription subscription;
    private LinearLayoutManager mLayoutManager;
    public int mId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_radio_same,container,false);
        initView(view);
        initData();
        return view;
    }

    public SameFragment(){

    }
    private void initView(View view) {
        recyclerView= (RecyclerView) view.findViewById(R.id.recyleview);
        adapter=new ClassToRadioAdapter(mData,getActivity());
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(mLayoutManager);
    }
    //跳转等逻辑
    private void initData() {
        ToosUtil.getInstance().addEventUmeng(getActivity(),"event_music_radio_relative");
        adapter.setOnClickListener(new ClassToRadioAdapter.OnClickListener() {
            @Override
            public void onClick(Object object, int position) {
                Intent intent = new Intent(getActivity(), RadioDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("id",((ClassToRadio.data) object).fm_id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        recyclerView.addOnScrollListener(new EndLessOnScrollListener(false,mLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                getClassRadio(mId,currentPage);
            }
        });
    }


    //获取分类下的电台
    public void getClassRadio(int id,int page) {
        mId=id;
        subscription= Api.getMangoApi().getRadio(id,page).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
    Observer<ClassToRadio> observer=new Observer<ClassToRadio>() {
        @Override
        public void onCompleted() {
            unRegist(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(ClassToRadio classToRadio) {
            if(classToRadio!=null&&classToRadio.data!=null){
                mData.addAll(classToRadio.data);
                adapter.dataChange(mData);
            }
        }
    };
    private void unRegist(Subscription sub){
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }

    }
}
