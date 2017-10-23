package com.xgimi.zhushou.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xgimi.device.device.FeedbackInfo;
import com.xgimi.device.device.HttpServer;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.socket.UdpManager;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.PictureimageGridAdapter;
import com.xgimi.zhushou.bean.ImageInfo;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.GlobalConsts;
import com.xgimi.zhushou.util.StringUtils;
import com.xgimi.zhushou.util.ToosUtil;

import java.io.File;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/12/2.
 */
public class PictureGridViewActivity extends BaseActivity implements View.OnTouchListener,View.OnClickListener{
    private ImageView back;
    private TextView title;
    private ImageView iv_remount;
    private GridView gridView;
    private List<ImageInfo> childPathList;
    private RelativeLayout tishi;
    private TextView downName;
    private TextView downProgress;
    //打钩选择的图片就保存在这个集合里面（按点击的先后顺序）
    private List<ImageInfo> mImages=new ArrayList<>(); // 图片信息
    private List<ImageInfo> mImages1=new ArrayList<>(); // 图片信息
    private RelativeLayout rl_chuanshu;
    private String path = null;
    private PictureimageGridAdapter adapter;
    //这个boolean类型是解决下载应用时，这边会收到进度从而弹出受影响
    private boolean isClick;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_grid);
        initView();
        initListener();
    }
    private void initView() {
        EventBus.getDefault().register(this);
        back= (ImageView) findViewById(R.id.title).findViewById(R.id.back);
        title= (TextView) findViewById(R.id.title).findViewById(R.id.tv_titile);
        tishi= (RelativeLayout) findViewById(R.id.tishi);
        tishi.setVisibility(View.GONE);
        downName= (TextView) findViewById(R.id.downName);
        downProgress= (TextView) findViewById(R.id.downProgress);
        back(back);
        playLists = new ArrayList<>();
        title.setText("图片");
        iv_remount = (ImageView) findViewById(R.id.title).findViewById(R.id.iv_remount);
        setYaokongBackground(iv_remount,this,"qita");
        iv_remount.setOnTouchListener(this);
        if(Constant.netStatus){
            iv_remount.setImageResource(R.drawable.yaokongqi);
        }else{
            iv_remount.setImageResource(R.drawable.gimi_yaokong);
        }
//        mImages.clear();
        position = getIntent().getIntExtra("position", 0);
        path = GlobalConsts.mImgKeyList.get(position);
        childPathList = GlobalConsts.mImgMap.get(path);
        for(int i=0;i<childPathList.size();i++){
            childPathList.get(i).setType(0);
        }
        for(int j=0;j<childPathList.size();j++){
            Log.e("tupian",childPathList.get(j).getTime()+childPathList.get(j).url);
        }
        gridView = (GridView) findViewById(R.id.local_photogridView);
        adapter=new PictureimageGridAdapter(this, childPathList);
        gridView.setAdapter(adapter);
        write();
        rl_chuanshu= (RelativeLayout) findViewById(R.id.rl_chuanshu);
        rl_chuanshu.setOnClickListener(this);
    }

    private void initListener(){
        //有没得打钩的，打钩一个传一个过来
        adapter.setLisener(new PictureimageGridAdapter.getIds() {
            @Override
            public void getMenmberIds(Map<Integer, ImageInfo> beans, Map<Integer, ImageInfo> cleanBeen, boolean isAdd) {
//                mImages=addImage(beans);

                if(isAdd){
                    addImage(cleanBeen);
                    mImages.addAll(image);
                }else {
                    deleteImage(cleanBeen);
                    for(int i = 0; i< mImages.size(); i++){
                        if((deletimage.get(0).getId())==(mImages.get(i).getId())){
                            mImages.remove(i);
                        }
                    }
                }
                cleanBeen.clear();
            }
        });
    }
    //遍历map集合，。取出其中的人名和id放入poeples中，shijiPoeples中存放被选中的人的个数,deletepoeples存放要删除的那一个数据
    private List<ImageInfo> image=new ArrayList<>();
    private List<ImageInfo> deletimage=new ArrayList<>();
    private Map<Integer, ImageInfo> shijiimage;
    //打钩时的正常遍历添加
    private List<ImageInfo> addImage(Map<Integer, ImageInfo> beans){
        image.clear();
        Iterator iter = beans.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int) entry.getKey();
            ImageInfo val = (ImageInfo) entry.getValue();
            image.add(new ImageInfo(val.getId(),val.getUrl()));
        }
        return image;
    }
    //取消打钩的遍历，从集合中找到他并删除他个狗日的，妈卖批
    private List<ImageInfo> deleteImage(Map<Integer, ImageInfo> beans){
        deletimage.clear();
        Iterator iter = beans.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            int key = (int) entry.getKey();
            ImageInfo val = (ImageInfo) entry.getValue();
            deletimage.add(new ImageInfo(val.getId(),val.getUrl()));
        }
        return deletimage;
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
    private ArrayList<VcontrolCmd.CustomPlay.PlayList> playLists;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //立即传输按钮点击事件
            case R.id.rl_chuanshu:
                mImages1=mImages;
                Log.e("mImagessize","mImages的size"+mImages.size()+"");
                Log.e("mImagessize","mImages1的size"+mImages1.size()+"");
                if(mImages1.size()>0){
                    isClick=true;
                    Toast.makeText(this,"准备中",Toast.LENGTH_SHORT).show();
                    onSendBtn(mImages1);
                }
                break;

        }
    }
    // 传送文件命令
    public void onSendBtn(List<ImageInfo> mImages) {
//        showDilog("传输中...");
        ToosUtil.getInstance().addEventUmeng(PictureGridViewActivity.this,"event_file_send_image");
        ToosUtil.getInstance().addEventUmeng(PictureGridViewActivity.this,"event_file_send");
        for(int i=0;i<mImages.size();i++){
            String id = GlobalConsts.IMAGE_PREFIX +mImages.get(i).getId();
            String title = mImages.get(i).getUrl();
            String sendCommand = "http://" + App.mPhoneIP + ":" + HttpServer.PORT + "/" + id + UdpManager.separator + title;
            //GMDeviceController.getInstance().SendJC(sendCommand);
            VcontrolCmd.CustomPlay.PlayList playList=new VcontrolCmd.CustomPlay.PlayList("."+getHouZhui(title),null,null,sendCommand,getFileName(title),null);
            playLists.add(playList);
            VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200,
                    "2", GMSdkCheck.appId, null, new VcontrolCmd.CustomPlay(22, 0, null, playLists, 0), null, null, null)));
        }
    }
    private FeedbackInfo myInfo;
    public void onEventMainThread(FeedbackInfo info) {
        if(isClick){
            rl_chuanshu.setVisibility(View.GONE);
            tishi.setVisibility(View.VISIBLE);
            myInfo=info;
            if(myInfo!=null&&myInfo.downloadInfo!=null){
                Message message = mhanHandler.obtainMessage();
                message.obj = myInfo;
                message.what = 0;
                mhanHandler.sendMessage(message);
            }
        }
    }
    private int position=0;
    //这个boolean类型是解决传几张图片的时候一直弹toast的bug。不科学。但可以骗产品经理
    private boolean isTanchu;
    Handler mhanHandler=new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            if(downProgress!=null&&downName!=null&&isClick) {
                FeedbackInfo info = (FeedbackInfo) msg.obj;
                if (info.downloadInfo!= null) {
                    rl_chuanshu.setVisibility(View.GONE);

                    downProgress.setText(info.downloadInfo.progress+"%");
                    downName.setText(info.downloadInfo.filename);
                    tishi.setVisibility(View.VISIBLE);
                    if(info.downloadInfo.progress==100){
                        position++;
                    }
                    if(mImages1.size()==position&&isClick){
                        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(GMSdkCheck.appId,
                                "2",new VcontrolCmd.ControlCmd(30,0,0),20000)));
                        MissDilog();
                        App.getContext().destoryActivity("PictureGridViewActivity");
                        finish();
                        if(!isTanchu){
                            isTanchu=true;
                            Toast.makeText(PictureGridViewActivity.this,"传输完成",Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        }
    };

    //从url中获取图片的文件名
    public String getFileName(String pathandname){
        int start=pathandname.lastIndexOf("/");
        int end=pathandname.lastIndexOf(".");
        if(start!=-1 && end!=-1){
            return pathandname.substring(start+1,end);
        }else{
            return null;
        }
    }
    //从url中获取图片后缀名
    public String getHouZhui(String url){
        String prefix=url.substring(url.lastIndexOf(".")+1);
        return prefix;
    }

    /**
     * 按时间排序类
     */
    private void filePaixu(){
        //            Collections.sort(info.filespath, new FileComparator());//通过重写Comparator的实现类FileComparator来实现按文件创建时间排序。
        Collections.sort(childPathList, new Comparator<ImageInfo>() {
            /**
             *
             * @param lhs
             * @param rhs
             * @return an integer < 0 if lhs is less than rhs, 0 if they are
             *         equal, and > 0 if lhs is greater than rhs,比较数据大小时,这里比的是时间
             */
            @Override
            public int compare(ImageInfo lhs,ImageInfo rhs) {
                Date date1 = DateUtil.stringToDate(lhs.getTime());
                Date date2 = DateUtil.stringToDate(rhs.getTime());
                // 对日期字段进行升序before，如果欲降序可采用after方法
                if (date1.before(date2)) {
                    return 1;
                } else if (date1.after(date2)) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
//        for(int j=0;j<childPathList.size();j++){
//            Log.e("tupianxian",childPathList.get(j).getTime()+childPathList.get(j).url);
//        }
        adapter.dataChange(childPathList);
    }

    /**
     * 将获得的数据写入自己的类
     */
    public void write(){
        for (int i=0;i<childPathList.size();i++) {
            String filePath = childPathList.get(i).url;
            File file = new File(filePath);
            long time = 0;
            if (file.exists()) {
                time = file.lastModified();
            }
            childPathList.get(i).setTime(StringUtils.getfullTime(time));
//            Log.e("shijian"+i+"",StringUtils.getfullTime(time)+file.getName());
        }
        filePaixu();
    }
    public static class DateUtil {

        public static Date stringToDate(String dateString) {
            ParsePosition position = new ParsePosition(0);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateValue = simpleDateFormat.parse(dateString, position);
            return dateValue;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
