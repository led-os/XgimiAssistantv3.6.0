package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.music.model.Music;
import com.xgimi.zhushou.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends BaseAdapter implements OnClickListener{

	private Context context;

	private List<Music> mp3Infos;


	private int mpostion = -1;
	private boolean isZhankai=false;
	private ImageView imageview;
	private int Last=-1;
	public boolean isdelete;
	public MusicAdapter(Context context, List<Music> mp3Infos, boolean isDelet) {
		this.context = context;
		this.isdelete=isDelet;
		this.mp3Infos = mp3Infos;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mp3Infos.size() == 0 ? 0 : mp3Infos.size();
	}

	public void isShow(int postion){
		mpostion=postion;
		notifyDataSetChanged();
	}
	public void dataChange(List<Music> musics){
//		this.mp3Infos.clear();
		ArrayList<Music> m=new ArrayList<>();
		m.addAll(musics);
		this.mp3Infos=m;
		notifyDataSetChanged();
	}
	@Override
	public Music getItem(int position) {
		// TODO Auto-generated method stub
		return mp3Infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView,  ViewGroup parent) {
		// TODO Auto-generated method stub
		final int a=position;
		View view=convertView;
	final	ViewHolder vh;
				if(view==null){
					vh=new ViewHolder();
					view=View.inflate(context, R.layout.local_music_item, null);
					vh.geming=(TextView) view.findViewById(R.id.geming);
					vh.zuozhe=(TextView) view.findViewById(R.id.zuozhe);
					vh.iv_delete=(ImageView) view.findViewById(R.id.delete);

					view.setTag(vh);
				}else{
					vh=(ViewHolder) view.getTag();
				}
				if(isdelete){
					vh.iv_delete.setVisibility(View.VISIBLE);
				}
				vh.iv_delete.setOnClickListener(this);
				vh.iv_delete.setTag(position);
				vh.geming.setText(mp3Infos.get(position).mTitle);
				vh.zuozhe.setText(mp3Infos.get(position).mArtist);
				
		return view;
	}
	class ViewHolder{
		public TextView geming;
		public TextView zuozhe;
		public ImageView zhankai;
		public LinearLayout ll;
		public ImageView iv_bofang;
		public ImageView iv_shoucang;
		public ImageView iv_share;
		public ImageView iv_delete;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

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
	//传送歌曲json
	public String sendJson(int postion){
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray=new JSONArray();
		JSONObject js2 = new JSONObject();
		try {
			for (int i = 0; i < mp3Infos.size(); i++) {
				JSONObject jsobject3=new JSONObject();
				Music music = mp3Infos.get(i);
				jsobject3.put("id", music.mId);
				jsobject3.put("title", music.mTitle);
					jsobject3.put("singer", music.mArtist);
					jsobject3.put("url", null);
					jsonArray.put(jsobject3);
			}
			jsonObject.put("type", 0);
			jsonObject.put("pos", postion);
			jsonObject.put("playlist", jsonArray);
			js2.put("data", jsonObject);
			js2.put("action", 10);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return js2.toString();
	}
	
}
