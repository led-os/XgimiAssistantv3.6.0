package com.xgimi.zhushou.fragment.applyfragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.ApplyDetailActivity;
import com.xgimi.zhushou.adapter.ApplyGameAdapter;
import com.xgimi.zhushou.bean.ApplySearc;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.netUtil.HttpUrl;
import com.xgimi.zhushou.util.StringUtils;
import com.xgimi.zhushou.util.ToosUtil;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ApplyGameFragment extends BaseFragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{
    public ApplyGameAdapter mAdapter;
    private ListView mListview;
    private View net_connect;
    private AnimationDrawable animationDrawable;
    private View load_false;
    private View prog;
    public ApplyGameFragment() {
        // Required empty public constructor
    }
    private ApplySearc mData=new ApplySearc();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_apply_ying_yongragment, container, false);
        initView(view);
//        initLocal();
        return view;
    }
    private boolean isFirst;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser&&!isFirst){
            prog.setVisibility(View.VISIBLE);
            initLocal();
            isFirst=true;

        }

    }

    private void initView(View view) {
        mListview = (ListView) view.findViewById(R.id.listview);
        mAdapter = new ApplyGameAdapter(getActivity(),mData);
        mListview.setAdapter(mAdapter);
        mListview.setFocusable(false);
        load_false=view.findViewById(R.id.load_false);
        prog=view.findViewById(R.id.myprog);
        prog.setVisibility(View.VISIBLE);
//        LinearLayout tv= (LinearLayout) view.findViewById(R.id.more);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Intent intent=new Intent(getActivity(), ApplyDetailActivity.class);
                intent.putExtra("id",mAdapter.getItem(position).id);
                ToosUtil.getInstance().addEventUmeng(getActivity(),"event_game_list");
                startActivity(intent);
            }
        });
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getActivity(), MoreAppActivity.class);
//                intent.putExtra("type","1");
//                startActivity(intent);
//            }
//        });
        net_connect = view.findViewById(R.id.netconnect);
        net_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HttpUrl.isNetworkConnected(getActivity())){
                    net_connect.setVisibility(View.GONE);
                    prog.setVisibility(View.VISIBLE);
                    initData();
                }
            }
        });
    }
    //读取缓存本地数据
    private void initLocal() {
        String readHomeJson = app.readHomeJson("gamelysearc");
        if (!StringUtils.isEmpty(readHomeJson)) {
            ApplySearc mDatas = new Gson().fromJson(readHomeJson, ApplySearc.class);
            loadHome(mDatas);
        } else {
            if (!HttpUrl.isNetworkConnected(getActivity())) {
                net_connect.setVisibility(View.VISIBLE);
                prog.setVisibility(View.GONE);
                MissDilog();
            }
        }
        if (HttpUrl.isNetworkConnected(getActivity())) {
            initData();
        }
    }
    private void loadHome(ApplySearc channels) {
        mAdapter.dataChange(channels);
        prog.setVisibility(View.GONE);
        net_connect.setVisibility(View.GONE);
        load_false.setVisibility(View.GONE);

    }
    Observer<ApplySearc> observer = new Observer<ApplySearc>() {

        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            prog.setVisibility(View.GONE);
            load_false.setVisibility(View.VISIBLE);
        }

        @Override
        public void onNext(ApplySearc channels) {
            if (channels != null && channels.data != null) {
                App app = (App) getActivity().getApplicationContext();
                String gamelySearc=new Gson().toJson(channels);
                app.savaHomeJson("gamelysearc",gamelySearc);
                loadHome(channels);
            } else {
               load_false.setVisibility(View.VISIBLE);
                prog.setVisibility(View.GONE);
            }
            prog.setVisibility(View.GONE);
        }
    };
    private void initData(){
        subscription = Api.getMangoApi().getApply("1","全部",null,20,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
        initData();
    }
}