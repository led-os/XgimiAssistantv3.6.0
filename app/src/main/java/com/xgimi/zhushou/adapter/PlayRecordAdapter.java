package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.FilmDetailBean;
import com.xgimi.zhushou.bean.PlayHostory;
import com.xgimi.zhushou.util.SaveData;

import java.util.List;

import de.greenrobot.event.EventBus;

public class PlayRecordAdapter extends BaseAdapter {

    public Context mcontext;
    public List<PlayHostory> hostory;

    public PlayRecordAdapter(Context context, List<PlayHostory> hostorys) {
        this.mcontext = context;
        this.hostory = hostorys;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return hostory.size() == 0 ? 0 : hostory.size();
    }

    @Override
    public PlayHostory getItem(int position) {
        // TODO Auto-generated method stub
        return hostory.get(hostory.size() - position - 1);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public void dataChange(List<PlayHostory> hostorys) {
        this.hostory = hostorys;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = View.inflate(mcontext, R.layout.play_record_item, null);
            vh.iv = (ImageView) convertView.findViewById(R.id.detailfile_icon);
            vh.tv = (TextView) convertView.findViewById(R.id.fileName);
            vh.tv2 = (TextView) convertView.findViewById(R.id.fileTime);
            vh.tv1 = (TextView) convertView.findViewById(R.id.time);
            vh.playtime = (TextView) convertView.findViewById(R.id.playtime);
            vh.play_now = (TextView) convertView.findViewById(R.id.play_now);
            convertView.findViewById(R.id.play_now);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
//        vh.play_now.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SaveData.getInstance().hositoryId = hostory.get(hostory.size() - 1 - position).getId();
//                EventBus.getDefault().post(new FilmDetailBean());
//            }
//        });
        PlayHostory play = hostory.get(hostory.size() - position - 1);
        vh.tv.setText(play.title);
        vh.playtime.setText(play.time);
        vh.tv2.setText(play.kind);
        ImageLoader.getInstance().displayImage(play.icon, vh.iv);
        return convertView;
    }

    class ViewHolder {
        public ImageView iv;
        public TextView tv;
        public TextView tv1, tv2, playtime;
        public TextView play_now;
    }

}
