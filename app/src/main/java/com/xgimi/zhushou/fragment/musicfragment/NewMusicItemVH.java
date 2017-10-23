package com.xgimi.zhushou.fragment.musicfragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xgimi.zhushou.R;

/**
 * Created by linzh on 2017/10/23.
 */

public class NewMusicItemVH extends NewMusicBaseVH {

    private ImageView iv1, iv2, iv3;
    private TextView tv1, tv2, tv3;
    public NewMusicItemVH(View itemView) {
        super(itemView);
        iv1 = (ImageView) itemView.findViewById(R.id.iv_cover_1_music_item);
        iv2 = (ImageView) itemView.findViewById(R.id.iv_cover_2_music_item);
        iv3 = (ImageView) itemView.findViewById(R.id.iv_cover_3_music_item);

        tv1 = (TextView) itemView.findViewById(R.id.tv_name_1_music_item);
        tv2 = (TextView) itemView.findViewById(R.id.tv_name_2_music_item);
        tv3 = (TextView) itemView.findViewById(R.id.tv_name_3_music_item);
    }

    public ImageView getIv1() {
        return iv1;
    }

    public ImageView getIv2() {
        return iv2;
    }

    public ImageView getIv3() {
        return iv3;
    }

    public TextView getTv1() {
        return tv1;
    }

    public TextView getTv2() {
        return tv2;
    }

    public TextView getTv3() {
        return tv3;
    }
}
