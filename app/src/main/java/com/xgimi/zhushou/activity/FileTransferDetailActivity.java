package com.xgimi.zhushou.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xgimi.device.device.FeedbackInfo;
import com.xgimi.device.device.GMDeviceController;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.socket.UdpManager;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.utils.FileUtils;
import com.xgimi.device.utils.StringUtils;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.FileDetailTransferAdapter;
import com.xgimi.zhushou.bean.FileInfo;
import com.xgimi.zhushou.netUtil.HttpServer;
import com.xgimi.zhushou.netUtil.NetUtil;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.GlobalConsts;
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

/**
 * Created by Administrator on 2016/12/6.
 */
public class FileTransferDetailActivity extends BaseActivity{
    private String type_Title; // 文件类型标题
    private ListView listView;
    public FileDetailTransferAdapter adapter;
    private List<Map<String, Object>> fileMapGroup; // 文件列表
    private TextView title;
    private RelativeLayout tishi;
    private TextView downName;
    private TextView downProgress;
    private int item;
    public List<FileInfo> fileInfo=new ArrayList<>();
    public FileInfo myFileInfo;
    //这个boolean类型是解决下载应用时，这边会收到进度从而弹出受影响
    private boolean isClick;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_transfer_detail);
        type_Title = getIntent().getStringExtra("type_title");
        item = getIntent().getIntExtra("position", 0);
        fileMapGroup = GlobalConsts.files.get(item);
        adapter = new FileDetailTransferAdapter(this, item, fileInfo);
        write();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //	MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //MobclickAgent.onPause(this);
    }

    private void initView() {
        EventBus.getDefault().register(this);
        ImageView iv_remount = (ImageView) findViewById(R.id.title).findViewById(R.id.iv_remount);
        setYaokongBackground(iv_remount,this,"qita");
        if(Constant.netStatus){
            iv_remount.setImageResource(R.drawable.yaokongqi);
        }else{
            iv_remount.setImageResource(R.drawable.gimi_yaokong);
        }
        tishi= (RelativeLayout) findViewById(R.id.tishi);
        tishi.setVisibility(View.GONE);
        downName= (TextView) findViewById(R.id.downName);
        downProgress= (TextView) findViewById(R.id.downProgress);
        ImageView back = (ImageView) findViewById(R.id.title).findViewById(R.id.back);
        back(back);
        TextView title = (TextView) findViewById(R.id.tv_titile);
        title.setText(type_Title);
        listView = (ListView) findViewById(R.id.file_detail_listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) fileInfo.get(position).id;
                String path = (String) fileInfo.get(position).filepath;
                String name=(String) fileInfo.get(position).fileName;
                long size = (Long) fileInfo.get(position).size;
                String packageName=(String) fileInfo.get(position).packageName;
                onSendFile(str, "" + size,name,packageName,path);
            }
        });

    }
    List< VcontrolCmd.CustomPlay.PlayList> mPlyLists=new ArrayList<>();

    private String type;
    // 传送文件命令,需要判断设备是否连接
    public void onSendFile(String id, String size,String name,String packageName,String filepath) {
        if (Constant.netStatus) {
            if(ToosUtil.getInstance().isInstallTvControl(FileTransferDetailActivity.this) ){
//                showDilog("传输中...");
                ToosUtil.getInstance().addEventUmeng(FileTransferDetailActivity.this,"event_file_send_doc");
                ToosUtil.getInstance().addEventUmeng(FileTransferDetailActivity.this,"event_file_send");
                Toast.makeText(this,"准备中",Toast.LENGTH_SHORT).show();
                String sendCommand = "http://" + App.mPhoneIP+ ":" + HttpServer.PORT + "/" + id /*+ UdpManager.separator + size*/;
                mPlyLists.clear();
                if(type_Title.equals("APK文件")){
                    type=".apk";
                }else if(type_Title.equals("TXT文件")){
                    type=".txt";
                }else if(type_Title.equals("PPT文件")){
                    type=".ppt";
                }
                else if(type_Title.equals("PDF文件")){
                    type=".pdf";
                }
                else if(type_Title.equals("XLS文件")){
                    type=".xls";
                }
                else if(type_Title.equals("DOC文件")){
                    type=".doc";
                }
                String newId = id;
                String title = filepath;
                String newSendCommand = "http://" + App.mPhoneIP + ":" + com.xgimi.device.device.HttpServer.PORT + "/" + newId + UdpManager.separator + title;
                //GMDeviceController.getInstance().SendJC(sendCommand);
                VcontrolCmd.CustomPlay.PlayList playList=new VcontrolCmd.CustomPlay.PlayList("."+getHouZhui(title),null,null,newSendCommand,getFileName(title),null);
                mPlyLists.add(playList);
                VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200,
                        "2", GMSdkCheck.appId, null, new VcontrolCmd.CustomPlay(23, 0, null, mPlyLists, 0), null, null, null)));
                isClick=true;
