package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.music.model.Music;
import com.xgimi.zhushou.R;

import java.util.List;

public class SearchMusicAdapter extends BaseAdapter{

	private List<Music> mMusic;
	private Context mContext;
	public SearchMusicAdapter(List<Music> musi, Context context){
		this.mMusic=musi;
		this.mContext=context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mMusic==null||mMusic.size()==0?0:mMusic.size();
	}
	public void dataChange(List<Music> mMusic){
		this.mMusic=mMusic;
		notifyDataSetChanged();
	}

	@Override
	public Music getItem(int position) {
		// TODO Auto-generated method stub
		return mMusic.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder vh;
		View view=convertView;
		if(view==null){
			vh=new ViewHolder();
			 view=View.inflate(mContext, R.layout.music_adapter_item, null);
			  vh.tv = (TextView) view.findViewById(R.id.gequ);
			  vh.tv1 = (TextView) view.findViewById(R.id.singer);
			  view.setTag(vh);
		}else{
			vh=(ViewHolder) view.getTag();
		}
		vh.tv.setText(mMusic.get(position).mTitle);
		vh.tv1.setText(mMusic.get(position).mArtist);
		return view;
	}
	class ViewHolder{
		public TextView tv;
		public TextView tv1;
	}

}
