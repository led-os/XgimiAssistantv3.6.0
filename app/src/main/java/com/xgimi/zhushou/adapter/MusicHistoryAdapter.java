package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.activity.MusicSearchActivity;
import com.xgimi.zhushou.db.MusicHistoryDao;

import java.util.List;

/**
 * Created by XGIMI on 2017/7/5.
 */

public class MusicHistoryAdapter extends BaseAdapter {

    private List<String> mMusicHistoryList;

    private MusicHistoryDao mMusicHistoryDao;
    private Context mContext;

    public MusicHistoryAdapter(List<String> mMusicHistoryList, MusicHistoryDao mMusicHistoryDao, Context mContext) {
        this.mMusicHistoryList = mMusicHistoryList;
        this.mMusicHistoryDao = mMusicHistoryDao;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mMusicHistoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMusicHistoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<String> getMusicHistoryList() {
        return mMusicHistoryList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.record_item, null);
            holder = new ViewHolder();
            holder.contentTv = (TextView) convertView.findViewById(R.id.record_tv);
            holder.deleteImg = (ImageView) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.contentTv.setText(mMusicHistoryList.get(position));
        holder.deleteImg.setOnClickListener(new OnDeleteImgClickListener(position));
        return convertView;
    }

    class ViewHolder {
        TextView contentTv;
        ImageView deleteImg;
    }

    class OnDeleteImgClickListener implements View.OnClickListener {

        private int deletePostion;
        private String deleteContent;

        public OnDeleteImgClickListener(int deletePostion) {
            this.deletePostion = deletePostion;
            deleteContent = mMusicHistoryList.get(deletePostion);
        }

        @Override
        public void onClick(View v) {
            mMusicHistoryDao.deleteMusicSearchRecod(deleteContent);
            MusicHistoryAdapter.this.getMusicHistoryList().remove(deletePostion);
            MusicHistoryAdapter.this.notifyDataSetChanged();
            if (MusicHistoryAdapter.this.getMusicHistoryList().isEmpty()) {
                ((MusicSearchActivity)mContext).hideMusicHistoryLayout();
            }
        }
    }
}
