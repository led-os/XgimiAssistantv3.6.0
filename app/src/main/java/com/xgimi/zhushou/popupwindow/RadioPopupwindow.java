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
import com.xgimi.zhushou.adapter.BaiDuMusicAdapter;
import com.xgimi.zhushou.adapter.RadioPoPuListAdapter;
import com.xgimi.zhushou.bean.MusicInfor;
import com.xgimi.zhushou.bean.RadioShow;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.ToosUtil;

import de.greenrobot.event.EventBus;
import rx.Subscription;

/**
 * Created by Administrator on 2016/8/29.
 */
public class RadioPopupwindow extends PopupWindow {
    public ListView listView;
    private View mMenuView;
    private Subscription subscription;
    public int page = 0;
    public int psize = 10;
    public String keywords;
    public RadioPoPuListAdapter adapter;
    public BaiDuMusicAdapter adapter1;
    private RelativeLayout btn_cancel;
    public TextView songname;

    public RadioPopupwindow(Activity context,int type,String title) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popuwindow_item, null);

        btn_cancel = (RelativeLayout) mMenuView.findViewById(R.id.rl_cancel);
        listView = (ListView) mMenuView.findViewById(R.id.listview);
        songname = (TextView) mMenuView.findViewById(R.id.song_name);
        songname.setText(title);
        //取消按钮
        btn_cancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        //设置按钮监听
       // listView.setOnClickListener(itemsOnClick);
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
        if (type == 1) {
            ToosUtil.getInstance().addEventUmeng(context,"event_music_radio_play_list");
            adapter = new RadioPoPuListAdapter(context, SaveData.getInstance().mRadioShow);
            listView.setAdapter(adapter);
            adapter.dataChange(SaveData.getInstance().mRadioShow);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SaveData.getInstance().program_title=SaveData.getInstance().mRadioShow.get(position).program_title;
                    SaveData.getInstance().program_play_time=SaveData.getInstance().mRadioShow.get(position).program_play_time;
                    songname.setText(SaveData.getInstance().mRadioShow.get(position).program_title);
                    SaveData.getInstance().position=position;
                    EventBus.getDefault().post(new RadioShow());
                    dismiss();
                }
            });
        }
        else if (type == 2) {
            adapter1 = new BaiDuMusicAdapter(context, SaveData.getInstance().mPlayLists);
            listView.setAdapter(adapter1);
            adapter1.dataChange(SaveData.getInstance().mPlayLists);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    songname.setText(SaveData.getInstance().mPlayLists.get(position).title);
                    SaveData.getInstance().music_title=SaveData.getInstance().mPlayLists.get(position).title;
                    SaveData.getInstance().music_singer=SaveData.getInstance().mPlayLists.get(position).singer;
                    SaveData.getInstance().position=position;
                    EventBus.getDefault().post(new MusicInfor());
                    dismiss();
                }
            });
        }

        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

}
