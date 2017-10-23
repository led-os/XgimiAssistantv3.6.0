package com.xgimi.zhushou.adapter.RecommendViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.xgimi.zhushou.R;

/**
 * Created by 霍长江 on 2016/8/15.
 */
public class AppItemSecondViewHolder extends AppItemViewHolder{
    public TextView name,name1,name2;
    public ImageView image;
    public ImageView image1;
    public ImageView image2;
    public AppItemSecondViewHolder(View itemView) {
        super(itemView);
        initView(itemView);
    }
    private void initView(View view){
        image = (ImageView) view.findViewById(R.id.image);
        image1 = (ImageView) view.findViewById(R.id.image1);
        image2 = (ImageView) view.findViewById(R.id.image2);
        name = (TextView) view.findViewById(R.id.name);
        name1 = (TextView) view.findViewById(R.id.name1);
        name2 = (TextView) view.findViewById(R.id.name2);
    }
}
