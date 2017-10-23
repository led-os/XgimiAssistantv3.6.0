package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.ImageInfo;

import java.util.List;

public class YunLocalimageGridAdapter extends BaseAdapter {
	private Context mContext;

	public String uid = null;
	private List<ImageInfo> mImages; // 图片信息
	private boolean mIsStartByPhotoWall = false;

	public YunLocalimageGridAdapter(Context context, List<ImageInfo> images, boolean isStartByPhotoWall) {
		this.mContext = context;
		this.mImages = images;
		this.mIsStartByPhotoWall = isStartByPhotoWall;
	}
	public YunLocalimageGridAdapter(Context context, List<ImageInfo> images) {
		this.mContext = context;
		this.mImages = images;
	}
	public void dataChange(List<ImageInfo> images){
		this.mImages = images;
		notifyDataSetChanged();
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
		String url = mImages.get(position).getUrl();
		String imageUri = new StringBuffer().append("file://").append(url)
				.toString();
		if (convertView == null) {
			viewHolder = new MyGridViewHolder();
			if (!mIsStartByPhotoWall) {
				convertView = View.inflate(mContext, R.layout.yungridview_item, null);
			} else {
				convertView = View.inflate(mContext, R.layout.yungridview_item_for_photo_wall, null);
			}

			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.yunimage);
			convertView.setTag(viewHolder);

		} else {

			viewHolder = (MyGridViewHolder) convertView.getTag();

		}


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
		public ImageView imageView;
	}

}
