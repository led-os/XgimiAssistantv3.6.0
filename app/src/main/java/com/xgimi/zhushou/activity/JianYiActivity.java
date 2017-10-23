package com.xgimi.zhushou.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.Conversation.SyncListener;
import com.umeng.fb.model.DevReply;
import com.umeng.fb.model.Reply;
import com.umeng.fb.model.UserInfo;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.adapter.FeedBackAdapter;
import com.xgimi.zhushou.bean.GimiUser;
import com.xgimi.zhushou.bean.YiJianBeen;
import com.xgimi.zhushou.db.RecordDao;
import com.xgimi.zhushou.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JianYiActivity extends BaseActivity implements
OnClickListener,OnTouchListener{

	private TextView send;
	private EditText editText;
	public App app;
	private List<YiJianBeen> mData=new ArrayList<>();
	private RecordDao mDao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jian_yi);
		initView();
		initData();
	}
	@SuppressLint("ShowToast")
	private void initView(){

		mDao = new RecordDao(this);
		app = (App) getApplicationContext();
		GimiUser loginInfo = app.getLoginInfo();
		send = (TextView) findViewById(R.id.fasong);
		TextView tv = (TextView) findViewById(R.id.tv_ivremount);
		tv.setVisibility(View.GONE);
		editText = (EditText) findViewById(R.id.et);
		listview = (ListView) findViewById(R.id.listview);

		agent=new FeedbackAgent(this);
		info = new UserInfo();
		Map<String, String> map = info.getContact();
		if(app.getLoginInfo()!=null){
			map.put("plain", app.getLoginInfo().data.username);
		}else{
			map.put("plain", "niming");
		}
		info.setContact(map);
		agent.setUserInfo(info);
		back = (ImageView) findViewById(R.id.back);

		back(back);

		defaultConversation = agent.getDefaultConversation();
		defaultConversation.sync(new SyncListener() {

			@Override
			public void onSendUserReply(List<Reply> arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onReceiveDevReply(List<DevReply> arg0) {
				// TODO Auto-generated method stub
				if(arg0!=null&&arg0.size()>0) {
					for (int i=0;i<arg0.size();i++){
						mData.add(new YiJianBeen(defaultConversation.getReplyList().get(i).getContent(),"DevReply"));
						mDao.addYiJianRecord(defaultConversation.getReplyList().get(i).getContent(),"DevReply");
					}
				}else {
					//mData.add(new YiJianBeen("请问您有什么问题？","DevReply"));
				}

				mHandler.sendEmptyMessageDelayed(2, 1000);
			}
		});
//	List<Reply> replyList = defaultConversation.getReplyList();
//	for(int i=0;i<replyList.size();i++){
//		mData.add(new YiJianBeen(replyList.get(i).getContent(),"Reply"));
//	}
		mData=mDao.getYiJianRecord();
		adapter = new FeedBackAdapter(this,loginInfo,mData);
		listview.setAdapter(adapter);
//	listview.setSelection(listview.getBottom());
		listview.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listview.setStackFromBottom(true);

	}

	Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 0:
					adapter.dataChange(mData);
					mHandler.sendEmptyMessageDelayed(3, 500);

					mData.add(new YiJianBeen("已收到您的反馈，我们会尽快处理，谢谢！","DevReply"));
					mDao.addYiJianRecord("已收到您的反馈，我们会尽快处理，谢谢！","DevReply");
					mHandler.sendEmptyMessageDelayed(2, 1000);
					MissDilog();
					break;
				case 2:
					adapter.dataChange(mData);
//			listview.setSelection(listview.getBottom());
					listview.post(new Runnable() {
						@Override
						public void run() {
							// Select the last row so it will scroll into view...
							listview.setSelection(adapter.getCount() - 1);
						}
					});
					break;
				case 3:
					listview.setSelection(listview.getBottom());

					break;

				default:
					break;
			}

		}
	};
	private FeedbackAgent agent;
	private Conversation defaultConversation;
	private ImageView back;
	private UserInfo info;
	private FeedBackAdapter adapter;
	private ListView listview;
	private void initData(){
		send.setOnClickListener(this);

	}




	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}
	/**
	 * 使用友盟反馈
	 *
	 * @param Content
	 */
	private void send(String Content) {

//	agent.setUserInfo(info);


//	info = new UserInfo();
//	Map<String, String> map = info.getContact();
//	if(app.getLoginInfo()!=null){
//	    map.put("plain", app.getLoginInfo().data.username);
//
//    }else{
//    	map.put("plain", "niming");
//    }
//	info.setContact(map);
//	agent.setUserInfo(info);
//	defaultConversation = agent.getDefaultConversation();
		defaultConversation.addUserReply(Content);
		defaultConversation.sync(new SyncListener() {

			@Override
			public void onSendUserReply(List<Reply> arg0) {
				Log.e("yijian",arg0+"");
				for(int i=0;i<arg0.size();i++){
					mData.add(new YiJianBeen(arg0.get(i).getContent(),"Reply"));
					mDao.addYiJianRecord(arg0.get(i).getContent(),"Reply");
				}
				mHandler.sendEmptyMessageDelayed(0, 500);
			}

			@Override
			public void onReceiveDevReply(List<DevReply> arg0) {

			}
		});


//	defaultConversation.addUserReply(Content);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.fasong:
				String Content = editText.getText().toString();
				if (StringUtils.isEmpty(Content)) {
					Toast.makeText(JianYiActivity.this, R.string.feedback_null, Toast.LENGTH_SHORT).show();
				} else if (Content.length() < 5) {
					Toast.makeText(JianYiActivity.this, R.string.feedback_saymore, Toast.LENGTH_SHORT).show();
				} else {
					send(Content);
					editText.setText("");
					showDilog("意见反馈中...");
//			Toast.makeText(JianYiActivity.this, R.string.feedback_ok, 0).show();
//			finish();
				}
				break;

			default:
				break;
		}
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.back:
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						back.setAlpha(0.6f);
						break;
					case MotionEvent.ACTION_UP:
						back.setAlpha(1.0f);
						break;
				}
				break;

		}
		return false;
	}
	
}
