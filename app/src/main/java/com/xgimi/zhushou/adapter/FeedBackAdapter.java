package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.fb.model.Reply;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.GimiUser;
import com.xgimi.zhushou.bean.YiJianBeen;
import com.xgimi.zhushou.widget.yuanxing;

import java.util.List;

public class FeedBackAdapter extends BaseAdapter{

	private Context mcontext;
	private List<Reply> replys;
	List<YiJianBeen> data;
	private String type;
	private GimiUser loginInfo;
	public FeedBackAdapter(Context context, GimiUser loginInfo,List<YiJianBeen> data){
		this.mcontext=context;
		this.data=data;
		this.loginInfo=loginInfo;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	public void dataChange(List<YiJianBeen> data){

		this.data=data;
		notifyDataSetChanged();
	}


	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View view=arg1;
		ViewHolder vh;
		if(view==null){
			vh=new ViewHolder();
			view=View.inflate(mcontext, R.layout.message_item, null);
			vh.rl_louzhu=(LinearLayout) view.findViewById(R.id.rl_louzhu);
			vh.louzhu=(TextView) view.findViewById(R.id.louzhu);
			vh.rl_user=(RelativeLayout)view.findViewById(R.id.rl_user);
			vh.user=(TextView) view.findViewById(R.id.user);
			vh.iv_user=(yuanxing) view.findViewById(R.id.iv_user);
			view.setTag(vh);
		}else {
			vh=(ViewHolder) view.getTag();
		}
		if(loginInfo!=null){
			if(!loginInfo.data.avatar.equals("")){
				ImageLoader.getInstance().displayImage(loginInfo.data.avatar, vh.iv_user);
			}
		}
		String reply = data.get(arg0).getType();
//		if(reply.equals("Reply")){
//			vh.rl_louzhu.setVisibility(View.GONE);
//			vh.rl_user.setVisibility(View.VISIBLE);
////			vh.user.setText(replys.get(arg0).getContent());
//			vh.user.setText(data.get(arg0));
//		}else if(reply.equals("Reply")){
//			vh.rl_louzhu.setVisibility(View.GONE);
//			vh.rl_user.setVisibility(View.VISIBLE);
////			vh.user.setText(replys.get(arg0).getContent());
//			vh.user.setText(data.get(arg0));
//		}else
		if(reply.equals("DevReply"))
		{
//			vh.louzhu.setText(replys.get(arg0).getContent());
			vh.louzhu.setText(data.get(arg0).getData());
			vh.rl_louzhu.setVisibility(View.VISIBLE);
			vh.rl_user.setVisibility(View.GONE);
		}else {
			vh.rl_louzhu.setVisibility(View.GONE);
			vh.rl_user.setVisibility(View.VISIBLE);
//			vh.user.setText(replys.get(arg0).getContent());
			vh.user.setText(data.get(arg0).getData());
		}

		return view;
	}
	class ViewHolder
	{
		public LinearLayout rl_louzhu;
		public TextView louzhu;
		public RelativeLayout rl_user;
		public TextView user;
		public yuanxing iv_user;
	}
}
