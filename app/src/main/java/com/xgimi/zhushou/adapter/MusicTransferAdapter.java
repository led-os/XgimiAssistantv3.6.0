package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.ApplyTitleDanLi;
import com.xgimi.zhushou.bean.Mp3Info;
import com.xgimi.zhushou.bean.UserBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/5.
 */
public class MusicTransferAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;

    private List<Mp3Info> mp3Infos;

    private Mp3Info mp3Info;

    private int mpostion = -1;
    private boolean isZhankai = false;
    private ImageView imageview;
    private int Last = -1;
    public boolean isdelete;
    public static Map<Integer, Mp3Info> maps = new HashMap<>();
    public static Map<Integer, Mp3Info> mapsbeen = new HashMap<>();


    public MusicTransferAdapter(Context context, List<Mp3Info> mp3Infos, boolean isDelete) {
        this.isdelete = isDelete;
        this.context = context;
        this.mp3Infos = mp3Infos;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mp3Infos == null || mp3Infos.size() == 0 ? 0 : mp3Infos.size();
    }

    getIds ids;

    public void setLisener(getIds id) {
        this.ids = id;
    }

    public interface getIds {
        void getMenmberIds(Map<Integer, Mp3Info> beans, Map<Integer, Mp3Info> cleanBeen, boolean isAdd);
    }

    public void dataChange(List<Mp3Info> mp3) {
        this.mp3Infos = mp3;
        notifyDataSetChanged();
    }

    @Override
    public Mp3Info getItem(int position) {
        // TODO Auto-generated method stub
        return mp3Infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public void isShow(int postion) {
        mpostion = postion;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final int a = position;
        View view = convertView;
        final ViewHolder vh;
        final int index = position;
        if (view == null) {
            vh = new ViewHolder();
            view = View.inflate(context, R.layout.transfer_music_item, null);
            vh.geming = (TextView) view.findViewById(R.id.geming);
            vh.zuozhe = (TextView) view.findViewById(R.id.zuozhe);
            vh.time = (TextView) view.findViewById(R.id.xiugaitime);
            vh.select = (ImageView) view.findViewById(R.id.select);
            vh.iv_delete = (ImageView) view.findViewById(R.id.delete);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        if (isdelete) {
            vh.iv_delete.setVisibility(View.VISIBLE);
        }
        vh.geming.setText(mp3Infos.get(position).getTitle());
        vh.zuozhe.setText(mp3Infos.get(position).getArtist());
        vh.time.setText(mp3Infos.get(position).getFileTime() + "");
        vh.iv_delete.setOnClickListener(this);
        vh.iv_delete.setTag(position);
        if (mp3Infos.get(index).type == UserBean.TYPE_CHECKED) {
            vh.select.setImageResource(R.drawable.xuanzhong);
        } else {
            vh.select.setImageResource(R.drawable.weixuanzhong);
        }
//        maps.clear();
//        mapsbeen.clear();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewHolder.select.setImageResource(R.drawable.xuanzhong);
//                mHasMaps.put(index,mImages.get(index));
                if (mp3Infos.get(index).type == UserBean.TYPE_CHECKED) {
                    mp3Infos.get(index).type = UserBean.TYPE_NOCHECKED;
                    if (ids != null) {
                        if (maps != null && maps.size() > 0) {
                            vh.select.setImageResource(R.drawable.weixuanzhong);
                            maps.remove(index);
                            mapsbeen.remove(index);
                            mapsbeen.put(index, mp3Infos.get(index));
                            ids.getMenmberIds(maps, mapsbeen, false);
                            mapsbeen.remove(index);
                        }
                    }
                } else {
                    mp3Infos.get(index).type = UserBean.TYPE_CHECKED;
                    if (ids != null) {
                        vh.select.setImageResource(R.drawable.xuanzhong);
                        maps.put(index, mp3Infos.get(index));
                        mapsbeen.put(index, mp3Infos.get(index));
                        ids.getMenmberIds(maps, mapsbeen, true);
                    }
                }
            }
        });
        return view;
    }

    class ViewHolder {
        public TextView geming;
        public TextView zuozhe;
        public ImageView zhankai;
        public LinearLayout ll;
        public ImageView iv_bofang;
        public ImageView iv_shoucnag;
        public ImageView iv_share;
        public ImageView iv_delete;
        public TextView time;
        public ImageView select;


    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.delete:
                int postion = (int) v.getTag();
                ApplyTitleDanLi.getInstance().mp3s.remove(postion);
                ApplyTitleDanLi.getInstance().musicPostion = postion - 1;
//			GMDeviceController.getInstance().SendJC(playJson(4));
                dataChange(ApplyTitleDanLi.getInstance().mp3s);

                break;

            default:
                break;
        }
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
