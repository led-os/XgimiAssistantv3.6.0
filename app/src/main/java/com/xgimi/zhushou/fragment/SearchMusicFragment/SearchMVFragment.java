package com.xgimi.zhushou.fragment.SearchMusicFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.MVDetailActivity;
import com.xgimi.zhushou.adapter.SearchMVAdapter;
import com.xgimi.zhushou.bean.MVList;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.netUtil.HttpUrl;
import com.xgimi.zhushou.util.DeviceUtils;
import com.xgimi.zhushou.util.SaveData;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/13.
 */
public class SearchMVFragment extends BaseFragment {
    private View view;
    private static SearchMVFragment fragment;
    private RecyclerView recyclerview;
    private Subscription subscription;
    private SearchMVAdapter adapter;
    private List<MVList.data> mData=new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private String key;
    private int page=1;
    private int lastVisibleItem;
    private boolean isclose=false;
    //是否正在加载更多的标志
    private boolean isMoreLoading = false;
    private View close;
    private View nodata;
    public String mvLeixing;
    public SearchMVFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_search_mv,container,false);
        initView(view);
        initData();
        return view;
    }

    // TODO: Rename and change types and number of parameters
//    public static SearchMVFragment newInstance() {
//        if (fragment == null)
//            fragment = new SearchMVFragment();
//        return fragment;
//    }

    List< VcontrolCmd.CustomPlay.PlayList> mPlyLists=new ArrayList<>();
    private void initView(View view) {
        close=view.findViewById(R.id.close);
        nodata= view.findViewById(R.id.nodata);
        recyclerview= (RecyclerView) view.findViewById(R.id.recyleview);
        adapter=new SearchMVAdapter(getActivity(),mData);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(mLayoutManager);
        if(HttpUrl.isNetworkConnected(getActivity())){
            if(SaveData.getInstance().mv==0){
                recyclerview.setVisibility(View.GONE);
                close.setVisibility(View.VISIBLE);
                isclose=true;
            }else {
                recyclerview.setVisibility(View.VISIBLE);
                close.setVisibility(View.GONE);
                isclose=false;
            }
        }
    }

    private void initData() {
        recyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    if (!isMoreLoading) {
                        isMoreLoading = true;
                        page++;
                        getSearchMV(key,page);
                    }
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
        adapter.setOnClickListener(new SearchMVAdapter.OnClickListener() {
            @Override
            public void onClick(Object object, int position) {
                Intent intent = new Intent(getActivity(), MVDetailActivity.class);
                intent.putExtra("title",((MVList.data) object).mv_title);
                intent.putExtra("artist",((MVList.data) object).mv_artist);
                intent.putExtra("bitmap",((MVList.data) object).mv_thumb);
                intent.putExtra("mv_play_address",((MVList.data) object).mv_play_address);
                Bundle bundle = new Bundle();
                bundle.putSerializable("type",3);
                intent.putExtras(bundle);
                SaveData.getInstance().mList=mData;
                intent.putExtra("collect_id",((MVList.data) object).collect_id);
                intent.putExtra("mv_id",((MVList.data) object).mv_id);

                SaveData.getInstance().mv_id=mData.get(position).mv_id;
                SaveData.getInstance().mv_title=((MVList.data) object).mv_title;
                SaveData.getInstance().mv_artist=((MVList.data) object).mv_artist;
                SaveData.getInstance().bitmap=((MVList.data) object).mv_thumb;
                SaveData.getInstance().mvid=((MVList.data) object).mv_id;
                SaveData.getInstance().position=position;
                startActivity(intent);
                VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200,"2",
                        GMSdkCheck.appId, DeviceUtils.getappVersion(getActivity()), App.getContext().PACKAGENAME,null,
                        new VcontrolCmd.CustomPlay(0,0,null,mPlyLists,position),
                        null,null,null)));
//                getActivity().finish();
            }
        });
    }
    //传入key
    public void getSearchMV(String keywords,int mpage) {
        if(keywords!=key){
            mData.clear();
            page=1;
            isMoreLoading = false;
        }
        if(keywords.length()!=0){
            key=keywords;
            if(recyclerview!=null){
                recyclerview.setVisibility(View.VISIBLE);
            }

            subscription= Api.getMangoApi().getSearchMV(keywords,mpage).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(observer);
        }else {
            isMoreLoading = false;
            if(recyclerview!=null) {
                recyclerview.setVisibility(View.GONE);
            }
        }

    }
    Observer<MVList> observer=new Observer<MVList>() {
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
        }

        @Override
        public void onNext(MVList searchMV) {
            if(searchMV!=null&&searchMV.data.size()>0){
                mData.addAll(searchMV.data);
                adapter.dataChange(mData);
                nodata.setVisibility(View.GONE);
                close.setVisibility(View.GONE);
                recyclerview.setVisibility(View.VISIBLE);
            }else if(page==1){
                nodata.setVisibility(View.VISIBLE);
                close.setVisibility(View.GONE);
                recyclerview.setVisibility(View.GONE);
            }
            if(isclose){
                nodata.setVisibility(View.GONE);
                close.setVisibility(View.VISIBLE);
                recyclerview.setVisibility(View.GONE);
            }
            mPlyLists.clear();
            for (int i = 0; i < mData.size(); i++) {
                if(mData.get(i).mv_type_name!=null){
                    mvLeixing=mData.get(i).mv_type_name;
                }else {
                    mvLeixing="";
                }
                VcontrolCmd.CustomPlay.PlayList mPlayList=new VcontrolCmd.CustomPlay.PlayList(null,mData.get(i).mv_id,null,mData.get(i).mv_title,null,mData.get(i).mv_play_address,null,0,null,
                        App.getContext().YINYUETAI);
                mPlyLists.add(mPlayList);
            }
            MissDilog();
            isMoreLoading = false;
        }
    };
    private void unRegist(Subscription sub){
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
