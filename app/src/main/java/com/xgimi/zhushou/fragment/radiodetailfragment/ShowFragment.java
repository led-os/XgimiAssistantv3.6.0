package com.xgimi.zhushou.fragment.radiodetailfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xgimi.device.device.GMDeviceStorage;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.RadioBoFangActivity;
import com.xgimi.zhushou.adapter.RadioShowListAdapter;
import com.xgimi.zhushou.bean.RadioShow;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.DeviceUtils;
import com.xgimi.zhushou.util.LogUtil;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;
import com.xgimi.zhushou.widget.MyListview;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/8/18.
 */
public class ShowFragment extends BaseFragment {
    private View view;
    private MyListview listview;
    private Subscription subscription;
    private RadioShowListAdapter adapter;
    private List<RadioShow.data> mData = new ArrayList<>();
    private List<VcontrolCmd.CustomPlay.PlayList> mPlayLists;
    private RelativeLayout mPlayAllRl;

    private View.OnClickListener mOnPlayAllClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Constant.netStatus && mData != null && mData.size() > 0) {
                if (ToosUtil.getInstance().isInstallTvControl(getActivity())) {
                    Intent intent = new Intent(getActivity(), RadioBoFangActivity.class);
                    intent.putExtra("title", mData.get(0).program_title);
                    intent.putExtra("time", mData.get(0).program_play_time);
                    intent.putExtra("id", mData.get(0).program_id);
                    intent.putExtra("type", "1");
                    SaveData.getInstance().mRadioShow = mData;
                    SaveData.getInstance().fenlei = "1";
                    SaveData.getInstance().position = 0;
                    SaveData.getInstance().mPlayLists = mPlayLists;
                    startActivity(intent);
                    VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200, "2",
                            GMSdkCheck.appId, DeviceUtils.getappVersion(getActivity()), App.getContext().PACKAGENAME, new VcontrolCmd.ThirdPlay(mData.get(0).program_title, mData.get(0).program_id, 0, SaveData.getInstance().dianTaiClass, App.getContext().QingTingFM),
                            new VcontrolCmd.CustomPlay(1, -1, null, mPlayLists, 0),
                            null, null, null)));
                    ToosUtil.getInstance().addEventUmeng(getActivity(), "event_music_radio_play");
                }
            } else {
                ToosUtil.getInstance().isConnectTv(getActivity());
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_radio_show, container, false);
        initView(view);
        return view;
    }

    public ShowFragment() {

    }


    private void initView(View view) {
        mPlayLists = new ArrayList<>();
        mPlayAllRl = (RelativeLayout) view.findViewById(R.id.head);
        mPlayAllRl.setOnClickListener(mOnPlayAllClickListener);
        listview = (MyListview) view.findViewById(R.id.lisetview);
        adapter = new RadioShowListAdapter(0, getActivity(), mData);
        listview.setAdapter(adapter);
        listview.setFocusable(false);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (Constant.netStatus) {
                    if (ToosUtil.getInstance().isInstallTvControl(getActivity())) {
                        Intent intent = new Intent(getActivity(), RadioBoFangActivity.class);
                        intent.putExtra("title", mData.get(position).program_title);
                        intent.putExtra("time", mData.get(position).program_play_time);
                        intent.putExtra("id", mData.get(position).program_id);
                        intent.putExtra("type", "1");
                        SaveData.getInstance().mRadioShow = mData;
                        SaveData.getInstance().fenlei = "1";
                        SaveData.getInstance().position = position;
                        SaveData.getInstance().mPlayLists = mPlayLists;
                        startActivity(intent);
                        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200, "2",
                                GMSdkCheck.appId, DeviceUtils.getappVersion(getActivity()), App.getContext().PACKAGENAME, new VcontrolCmd.ThirdPlay(mData.get(position).program_title, mData.get(position).program_id, 0, SaveData.getInstance().dianTaiClass, App.getContext().QingTingFM),
                                new VcontrolCmd.CustomPlay(1, -1, null, mPlayLists, position),
                                null, null, null)));
                        ToosUtil.getInstance().addEventUmeng(getActivity(), "event_music_radio_play");
                    }

                } else {
                    ToosUtil.getInstance().isConnectTv(getActivity());
                }
            }
        });
        ToosUtil.getInstance().addEventUmeng(getActivity(), "event_music_radio_play_all");
    }

    //获取电台节目信息
    public void getShow(int id) {
        XGIMILOG.E("开始获取节目详情 : " + id);
        subscription = Api.getMangoApi().getRadioShow(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    Observer<RadioShow> observer = new Observer<RadioShow>() {
        @Override
        public void onCompleted() {
            unRegist(subscription);
        }

        @Override
        public void onError(Throwable e) {
            XGIMILOG.E("");
            mPlayAllRl.setVisibility(View.GONE);
            listview.setVisibility(View.GONE);
            Toast.makeText(getContext(), "暂时没有内容，看看其他的吧", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onNext(RadioShow radioShow) {
            XGIMILOG.E("获取节目详情成功");
//            LogUtil.d(XGIMILOG.getTag(), "++++++++++++++" + new Gson().toJson(radioShow));
            if (radioShow != null && radioShow.data != null && radioShow.code == 200) {
                mPlayAllRl.setVisibility(View.VISIBLE);
                listview.setVisibility(View.VISIBLE);
                mData.addAll(radioShow.data);
                adapter.dataChange(mData);
                for (int i = 0; i < radioShow.data.size(); i++) {
                    VcontrolCmd.CustomPlay.PlayList playList = new VcontrolCmd.CustomPlay.PlayList(null, radioShow.data.get(i).program_id,
                            null, radioShow.data.get(i).program_title, null, radioShow.data.get(i).program_play_address, null, null, null, radioShow.data.get(i).program_duration,
                            SaveData.getInstance().cover_path, radioShow.data.get(i).program_update_time, null, 0, SaveData.getInstance().dianTaiClass, App.getContext().QingTingFM, SaveData.getInstance().fm_title, SaveData.getInstance().category_id + "");
                    mPlayLists.add(playList);
                }
            } else {
                mPlayAllRl.setVisibility(View.GONE);
                listview.setVisibility(View.GONE);
                Toast.makeText(getContext(), "暂时没有内容，看看其他的吧", Toast.LENGTH_LONG).show();
            }
        }
    };

    private void unRegist(Subscription sub) {
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }

    }
}
