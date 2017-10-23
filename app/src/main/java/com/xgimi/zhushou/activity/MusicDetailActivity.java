package com.xgimi.zhushou.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.music.model.Music;
import com.baidu.music.onlinedata.PlayinglistManager;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.MusicAdapter;
import com.xgimi.zhushou.fragment.musicfragment.BaiDuMusicList;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.DeviceUtils;
import com.xgimi.zhushou.util.GaoSi;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.SaveTVApp;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;
import com.xgimi.zhushou.widget.MyListview;
import com.xgimi.zhushou.widget.yuanxing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MusicDetailActivity extends BaseActivity implements
        PlayinglistManager.OnPlayListListener, PlayinglistManager.OnLoadMusicListListener {

    private String mType;
    List<Music> musics = new ArrayList<Music>();
    private MusicAdapter musicAdapter1;
    private int gezhong = -1;
    private yuanxing touxinag;
    private int index = -1;
    private String geName;
    private ImageView back;
    private String id;
    private String image;
    private String guanggaoittle;
    private ScrollView scrollview;
    private int height1;
    private List<Music> mData;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @SuppressWarnings("deprecation")
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    // if(!app.music.equals("")){
                    // Drawable drawable = Drawable.createFromPath(app.music);
                    // if(drawable!=null)
                    // gaosi.setBackgroundDrawable(new
                    // BitmapDrawable(GaoSi.doBlur(drawableToBitmap(drawable), 20,
                    // false)));
                    // }
                    Bitmap bitmap = (Bitmap) msg.obj;
                    gaosi.setBackgroundDrawable(new BitmapDrawable(GaoSi.doBlur(
                            bitmap, 30, false)));
//				touxinag.setImageBitmap(resizeImage(bitmap,100,100));
//				touxinag.setImageBitmap(bitmap);
                    break;
                case 2:
                    break;

                default:
                    break;
            }
        }
    };
    private RelativeLayout gaosi;
    private MyListview myListview;
    private RelativeLayout rl;
    private ImageView iv_remount;
    private FrameLayout dibuMusic;
    private ImageView iv_more;
    private LinearLayout ll_music;
    private LinearLayout ll_music_detail;
    private ImageView suiji;
    private TextView musicName;
    private TextView musicSinger;
    private ImageView musicPause;
    private ImageView muscinNext;
    private yuanxing touxiang;
    private int height0;
    private List<VcontrolCmd.CustomPlay.PlayList> mPlayLists;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_detail);
        initExras();
        BaiDuMusicList.getInstance().getBangDanDetail(this, this, this, mType);
        initView();
        initData();
    }

    private void initExras() {
        Intent intent = getIntent();
        if (intent != null) {
            mType = intent.getStringExtra("leixing");
            gezhong = intent.getIntExtra("class", -1);
            index = intent.getIntExtra("index", -1);
            geName = intent.getStringExtra("geName");
            id = intent.getStringExtra("id");
            image = intent.getStringExtra("image");
            guanggaoittle = intent.getStringExtra("title");
        }
    }

    @SuppressWarnings("deprecation")
    private void initView() {
        scrollview = (ScrollView) findViewById(R.id.scrollview);

        rl = (RelativeLayout) findViewById(R.id.rl);
        // 透明状态栏
        controlTitle(findViewById(R.id.id_toolbar), true, true, false, false);
        tv.setText(geName);
        myListview = (MyListview) findViewById(R.id.listview);
        myListview.setFocusable(false);
        myListview.setFocusableInTouchMode(false);
        musicAdapter1 = new MusicAdapter(this, musics, false);
        gaosi = (RelativeLayout) findViewById(R.id.rl_gaosi);
        myListview.setAdapter(musicAdapter1);
        touxinag = (yuanxing) findViewById(R.id.touxiang);

        mPlayLists = new ArrayList<>();
//        List< VcontrolCmd.CustomPlay.PlayList> mPlyLists=new ArrayList<>();

        myListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                GMDeviceController.getInstance().SendJC(TvJson.getInstance().songJson(musics,position));
                if (Constant.netStatus) {
                    try {
                        if (ToosUtil.getInstance().isInstallTvControl(MusicDetailActivity.this)) {
                            if (mPlayLists.size() > 0) {
                                Log.e("yinyuetongji", mData.get(position).mTitle + "--" + mData.get(position).mId + "--" + geName);
                                VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200, "2",
                                        GMSdkCheck.appId, DeviceUtils.getappVersion(MusicDetailActivity.this), App.getContext().PACKAGENAME, new VcontrolCmd.ThirdPlay(mData.get(position).mTitle, mData.get(position).mId, 0, geName,
                                        App.getContext().BAIDUYINYUE),
                                        new VcontrolCmd.CustomPlay(1, 0, null, mPlayLists, position),
                                        null, null, null)));
                                Intent intent = new Intent(MusicDetailActivity.this, RadioBoFangActivity.class);
                                intent.putExtra("type", "2");
                                intent.putExtra("index", index + "");
                                intent.putExtra("geName", geName);
                                intent.putExtra("title", mPlayLists.get(position).title);
                                intent.putExtra("singer", mPlayLists.get(position).singer);
                                SaveData.getInstance().position = position;
                                if (SaveData.getInstance().mRadioShow != null) {
                                    SaveData.getInstance().mRadioShow.clear();
                                    SaveData.getInstance().mRadioShow = null;
                                }
                                ToosUtil.getInstance().addEventUmeng(MusicDetailActivity.this, "event_music_list_play");
                                SaveData.getInstance().fenlei = "2";
                                SaveData.getInstance().index = index + "";
                                SaveData.getInstance().geName = geName;
                                startActivity(intent);
                            }
                        } else {
                            ToosUtil.getInstance().isConnectTv(MusicDetailActivity.this);
                        }
                    } catch (Exception e) {

                    }
                }
            }

        });
        ImageLoader.getInstance().displayImage(
                SaveTVApp.getInstance(this).BangDanTubiao[index],
                touxinag, new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String arg0, View arg1) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onLoadingFailed(String arg0, View arg1,
                                                FailReason arg2) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onLoadingComplete(String arg0, View arg1,
                                                  Bitmap arg2) {
                        // TODO Auto-generated method stub
                        Message message = handler.obtainMessage();
                        message.obj = resizeImage(arg2, 100, 100);
                        message.what = 0;

                        handler.sendMessage(message);
                    }

                    @Override
                    public void onLoadingCancelled(String arg0, View arg1) {
                        // TODO Auto-generated method stub

                    }
                });
    }

    private void initData() {

    }

    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
        return resizedBitmap;
    }


    private boolean isup;

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public void onLoadMusicList(List<Music> arg0) {
        // TODO Auto-generated method stub
        if (arg0 != null && arg0.size() > 0) {
            // ApplyTitleDanLi.getInstance().musicsList=arg0;
            musics = arg0;
            XGIMILOG.E("music size = " + musics.size());
            for (int i = 0; i < arg0.size(); i++) {
                VcontrolCmd.CustomPlay.PlayList playList = new VcontrolCmd.CustomPlay.PlayList(null, arg0.get(i).mId,
                        null, arg0.get(i).mTitle, arg0.get(i).mArtist, null, null, 0, geName,
                        App.getContext().BAIDUYINYUE);
                mPlayLists.add(playList);
                SaveData.getInstance().mPlayLists = mPlayLists;
            }
            mData = arg0;
            musicAdapter1.dataChange(arg0);
        }
    }

    @Override
    public void onPlayError(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayInfoChanged() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPlayListChanged() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayListEnd() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayStatusChanged() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPlayerPrepared() {
        // TODO Auto-generated method stub
    }


    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(

                drawable.getIntrinsicWidth(),

                drawable.getIntrinsicHeight(),

                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        // canvas.setBitmap(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;

    }


    private boolean vistivis;
    private View view;

    private TextView tv_size;

    public String sendJson(int postion) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject js2 = new JSONObject();
        try {
            for (int i = 0; i < musics.size(); i++) {
                JSONObject jsobject3 = new JSONObject();
                Music music = musics.get(i);
                jsobject3.put("id", music.mId);
                jsobject3.put("title", music.mTitle);
                jsobject3.put("singer", music.mArtist);
                jsobject3.put("url", null);
                jsonArray.put(jsobject3);
            }
            jsonObject.put("type", 0);
            jsonObject.put("pos", postion);
            jsonObject.put("playlist", jsonArray);
            js2.put("data", jsonObject);
            js2.put("action", 10);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return js2.toString();
    }

    // 发送json数据给投影一
    public String playJson(int mode) {
        JSONObject jsonObject = new JSONObject();
        JSONObject js2 = new JSONObject();
        try {
            jsonObject.put("type", mode);
            js2.put("data", jsonObject);
            js2.put("action", 10);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(js2.toString());
        return js2.toString();
    }
}

