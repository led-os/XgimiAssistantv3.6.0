package com.xgimi.zhushou.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xgimi.device.device.FeedbackInfo;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.AlreadyTransferFileAdapter;
import com.xgimi.zhushou.bean.FileInfo;
import com.xgimi.zhushou.bean.Mp3Info;
import com.xgimi.zhushou.bean.YiJianBeen;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.GlobalConsts;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;
import com.xgimi.zhushou.widget.SignOutDilog;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/12/2.
 */
public class FileTransferActivity extends BaseActivity implements View.OnTouchListener, SwipeRefreshLayout.OnRefreshListener {
    private TextView title;
    private ImageView back;
    private ImageView iv_remount;
    private Button button;
    private ListView lisetview;
    private RelativeLayout dataList, rl_beijing;
    private TextView tv_choose;
    private AlreadyTransferFileAdapter adapter;
    private List<FeedbackInfo.fileLists> minfo = new ArrayList<>();
    private List<FeedbackInfo.fileLists> myinfo = new ArrayList<>();
    private RelativeLayout rl_chuanshu, rl;
    //保存已经选择的类型
    private String selecteType;
    //保存当前选择和之前的相同不
    private boolean isSame = true;
    private SignOutDilog dilog;
    private View myprog;
    private RelativeLayout help;
    private boolean isShanchu;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_transfer);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        EventBus.getDefault().register(this);
        title = (TextView) findViewById(R.id.title).findViewById(R.id.tv_titile);
        title.setText("文件管理");
        back = (ImageView) findViewById(R.id.title).findViewById(R.id.back);
        back(back);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.pull_to_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        iv_remount = (ImageView) findViewById(R.id.title).findViewById(R.id.iv_remount);
        setYaokongBackground(iv_remount, this, "qita");
        iv_remount.setOnTouchListener(this);
        button = (Button) findViewById(R.id.button);
        lisetview = (ListView) findViewById(R.id.lisetview);
        dataList = (RelativeLayout) findViewById(R.id.dataList);
        tv_choose = (TextView) findViewById(R.id.tv_choose);
        rl_beijing = (RelativeLayout) findViewById(R.id.rl_beijing);
        rl = (RelativeLayout) findViewById(R.id.rl);
        rl_chuanshu = (RelativeLayout) findViewById(R.id.rl_chuanshu);
        myprog = findViewById(R.id.myprog);
        myprog.setVisibility(View.VISIBLE);
        rl.setVisibility(View.GONE);
        rl_beijing.setVisibility(View.GONE);
        dataList.setVisibility(View.GONE);
        help = (RelativeLayout) findViewById(R.id.help);
        jiance();
        adapter = new AlreadyTransferFileAdapter(FileTransferActivity.this, SaveData.getInstance().transferFile, false);
        lisetview.setAdapter(adapter);
    }

    //检测有没得数据显示哪个
    private void jiance() {
        if (SaveData.getInstance().transferFile != null && SaveData.getInstance().transferFile.size() > 0) {
            Log.e("sizeold", SaveData.getInstance().transferFile.size() + "");
            rl_beijing.setFocusable(false);
            rl.setFocusable(false);
//            rl.setVisibility(View.GONE);
            rl_beijing.setVisibility(View.GONE);
            dataList.setVisibility(View.VISIBLE);
            myprog.setVisibility(View.GONE);
        } else {
            rl.setFocusable(false);
            dataList.setFocusable(false);
            rl.setVisibility(View.GONE);
            rl_beijing.setVisibility(View.VISIBLE);
            dataList.setVisibility(View.GONE);
            myprog.setVisibility(View.GONE);
        }
    }

    //拉取已经传输了的数据
    private void initData() {
        tv_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(1);
            }
        });
        //有没得打钩的，打钩一个传一个过来
        adapter.setLisener(new AlreadyTransferFileAdapter.getIds() {
            @Override
            public void getMenmberIds(Map<Integer, FeedbackInfo.fileLists> beans, Map<Integer, FeedbackInfo.fileLists> cleanBeen, boolean isAdd) {
                if (tv_choose.getText().equals("取消")) {
//                minfo=addImage(beans);
                    //注释的这段代码可以实现，按图片点击的先后顺序加入（另一个项目中可以，这个项目中未验证）
                    if (isAdd) {
                        addImage(cleanBeen);
                        isTogether(minfo, mp3, false);

                        minfo.addAll(mp3);
                    } else {
                        deleteImage(cleanBeen);
                        for (int i = 0; i < minfo.size(); i++) {
                            if ((deletmp3.get(0).getFilepath()) == (minfo.get(i).getFilepath())) {
                                minfo.remove(i);
                                if (minfo != null && minfo.size() > 0) {
                                    myinfo.clear();
                                    myinfo.add(minfo.get(0));
                                    isTogether(minfo, myinfo, true);
                                }
                            }
                        }
                    }
                    cleanBeen.clear();
                }
            }
        });
        rl_chuanshu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSame) {
                    for (int i = 0; i < mp3.size(); i++) {
                        XGIMILOG.E(new Gson().toJson(mp3.get(i)));
                    }
                    onOpenBtn(minfo);
                } else {
                    Toast.makeText(FileTransferActivity.this, "只有图片和视频能混合打开", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        lisetview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(tv_choose.getText().equals("选择")){
//
//                }
//            }
//        });
    }

    //遍历map集合，。取出其中的人名和id放入poeples中，shijiPoeples中存放被选中的人的个数,deletepoeples存放要删除的那一个数据
    private List<FeedbackInfo.fileLists> mp3 = new ArrayList<>();
    private List<FeedbackInfo.fileLists> deletmp3 = new ArrayList<>();
    private Map<Integer, FeedbackInfo.fileLists> shijiimage;

    //打钩时的正常遍历添加
    private List<FeedbackInfo.fileLists> addImage(Map<Integer, FeedbackInfo.fileLists> beans) {
        mp3.clear();
        Iterator iter = beans.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int) entry.getKey();
            FeedbackInfo.fileLists val = (FeedbackInfo.fileLists) entry.getValue();
            mp3.add(new FeedbackInfo.fileLists(val.getFilepath(), val.getType(), val.getImagepath(), val.getTime()));
        }
        return mp3;
    }

    private List<FeedbackInfo.fileLists> deleteImage(Map<Integer, FeedbackInfo.fileLists> beans) {
        deletmp3.clear();
        Iterator iter = beans.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int) entry.getKey();
            FeedbackInfo.fileLists val = (FeedbackInfo.fileLists) entry.getValue();
            deletmp3.add(new FeedbackInfo.fileLists(val.getFilepath(), val.getType(), val.getImagepath(), val.getTime()));
        }
        return deletmp3;
    }

    private boolean isVISIBLE;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (tv_choose.getText().equals("选择")) {
                        isVISIBLE = true;
                        minfo.clear();
                        tv_choose.setText("取消");
                        rl.setVisibility(View.VISIBLE);
                        button.setVisibility(View.GONE);
                        adapter.dataChange(isVISIBLE, SaveData.getInstance().transferFile);
                    } else if (tv_choose.getText().equals("取消")) {
                        isVISIBLE = false;
                        minfo.clear();
                        cleanType();
                    }
                    break;
                case 2:
                    if (SaveData.getInstance().transferFile != null && SaveData.getInstance().transferFile.size() > 0) {
                        adapter.dataChange(SaveData.getInstance().transferFile);
                        Log.e("sizexian", SaveData.getInstance().transferFile.size() + "");
                    }
                    break;
                case 3:
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initListener() {
        if (Constant.netStatus) {
            iv_remount.setImageResource(R.drawable.yaokongqi);
        } else {
            iv_remount.setImageResource(R.drawable.gimi_yaokong);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FileTransferActivity.this, CeShiActivity.class);
                startActivity(intent);
            }
        });
        lisetview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//                if(tv_choose.getText().equals("选择")) {
