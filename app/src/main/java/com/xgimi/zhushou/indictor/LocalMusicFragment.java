package com.xgimi.zhushou.indictor;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.music.model.Music;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.PhoneZiYuanActivity;
import com.xgimi.zhushou.activity.RemountActivity;
import com.xgimi.zhushou.activity.NewSearchDeviceActivity;
import com.xgimi.zhushou.adapter.GuangGaoAdapter;
import com.xgimi.zhushou.adapter.Local_Music_adapter;
import com.xgimi.zhushou.adapter.MusicAdapter;
import com.xgimi.zhushou.bean.ApplyTitleDanLi;
import com.xgimi.zhushou.bean.CodeMsg;
import com.xgimi.zhushou.bean.GuanGaoList;
import com.xgimi.zhushou.bean.Mp3Info;
import com.xgimi.zhushou.bean.Update;
import com.xgimi.zhushou.bean.Vctrolbean;
import com.xgimi.zhushou.bean.XgimiBean;
import com.xgimi.zhushou.db.RecordDao;
import com.xgimi.zhushou.fragment.BaseFragment;
import com.xgimi.zhushou.netUtil.HttpServer;
import com.xgimi.zhushou.netUtil.NetUtil;
import com.xgimi.zhushou.popupwindow.LocalMusicPop;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.GlobalConsts;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.StringUtils;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;
import com.xgimi.zhushou.widget.MyEditText;
import com.xgimi.zhushou.widget.SignOutDilog;
import com.xgimi.zhushou.widget.yuanxing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

public class LocalMusicFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private ListView listViewMusic;

    private Local_Music_adapter localAdapter;
    private int postion;
    private int height;
    private TextView musicName;

    private TextView musicSinger;

    private ImageView musicPause;

    private ImageView muscinNext;

    private yuanxing touxiang;
    private ImageView iv_more;
    private PhoneZiYuanActivity phongzhiyuan;
    private RecordDao dao;
    private MyEditText EditText;
    private View music_search;
    private List<Mp3Info> mData = new ArrayList<>();
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageView image_pop;
    private LocalMusicPop menuWindow;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.localmusic, null);
        initView(view);
        phongzhiyuan = (PhoneZiYuanActivity) getActivity();
        initData();
        dao = new RecordDao(getActivity());
        //	EventBus.getDefault().register(this);
        return view;
    }


    private void initView(View view) {
        EventBus.getDefault().register(this);
        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.rl);
        rl.setVisibility(View.GONE);
        FrameLayout framelayout = (FrameLayout) view
                .findViewById(R.id.dibu_music);
//		framelayout.setOnTouchListener(this);
        listViewMusic = (ListView) view.findViewById(R.id.listview_music);
