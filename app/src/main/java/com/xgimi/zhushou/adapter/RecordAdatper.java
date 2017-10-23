package com.xgimi.zhushou.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.SearchLiveRecord;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class RecordAdatper extends BaseAdapter {

    private Activity mActivity;
    private List<SearchLiveRecord> mrecords = new ArrayList<>();

    public RecordAdatper(Activity activity, List<SearchLiveRecord> record) {
        this.mActivity = activity;
        this.mrecords = record;
    }

    public void changeData(ArrayList<SearchLiveRecord> record) {
        if (record != null) {
            this.mrecords = record;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mrecords.size();
    }

    @Override
    public SearchLiveRecord getItem(int position) {
        // TODO Auto-generated method stub
        return mrecords.get(mrecords.size() - position - 1);
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
        View view = convertView;
        final SearchLiveRecord record = mrecords.get(mrecords.size() - position - 1);
        if (view == null) {
            vh = new ViewHolder();
            view = View.inflate(mActivity, R.layout.record_item, null);
            vh.tv = (TextView) view.findViewById(R.id.record_tv);
            vh.iv = (ImageView) view.findViewById(R.id.delete);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        vh.tv.setText(record.getName());
        vh.iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EventBus.getDefault().post(record);
            }
        });
        return view;
    }

    class ViewHolder {
        public TextView tv;
        public ImageView iv;
    }
}
