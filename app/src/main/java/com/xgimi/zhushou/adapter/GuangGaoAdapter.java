package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.ApplyTitleDanLi;
import com.xgimi.zhushou.bean.GuanGaoList;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class GuangGaoAdapter extends BaseAdapter implements OnClickListener{

	private Context context;

	private List<GuanGaoList.Infor> mp3Infos;
	public boolean isdelete;

	public GuangGaoAdapter(Context context, List<GuanGaoList.Infor> mp3Infos, boolean isdelet) {
		this.context = context;
		this.isdelete=isdelet;
		this.mp3Infos = mp3Infos;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mp3Infos.size() == 0 ? 0 : mp3Infos.size();
	}

	public void dataChange(List<GuanGaoList.Infor> musics){
		this.mp3Infos=musics;
		notifyDataSetChanged();
	}
	@Override
	public GuanGaoList.Infor getItem(int position) {
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
				vh.geming.setText(mp3Infos.get(position).title);
				vh.zuozhe.setText(mp3Infos.get(position).singer);
				
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
		case R.id.delete:
			int  postion = (int) v.getTag();
			ApplyTitleDanLi.getInstance().musicPostion=postion;
			ApplyTitleDanLi.getInstance().guangList.remove(postion);
//			GMDeviceController.getInstance().SendJC(playJson(4));
			dataChange(ApplyTitleDanLi.getInstance().guangList);
			break;
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
}
