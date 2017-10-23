package com.xgimi.zhushou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.AllRadioClass;
import com.xgimi.zhushou.netUtil.ImageLoaderUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/9/6.
 */
public class AllClassGridAdapter extends BaseAdapter {
    private Context mcontext;
    private List<AllRadioClass.data> data;
    private DisplayImageOptions options;

    public AllClassGridAdapter(Context context, List<AllRadioClass.data> data) {
        this.mcontext = context;
        this.data = data;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data != null ? data.size() : 0;
    }

    public void dataChange(List<AllRadioClass.data> sea) {
        this.data = sea;
        notifyDataSetChanged();
    }

    @Override
    public AllRadioClass.data getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder vh = null;
        View view = convertView;
        if (view == null) {
            vh = new ViewHolder();
            view = View.inflate(mcontext, R.layout.radio_all_grid_class, null);
            vh.image1 = (ImageView) view.findViewById(R.id.image1);
//            vh.image2= (ImageView)  view.findViewById(R.id.image2);
//            vh.image3= (ImageView)  view.findViewById(R.id.image3);
            vh.tv1 = (TextView) view.findViewById(R.id.text1);
//            vh.tv2= (TextView) view.findViewById(R.id.text2);
//            vh.tv3= (TextView)  view.findViewById(R.id.text3);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        ImageLoader.getInstance().displayImage(data.get(position).category_thumb, vh.image1);
        vh.tv1.setText(data.get(position).category_name);
//        ImageLoaderUtils.display(mcontext,vh.image2, data.get(position*3 + 1).category_thumb);
//       vh.tv2.setText(data.get(position*3 + 1).category_name);
//        ImageLoaderUtils.display(mcontext, vh.image3, data.get(position*3 + 2).category_thumb);
//        vh.tv3.setText(data.get(position*3 + 2).category_name);
        return view;
    }

    class ViewHolder {
        private ImageView image1;
        private TextView tv1;
    }
}
