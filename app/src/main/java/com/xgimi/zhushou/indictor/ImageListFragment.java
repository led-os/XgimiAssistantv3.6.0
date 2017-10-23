package com.xgimi.zhushou.indictor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.ImageGridViewActivity;
import com.xgimi.zhushou.bean.ImageInfo;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.util.GlobalConsts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

public class ImageListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private ListView listview;
    public LocalimageListAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public static List<String> myImageList = new ArrayList<String>();
    public static HashMap<String, List<ImageInfo>> myGruopMap = new HashMap<String, List<ImageInfo>>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.local_listview_fragment, container, false);
        EventBus.getDefault().register(this);
        if (myGruopMap != null) {
            myGruopMap.clear();
        }
        if (myImageList != null) {
            myImageList.clear();
        }
        listview = (ListView) v.findViewById(R.id.listView);
        myImageList.addAll(GlobalConsts.mImgKeyList);
        myGruopMap.putAll(GlobalConsts.mImgMap);
        adapter = new LocalimageListAdapter(getActivity(), myImageList, myGruopMap);
        listview.setAdapter(adapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.pull_to_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(),
                        ImageGridViewActivity.class);
                Bundle b = new Bundle();
                b.putInt("position", position);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        View emptyView = inflater.inflate(R.layout.local_empty, container, false);
        ((ViewGroup) listview.getParent()).addView(emptyView);
        listview.setEmptyView(emptyView);
        return v;
    }

    //下拉刷星
    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        listview.setVisibility(View.VISIBLE);
        App.getContext().findImage();
    }

    //接收遍历完后返回的Eventbus
    public void onEventMainThread(ImageInfo imageInfo) {
        if (myImageList != null) {
            myImageList.clear();
        }
        if (myGruopMap != null) {
            myGruopMap.clear();
        }
        myImageList.addAll(GlobalConsts.mImgKeyList);
        myGruopMap.putAll(GlobalConsts.mImgMap);
        adapter.dataChange(myImageList, myGruopMap);
        mSwipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
