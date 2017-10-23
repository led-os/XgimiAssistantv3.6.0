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
import com.xgimi.zhushou.bean.VideoInfo;

import java.util.List;

/**
 * ������Դ������Ƶ����
 * 
 */
public class VideoAdapter extends BaseAdapter {

	private Context context;

	private List<VideoInfo> videoList;

	public static int scrollState = 0;

	public VideoAdapter(Context context, List<VideoInfo> list) {
		this.context = context;
		this.videoList = list;
	}

	public void dataChange(List<VideoInfo> list){
		this.videoList = list;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return videoList!=null&&videoList.size()>0?videoList.size():0;
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

		ViewHooder hooder = null;

		if (convertView == null) {

			hooder = new ViewHooder();

			convertView = LayoutInflater.from(context).inflate(R.layout.local_video_item, null);
			hooder.image = (ImageView) convertView.findViewById(R.id.videoImage);
			hooder.title = (TextView) convertView.findViewById(R.id.videoTitle);
//			hooder.image1 = (ImageView) convertView.findViewById(R.id.videoImage1);
//			hooder.title1 = (TextView) convertView.findViewById(R.id.videoTitle1);


			convertView.setTag(hooder);
		} else {
			hooder = (ViewHooder) convertView.getTag();
		}

//		String title = (String) videoList.get(position).get("title");
		String title = (String) videoList.get(position).title;
//		String title1 = (String) videoList.get(position*2+1).title;

		hooder.title.setText("" + title);
//		hooder.title1.setText("" + title1);

//		hooder.image.setImageBitmap((Bitmap) videoList.get(position).get("tempThumb"));

		hooder.image.setImageBitmap((Bitmap) videoList.get(position).tempThumb);
//		hooder.image.setImageBitmap((Bitmap) videoList.get(position*2+1).tempThumb);
		return convertView;
	}

	private class ViewHooder { // �Զ���ؼ�����
		public ImageView image;
		public TextView title;
	}

}
