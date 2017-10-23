package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;

import java.util.List;
import java.util.Map;

public class FileTypeAdapter extends BaseAdapter {

	private int[] fileName = { R.string.file_ppt_name, R.string.file_pdf_name, R.string.file_doc_name, R.string.file_xls_name,
			R.string.file_txt_name, R.string.file_apk_name };

	private int[] filetypes = { R.drawable.file_ppt_flag, R.drawable.file_pdf_flag, R.drawable.file_doc_flag, R.drawable.file_xls_flag,
			R.drawable.file_txt_flag, R.drawable.file_apk_flag };

	private Context context;
	public static int scrollState = 0;

	private List<List<Map<String, Object>>> files;

	public FileTypeAdapter(Context context, List<List<Map<String, Object>>> list) {
		this.context = context;
		this.files = list;

	}
	public void dataChange(List<List<Map<String, Object>>> list){
		this.files = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return files.size();
	}

	@Override
	public Object getItem(int position) {
		return context.getResources().getString(fileName[position]);
	}

	public int getItemSize(int position) {
		return files.get(position).size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHooder hooder;

		View view = convertView;

		if (view == null) {

			view = LayoutInflater.from(context).inflate(R.layout.local_file_item, null);

			hooder = new ViewHooder();
			hooder.image = (ImageView) view.findViewById(R.id.localres_file_item_type);
			hooder.size = (TextView) view.findViewById(R.id.localres_file_item_size);
			hooder.file_name=(TextView) view.findViewById(R.id.file_name);
			view.setTag(hooder);
		} else {
			hooder = (ViewHooder) view.getTag();
		}

		hooder.image.setBackgroundResource(filetypes[position]);
		hooder.size.setText("" + files.get(position).size());
		if(position==0){
			hooder.file_name.setText("PPT");
		}else if(position==1){
			hooder.file_name.setText("PDF");
		}else if(position==2){
			hooder.file_name.setText("WORD");
		}else if(position==3){
			hooder.file_name.setText("EXCEL");
		}else if(position==4){
			hooder.file_name.setText("TXT");
		}else if(position==5){
			hooder.file_name.setText("安装包");
		}

		return view;
	}

	private class ViewHooder {
		public ImageView image;
		public TextView size,file_name;
	}

}
