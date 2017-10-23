package com.xgimi.zhushou.yaokongqi;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.waxrain.droidsender.SenderService;
import com.xgimi.device.callback.GMDeviceReceiveListener;
import com.xgimi.device.device.FeedbackInfo;
import com.xgimi.device.device.GMDeviceConnector;
import com.xgimi.device.device.GMDeviceController;
import com.xgimi.device.device.GMDeviceStorage;
import com.xgimi.device.device.GMKeyCommand;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.utils.StringUtils;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.IntelligenceActivity;
import com.xgimi.zhushou.activity.NewSearchDeviceActivity;
import com.xgimi.zhushou.activity.TontPingQianActivity;
import com.xgimi.zhushou.adapter.BaiBaoAdatper;
import com.xgimi.zhushou.bean.BaiBao;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.netUtil.HttpRequest;
import com.xgimi.zhushou.popupwindow.ScreenShotPop;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;
import com.xgimi.zhushou.widget.FocusDilog;
import com.xgimi.zhushou.widget.SanDiDilog;
import com.xgimi.zhushou.widget.TurnOffDilog;
import com.xgimi.zhushou.widget.WifiAilplay;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BaiBaoFragment extends BaseFragment implements GMDeviceReceiveListener, SenderService.passwordLisener {

    private static final String TAG = "BaiBaoFragment";
    List<BaiBao> baibaos = new ArrayList<BaiBao>();
    private View rootview;
    private GridView gridview;
    private GMKeyCommand gmKeyCommand;
    public static final int SCREENSHOTSUCCESS = 101;
    private ScreenShotPop screenShotPop;// 截屏成功提示对话框
    private BaiBaoAdatper baiBaoAdatper;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (null != rootview) {
            ViewGroup parent = (ViewGroup) rootview.getParent();
            if (null != parent) {
                parent.removeView(rootview);
            }
        } else {
            rootview = inflater.inflate(R.layout.baibaofragment, null);
            initView(rootview);// 控件初始化
            initData();
        }
//		View view=View.inflate(getActivity(),R.layout.baibaofragment, null);
        return rootview;
    }

    @Override
    public void onResume() {
        if (GMDeviceStorage.getInstance().gmdevice != null) {
            if (GMDeviceStorage.getInstance().gmdevice.isFocusAble() && !baibaos.get(0).getName().equals("调焦")) {
                baibaos.add(0, new BaiBao("调焦", R.drawable.tiaojiao));
                XGIMILOG.D("加入调焦");
                baiBaoAdatper.notifyDataSetChanged();
            }
            if (!GMDeviceStorage.getInstance().gmdevice.isFocusAble() && baibaos.get(0).getName().equals("调焦")) {
                baibaos.remove(0);
                XGIMILOG.D("移除调焦");
                baiBaoAdatper.notifyDataSetChanged();
            }
            boolean contains = containsBaibao("无极变焦");
            if ("mangosteen".equals(GMDeviceStorage.getInstance().gmdevice.getDevicetype()) && contains) {
                removeBaibao("无极变焦");
                XGIMILOG.D("移除无极变焦");
                baiBaoAdatper.notifyDataSetChanged();
            } else if (!"mangosteen".equals(GMDeviceStorage.getInstance().gmdevice.getDevicetype()) && !contains) {
                insertAfter("图像模式");
                XGIMILOG.D("加入无极变焦");
                baiBaoAdatper.notifyDataSetChanged();
            }
        } else {
            XGIMILOG.D("GMDeviceStorage.getInstance().gmdevice == null");
        }
        super.onResume();
    }

    private boolean containsBaibao(String name) {
        boolean res = false;
        for (int i = 0; i < baibaos.size(); i++) {
            if (baibaos.get(i).getName().equals(name)) {
                res = true;
                break;
            }
        }
        return res;
    }

    private void removeBaibao(String name) {
        for (int i = 0; i < baibaos.size(); i++) {
            if (baibaos.get(i).getName().equals(name)) {
                baibaos.remove(i);
                break;
            }
        }
    }

    private void insertAfter(String name) {
        for (int i = 0; i < baibaos.size(); i++) {
            if (baibaos.get(i).getName().equals(name)) {
                baibaos.add(i + 1, new BaiBao("无极变焦", R.drawable.wuji));
                break;
            }
        }
    }


    private void initView(View view) {
        screenShotPop = new ScreenShotPop(getActivity(), window_height, width);
        gmKeyCommand = new GMKeyCommand();
//		String type = GMDeviceStorage.getInstance().gmdevice.type;

        if (GMDeviceStorage.getInstance().gmdevice != null && GMDeviceStorage.getInstance().gmdevice.isFocusAble()) {
            XGIMILOG.D("加入调焦");
            baibaos.add(new BaiBao("调焦", R.drawable.tiaojiao));
        } else {
            XGIMILOG.D("GMDeviceStorage.getInstance().gmdevice == null");
        }
        baibaos.add(new BaiBao("输入", R.drawable.shuru));
        baibaos.add(new BaiBao("截屏", R.drawable.jieping));
        baibaos.add(new BaiBao("3D设置", R.drawable.sandi));
        baibaos.add(new BaiBao("图像模式", R.drawable.tuxiang));
//		if (!GMDeviceStorage.getInstance().gmdevice.getDevicetype().equals("mangosteen")) {
//			baibaos.add(new BaiBao("无极变焦", R.drawable.wuji));
//		}
        baibaos.add(new BaiBao("内存清理", R.drawable.neicun));
        baibaos.add(new BaiBao("同屏", R.drawable.tongping));
        baibaos.add(new BaiBao("连接管理", R.drawable.lianjie));
        baibaos.add(new BaiBao("关机选项", R.drawable.guanji));
        gridview = (GridView) view.findViewById(R.id.baibaogridview);
        baiBaoAdatper = new BaiBaoAdatper(getActivity(), (ArrayList<BaiBao>) baibaos);
        GMDeviceConnector.getInstance().setGmRecivceListener(this);
        gridview.setAdapter(baiBaoAdatper);
        SenderService.setLisener(this);
    }

    private void initData() {
        gridview.setOnItemClickListener(new OnItemClickListener() {
            private BaiBao item;

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                item = baiBaoAdatper.getItem(position);
                if (item.getName().equals("调焦")) {
                    FocusDilog focusDilog = new FocusDilog(getActivity(), "b");
                    focusDilog.show();
                } else if (item.getName().equals("输入")) {
                    FocusDilog focusDilog = new FocusDilog(getActivity(), "shurukuang");
                    focusDilog.show();
                } else if (item.getName().equals("截屏")) {
                    VcontrolControl.getInstance().sendNewCommand(
                            VcontrolControl.getInstance().getConnectControl(
                                    new VcontrolCmd(20000, "2", GMSdkCheck.appId, null,
                                            null, new VcontrolCmd.ControlCmd(9, 1, null), null, null)
                            )
                    );
//					GMDeviceController.getInstance().SendJC(Constant.SCREENSHOT_COM);
                    ToosUtil.getInstance().addEventUmeng(getActivity(), "event_device_screenshoot");
                } else if (item.getName().equals("3D设置")) {
                    SanDiDilog sanDiDilog = new SanDiDilog(getActivity(), "a");
                    ToosUtil.getInstance().addEventUmeng(getActivity(), "event_3d_touch");
                    sanDiDilog.show();
                } else if (item.getName().equals("图像模式")) {
                    SanDiDilog sanDiDilog = new SanDiDilog(getActivity(), "tuxiang");
                    ToosUtil.getInstance().addEventUmeng(getActivity(), "event_device_imagemode");
                    sanDiDilog.show();
                } else if (item.getName().equals("无极变焦")) {
                    FocusDilog focusDilog = new FocusDilog(getActivity(), "wuji");
                    ToosUtil.getInstance().addEventUmeng(getActivity(), "event_device_noextremfocus");
                    focusDilog.show();
                } else if (item.getName().equals("内存清理")) {
                    VcontrolControl.getInstance().sendNewCommand(
                            VcontrolControl.getInstance().getConnectControl(
                                    new VcontrolCmd(20000, "2", GMSdkCheck.appId, null,
                                            null, new VcontrolCmd.ControlCmd(9, 2, null), null, null)
                            )
                    );
//					GMDeviceController.getInstance().SendJC(Constant.CLEARMEMORY_COM);
                    ToosUtil.getInstance().addEventUmeng(getActivity(), "event_device_clearmemory");
                } else if (item.getName().equals("同屏")) {
                    ToosUtil.getInstance().addEventUmeng(getActivity(), "event_device_onescreen");
                    if (GMDeviceStorage.getInstance().getConnectedDevice() == null
                            || GMDeviceStorage.getInstance().getConnectedDevice().ip == null) {
                        return;
                    } else {
                        Intent intent = new Intent(getActivity(), TontPingQianActivity.class);
                        startActivity(intent);
                    }
                } else if (item.getName().equals("连接管理")) {
                    Intent intent = new Intent(getActivity(), NewSearchDeviceActivity.class);
                    intent.putExtra("laiyuan", "laiyuan");
                    startActivity(intent);
                } else if (item.getName().equals("智能家居")) {
                    Intent intent = new Intent(getActivity(), IntelligenceActivity.class);
                    startActivity(intent);
                } else if (item.getName().equals("关机选项")) {
                    TurnOffDilog turnoff = new TurnOffDilog(getActivity(), "", mHandler);
                    ToosUtil.getInstance().addEventUmeng(getActivity(), "event_device_shutdown");
                    turnoff.show();
                }
            }
        });
    }

    private void updateGallery(String filename)//filename是我们的文件全名，包括后缀哦
    {
        MediaScannerConnection.scanFile(getContext(),
                new String[]{filename}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        XGIMILOG.E(path);
                        XGIMILOG.E(uri.toString());
                    }
                });
    }

    private void updataMedia(String filename) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)//版本号的判断  4.4为分水岭，发送广播更新媒体库
