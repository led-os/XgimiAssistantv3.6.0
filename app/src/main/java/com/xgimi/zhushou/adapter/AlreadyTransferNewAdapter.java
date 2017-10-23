package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgimi.device.device.FeedbackInfo;
import com.xgimi.zhushou.R;

import java.util.List;

/**
 * Created by Administrator on 2016/12/7.
 */
public class AlreadyTransferNewAdapter extends BaseAdapter{
    private List<FeedbackInfo.fileLists> info;
    private Context context;
    @Override
    public int getCount() {
        return info!=null&&info!=null&&info.size()>0?info.size():0;
    }
    public void dataChange(List<FeedbackInfo.fileLists> info){
        this.info=info;
        notifyDataSetChanged();
    }
    public AlreadyTransferNewAdapter(Context context,List<FeedbackInfo.fileLists> info){
        this.context=context;
        this.info=info;
    }
    @Override
    public List<FeedbackInfo.fileLists> getItem(int position) {
        return info;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View myView =convertView;
        ViewHolder vh;
        if(myView==null){
            vh=new ViewHolder();
            myView=View.inflate(context, R.layout.already_transfer_file_item,null);
            vh.name= (TextView) myView.findViewById(R.id.name);
            vh.time= (TextView) myView.findViewById(R.id.time);
            vh.image= (ImageView) myView.findViewById(R.id.image);
            vh.select= (ImageView) myView.findViewById(R.id.select);
            vh.xian=myView.findViewById(R.id.xian);
            myView.setTag(vh);
        }else {
            vh= (ViewHolder) convertView.getTag();
        }
        String name=getFileName(info.get(position).Filepath)+"."+getHouZhui(info.get(position).Filepath);
        vh.name.setText(name);
        vh.time.setText(info.get(position).time);
        if(info.get(position).Imagepath!=null){
            ImageLoader.getInstance().displayImage(info.get(position).Imagepath,vh.image);
        }
        if(position==info.size()-1){
            vh.xian.setVisibility(View.GONE);
        }
        return myView;
    }
    public class ViewHolder{
        public TextView name,time;
        public ImageView image,select;
        public View xian;
    }
    //从url中获取图片的文件名
    public String getFileName(String pathandname){
        int start=pathandname.lastIndexOf("/");
        int end=pathandname.lastIndexOf(".");
        if(start!=-1 && end!=-1){
            return pathandname.substring(start+1,end);
        }else{
            return null;
        }
    }
    //从url中获取图片后缀名
    public String getHouZhui(String url){
        String prefix=url.substring(url.lastIndexOf(".")+1);
        return prefix;
    }
}
