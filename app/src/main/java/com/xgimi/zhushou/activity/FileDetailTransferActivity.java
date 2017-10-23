package com.xgimi.zhushou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.DetailFileListAdapter;
import com.xgimi.zhushou.netUtil.HttpServer;
import com.xgimi.zhushou.netUtil.NetUtil;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.GlobalConsts;
import com.xgimi.zhushou.util.ToosUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/5.
 */
public class FileDetailTransferActivity extends BaseActivity{
    private String type_Title; // 文件类型标题
    private ListView listView;
    public DetailFileListAdapter adapter;
    private List<Map<String, Object>> fileMapGroup; // 文件列表
    private TextView title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_detail_transfer_layout);
        type_Title = getIntent().getStringExtra("type_title");

        int item = getIntent().getIntExtra("position", 0);

        fileMapGroup = GlobalConsts.files.get(item);

        adapter = new DetailFileListAdapter(this, item, fileMapGroup);

        initView();
    }
    private void initView() {

        ImageView iv_remount = (ImageView) findViewById(R.id.title).findViewById(R.id.iv_remount);

        setYaokongBackground(iv_remount,this,"qita");
        if(Constant.netStatus){
            iv_remount.setImageResource(R.drawable.yaokongqi);
        }else{
            iv_remount.setImageResource(R.drawable.gimi_yaokong);
        }
        ImageView back = (ImageView) findViewById(R.id.title).findViewById(R.id.back);
        back(back);
        TextView title = (TextView) findViewById(R.id.tv_titile);
        title.setText(type_Title);

        listView = (ListView) findViewById(R.id.file_detail_listView);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String str = (String) fileMapGroup.get(position).get("id");

                String path = (String) fileMapGroup.get(position).get("filepath");

                String name=(String) fileMapGroup.get(position).get("name");

                long size = (Long) fileMapGroup.get(position).get("size");
                String packageName=(String) fileMapGroup.get(position).get("package");
                onSendFile(str, "" + size,name,packageName);
            }
        });
    }
    List< VcontrolCmd.CustomPlay.PlayList> mPlyLists=new ArrayList<>();

    private String type;
    // 传送文件命令,需要判断设备是否连接
    public void onSendFile(String id, String size,String name,String packageName) {
        if (Constant.netStatus) {
            if(ToosUtil.getInstance().isInstallTvControl(FileDetailTransferActivity.this) ){

                String sendCommand = "http://" + App.mPhoneIP + ":" + HttpServer.PORT + "/" + id /*+ UdpManager.separator + size*/;
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
}
