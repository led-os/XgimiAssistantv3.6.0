package com.xgimi.zhushou.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgimi.device.device.FeedbackInfo;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.FileTransferActivity;
import com.xgimi.zhushou.activity.IntelligenceActivity;
import com.xgimi.zhushou.activity.JianYiActivity;
import com.xgimi.zhushou.activity.LogoActivity;
import com.xgimi.zhushou.activity.MyCollectActivity;
import com.xgimi.zhushou.activity.MyMessageActivity;
import com.xgimi.zhushou.activity.PhoneZiYuanActivity;
import com.xgimi.zhushou.activity.PlayHostoryActivity;
import com.xgimi.zhushou.activity.SettingActivity;
import com.xgimi.zhushou.activity.UserInfoActivity;
import com.xgimi.zhushou.activity.WallpaperActivity;
import com.xgimi.zhushou.activity.XgimiCommunityActivity;
import com.xgimi.zhushou.adapter.FindAdapter;
import com.xgimi.zhushou.bean.GimiUser;
import com.xgimi.zhushou.bean.Mp3Info;
import com.xgimi.zhushou.bean.PlayHostory;
import com.xgimi.zhushou.bean.XgimiBean;
import com.xgimi.zhushou.db.RecordDao;
import com.xgimi.zhushou.netUtil.Api;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;
import com.xgimi.zhushou.widget.MyListview;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import confignetwork.WifiAdminSimple;
import de.greenrobot.event.EventBus;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class FindFragment extends BaseFragment implements View.OnClickListener {

    private List<PlayHostory> playHostories;
    private TextView tv_name_two;
    private ImageView touxiang;
    private TextView name;
    private GimiUser loginInfo;
    private Intent intent;
    public XgimiBean mData = new XgimiBean();
    private FindAdapter mAdapter;
    private RelativeLayout xinxi;
    private TextView tv;
    private ImageView iv_message;

    public FindFragment() {
        // Required empty public constructor
    }

    public static FindFragment fragment;

    public static FindFragment getInstance() {
        if (fragment == null) {
            fragment = new FindFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        initView(view);
        initSend();
        getXgimi();
        return view;
    }

    /**
     * 获取到TV端返回的数据
     */
    public void onEventMainThread(FeedbackInfo info) {
        if (info != null && info.filespath != null) {
//            Collections.sort(info.filespath, new FileComparator());//通过重写Comparator的实现类FileComparator来实现按文件创建时间排序。
            Collections.sort(info.filespath, new Comparator<FeedbackInfo.fileLists>() {
                /**
                 *
                 * @param lhs
                 * @param rhs
                 * @return an integer < 0 if lhs is less than rhs, 0 if they are
                 *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
                 */
                @Override
                public int compare(FeedbackInfo.fileLists lhs, FeedbackInfo.fileLists rhs) {
                    Date date1 = DateUtil.stringToDate(lhs.getTime());
                    Date date2 = DateUtil.stringToDate(rhs.getTime());
                    // 对日期字段进行升序，如果欲降序可采用after方法
                    if (date1.before(date2)) {
                        return 1;
                    } else if (date1.after(date2)) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
            SaveData.getInstance().transferFile = info.filespath;
            EventBus.getDefault().post(new Mp3Info());
        }
    }

    /**
     * 按时间排序类
     */
    public static class DateUtil {

        public static Date stringToDate(String dateString) {
            ParsePosition position = new ParsePosition(0);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateValue = simpleDateFormat.parse(dateString, position);
            return dateValue;
        }

    }

    public class FileComparator implements Comparator<FeedbackInfo.fileLists> {
        public int compare(FeedbackInfo.fileLists file1, FeedbackInfo.fileLists file2) {
            if (Long.parseLong(file1.getTime()) < Long.parseLong(file2.getTime())) {
                return -1;
            } else if (Long.parseLong(file1.getTime()) > Long.parseLong(file2.getTime())) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    //发送拉取我已经传输过的文件的命令
    private void initSend() {
        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(GMSdkCheck.appId,
                "2", new VcontrolCmd.ControlCmd(30, 0, 0), 20000)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView(View view) {
        EventBus.getDefault().register(this);
        touxiang = (ImageView) view.findViewById(R.id.touxiang);
        touxiang.setOnClickListener(this);
        xinxi = (RelativeLayout) view.findViewById(R.id.rl);
        xinxi.setOnClickListener(this);
        name = (TextView) view.findViewById(R.id.name);
        name.setOnClickListener(this);
        TextView tv_one = (TextView) view.findViewById(R.id.one).findViewById(
                R.id.tv);
        tv_one.setText("手机资源");
        RelativeLayout ziyuanRl = (RelativeLayout) view.findViewById(R.id.one);
        ziyuanRl.setOnClickListener(this);
        TextView tv_two = (TextView) view.findViewById(R.id.two).findViewById(
                R.id.tv);
        tv_two.setText("最近观看");

        TextView tv_three = (TextView) view.findViewById(R.id.three).findViewById(
                R.id.tv);
        tv_three.setText("我的收藏");
        RelativeLayout collectRl = (RelativeLayout) view.findViewById(R.id.three);
        collectRl.setOnClickListener(this);

        TextView tv_four = (TextView) view.findViewById(R.id.four).findViewById(
                R.id.tv);
        tv_four.setText("极米社区");

        TextView tv_five = (TextView) view.findViewById(R.id.five).findViewById(
                R.id.tv);
        tv_five.setText("极米商城");

        TextView tv_six = (TextView) view.findViewById(R.id.six).findViewById(
                R.id.tv);
        tv_six.setText("极米服务");

        TextView tv_seven = (TextView) view.findViewById(R.id.seven).findViewById(
                R.id.tv);
        tv_seven.setText("智能幕布");
        View seven = view.findViewById(R.id.seven);
        seven.setOnClickListener(this);

        TextView tv_eitht = (TextView) view.findViewById(R.id.eight).findViewById(
                R.id.tv);
        tv_eitht.setText("给产品经理打小报告");
        RelativeLayout yijian = (RelativeLayout) view.findViewById(R.id.eight);
        yijian.setOnClickListener(this);

        TextView tv_nine = (TextView) view.findViewById(R.id.nine).findViewById(
                R.id.tv);
        tv_nine.setText("设置");
        RelativeLayout setting = (RelativeLayout) view.findViewById(R.id.nine);
        setting.setOnClickListener(this);
        //文件传输初始化
        TextView tv_filetransfer = (TextView) view.findViewById(R.id.filetransfer).findViewById(R.id.tv);
        tv_filetransfer.setText("传文件到TV");

//        RelativeLayout photoWallRl = (RelativeLayout) view.findViewById(R.id.include_photo_wall);
//        photoWallRl.setOnClickListener(this);
//        ((TextView)photoWallRl.findViewById(R.id.tv)).setText("照片墙");

        tv = (TextView) view.findViewById(R.id.myMessage).findViewById(R.id.tv);
        tv.setText("我的消息");
        iv_message = (ImageView) view.findViewById(R.id.myMessage).findViewById(R.id.iv);

        tv_name_two = (TextView) view.findViewById(R.id.two).findViewById(
                R.id.tv_name);
        tv_name_two.setVisibility(View.VISIBLE);
        playHostories = new RecordDao(getActivity()).ChaXunPlay();
        tv_name_two.setText(playHostories != null && playHostories.size() > 0 ? playHostories.get(playHostories.size() - 1).getTitle() : "");

        TextView tv_name_four = (TextView) view.findViewById(R.id.four).findViewById(
                R.id.tv_name);
        tv_name_two.setVisibility(View.VISIBLE);
        tv_name_four.setText("加湿器");

        TextView tv_name_five = (TextView) view.findViewById(R.id.five).findViewById(
                R.id.tv_name);
        tv_name_five.setVisibility(View.VISIBLE);
        tv_name_five.setText("最高20000-0.001可以领取");
        View v_two = view.findViewById(R.id.two);
        View v_one = view.findViewById(R.id.one);
        View v_three = view.findViewById(R.id.three);
        View v_four = view.findViewById(R.id.four);
        View v_five = view.findViewById(R.id.five);
        View v_six = view.findViewById(R.id.six);
        View v_filetransfer = view.findViewById(R.id.filetransfer);
        View v_message = view.findViewById(R.id.myMessage);
        view.findViewById(R.id.filetransfer).findViewById(R.id.view).setVisibility(View.GONE);
//        view.findViewById(R.id.seven).findViewById(R.id.view).setVisibility(View.GONE);
        view.findViewById(R.id.eight).findViewById(R.id.view).setVisibility(View.GONE);
        MyListview mListview = (MyListview) view.findViewById(R.id.listview);
        mAdapter = new FindAdapter(getActivity(), mData);
        mListview.setAdapter(mAdapter);

        v_two.setOnClickListener(this);
        v_one.setOnClickListener(this);
        v_three.setOnClickListener(this);
        v_four.setOnClickListener(this);
        v_five.setOnClickListener(this);
        v_six.setOnClickListener(this);
        v_filetransfer.setOnClickListener(this);
        v_message.setOnClickListener(this);
        ImageView iv_one = (ImageView) view.findViewById(R.id.one).findViewById(R.id.iv);
        ImageView iv_two = (ImageView) view.findViewById(R.id.two).findViewById(R.id.iv);
        ImageView iv_three = (ImageView) view.findViewById(R.id.three).findViewById(R.id.iv);
        ImageView iv_four = (ImageView) view.findViewById(R.id.four).findViewById(R.id.iv);
        ImageView iv_five = (ImageView) view.findViewById(R.id.five).findViewById(R.id.iv);
        ImageView iv_six = (ImageView) view.findViewById(R.id.six).findViewById(R.id.iv);
        ImageView iv_seven = (ImageView) view.findViewById(R.id.seven).findViewById(R.id.iv);
        ImageView iv_eight = (ImageView) view.findViewById(R.id.eight).findViewById(R.id.iv);
        ImageView iv_nine = (ImageView) view.findViewById(R.id.nine).findViewById(R.id.iv);
        ImageView iv_filetransfer = (ImageView) view.findViewById(R.id.filetransfer).findViewById(R.id.iv);
//        ImageView ivPhotoWall = (ImageView) photoWallRl.findViewById(R.id.iv);
//        ivPhotoWall.setImageResource(R.drawable.img_photo_wall5);
        iv_one.setImageResource(R.drawable.phone_ziyuan);
        iv_two.setImageResource(R.drawable.last_see);
        iv_three.setImageResource(R.drawable.find_shoucang);
        iv_four.setImageResource(R.drawable.jimishequ);
        iv_five.setImageResource(R.drawable.jimishangcheng);
        iv_six.setImageResource(R.drawable.jimifuwu);
        iv_eight.setImageResource(R.drawable.jianyi);
        iv_seven.setImageResource(R.drawable.zhinengpeijian);
        iv_nine.setImageResource(R.drawable.shezhi);
        iv_filetransfer.setImageResource(R.drawable.transfer);
        iv_message.setImageResource(R.drawable.wodetuisong);
        mListview.setFocusable(false);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private XgimiBean.DataBean item;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(getActivity(), XgimiCommunityActivity.class);
                item = mAdapter.getItem(position);
                ToosUtil.getInstance().addEventUmeng(getActivity(), "event_xgimi_service" + (position + 1));
                intent.putExtra("url", item.link_address);
                intent.putExtra("name", item.cate_name);
                getActivity().startActivity(intent);
            }
        });

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.one:
                ToosUtil.getInstance().addEventUmeng(getActivity(), "event_local_resource");
                Intent intent = new Intent(getActivity(), PhoneZiYuanActivity.class);
                startActivity(intent);
                break;
            case R.id.touxiang:
//                MobclickAgent.onEvent(UserFunctionActivity.this, "user_phono");
                if (app.getLoginInfo() != null) {
                    intent = new Intent(getActivity(), UserInfoActivity.class);
                } else {
                    intent = new Intent(getActivity(), LogoActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.rl:
                if (app.getLoginInfo() != null) {
                    intent = new Intent(getActivity(), UserInfoActivity.class);
                } else {
                    intent = new Intent(getActivity(), LogoActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.two:
                ToosUtil.getInstance().addEventUmeng(getActivity(), "event_movie_history");
                Intent intent2 = new Intent(getActivity(), PlayHostoryActivity.class);
                startActivity(intent2);
                break;
            case R.id.four:
                intent = new Intent(getActivity(), XgimiCommunityActivity.class);
                intent.putExtra("id", 1);
                startActivity(intent);
                break;
            case R.id.five:
                intent = new Intent(getActivity(), XgimiCommunityActivity.class);
                intent.putExtra("id", 2);
                startActivity(intent);
                break;
            case R.id.six:
                ToosUtil.getInstance().addEventUmeng(getActivity(), "event_xgimi_service");
                intent = new Intent(getActivity(), XgimiCommunityActivity.class);
                intent.putExtra("id", 3);
                startActivity(intent);
                break;
            case R.id.three:
                ToosUtil.getInstance().addEventUmeng(getActivity(), "event_my_collect");
                XGIMILOG.D(new Gson().toJson(app.getLoginInfo()));
                Intent intent1 = null;
                if (app.getLoginInfo() != null) {
                    intent1 = new Intent(getActivity(), MyCollectActivity.class);
                } else {
                    intent1 = new Intent(getActivity(), LogoActivity.class);
                }
                startActivity(intent1);
                break;
            case R.id.nine:
                Intent intent9 = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent9);
                break;
            case R.id.eight:
                intent = new Intent(getActivity(), JianYiActivity.class);
                startActivity(intent);
                break;
            case R.id.seven:
                String apSsid = new WifiAdminSimple(getContext()).getWifiConnectedSsid();
                if (apSsid == null || "".equals(apSsid)) {
                    Toast.makeText(getContext(), "请先连接WiFi", Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent(getActivity(), IntelligenceActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.filetransfer:
                //判断是否连接。没连接跳到扫描界面
                if (Constant.netStatus) {
                    intent = new Intent(getActivity(), FileTransferActivity.class);
                    startActivity(intent);
                } else {
                    ToosUtil.getInstance().isConnectTv(getActivity());
//                    intent = new Intent(getActivity(), SearchDeviceActivity.class);
//                    startActivity(intent);
                }

                break;
            case R.id.myMessage:
                ToosUtil.getInstance().addEventUmeng(getActivity(), "event_msg_list_click");
                intent = new Intent(getActivity(), MyMessageActivity.class);
                startActivity(intent);
                break;
//            case R.id.include_photo_wall:
//                if (App.phone_ip == null || "null".equals(App.phone_ip) || "".equals(App.phone_ip)) {
//                    Toast.makeText(getActivity(), "请先连接设备", Toast.LENGTH_SHORT).show();
//                } else {
//                    Intent intent3 = new Intent(getActivity(), WallpaperActivity.class);
//                    getActivity().startActivity(intent3);
//                }
//                break;
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        loginInfo = app.getLoginInfo();
        playHostories = new RecordDao(getActivity()).ChaXunPlay();
        tv_name_two.setText(playHostories != null && playHostories.size() > 0 ? playHostories.get(playHostories.size() - 1).getTitle() : "");
        if (loginInfo == null) {
            name.setText("请登录");
            touxiang.setImageResource(R.drawable.touxiao);
        } else {
            name.setText(loginInfo.data.username);
            if (app.getLoginInfo().data.avatar != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ImageLoader.getInstance().displayImage(App.getContext().getLoginInfo().data.avatar, touxiang);
//                        ImageLoaderUtils.display(getActivity(),touxiang,app.getLoginInfo().data.avatar);
                    }
                });
//                ImageLoader.getInstance().displayImage(
//                        app.getLoginInfo().data.avatar, touxiang);
            }
            // ImageLoader.getInstance().displayImage(loginInfo.data.avatar,
            // touxiang);
        }
    }


    public void getXgimi() {
        subscription = Api.getMangoApi().getXgimi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer1);
    }

    Observer<XgimiBean> observer1 = new Observer<XgimiBean>() {
        @Override
        public void onCompleted() {
            unsubscribe(subscription);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(XgimiBean channels) {
            if (channels != null && channels.data != null) {
                mAdapter.dataChange(channels);
            }
        }

    };

}

