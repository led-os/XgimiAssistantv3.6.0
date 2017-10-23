package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.FilmDetailBean;
import com.xgimi.zhushou.bean.IqiyiBean;
import com.xgimi.zhushou.util.DeviceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class JiShuGridviewAdapter extends BaseAdapter{

	public Context mContext;
	private int mpostion=-1;
	public int page;
	public IqiyiBean mXiangqing;
	private Button tv;
	int getshu;
	private String s;
	FilmDetailBean mDatas;
	 IqiyiBean.DataBean dataBean;


	public JiShuGridviewAdapter(Context context, FilmDetailBean mDatas, IqiyiBean xiangQingItem){
		this.mContext=context;
		this.mXiangqing=xiangQingItem;
		this.mDatas=mDatas;

	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mXiangqing ==null||mXiangqing.data ==null){
			return 0;
		}
		return mXiangqing.data !=null&& mXiangqing.data.size()<10? mXiangqing.data.size():10;
	}

	@Override
	public IqiyiBean.DataBean getItem(int position) {
		// TODO Auto-generated method stub
		return mXiangqing.data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	public void changeColor(int postion){
		this.mpostion=postion;
		notifyDataSetChanged();
	}
		public void dataChange(IqiyiBean xiangQingItem){
			if(xiangQingItem!=null){
				this.mXiangqing=xiangQingItem;
				notifyDataSetChanged();
			}

		}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
	final ViewHolder vh;
	final int pos=position;
		View view=convertView;
		if(view==null){
			vh=new ViewHolder();
			view=View.inflate(mContext, R.layout.jishuadapter, null);
			 vh.button = (Button) view.findViewById(R.id.jishu);
			 view.setTag(vh);
		 }else{
			 vh=(ViewHolder) view.getTag();
		 }
		if(mpostion==position){
			vh.button.setBackgroundColor(Color.parseColor("#4392f3"));
			vh.button.setTextColor(Color.parseColor("#ffffff"));
		}else {
			vh.button.setBackgroundResource(R.drawable.moviebackground);
			vh.button.setTextColor(Color.parseColor("#000000"));

		}
		if(position==9){
			vh.button.setText("...");
		}else {
			vh.button.setText(pos + 1 + "");
		}
		dataBean = mXiangqing.data.get(position);
		vh.button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(vh.button.getText().toString().equals("...")){
					EventBus.getDefault().post(mXiangqing);
				}else {
					if(!dataBean.gm_intent.gm_is.isEmpty()||dataBean.gm_intent.gm_is.size()>0){
						VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30000,"2", GMSdkCheck.appId,new VcontrolCmd.ThirdPlay(dataBean.gm_intent.n,
								dataBean.gm_intent.o,
								dataBean.gm_intent.u,
								dataBean.gm_intent.p,
								dataBean.gm_intent.gm_is.get(0).i,
								dataBean.gm_intent.gm_is.get(0).m),null,null,null,null)));
					}else {
						Toast.makeText(mContext,"播放来源出问题了",Toast.LENGTH_SHORT).show();
					}
				}
				changeColor(pos);
			}
		});
		return view;
	}
	public class ViewHolder{
		public Button button;
	}
	//发送json数据给投影一
		public String sendJson(String id,String jishiu,String name){
				JSONObject jsonObject=new JSONObject();
				JSONObject js2=new JSONObject();
						try {
					jsonObject.put("type","1");
					jsonObject.put("id", id);
					jsonObject.put("name", name);
					jsonObject.put("number", jishiu);
					js2.put("data", jsonObject);
					js2.put("action", 0);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(js2.toString());
				return js2.toString();
		}
}
