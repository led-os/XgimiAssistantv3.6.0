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
import com.xgimi.zhushou.activity.FileDetailActivity;
import com.xgimi.zhushou.adapter.FileTypeAdapter;
import com.xgimi.zhushou.bean.FileInfo;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.util.GlobalConsts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class FileFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
	private FileTypeAdapter adapter;
	private ListView listview;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	public static List<List<Map<String, Object>>> myFiles =new ArrayList<List<Map<String, Object>>>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.local_listview_fragment, container, false);
		EventBus.getDefault().register(this);
		listview = (ListView) v.findViewById(R.id.listView);
		if(myFiles!=null){
			myFiles.clear();
		}
		myFiles.addAll(GlobalConsts.files);
		adapter = new FileTypeAdapter(getActivity(),myFiles);
		listview.setAdapter(adapter);
		mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.pull_to_refresh);
		mSwipeRefreshLayout.setOnRefreshListener(this);
		mSwipeRefreshLayout.setVisibility(View.VISIBLE);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if (adapter.getItemSize(position) == 0) {
					return;
				}

				Intent intent = new Intent(getActivity(), FileDetailActivity.class);

				intent.putExtra("position", position);
				intent.putExtra("type_title", adapter.getItem(position).toString());

				getActivity().startActivity(intent);

			}
		});

		return v;
	}

	@Override
	public void onRefresh() {
		App.getContext().findFile();

	}
	public void onEventMainThread(FileInfo fileInfo){
		if(myFiles!=null){
			myFiles.clear();
		}
		myFiles.addAll(GlobalConsts.files);
		adapter.dataChange(myFiles);
		mSwipeRefreshLayout.setRefreshing(false);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
