package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.FileInfo;
import com.xgimi.zhushou.util.XGIMILOG;

import java.util.List;

/**
 * Created by Administrator on 2016/12/13.
 */
public class FileDetailTransferAdapter extends BaseAdapter {
    private Context mContext = null;

    private List<FileInfo> mFileMapGroup = null;

    private int item = -1; // 标识是不是apk项，5
    private boolean mShowSelectionIv;

    public FileDetailTransferAdapter(Context context, int item, List<FileInfo> fileMapGroup) {
        XGIMILOG.E("-----------------------------------------------------------");
        this.mContext = context;
        this.mFileMapGroup = fileMapGroup;
        this.item = item;
    }

    public FileDetailTransferAdapter(Context context, int item, List<FileInfo> fileMapGroup, boolean showSelectionIv) {
        XGIMILOG.E("-----------------------------------------------------------");
        this.mContext = context;
        this.mFileMapGroup = fileMapGroup;
        this.item = item;
        this.mShowSelectionIv = showSelectionIv;
    }

    public void dataChange(int item, List<FileInfo> fileMapGroup){
        this.mFileMapGroup = fileMapGroup;
        this.item = item;
        notifyDataSetChanged();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {

            convertView = LayoutInflater.from(mContext).inflate(R.layout.local_detailfile_item, null);

            viewHolder = new ViewHolder();
//            viewHolder.ivSelected = (ImageView) convertView.findViewById(R.id.iv_file_item_selected);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.detailfile_icon);
            viewHolder.name = (TextView) convertView.findViewById(R.id.fileName);
            viewHolder.time = (TextView) convertView.findViewById(R.id.fileTime);
            viewHolder.size = (TextView) convertView.findViewById(R.id.fileSize);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }

//		Map<String, Object> map = mFileMapGroup.get(position);
//
//		String filePath = (String) map.get("filepath");
//
//		File file = new File(filePath);
//
//		String fileName = "";
//
//		long fileSize = 0;
//
//		long time = 0;
//
//		if (file.exists()) {
//			fileName = file.getName();
//			fileSize = file.length();
//			time = file.lastModified();
//		}

        viewHolder.name.setText(mFileMapGroup.get(position).fileName);

        viewHolder.time.setText(mFileMapGroup.get(position).time);

        viewHolder.size.setText(mFileMapGroup.get(position).fileSize);

        if (item == 5) {
            viewHolder.icon.setVisibility(View.VISIBLE);

            if (mFileMapGroup.get(position).icon == null) {
                viewHolder.icon.setImageResource(R.drawable.app_icon);
            } else {
                viewHolder.icon.setImageDrawable(mFileMapGroup.get(position).icon);
            }
        }

//        if (mShowSelectionIv) {
//            viewHolder.ivSelected.setVisibility(View.VISIBLE);
//            if (!mFileMapGroup.get(position).isSelcted()) {
//                viewHolder.ivSelected.setImageResource(R.drawable.weixuanzhong);
//            } else {
//                viewHolder.ivSelected.setImageResource(R.drawable.xuanzhong);
//            }
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mFileMapGroup.get(position).isSelcted()) {
//                        mFileMapGroup.get(position).setSelcted(false);
//                        viewHolder.ivSelected.setImageResource(R.drawable.weixuanzhong);
//                    } else {
//                        mFileMapGroup.get(position).setSelcted(true);
//                        viewHolder.ivSelected.setImageResource(R.drawable.xuanzhong);
//                    }
//                }
//            });
//        }
        return convertView;
    }

    private class ViewHolder {
        ImageView ivSelected;
        ImageView icon;
        TextView name; // 文件名
        TextView time; // 创建时间
        TextView size; // 文件大小
    }

}
