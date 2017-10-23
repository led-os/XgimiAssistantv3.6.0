package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.xgimi.device.device.GMDeviceController;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.FilmDetailBean;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * Created by 霍长江 on 2016/8/16.
 */
public class AllNumberAdapter extends BaseAdapter{

    public Context mContext;
    private int mpostion=-1;
    public int page;
    public FilmDetailBean.DataBean mXiangqing;
    private Button tv;
    int getshu;
    private String s;
    public AllNumberAdapter(Context context, FilmDetailBean.DataBean xiangQingItem){
        this.mContext=context;
        this.mXiangqing=xiangQingItem;

    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if(mXiangqing==null){
            return 0;
        }
        return mXiangqing!=null&&mXiangqing.updateto<10?mXiangqing.updateto:10;
    }

    @Override
    public FilmDetailBean.DataBean getItem(int position) {
        // TODO Auto-generated method stub
        return mXiangqing;
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
    public void dataChange(FilmDetailBean.DataBean xiangQingItem){
        if(xiangQingItem!=null){
            this.mXiangqing=xiangQingItem;
            notifyDataSetChanged();
        }

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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
            vh.button.setText(pos + 1 + "");
        vh.button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(vh.button.getText().toString().equals("...")){
                    EventBus.getDefault().post(mXiangqing);
//
                }else {
                    GMDeviceController.getInstance().SendJC(sendJson(mXiangqing.id, vh.button.getText().toString().trim(), mXiangqing.title));
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