//		pinyinComparator = new PinyinComparator();
//		Collections.sort(GlobalConsts.mp3Infos, pinyinComparator);
        localAdapter = new Local_Music_adapter(getActivity(), GlobalConsts.mp3Infos, false);
        listViewMusic.setAdapter(localAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.pull_to_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        WindowManager wm = getActivity().getWindowManager();
        ll_music = (LinearLayout) view.findViewById(R.id.ll_music);
        ll_music_detail = (LinearLayout) view
                .findViewById(R.id.ll_music_detail);
        musicName = (TextView) view.findViewById(R.id.music_name);
        musicSinger = (TextView) view.findViewById(R.id.music_singer);
        iv_more = (ImageView) view.findViewById(R.id.iv_more);
        musicPause = (ImageView) view.findViewById(R.id.music_pause);
        muscinNext = (ImageView) view.findViewById(R.id.music_next);
        touxiang = (yuanxing) view.findViewById(R.id.leftmenu_heah);
        LayoutParams layoutParams = ll_music.getLayoutParams();
        height = layoutParams.height;
        LayoutParams layoutParams2 = ll_music_detail.getLayoutParams();
        height1 = layoutParams2.height;
        ImageView deleter = (ImageView) view.findViewById(R.id.delete);
        suiji = (ImageView) view.findViewById(R.id.suiji);
        ImageView liebiao = (ImageView) view.findViewById(R.id.iv_liebiao);
        music_search = view.findViewById(R.id.music_search);
        EditText = (MyEditText) view.findViewById(R.id.music_search).findViewById(R.id.search);
        image_pop = (ImageView) view.findViewById(R.id.pop);
        daoXu();
    }

    private List<VcontrolCmd.CustomPlay.PlayList> mPlayLists = new ArrayList<>();

    private void initData() {
        // if(GlobalConsts.mp3Infos!=null){
        // Mp3Info mp3Info = GlobalConsts.mp3Infos.get(0);
        // musicName.setText(mp3Info.getTitle());
        // musicSinger.setText(mp3Info.getArtist());
        // }
        listViewMusic.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (Constant.netStatus && GlobalConsts.mp3Infos != null) {
                    int mpos = 0;
                    ToosUtil.getInstance().addEventUmeng(getActivity(), "event_local_music");
                    ApplyTitleDanLi.getInstance().mp3s.clear();
                    ApplyTitleDanLi.getInstance().guangList.clear();
                    ApplyTitleDanLi.getInstance().musicsList.clear();
                    if (GlobalConsts.mp3Infos.size() < 21) {
                        ApplyTitleDanLi.getInstance().mp3s
                                .addAll(GlobalConsts.mp3Infos);
                        mpos = position;
//						EventBus.getDefault().post(
//								GlobalConsts.mp3Infos.get(mpos));
//						GMDeviceController.getInstance().SendJC(sendJson(mpos));
                        ApplyTitleDanLi.getInstance().musicPostion = mpos;
                        sendJson(mpos);
                        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200, "2",
                                GMSdkCheck.appId, null,
                                new VcontrolCmd.CustomPlay(1, 0, null, mPlayLists, mpos),
                                null, null, null)));
                        SaveData.getInstance().mPlayLists = mPlayLists;
                        SaveData.getInstance().position = mpos;
                        if (SaveData.getInstance().mRadioShow != null) {
                            SaveData.getInstance().mRadioShow.clear();
                            SaveData.getInstance().mRadioShow = null;
                        }
                        SaveData.getInstance().fenlei = "2";
                        SaveData.getInstance().index = mpos + "";
                        Intent intent = new Intent(getContext(), RemountActivity.class);
                        getContext().startActivity(intent);
                    } else if (position + 21 > GlobalConsts.mp3Infos.size()) {
                        for (int i = position; i < GlobalConsts.mp3Infos.size(); i++) {
                            ApplyTitleDanLi.getInstance().mp3s
                                    .add(GlobalConsts.mp3Infos.get(i));
                        }
                        for (int i = 0; i < (position + 21)
                                - GlobalConsts.mp3Infos.size(); i++) {
                            ApplyTitleDanLi.getInstance().mp3s
                                    .add(GlobalConsts.mp3Infos.get(i));
                        }
                        mpos = 0;
                        sendJson(mpos);
                        ApplyTitleDanLi.getInstance().musicPostion = mpos;
                        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200, "2",
                                GMSdkCheck.appId, null,
                                new VcontrolCmd.CustomPlay(1, 0, null, mPlayLists, mpos),
                                null, null, null)));
                        SaveData.getInstance().mPlayLists = mPlayLists;
                        SaveData.getInstance().position = mpos;
                        if (SaveData.getInstance().mRadioShow != null) {
                            SaveData.getInstance().mRadioShow.clear();
                            SaveData.getInstance().mRadioShow = null;
                        }
                        SaveData.getInstance().fenlei = "2";
                        SaveData.getInstance().index = mpos + "";
                        Intent intent = new Intent(getContext(), RemountActivity.class);
                        getContext().startActivity(intent);
                    } else {
                        for (int i = position; i < GlobalConsts.mp3Infos.size(); i++) {
                            if (ApplyTitleDanLi.getInstance().mp3s.size() > 20) {
                                mpos = 0;
                                ApplyTitleDanLi.getInstance().musicPostion = 0;
                                EventBus.getDefault().post(
                                        GlobalConsts.mp3Infos.get(0));
                                sendJson(mpos);
                                VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200, "2",
                                        GMSdkCheck.appId, null,
                                        new VcontrolCmd.CustomPlay(1, 0, null, mPlayLists, mpos),
                                        null, null, null)));
                                SaveData.getInstance().mPlayLists = mPlayLists;
                                SaveData.getInstance().position = mpos;
                                if (SaveData.getInstance().mRadioShow != null) {
                                    SaveData.getInstance().mRadioShow.clear();
                                    SaveData.getInstance().mRadioShow = null;
                                }
                                SaveData.getInstance().fenlei = "2";
                                SaveData.getInstance().index = mpos + "";
                                Intent intent = new Intent(getContext(), RemountActivity.class);
                                getContext().startActivity(intent);
                                return;
                            }
                            ApplyTitleDanLi.getInstance().mp3s.add(GlobalConsts.mp3Infos.get(i));
                        }
                    }


                } else {
                    SignOutDilog singDilog = new SignOutDilog(getActivity(),
                            "是否现在连接无屏电视");
                    singDilog.show();
                    singDilog.setOnLisener(new SignOutDilog.onListern() {

                        @Override
                        public void send() {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(getActivity(),
                                    NewSearchDeviceActivity.class);
                            startActivity(intent);

                        }
                    });
                }
            }
        });
        EditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        /* 隐藏软键盘 */
						/* 隐藏软键盘 */

                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (inputMethodManager.isActive()) {
                            inputMethodManager.hideSoftInputFromWindow(
                                    v.getApplicationWindowToken(), 0);
                        }
                        if (EditText.getText().toString().trim().length() > 0) {
                            mData.clear();
                            String str = EditText.getText().toString().trim();
                            getmDataSub(str);
                        }
                    }
                }
                return false;
            }
        });
        EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (EditText.getText().toString().trim().length() == 0) {
                    localAdapter.dataChange(GlobalConsts.mp3Infos);
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager.isActive()) {
                        inputMethodManager.hideSoftInputFromWindow(
                                music_search.getApplicationWindowToken(), 0);
                    }
                }
            }
        });
