package com.xgimi.zhushou.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class FileDetailActivity extends BaseActivity {
    private String type_Title; // 文件类型标题
    private ListView listView;
    public FileDetailTransferAdapter adapter;
    private List<Map<String, Object>> fileMapGroup; // 文件列表
    private TextView title;
    public int item;
    public List<FileInfo> fileInfo = new ArrayList<>();
    public FileInfo myFileInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.file_detail_layout);

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

        ImageView iv_remount = (ImageView) findViewById(R.id.title).findViewById(R.id.iv_remount);

        setYaokongBackground(iv_remount, this, "qita");
        if (Constant.netStatus) {
            iv_remount.setImageResource(R.drawable.yaokongqi);
        } else {
            iv_remount.setImageResource(R.drawable.gimi_yaokong);
        }
        ImageView back = (ImageView) findViewById(R.id.title).findViewById(R.id.back);
        back(back);
        TextView title = (TextView) findViewById(R.id.tv_titile);
        title.setText(type_Title);

        listView = (ListView) findViewById(R.id.file_detail_listView);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				String str = (String) fileMapGroup.get(position).get("id");
//				String path = (String) fileMapGroup.get(position).get("filepath");
//				String name=(String) fileMapGroup.get(position).get("name");
//				long size = (Long) fileMapGroup.get(position).get("size");
//				String packageName=(String) fileMapGroup.get(position).get("package");
                String str = (String) fileInfo.get(position).id;
                String path = (String) fileInfo.get(position).filepath;
                String name = (String) fileInfo.get(position).fileName;
                long size = (Long) fileInfo.get(position).size;
                String packageName = (String) fileInfo.get(position).packageName;
                onSendFile(str, "" + size, name, packageName);
                Intent intent = new Intent(FileDetailActivity.this, RemountActivity.class);
                FileDetailActivity.this.startActivity(intent);
            }
        });

    }

    List<VcontrolCmd.CustomPlay.PlayList> mPlyLists = new ArrayList<>();

    private String type;

    // 传送文件命令,需要判断设备是否连接
    public void onSendFile(String id, String size, String name, String packageName) {
        if (Constant.netStatus) {
            if (ToosUtil.getInstance().isInstallTvControl(FileDetailActivity.this)) {

                String sendCommand = "http://" + App.getContext().getPhoneIp() + ":" + HttpServer.PORT + "/" + id /*+ UdpManager.separator + size*/;
                mPlyLists.clear();
                if (type_Title.equals("APK文件")) {
                    type = ".apk";
                } else if (type_Title.equals("TXT文件")) {
                    type = ".txt";
                } else if (type_Title.equals("PPT文件")) {
                    type = ".ppt";
                } else if (type_Title.equals("PDF文件")) {
                    type = ".pdf";
                } else if (type_Title.equals("XLS文件")) {
                    type = ".xls";
                } else if (type_Title.equals("DOC文件")) {
                    type = ".doc";
                }
                VcontrolCmd.CustomPlay.PlayList mPlayList = new VcontrolCmd.CustomPlay.PlayList(type, packageName, null, sendCommand, name, null);
                mPlyLists.add(mPlayList);

                VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200, "2",
                        GMSdkCheck.appId, null,
                        new VcontrolCmd.CustomPlay(3, 0, null, mPlyLists, 0),
                        null, null, null)));
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
    /**
     * 按时间排序类
     */
    public Map<String, Object> map = new ArrayMap<>();

    private void filePaixu() {

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
            public int compare(FileInfo lhs, FileInfo rhs) {
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
    public void write() {
        for (int i = 0; i < GlobalConsts.files.get(item).size(); i++) {
            map = GlobalConsts.files.get(item).get(i);
            String filePath = (String) map.get("filepath");
            String id = (String) map.get("id");
            String path = (String) map.get("filepath");
            String name = (String) map.get("name");
            long size = (Long) map.get("size");
            String packageName = (String) map.get("package");
            File file = new File(filePath);

            String fileName = "";

            long fileSize = 0;

            long time = 0;
            Drawable icon = null;
            if (file.exists()) {
                fileName = file.getName();
                fileSize = file.length();
                time = file.lastModified();
                if (item == 5) {
                    icon = (Drawable) map.get("icon");
                }
            }
            myFileInfo = new FileInfo(fileName, FileUtils.formatFileSize(fileSize), StringUtils.getfullTime(time), icon, id, path, size, packageName);
            fileInfo.add(myFileInfo);
        }
        filePaixu();
        removeDuplicate(fileInfo);
        adapter.dataChange(item, fileInfo);
    }

    //去掉重复的数据
    private List<FileInfo> removeDuplicate(List<FileInfo> data) {
        for (int i = 0; i < data.size(); i++) {
            for (int j = data.size() - 1; j > i; j--) {
                if (data.get(j).time.equals(data.get(i).time) && data.get(j).fileName.equals(data.get(i).fileName) && data.get(j).fileSize.equals(data.get(i).fileSize)) {
                    data.remove(j);
                }
            }
        }
        return data;
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
