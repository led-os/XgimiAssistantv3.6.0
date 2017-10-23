package com.xgimi.zhushou.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.MVPoPuListAdapter;
import com.xgimi.zhushou.adapter.MVSearchPoPListAdapter;
import com.xgimi.zhushou.bean.MVList;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.ToosUtil;

import de.greenrobot.event.EventBus;
import rx.Subscription;

/**
 * Created by Administrator on 2016/8/19.
 */
public class MVPopupWindow extends PopupWindow{
    public ListView listView;
    private View mMenuView;
    private Subscription subscription;
    public int page=0;
    public int psize=10;
    public String keywords;
    public MVPoPuListAdapter adapter;
    public MVPoPuListAdapter adapter1;
    public MVSearchPoPListAdapter adapter2;
    private RelativeLayout btn_cancel;
    public TextView songname;
    public MVPopupWindow(Activity context,int type) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popuwindow_item, null);
        btn_cancel= (RelativeLayout) mMenuView.findViewById(R.id.rl_cancel);
        listView= (ListView) mMenuView.findViewById(R.id.listview);
        songname= (TextView) mMenuView.findViewById(R.id.song_name);
        //取消按钮
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        //设置按钮监听
//        listView.setOnClickListener(itemsOnClick);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setAnimationStyle(R.style.PopupAnimation);
        //设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.PopupAnimation);
//        //实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(1);
//        //设置SelectPicPopupWindow弹出窗体的背景
//        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//        if(type==1){
        ToosUtil.getInstance().addEventUmeng(context,"event_music_mv_play_list");
            adapter=new MVPoPuListAdapter(context, SaveData.getInstance().mList);
            listView.setAdapter(adapter);
        if(SaveData.getInstance().mList!=null){
            adapter.dataChange(SaveData.getInstance().mList);
        }
        if(SaveData.getInstance().nextTitle!=null){
            songname.setText(SaveData.getInstance().nextTitle);
        }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SaveData.getInstance().mv_title=SaveData.getInstance().mList.get(position).mv_title;
                    SaveData.getInstance().mv_artist=SaveData.getInstance().mList.get(position).mv_artist;
                    SaveData.getInstance().bitmap=SaveData.getInstance().mList.get(position).mv_thumb;
                    SaveData.getInstance().mvid=SaveData.getInstance().mList.get(position).mv_id;
                    songname.setText(SaveData.getInstance().mList.get(position).mv_title);
                    SaveData.getInstance().position=position;
                    EventBus.getDefault().post(new MVList());
                    dismiss();
                }
            });
//        }else if(type==2){
//            adapter1=new MVPoPuListAdapter(context,SaveData.getInstance().tuijianList);
//            listView.setAdapter(adapter1);
//            adapter1.dataChange(SaveData.getInstance().tuijianList);
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    SaveData.getInstance().mv_title=SaveData.getInstance().tuijianList.get(position).mv_title;
//                    SaveData.getInstance().mv_artist=SaveData.getInstance().tuijianList.get(position).mv_artist;
//                    songname.setText(SaveData.getInstance().tuijianList.get(position).mv_title);
//                    SaveData.getInstance().bitmap=SaveData.getInstance().tuijianList.get(position).mv_thumb;
//                    SaveData.getInstance().mvid=SaveData.getInstance().tuijianList.get(position).mv_id;
//                    SaveData.getInstance().position=position;
//                    EventBus.getDefault().post(new MVList());
//                    dismiss();
//                }
//            });
//        }else if(type==3){
//            adapter2=new MVSearchPoPListAdapter(context,SaveData.getInstance().mVSearch);
//            listView.setAdapter(adapter2);
//            adapter2.dataChange(SaveData.getInstance().mVSearch);
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    SaveData.getInstance().mv_title=SaveData.getInstance().mVSearch.get(position).mv_title;
//                    SaveData.getInstance().mv_artist=SaveData.getInstance().mVSearch.get(position).mv_artist;
//                    songname.setText(SaveData.getInstance().mVSearch.get(position).mv_title);
//                    SaveData.getInstance().bitmap=SaveData.getInstance().mVSearch.get(position).mv_thumb;
//                    SaveData.getInstance().mvid=SaveData.getInstance().mVSearch.get(position).mv_id;
//                    SaveData.getInstance().position=position;
//                    EventBus.getDefault().post(new MVList());
//                    dismiss();
//                }
//            });
//        }

        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });
    }


//    private void getMVRecommend() {
//        subscription= Api.getMangoApi().getMVTuijian().subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(observer1);
//    }
//    //获取MV推荐数据
//    Observer<MVTuijian> observer1=new Observer<MVTuijian>() {
//        @Override
//        public void onCompleted() {
//            unRegist(subscription);
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            e.printStackTrace();
//        }
//
//        @Override
//        public void onNext(MVTuijian mvTuijian) {
//            if(mvTuijian!=null && mvTuijian.data!=null){
//                mvTuijianData.addAll(mvTuijian.data);
////                adpter.dataChange(mvTuijianData);
//            }
//        }
//    };
//    private void unRegist(Subscription sub) {
//        if (sub != null && !sub.isUnsubscribed()) {
//            sub.unsubscribe();
//        }
//    }
    }
