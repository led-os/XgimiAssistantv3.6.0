package com.xgimi.zhushou.indictor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.NewVideoPlayActivity;
import com.xgimi.zhushou.activity.VideoPlayActivity;
import com.xgimi.zhushou.adapter.VideoAdapter;
import com.xgimi.zhushou.bean.VideoInfo;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.util.GlobalConsts;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.StringUtils;
import com.xgimi.zhushou.util.ToosUtil;

import java.io.File;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class VideoFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "VideoFragment";
    private GridView gridview;
    private List<VideoInfo> videoInfos = new ArrayList<>();
    private List<VideoInfo> mVideoList = new ArrayList<>();
    public Map<String, Object> map = new ArrayMap<>();
    private VideoAdapter adapter;
    private String path = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int mySize;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = View.inflate(getActivity(), R.layout.activity_local_vieo, null);
        initView(view);
        return view;
    }

    /*
     * Java文件操作 获取不带扩展名的文件名
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    //去掉重复的数据
    public Map<String, Object> myMap = new ArrayMap<>();
    public Map<String, Object> myMap1 = new ArrayMap<>();

    private List<Map<String, Object>> removeDuplicate(List<Map<String, Object>> data) {
        for (int i = 0; i < data.size(); i++) {
            myMap = data.get(i);
            for (int j = data.size() - 1; j > i; j--) {
                myMap1 = data.get(j);
                if (getFileNameNoEx(myMap1.get("filepath").toString()).equals(getFileNameNoEx(myMap.get("filepath").toString())) || myMap1.get("title").toString().equals(myMap.get("title").toString())
                        || myMap1.get("id").toString().equals(myMap.get("id").toString()) || myMap1.get("duration").toString().equals(myMap.get("duration").toString())) {
                    data.remove(j);
                }
            }
        }
        return data;
    }

    private List<VideoInfo> removeDuplicate1(List<VideoInfo> data) {
        for (int i = 0; i < data.size(); i++) {
            for (int j = data.size() - 1; j > i; j--) {
                if (getFileNameNoEx(data.get(i).title.toString()).equals(getFileNameNoEx(data.get(j).title.toString()))) {
                    data.remove(j);
                }
            }
        }
        return data;
    }

    private void initView(View view) {
        EventBus.getDefault().register(this);
        View title = view.findViewById(R.id.title);
        title.setVisibility(View.GONE);
        gridview = (GridView) view.findViewById(R.id.gridview);
        if (GlobalConsts.videoList != null && GlobalConsts.videoList.size() > 0) {
            removeDuplicate(GlobalConsts.videoList);
            mySize = GlobalConsts.videoList.size();
            if (GlobalConsts.videoList.size() != GlobalConsts.mVideoKeyList.size()) {
                mySize = GlobalConsts.mVideoKeyList.size();
            }
            for (int j = 0; j < mySize; j++) {
                if (GlobalConsts.mVideoKeyList != null && GlobalConsts.mVideoKeyList.size() > 0) {
                    path = GlobalConsts.mVideoKeyList.get(j);
                    mVideoList = GlobalConsts.mVideoMap.get(path);
                    videoInfos.addAll(mVideoList);
                }
            }
        }
        adapter = new VideoAdapter(getActivity(), videoInfos);
        gridview.setAdapter(adapter);
        write();
        for (int k = 0; k < videoInfos.size(); k++) {
//            Log.e("shijianpaixu" + k + "" + "xianzai", videoInfos.get(k).time + videoInfos.get(k).title);
        }
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.pull_to_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        gridview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Log.d(TAG, "onItemClick: ");
                Intent intent = new Intent(getActivity(),
                        NewVideoPlayActivity.class);
                intent.putExtra("position", position);
                ToosUtil.getInstance().addEventUmeng(getActivity(), "event_local_video");
                startActivity(intent);
            }
        });
    }

    /**
     * 按时间排序类
     */
    private void filePaixu() {
        //            Collections.sort(info.filespath, new FileComparator());//通过重写Comparator的实现类FileComparator来实现按文件创建时间排序。
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        Collections.sort(videoInfos, new Comparator<VideoInfo>() {
            /**
             *
             * @param lhs
             * @param rhs
             * @return an integer < 0 if lhs is less than rhs, 0 if they are
             *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
             */
            @Override
            public int compare(VideoInfo lhs, VideoInfo rhs) {
                Date date1 = DateUtil.stringToDate(lhs.getTime());
                Date date2 = DateUtil.stringToDate(rhs.getTime());
                // 对日期字段进行升序before，如果欲降序可采用after方法
                if (date1.before(date2)) {
                    return 1;
                } else if (date1.equals(date2)) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
        for (int k = 0; k < videoInfos.size(); k++) {
//            Log.e("shijianpaixu" + k + "", videoInfos.get(k).time + videoInfos.get(k).title);
        }
        removeDuplicate1(videoInfos);
        SaveData.getInstance().myVideoInfo = videoInfos;
        adapter.dataChange(videoInfos);
    }

    /**
     * 将获得的数据写入自己的类
     */
    public void write() {
        for (int i = 0; i < videoInfos.size(); i++) {
            String filePath = videoInfos.get(i).filePath;
            File file = new File(filePath);
            long time = 0;
            long duration = videoInfos.get(i).duration;
            if (file.exists()) {
                time = file.lastModified();
            }
            videoInfos.get(i).setDuration(duration);
            videoInfos.get(i).setTime(StringUtils.getfullTime(time));
//            Log.e("shijian" + i + "", StringUtils.getfullTime(time) + file.getName());
        }
        filePaixu();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //查询完成后发送的eventbus
    public void onEventMainThread(VideoInfo videoInfo) {
        if (GlobalConsts.videoList != null && GlobalConsts.videoList.size() > 0 && GlobalConsts.mVideoKeyList != null
                && GlobalConsts.mVideoKeyList.size() > 0) {
            if (videoInfos != null) {
                videoInfos.clear();
            }
            if (mVideoList != null) {
                mVideoList.clear();
            }
            if (GlobalConsts.videoList != null && GlobalConsts.videoList.size() > 0) {
                for (int j = 0; j < mySize; j++) {
                    if (GlobalConsts.videoList != null && GlobalConsts.videoList.size() > 0) {
                        path = GlobalConsts.mVideoKeyList.get(j);
                        mVideoList = GlobalConsts.mVideoMap.get(path);
                        videoInfos.addAll(mVideoList);
                    }
                }
            }
        }
        write();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        App.getContext().findVideo();


//		mHandler.sendEmptyMessageDelayed(1,4000);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (GlobalConsts.videoList != null && GlobalConsts.videoList.size() > 0 && GlobalConsts.mVideoKeyList != null
                            && GlobalConsts.mVideoKeyList.size() > 0) {
                        for (int j = 0; j < mySize; j++) {
                            path = GlobalConsts.mVideoKeyList.get(j);
                            mVideoList = GlobalConsts.mVideoMap.get(path);
                            videoInfos.addAll(mVideoList);
                        }
                    }
                    write();
                    mSwipeRefreshLayout.setRefreshing(false);
                    break;
            }
        }
    };

    public static class DateUtil {

        public static Date stringToDate(String dateString) {
            ParsePosition position = new ParsePosition(0);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateValue = simpleDateFormat.parse(dateString, position);
            return dateValue;
        }
    }
}