//                    if (dilog == null) {
//                        dilog = new SignOutDilog(FileTransferActivity.this, "确认删除");
//                    }
//                        dilog.show();
//                        dilog.setOnLisener(new SignOutDilog.onListern() {
//                            @Override
//                            public void send() {
//                                deleteOne(SaveData.getInstance().transferFile, position);
//                            }
//                        });
//                    }

                return true;
            }
        });

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.iv_remount:
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        iv_remount.setAlpha(0.6f);
                        break;
                    case MotionEvent.ACTION_UP:
                        iv_remount.setAlpha(1.0f);
                        break;
                }
                break;
        }
        return false;
    }


    // 头像设置的提示对话框方法
    @SuppressLint("NewApi")
    private void showDialog() {

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_filetransfer,
                null);

        final Dialog dialog = new Dialog(this,
                R.style.transparentFrameWindowStyle);
        dialog.setContentView(view);

        Button dialog_tupian = (Button) view
                .findViewById(R.id.dialog_tupian);
        Button dialog_vidoe = (Button) view.findViewById(R.id.dialog_vidoe);
        Button dialog_music = (Button) view.findViewById(R.id.dialog_music);
        Button dialog_file = (Button) view.findViewById(R.id.dialog_file);
        Button btn_cancel = (Button) view
                .findViewById(R.id.dialog_avatar_cancel);

        dialog_tupian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FileTransferActivity.this, PictureActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog_vidoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FileTransferActivity.this, VideoActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalConsts.mp3Infos == null) {
                    Toast.makeText(FileTransferActivity.this, "暂无音乐", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(FileTransferActivity.this, MusicActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                }
            }
        });
        dialog_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FileTransferActivity.this, FileActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        window.setDimAmount(0.2f);
        WindowManager.LayoutParams wl = window.getAttributes();

        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();

        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private ArrayList<VcontrolCmd.CustomPlay.PlayList> playLists = new ArrayList<>();
    private int mediatype;

    //没点选择时打开一个的时候
    private void onOpenOne(List<FeedbackInfo.fileLists> data, int i) {
        Toast.makeText(FileTransferActivity.this, "正在无屏电视打开", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(FileTransferActivity.this, RemountActivity.class);
        startActivity(intent);
        ToosUtil.getInstance().addEventUmeng(FileTransferActivity.this, "event_file_open");
        if (playLists != null) {
            playLists.clear();
        }
        if (data.get(i).Imagepath != null) {
            mediatype = 12;
        } else if (data.get(i).Type.equals("mp4") || data.get(i).Type.equals("rmvb") || data.get(i).Type.equals("mkv") || data.get(i).Type.equals("mov") || data.get(i).Type.equals("flv")) {
            mediatype = 10;
        } else if (data.get(i).Type.equals("mp3") || data.get(i).Type.equals("wav") || data.get(i).Type.equals("m4a")) {
            mediatype = 11;
        } else if (data.get(i).Type.equals("apk") || data.get(i).Type.equals("txt") || data.get(i).Type.equals("ppt") || data.get(i).Type.equals("pptx") || data.get(i).Type.equals("pdf")
                || data.get(i).Type.equals("xls") || data.get(i).Type.equals("xlsx") || data.get(i).Type.equals("doc") || data.get(i).Type.equals("docx")) {
            mediatype = 13;
        }
        String title = getFileName(data.get(i).getFilepath()) + getHouZhui(data.get(i).getFilepath());
        VcontrolCmd.CustomPlay.PlayList playList = new VcontrolCmd.CustomPlay.PlayList(title, data.get(i).getFilepath());
        playLists.add(playList);
        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200,
                "0", GMSdkCheck.appId, null, new VcontrolCmd.CustomPlay(mediatype, 0, null, playLists, 0), null, null, null)));
    }

    //删除某个文件
    private void deleteOne(List<FeedbackInfo.fileLists> data, int i) {
        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(GMSdkCheck.appId,
                "2", new VcontrolCmd.ControlCmd(8, data.get(i).Filepath), 20000)));
        data.remove(i);
        adapter.dataChange(false, data);
        Log.e("sizexian", SaveData.getInstance().transferFile.size() + "");
        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(GMSdkCheck.appId,
                "2", new VcontrolCmd.ControlCmd(30, 0, 0), 20000)));
        jiance();
    }

    //打开多个文件的方法
    private void onOpenBtn(List<FeedbackInfo.fileLists> data) {
        playLists.clear();
        Toast.makeText(FileTransferActivity.this, "正在无屏电视打开", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(FileTransferActivity.this, RemountActivity.class);
        startActivity(intent);
        ToosUtil.getInstance().addEventUmeng(FileTransferActivity.this, "event_file_open");
        boolean containsVideo = false;
        boolean containsImg = false;
        for (int i = 0; i < data.size(); i++) {
            //打开视频的mediatype
            if (data.get(i).Imagepath != null) {
                containsImg = true;
                mediatype = 12;
            } else if (data.get(i).Type.equals("mp4") || data.get(i).Type.equals("rmvb") || data.get(i).Type.equals("mkv") || data.get(i).Type.equals("mov") || data.get(i).Type.equals("flv")) {
                mediatype = 10;
                containsVideo = true;
            } else if (data.get(i).Type.equals("mp3") || data.get(i).Type.equals("wav") || data.get(i).Type.equals("m4a")) {
                mediatype = 11;
            } else if (data.get(i).Type.equals("apk") || data.get(i).Type.equals("txt") || data.get(i).Type.equals("ppt") || data.get(i).Type.equals("pptx") || data.get(i).Type.equals("pptx") || data.get(i).Type.equals("pdf")
                    || data.get(i).Type.equals("xls") || data.get(i).Type.equals("xlsx") || data.get(i).Type.equals("doc") || data.get(i).Type.equals("docx")) {
                mediatype = 13;
            }
            String title = getFileName(data.get(i).getFilepath()) + getHouZhui(data.get(i).getFilepath());
            VcontrolCmd.CustomPlay.PlayList playList = new VcontrolCmd.CustomPlay.PlayList(title, data.get(i).getFilepath());
            playLists.add(playList);
            XGIMILOG.E("send : " + new Gson().toJson(playList));
        }
        if (containsVideo) {
            mediatype = 10;
        }
        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200,
                "2", GMSdkCheck.appId, null, new VcontrolCmd.CustomPlay(mediatype, 0, null, playLists, 0), null, null, null)));
        cleanType();
    }

    //从url中获取图片的文件名
    public String getFileName(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return null;
        }
    }

    //从url中获取图片后缀名
    public String getHouZhui(String url) {
        String prefix = url.substring(url.lastIndexOf(".") + 1);
        return prefix;
    }

    //点击立即打开清除后清除type值
    public void cleanType() {
        if (SaveData.getInstance().transferFile != null && SaveData.getInstance().transferFile.size() > 0) {
            for (int i = 0; i < SaveData.getInstance().transferFile.size(); i++) {
                SaveData.getInstance().transferFile.get(i).setMyType(0);
            }
        }
        isVISIBLE = false;
        tv_choose.setText("选择");
        rl.setVisibility(View.GONE);
        button.setVisibility(View.VISIBLE);
        adapter.dataChange(isVISIBLE, SaveData.getInstance().transferFile);
    }

    //判断只有音乐和视频才能一起放
    private void isTogether(List<FeedbackInfo.fileLists> mData, List<FeedbackInfo.fileLists> data, boolean isShanchu) {
        if (mData.isEmpty()) {
            isWhat(data, 0);
        } else if (mData != null) {
            if (isShanchu) {
                if (mData.size() == 1) {
                    isSame = true;
                } else {
                    for (int i = 0; i < mData.size(); i++) {
                        if (isWhat(mData, i).equals(isWhat(data, 0))) {
                            isSame = true;
                        } else {
                            isSame = false;
//                            Toast.makeText(FileTransferActivity.this,"只有图片和视频能混合打开",Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                }
            } else {
                for (int i = 0; i < mData.size(); i++) {
                    if (isWhat(mData, i).equals(isWhat(data, 0))) {
                        isSame = true;
                    } else {
                        isSame = false;
                        Toast.makeText(FileTransferActivity.this, "只有图片和视频能混合打开", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }

        }
    }

    //得到当前选择的是什么类型
    private String isWhat(List<FeedbackInfo.fileLists> data, int i) {
        if (data.get(i).Imagepath != null) {
            selecteType = "tupian";
        } else if (data.get(i).Type.equals("mp4") || data.get(i).Type.equals("rmvb") || data.get(i).Type.equals("mkv") || data.get(i).Type.equals("mov") || data.get(i).Type.equals("flv")) {
            selecteType = "tupian";
        } else if (data.get(i).Type.equals("mp3") || data.get(i).Type.equals("wav") || data.get(i).Type.equals("m4a")) {
            selecteType = "yinyue";
        } else if (data.get(i).Type.equals("apk") || data.get(i).Type.equals("txt") || data.get(i).Type.equals("pptx") || data.get(i).Type.equals("ppt") || data.get(i).Type.equals("pdf")
                || data.get(i).Type.equals("xls") || data.get(i).Type.equals("xlsx") || data.get(i).Type.equals("doc") || data.get(i).Type.equals("docx")) {
            selecteType = "wenjian";
        }
        return selecteType;
    }

    @Override
    protected void onResume() {
        super.onResume();
        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(GMSdkCheck.appId,
                "2", new VcontrolCmd.ControlCmd(30, 0, 0), 20000)));
        mHandler.sendEmptyMessageDelayed(2, 2000);
    }

    //获取到TV端返回的数据
    public void onEventMainThread(Mp3Info info) {
        XGIMILOG.E("");
        if (SaveData.getInstance().transferFile != null && SaveData.getInstance().transferFile != null) {
            adapter.dataChange(SaveData.getInstance().transferFile);
            mSwipeRefreshLayout.setRefreshing(false);
        }
        jiance();
    }

    /**
     * 响应adpter中打开单个item
     */
    public void onEventMainThread(FileInfo info) {
        if (SaveData.getInstance().transferFile != null) {
            onOpenOne(SaveData.getInstance().transferFile, SaveData.getInstance().fileTransferPos);
        }
    }

    //长按时的响应
    public void onEventMainThread(YiJianBeen info) {
        if (tv_choose.getText().equals("选择")) {
            if (dilog == null) {
                dilog = new SignOutDilog(FileTransferActivity.this, "确认删除");
            }
            dilog.show();
            dilog.setOnLisener(new SignOutDilog.onListern() {
                @Override
                public void send() {
                    deleteOne(SaveData.getInstance().transferFile, SaveData.getInstance().fileTransferPos);
                }
            });
        }
    }

    //刷新
    //发送拉取我已经传输过的文件的命令
    @Override
    public void onRefresh() {
        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(GMSdkCheck.appId,
                "2", new VcontrolCmd.ControlCmd(30, 0, 0), 20000)));
        mHandler.sendEmptyMessageDelayed(3, 5000);
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

}