//                VcontrolCmd.CustomPlay.PlayList mPlayList = new VcontrolCmd.CustomPlay.PlayList(type, packageName, null, sendCommand, name, null);
//                mPlyLists.add(mPlayList);
//                VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200, "2",
//                        GMSdkCheck.appId, null,
//                        new VcontrolCmd.CustomPlay(3, 0, null, mPlyLists, 0),
//                        null, null, null)));
//			//GMDeviceController.getInstance().SendJC(sendCommand);
            }
        } else {
            Toast.makeText(this, R.string.device_noconnected, Toast.LENGTH_SHORT).show();
        }

    }


    // 传送文件命令,需要判断设备是否连接
    public void onSendFile(String id, String size) {
        if (Constant.netStatus) {
            String sendCommand = "http://" + App.mPhoneIP + ":" + HttpServer.PORT + "/" + id + UdpManager.separator + size;
            GMDeviceController.getInstance().SendJC(sendCommand);
        } else {
            Toast.makeText(this, R.string.device_noconnected, Toast.LENGTH_SHORT).show();
        }

    }
    //从url中获取文件的文件名
    public String getFileName(String pathandname){
        int start=pathandname.lastIndexOf("/");
        int end=pathandname.lastIndexOf(".");
        if(start!=-1 && end!=-1){
            return pathandname.substring(start+1,end);
        }else{
            return null;
        }
    }
    //从url中获取文件后缀名
    public String getHouZhui(String url){
        String prefix=url.substring(url.lastIndexOf(".")+1);
        return prefix;
    }
    // xml布局文件中对应的结束activity按钮
    public void File_Detail_Finish(View v) {
        finish();
    }
//
//	// xml布局文件中对应的显示侧边栏按钮,暂时改为直接显示遥控器界面
//	public void Back_Menu(View v) {
//		// 通过EventBus跳出遥控器界面
//		EventBus.getDefault().post(new InItem(0, 1));
//		finish();
//	}

    private FeedbackInfo myInfo;
    public void onEventMainThread(FeedbackInfo info) {
        if(isClick) {
            tishi.setVisibility(View.VISIBLE);
            myInfo = info;
            if (myInfo != null && myInfo.downloadInfo != null) {
                Message message = mhanHandler.obtainMessage();
                message.obj = myInfo;
                message.what = 0;
                mhanHandler.sendMessage(message);
            }
        }
    }
    private int position=0;
    Handler mhanHandler=new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            if(downProgress!=null&&downName!=null&&isClick) {
                FeedbackInfo info = (FeedbackInfo) msg.obj;
                if (info.downloadInfo!= null) {

                    downProgress.setText(info.downloadInfo.progress+"%");
                    downName.setText(info.downloadInfo.savepath);
                    tishi.setVisibility(View.VISIBLE);
                    if(info.downloadInfo.progress==100&&isClick){
                        MissDilog();
                        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(GMSdkCheck.appId,
                                "2",new VcontrolCmd.ControlCmd(30,0,0),20000)));
                        App.getContext().destoryActivity("FileTransferDetailActivity");
                        finish();
                        Toast.makeText(FileTransferDetailActivity.this,"传输完成",Toast.LENGTH_SHORT).show();

                    }
//                    Toast.makeText(FileTransferDetailActivity.this, R.string.device_complete, Toast.LENGTH_SHORT).show();
//                    if(selectVideoList1.size()==position){

//                    }
                }
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    /**
     * 按时间排序类
     */
    public Map<String, Object> map=new ArrayMap<>();
    private void filePaixu(){

        //            Collections.sort(info.filespath, new FileComparator());//通过重写Comparator的实现类FileComparator来实现按文件创建时间排序。

        Collections.sort(fileInfo, new Comparator<FileInfo>() {
            /**
             *
             * @param lhs
             * @param rhs
             * @return an integer < 0 if lhs is less than rhs, 0 if they are
             *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
             */
            @Override
            public int compare(FileInfo lhs,FileInfo rhs) {
                Date date1 = DateUtil.stringToDate(lhs.time);
                Date date2 = DateUtil.stringToDate(rhs.time);
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
    }

    /**
     * 将获得的数据写入自己的类
     */
    public void write(){
        for (int i=0;i<GlobalConsts.files.get(item).size();i++) {
            map = GlobalConsts.files.get(item).get(i);
            String filePath = (String) map.get("filepath");
            String id = (String) map.get("id");
            String path = (String) map.get("filepath");
            String name=(String) map.get("name");
            long size = (Long) map.get("size");
            String packageName=(String) map.get("package");
            File file = new File(filePath);

            String fileName = "";

            long fileSize = 0;

            long time = 0;
            Drawable icon= null;
            if (file.exists()) {
                fileName = file.getName();
                fileSize = file.length();
                time = file.lastModified();
                if(item==5){
                    icon= (Drawable) map.get("icon");
                }
            }
            myFileInfo=new FileInfo(fileName, FileUtils.formatFileSize(fileSize), StringUtils.getfullTime(time),icon,id,path,size,packageName);
            fileInfo.add(myFileInfo);
        }
        filePaixu();
        adapter.dataChange(item,fileInfo);
    }



    public static class DateUtil {

        public static Date stringToDate(String dateString) {
            ParsePosition position = new ParsePosition(0);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateValue = simpleDateFormat.parse(dateString, position);
            return dateValue;
        }
    }

}
