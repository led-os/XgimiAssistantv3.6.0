package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.FilmDetailBean;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;
import com.xgimi.zhushou.util.ToosUtil;

import java.util.List;

/**
 * Created by 霍长江 on 2016/8/16.
 */
public class SourceAdapter extends BaseAdapter{
    private Context mContext;
    private List<FilmDetailBean.DataBean.SourceBean> mDatas;
    public SourceAdapter(Context context,List<FilmDetailBean.DataBean.SourceBean> data){
        this.mContext=context;
        this.mDatas=data;
    }

    public void dataChange(List<FilmDetailBean.DataBean.SourceBean> data){
        this.mDatas=data;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public FilmDetailBean.DataBean.SourceBean getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        View view=convertView;
        if(view==null){
            vh=new ViewHolder();
            view=View.inflate(mContext, R.layout.source_item,null);
            vh.iv= (ImageView) view.findViewById(R.id.iv);
            vh.tv= (TextView) view.findViewById(R.id.tv_name);
            vh.tv1= (TextView) view.findViewById(R.id.install);
            view.setTag(vh);
        }else{
            vh= (ViewHolder) view.getTag();

        }
        for (int i = 0; i < ToosUtil.getInstance().mInstallPacageNames.size(); i++) {
            if(mDatas.get(position).gm_intent.p.equals(ToosUtil.getInstance().mInstallPacageNames.get(i))){
                vh.tv1.setText("已安装");
                vh.tv1.setTextColor(mContext.getResources().getColor(R.color.color_bule));
            }
            if ("com.hunantv.market".equals(mDatas.get(position).gm_intent.p) && ToosUtil.getInstance().mInstallPacageNames.get(i).equals("com.hunantv.license")) {
                vh.tv1.setText("已安装");
                vh.tv1.setTextColor(mContext.getResources().getColor(R.color.color_bule));
            }
        }

        ImageLoaderUtils.display(mContext,vh.iv,mDatas.get(position).icon);
        vh.tv.setText(mDatas.get(position).gm_intent.n);
        return view;
    }

    public class ViewHolder{
        public ImageView iv;
        public TextView tv;
        public TextView tv1;
    }
}
