package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.ImageInfo;
import com.xgimi.zhushou.bean.UserBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/2.
 */
public class PictureimageGridAdapter extends BaseAdapter{
    private Context mContext;
    public String uid = null;
    private List<ImageInfo> mImages; // 图片信息
    public static Map<Integer,ImageInfo> maps=new HashMap<>();
    public static Map<Integer,ImageInfo> mapsbeen=new HashMap<>();

    public PictureimageGridAdapter(Context context, List<ImageInfo> images) {
        this.mContext = context;
        this.mImages = images;
    }
    public void dataChange(List<ImageInfo> images){
        this.mImages = images;
        notifyDataSetChanged();
    }
    getIds ids;
    public void setLisener(getIds id){
        this.ids=id;
    }
    public interface getIds{
        void getMenmberIds(Map<Integer,ImageInfo> beans, Map<Integer,ImageInfo> cleanBeen, boolean isAdd);
    }
    @Override
    public int getCount() {
        return mImages == null ? 0 : mImages.size();
    }

    @Override
    public ImageInfo getItem(int position) {
        return mImages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MyGridViewHolder viewHolder;
        final int index=position;
        String url = mImages.get(position).getUrl();
        String imageUri = new StringBuffer().append("file://").append(url).toString();
        if (convertView == null) {
            viewHolder = new MyGridViewHolder();
            convertView = View.inflate(mContext, R.layout.picture_grid_item, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.yunimage);
            viewHolder.select= (ImageView) convertView.findViewById(R.id.select);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyGridViewHolder) convertView.getTag();
        }
        if(mImages.get(index).type == UserBean.TYPE_CHECKED){
            viewHolder.select.setImageResource(R.drawable.xuanzhong);
        }else{
            viewHolder.select.setImageResource(R.drawable.weixuanzhong);
        }
//        maps.clear();
//        mapsbeen.clear();
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewHolder.select.setImageResource(R.drawable.xuanzhong);
//                mHasMaps.put(index,mImages.get(index));
                if(mImages.get(index).type== UserBean.TYPE_CHECKED){
                    mImages.get(index).type=UserBean.TYPE_NOCHECKED;
                    if(ids!=null){
                        if(maps!=null&&maps.size()>0) {
                            viewHolder.select.setImageResource(R.drawable.weixuanzhong);
                            maps.remove(index);
                            mapsbeen.remove(index);
                            mapsbeen.put(index,mImages.get(index));
                            ids.getMenmberIds(maps,mapsbeen,false);
                            mapsbeen.remove(index);
                        }
                    }
                }else{
                    mImages.get(index).type=UserBean.TYPE_CHECKED;
                    if(ids!=null){
                        viewHolder.select.setImageResource(R.drawable.xuanzhong);
                        maps.put(index,mImages.get(index));
                        mapsbeen.put(index,mImages.get(index));
                        ids.getMenmberIds(maps,mapsbeen,true);
                    }
                }
            }
        });

		/* 图片的单击弹出显示的完成窗口 */

//		viewHolder.imageView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				 Intent intent = new
//				 Intent(mContext,ImagePagerActivity.class);
//				 intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS,
//				 parentPath);
//				 intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX,
//				 position);
//				 mContext.startActivity(intent);
//				 Toast.makeText(mContext,mContext.getString(R.string.notice_uploading),
//				 0).show();
//			}
//		});
		/* 显示图片 */
        ImageLoader.getInstance().displayImage(imageUri, viewHolder.imageView);
        return convertView;
    }

    public static class MyGridViewHolder {
        public ImageView imageView,select;

    }

}