//		image_pop.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				menuWindow =new LocalMusicPop(getActivity());
//			//	menuWindow.showAsDropDown(image_pop);
//				menuWindow.showAtLocation(getActivity().findViewById(R.id.pop), Gravity.BOTTOM, 0, 0); //设置layout在PopupWindow中显示的位置
//				//Toast.makeText(getActivity(),"傻逼",Toast.LENGTH_SHORT).show();
//			}
//		});
    }

    /**
     * 时间倒序排序
     */
    private void daoXu() {

        if (GlobalConsts.mp3Infos != null) {
            System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
            Collections.sort(GlobalConsts.mp3Infos, new FileComparator());//通过重写Comparator的实现类FileComparator来实现按文件创建时间排序。
        }
        localAdapter.dataChange(GlobalConsts.mp3Infos);
    }

    /**
     * 按时间排序EVenbus
     */
    public void onEventMainThread(Update update) {
        if (GlobalConsts.mp3Infos != null) {
            Collections.sort(GlobalConsts.mp3Infos, new FileComparator());//通过重写Comparator的实现类FileComparator来实现按文件创建时间排序。
        }
        localAdapter.dataChange(GlobalConsts.mp3Infos);
    }

    /**
     * 默认排序
     */
    public void onEventMainThread(Vctrolbean vctrolbean) {

        if (SaveData.getInstance().formerData != null) {
            localAdapter.dataChange(SaveData.getInstance().formerData);
        }
    }

    /**
     * 按字母排序
     */
    public void onEventMainThread(XgimiBean xgimiBean) {
        if (GlobalConsts.mp3Infos != null) {
            Collections.sort(GlobalConsts.mp3Infos, new PinyinComparator());//通过重写Comparator的实现类FileComparator来实现按文件创建时间排序。
        }
        localAdapter.dataChange(GlobalConsts.mp3Infos);
    }

    public static HashMap<Integer, Boolean> mHasMaps = new HashMap<>();

    private void getmDataSub(String data) {
        for (int i = 0; i < GlobalConsts.mp3Infos.size(); i++) {
            if (GlobalConsts.mp3Infos.get(i).getTitle().contains(data) || GlobalConsts.mp3Infos.get(i).getArtist().contains(data)) {
                mData.add(GlobalConsts.mp3Infos.get(i));
            }
        }
        localAdapter.dataChange(mData);
    }

    @Override
    public void onRefresh() {
        App.getContext().findMusic();
    }

    public void onEventMainThread(Mp3Info mp3Info) {
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        daoXu();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 按时间排序类
     */
    public class FileComparator implements Comparator<Mp3Info> {
        public int compare(Mp3Info file1, Mp3Info file2) {
            if (Long.parseLong(file1.getFileTime()) > Long.parseLong(file2.getFileTime())) {
                return -1;
            } else if (Long.parseLong(file1.getFileTime()) < Long.parseLong(file2.getFileTime())) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    /**
     * 按字母排序类
     */
    public class PinyinComparator implements Comparator<Mp3Info> {

        public int compare(Mp3Info o1, Mp3Info o2) {
            //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
            if (StringUtils.getPingYin(o2.getTitle()).equals("#")) {
                return -1;
            } else if (StringUtils.getPingYin(o1.getTitle()).equals("#")) {
                return 1;
            } else {
//				return StringUtils.getPingYin(o1.getTitle()).compareTo(StringUtils.getPingYin(o2.getTitle()));
                return 0;
            }
        }
    }

    @Override
    public void onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private boolean isup;
    private LinearLayout ll_music;

    private LinearLayout ll_music_detail;

    private int height1;
    int pause = 0;

    private ImageView suiji;
    private boolean vistivis;
    private GuangGaoAdapter guanggaoadapter;


    public void deviceName(View v, int a) {
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(ObjectAnimator.ofFloat(v, "TranslationY", a, 0));
        animSet.setDuration(300).start();
    }

    public void deviceName2(View v, int a) {
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(ObjectAnimator.ofFloat(v, "y", a));
        animSet.setDuration(290).start();
    }

    public void deviceName1(View v, int a) {
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(ObjectAnimator.ofFloat(v, "TranslationY", 0, a));
        animSet.setDuration(300).start();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        deviceName1(ll_music_detail, height1);
        deviceName2(ll_music, height);
    }


    // 发送音乐json
    public String sendMusicJson(int postion) {
        Mp3Info mp3Info = ApplyTitleDanLi.getInstance().mp3s.get(postion);
        String id = GlobalConsts.AUDIO_PREFIX + mp3Info.getId();
        String title = mp3Info.getTitle();
        // String sendCommand = "http://" + MyApp.phone_ip + ":" +
        // HttpServer.PORT + "/" + id + UdpManager.separator + title +
        // UdpManager.separator
        // + mp3Info.getArtist();
        String sendCommand = "http://" + App.mPhoneIP + ":" + HttpServer.PORT
                + "/" + id;
        return sendCommand;

    }

    // 显示播放列表
    private Local_Music_adapter localAdapter1;

    private MusicAdapter musicAdapter;
    private TextView tv_size;

    private View view;

    // 传送歌曲json
    public String sendJson1(int postion) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject js2 = new JSONObject();
        try {
            for (int i = 0; i < ApplyTitleDanLi.getInstance().mp3s.size(); i++) {
                JSONObject jsobject3 = new JSONObject();
                jsobject3.put("id", null);
                jsobject3.put("title", ApplyTitleDanLi.getInstance().mp3s.get(i).getTitle());
                jsobject3.put("singer", ApplyTitleDanLi.getInstance().mp3s.get(i)
                        .getArtist());
                jsobject3.put("url", sendMusicJson(i));
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


    //加入音乐的json
    public void sendJson(int postion) {
        mPlayLists.clear();
        for (int i = 0; i < ApplyTitleDanLi.getInstance().mp3s.size(); i++) {
            VcontrolCmd.CustomPlay.PlayList playList = new VcontrolCmd.CustomPlay.PlayList(null, null,
                    null, ApplyTitleDanLi.getInstance().mp3s.get(i).getTitle(), ApplyTitleDanLi.getInstance().mp3s.get(i).getArtist(), sendMusicJson(i), null);
            mPlayLists.add(playList);
        }
    }

//	// 传送歌曲json
//	public String sendJson(int postion) {
//		JSONObject jsonObject = new JSONObject();
//		JSONArray jsonArray = new JSONArray();
//		JSONObject js2 = new JSONObject();
//		try {
//			for (int i = 0; i < ApplyTitleDanLi.getInstance().mp3s.size(); i++) {
//				JSONObject jsobject3 = new JSONObject();
//				jsobject3.put("id", null);
//				jsobject3.put("title", ApplyTitleDanLi.getInstance().mp3s
//						.get(i).getTitle());
//				jsobject3.put("singer",
//						ApplyTitleDanLi.getInstance().mp3s.get(i).getArtist());
//				jsobject3.put("url", sendMusicJson(i));
//				jsonArray.put(jsobject3);
//			}
//			jsonObject.put("type", 0);
//			jsonObject.put("pos", postion);
//			jsonObject.put("playlist", jsonArray);
//			js2.put("data", jsonObject);
//			js2.put("action", 10);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return js2.toString();
//	}

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

//	public void showPop(List<Music> musics, List<Mp3Info> mp3) {
//		View view1 = View.inflate(getActivity(), R.layout.music_menu, null);
//		final PopupWindow pop = new PopupWindow(view1, width, window_height
//				- getStatusBarHeight());
//		tv_size = (TextView) view1.findViewById(R.id.tv_size);
//		ImageView iv = (ImageView) view1.findViewById(R.id.iv);
//		iv.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				pop.dismiss();
//			}
//		});
//		SwipeMenuListView listViewMusic = (SwipeMenuListView) view1
//				.findViewById(R.id.listview_music);
////		initListView(listViewMusic);
//		localAdapter = new Local_Music_adapter(getActivity(), ApplyTitleDanLi.getInstance().mp3s,false);
//		musicAdapter = new MusicAdapter(getActivity(), ApplyTitleDanLi.getInstance().musicsList,false);
//		if ((musics == null || musics.size() == 0)&&(ApplyTitleDanLi.getInstance().guangList==null||ApplyTitleDanLi.getInstance().guangList.size()==0)) {
//			if(ApplyTitleDanLi.getInstance().mp3s==null||ApplyTitleDanLi.getInstance().mp3s.size()==0){
//				tv_size.setText("播放列表");
//			}else{
//				tv_size.setText("播放列表"+ApplyTitleDanLi.getInstance().mp3s.size()+"首");
//			}
//			listViewMusic.setAdapter(localAdapter);
//			listViewMusic.setOnItemClickListener(new OnItemClickListener() {
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view,
//						int position, long id) {
//					// TODO Auto-generated method stub
//					if(Constant.netStatus){
//						EventBus.getDefault().post(ApplyTitleDanLi.getInstance().mp3s.get(position));
//						GMDeviceController.getInstance().SendJC(sendJson1(position));
//					}else{
//						SignOutDilog singDilog = new SignOutDilog(getActivity(),
//								"是否现在连接无屏电视");
//						singDilog.show();
//						singDilog.setOnLisener(new SignOutDilog.onListern(){
//
//							@Override
//							public void send() {
//								// TODO Auto-generated method stub
//								Intent intent = new Intent(
//										getActivity(),
//										SearchDeviceActivity.class);
//								startActivity(intent);
//
//							}
//						});
//					}
//				}
//			});
//		}
//		else if((ApplyTitleDanLi.getInstance().musicsList==null||ApplyTitleDanLi.getInstance().musicsList.size()==0)
//							&&(ApplyTitleDanLi.getInstance().mp3s==null||ApplyTitleDanLi.getInstance().mp3s.size()==0))
//		{
//			tv_size.setText("播放列表 "+ApplyTitleDanLi.getInstance().guangList.size()+" 首");
//			guanggaoadapter = new GuangGaoAdapter(getActivity(), ApplyTitleDanLi.getInstance().guangList,false);
//			listViewMusic.setAdapter(guanggaoadapter);
//			listViewMusic.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view,
//						int position, long id) {
//					// TODO Auto-generated method stub
//					sendJsonGuanggao(position);
//				}
//			});
//		}
//
//		else {
//			tv_size.setText("播放列表 "+musics.size()+" 首");
//
//			listViewMusic.setAdapter(musicAdapter);
//			listViewMusic.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view,
//						int position, long id) {
//					// TODO Auto-generated method stub
//					// ApplyTitleDanLi.getInstance().musicsList=mp3Infos;
//					ApplyTitleDanLi.getInstance().mpos = position;
//					EventBus.getDefault().post(
//							ApplyTitleDanLi.getInstance().musicsList
//									.get(position));
//					ApplyTitleDanLi.getInstance().musicPostion = position;
//					GMDeviceController.getInstance().SendJC(sendJson(position));
//				}
//			});
//		}
//		// pop.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//		pop.setBackgroundDrawable(new BitmapDrawable());
//		pop.setFocusable(true);
//
//		pop.setAnimationStyle(R.style.pull_popup_anim);
//		// pop.showAtLocation(view, Gravity.CENTER, 0, 0);
//		int[] location = new int[2];
//		phongzhiyuan.view.getLocationOnScreen(location);
//
//		pop.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1]);
//
//	}

    public void onEventMainThread(CodeMsg message) {

        musicPause.setImageResource(R.drawable.bofang);
    }

    public void onEventMainThread(Music mp3) {
        musicName.setText(mp3.mTitle);
        musicSinger.setText(mp3.mArtist);
        musicPause.setImageResource(R.drawable.zanting);
        // progress.setProgress(0);
        // progress.setMax((int) mp3.getDuration());
        // Bitmap bitmap = MediaUtil.getArtwork(this, mp3.getId(),
        // mp3.getAlbumId(), true, true);// 获取专辑位图对象，为小图
        // touxiang.setImageBitmap(bitmap); // 这里显示专辑图片
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        Log.i("info", result + "ccc");
        return result;
    }

    //发送广告json

    public String sendJsonGuanggao(int postion) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject js2 = new JSONObject();
        try {
            for (int i = 0; i < ApplyTitleDanLi.getInstance().guangList.size(); i++) {
                JSONObject jsobject3 = new JSONObject();
                GuanGaoList.Infor music = ApplyTitleDanLi.getInstance().guangList.get(i);
                jsobject3.put("id", music.id);
                jsobject3.put("title", music.title);
                jsobject3.put("singer", music.singer);
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

    /**
     * 按首字母排序
     */
    public void ZiMuPaiXu() {

    }

}
