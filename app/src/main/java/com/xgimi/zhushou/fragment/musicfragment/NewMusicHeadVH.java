package com.xgimi.zhushou.fragment.musicfragment;

import android.view.View;
import android.widget.TextView;

import com.xgimi.zhushou.R;

/**
 * Created by linzh on 2017/10/23.
 */

public class NewMusicHeadVH extends NewMusicBaseVH {
    private TextView tvName;

    public NewMusicHeadVH(View itemView) {
        super(itemView);
        tvName = (TextView) itemView.findViewById(R.id.tv_music_head_name);
    }

    public TextView getTvName() {
        return tvName;
    }
}
