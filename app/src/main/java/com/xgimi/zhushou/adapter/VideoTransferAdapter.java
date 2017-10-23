package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.UserBean;
import com.xgimi.zhushou.bean.VideoInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/5.
 */
public class VideoTransferAdapter extends BaseAdapter{
    private Context context;
    private List<VideoInfo> videoList; // 图片信息
    public static int scrollState = 0;
    public static Map<Integer,VideoInfo> maps=new HashMap<>();
    public static Map<Integer,VideoInfo> mapsbeen=new HashMap<>();

    public VideoTransferAdapter(Context context, List<VideoInfo> list) {
        this.context = context;
        this.videoList = list;
    }
    public void dataChange(List<VideoInfo> list){
        this.videoList = list;
    }
    getIds ids;
    public void setLisener(getIds id){
        this.ids=id;
    }
    public interface getIds{
        void getMenmberIds(Map<Integer,VideoInfo> beans, Map<Integer,VideoInfo> cleanBeen, boolean isAdd);
    }
    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHooder hooder;
        final int index=position;
        if (convertView == null) {
            hooder = new ViewHooder();

            convertView = LayoutInflater.from(context).inflate(R.layout.transfer_video_item, null);
            hooder.image = (ImageView) convertView.findViewById(R.id.videoImage);
            hooder.title = (TextView) convertView.findViewById(R.id.videoTitle);
            hooder.select= (ImageView) convertView.findViewById(R.id.select);

            convertView.setTag(hooder);
        } else {
            hooder = (ViewHooder) convertView.getTag();
        }
        if(videoList.get(index).type == UserBean.TYPE_CHECKED){
            hooder.select.setImageResource(R.drawable.xuanzhong);
        }else{
            hooder.select.setImageResource(R.drawable.weixuanzhong);
        }
//        maps.clear();
//        mapsbeen.clear();
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewHolder.select.setImageResource(R.drawable.xuanzhong);
//                mHasMaps.put(index,mImages.get(index));
                if(videoList.get(index).type== UserBean.TYPE_CHECKED){
                    videoList.get(index).type=UserBean.TYPE_NOCHECKED;
                    if(ids!=null){
                        if(maps!=null&&maps.size()>0) {
                            hooder.select.setImageResource(R.drawable.weixuanzhong);
                            maps.remove(index);
                            mapsbeen.remove(index);
                            mapsbeen.put(index,videoList.get(index));
                            ids.getMenmberIds(maps,mapsbeen,false);
                            mapsbeen.remove(index);
                        }
                    }
                }else{
                    videoList.get(index).type=UserBean.TYPE_CHECKED;
                    if(ids!=null){
                        hooder.select.setImageResource(R.drawable.xuanzhong);
                        maps.put(index,videoList.get(index));
                        mapsbeen.put(index,videoList.get(index));
                        ids.getMenmberIds(maps,mapsbeen,true);
                    }
                }
            }
        });

        String title = (String) videoList.get(position).title;
        hooder.title.setText("" + title);
        hooder.image.setImageBitmap((Bitmap) videoList.get(position).tempThumb);
        return convertView;
    }

    private class ViewHooder { // �Զ���ؼ�����
        public ImageView image,select;
        public TextView title;
    }

}