//        {
//
//        } else {
//            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.fromFile(mRecorderView.getFilePath())));
//        }

        XGIMILOG.E("开始更新媒体库 : " + filename);
        MediaScannerConnection.scanFile(getContext(), new String[]{filename}, null, new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String path, Uri uri) {
                XGIMILOG.E(path + "----------------");
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(uri);
                getContext().sendBroadcast(mediaScanIntent);
            }
        });

    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SCREENSHOTSUCCESS:
                    String path = (String) msg.obj;
                    File file = new File(path);
                    XGIMILOG.E("截图保存路径 : " + path + ", " + file.getParentFile().getAbsolutePath());
//                    updateGallery(file.getParentFile().getAbsolutePath());
                    updataMedia(file.getAbsolutePath());

//                    getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
//                    getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
//                            + Environment.getExternalStorageDirectory())));
                    if (!StringUtils.isEmpty(path)) {
//					ImageLoaderUtils.addForWatermark(ImageUtils.getBitmapFromResources(getActivity(), R.drawable.xgimiwatermark), path);
                        screenShotPop.addImage(path);
                        if (!screenShotPop.isShowing()) {
                            screenShotPop.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
                        }
                    }
                    break;
                case 20:
                    gmKeyCommand.setKey(GMKeyCommand.GMKeyEventFocusTurnOFF);
                    GMDeviceController.getInstance().sendCommand(gmKeyCommand);
                    break;
                case 100:
                    gmKeyCommand.setKey(GMKeyCommand.GMKeyEventFocusTurnOFF);
                    GMDeviceController.getInstance().sendCommand(gmKeyCommand);
                    break;
            }
        }
    };

    @Override
    public void receive(FeedbackInfo arg0) {
        // TODO Auto-generated method stub
        String image_url = arg0.imagePath;
        if (GMDeviceStorage.getInstance().getConnectedDevice() != null && GMDeviceStorage.getInstance().getConnectedDevice().getIp() != null) {
            image_url = String.format(image_url, GMDeviceStorage.getInstance().getConnectedDevice().ip);
            XGIMILOG.E("screenShot url : " + image_url);
            HttpRequest.getInstance(getActivity()).downloadImage(mHandler, image_url);
        }
    }

    public String sendTurnOff() {
        JSONObject objecet = new JSONObject();
        JSONObject js = new JSONObject();
        try {
            js.put("type", 0);
            objecet.put("data", js);
            objecet.put("action", 6);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return objecet.toString();
    }


    @Override
    public void path(int path) {
        if (path == 1) {
            WifiAilplay dilog = new WifiAilplay(getActivity(), "");
            dilog.show();
        }
    }
}
