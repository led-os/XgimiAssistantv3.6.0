package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.ImageInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/12/2.
 */
public class PictureListAdapter extends BaseAdapter{
    private Context mContext = null;

    private List<String> imgInfoList = new ArrayList<String>();

    private HashMap<String, List<ImageInfo>> mGruopMap = null;

    public PictureListAdapter(Context context, List<String> imgInfoList,
                                 HashMap<String, List<ImageInfo>> gruopMap) {

        this.mContext = context;
        this.imgInfoList = imgInfoList;
        this.mGruopMap = gruopMap;
    }

    @Override
    public int getCount() {
        return imgInfoList.size();

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

        final ViewHolder viewHolder;

        String imagePath = imgInfoList.get(position);

        int num = mGruopMap.get(imagePath).size();

        if (convertView == null) {

            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.local_photolist_item, null);

            viewHolder.preview_iv = (ImageView) convertView
                    .findViewById(R.id.preview_iv);
            viewHolder.path_tv = (TextView) convertView
                    .findViewById(R.id.path_tv);
            viewHolder.image_num_tv = (TextView) convertView
                    .findViewById(R.id.image_num_tv);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String path = mGruopMap.get(imgInfoList.get(position)).get(0).getUrl();

        String imageUri = new StringBuffer().append("file://").append(path)
                .toString();

        ImageLoader.getInstance().displayImage(imageUri, viewHolder.preview_iv);

        viewHolder.image_num_tv.setText("" + num);

        viewHolder.path_tv.setText(imagePath);

        return convertView;
    }

    private class ViewHolder {
        ImageView preview_iv;
        TextView path_tv;
        TextView image_num_tv;
    }

}
