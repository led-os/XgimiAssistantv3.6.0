package com.xgimi.zhushou.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.Record;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by 霍长江 on 2016/8/19.
 */
public class RecordAdapter1  extends BaseAdapter {

    private Activity mActivity;
    private List<Record> mrecords = new ArrayList<>();

    public RecordAdapter1(Activity activity, ArrayList<Record> record) {
        this.mActivity = activity;
        this.mrecords = record;
    }

    public void changeData(ArrayList<Record> record) {
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
    public Record getItem(int position) {
        // TODO Auto-generated method stub
        return mrecords.get(mrecords.size()-position-1);
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
        final Record record=mrecords.get(mrecords.size()-position-1);
        if (view == null) {
            vh = new ViewHolder();
            view = View.inflate(mActivity, R.layout.record_item, null);
            vh.tv=(TextView) view.findViewById(R.id.record_tv);
            vh.iv=(ImageView) view.findViewById(R.id.delete);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        vh.tv.setText(record.getName());
        vh.iv.setOnClickListener(new View.OnClickListener() {

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

