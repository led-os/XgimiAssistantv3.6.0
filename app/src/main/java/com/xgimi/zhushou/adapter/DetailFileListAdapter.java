package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.device.utils.FileUtils;
import com.xgimi.device.utils.StringUtils;
import com.xgimi.zhushou.R;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 本地资源——文件——详细文件的容器
 *
 */
public class DetailFileListAdapter extends BaseAdapter {

	private Context mContext = null;

	private List<Map<String, Object>> mFileMapGroup = null;

	private int item = -1; // 标识是不是apk项，5

	public DetailFileListAdapter(Context context, int item, List<Map<String, Object>> fileMapGroup) {
		this.mContext = context;
		this.mFileMapGroup = fileMapGroup;
		this.item = item;
	}

	@Override
	public int getCount() {
		return mFileMapGroup.size();
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

		if (convertView == null) {

			convertView = LayoutInflater.from(mContext).inflate(R.layout.local_detailfile_item, null);

			viewHolder = new ViewHolder();

			viewHolder.icon = (ImageView) convertView.findViewById(R.id.detailfile_icon);
			viewHolder.name = (TextView) convertView.findViewById(R.id.fileName);
			viewHolder.time = (TextView) convertView.findViewById(R.id.fileTime);
			viewHolder.size = (TextView) convertView.findViewById(R.id.fileSize);

			convertView.setTag(viewHolder);

		} else {

			viewHolder = (ViewHolder) convertView.getTag();

		}

		Map<String, Object> map = mFileMapGroup.get(position);

		String filePath = (String) map.get("filepath");

		File file = new File(filePath);

		String fileName = "";

		long fileSize = 0;

		long time = 0;

		if (file.exists()) {
			fileName = file.getName();
			fileSize = file.length();
			time = file.lastModified();
		}

		viewHolder.name.setText("" + fileName);

		viewHolder.time.setText(StringUtils.getfullTime(time));

		viewHolder.size.setText(FileUtils.formatFileSize(fileSize));

		if (item == 5) {
			viewHolder.icon.setVisibility(View.VISIBLE);

			if ((Drawable) map.get("icon") == null) {
				viewHolder.icon.setImageResource(R.drawable.app_icon);
			} else {
				viewHolder.icon.setImageDrawable((Drawable) map.get("icon"));
			}
		}

		return convertView;
	}

	private class ViewHolder {
		ImageView icon;
		TextView name; // 文件名
		TextView time; // 创建时间
		TextView size; // 文件大小
	}

}
