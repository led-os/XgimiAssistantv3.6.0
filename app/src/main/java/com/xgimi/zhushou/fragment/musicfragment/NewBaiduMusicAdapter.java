package com.xgimi.zhushou.fragment.musicfragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.music.model.Artist;
import com.baidu.music.model.BaseObject;
import com.baidu.music.model.Playlist;
import com.baidu.music.model.PlaylistItems;
import com.baidu.music.model.TopList;
import com.bumptech.glide.Glide;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.NewMusicHomeBean;
import com.xgimi.zhushou.bean.NewMusicMyTopListBean;
import com.xgimi.zhushou.util.XGIMILOG;

import java.util.List;

import butterknife.OnClick;

/**
 * Created by linzh on 2017/10/23.
 */

public class NewBaiduMusicAdapter extends RecyclerView.Adapter<NewMusicBaseVH> {


    private static final int VIEW_TYPE_SINGER = 1;
    private static final int VIEW_TYPE_TOP_LIST = 2;
    private static final int VIEW_TYPE_PLAY_LIST = 3;
    private static final int VIEW_TYPE_HEAD = 4;
    private static final int VIEW_TYPE_FOOT = 5;
    private List<BaseObject> mDataList;
    private Context mContext;

    private OnFootClickListener mFootClickListener;
    private OnItemClickListenr mItemClickListener;


    public NewBaiduMusicAdapter(List<BaseObject> mDataList, Context mContext) {
        this.mDataList = mDataList;
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        BaseObject object = mDataList.get(position);
        if (object instanceof NewMusicHomeBean.HeadBean) {
            return VIEW_TYPE_HEAD;
        } else if (object instanceof NewMusicHomeBean.FootBean) {
            return VIEW_TYPE_FOOT;
        } else if (object instanceof NewMusicHomeBean.PlayListItemBean) {
            return VIEW_TYPE_PLAY_LIST;
        } else if (object instanceof NewMusicHomeBean.SingerItemBean) {
            return VIEW_TYPE_SINGER;
        } else if (object instanceof NewMusicHomeBean.TopListItemBean) {
            return VIEW_TYPE_TOP_LIST;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public NewMusicBaseVH onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_FOOT) {
            return new NewMusicFootVH(LayoutInflater.from(mContext).inflate(R.layout.item_layout_new_music_foot, parent, false));
        } else if (viewType == VIEW_TYPE_HEAD) {
            return new NewMusicHeadVH(LayoutInflater.from(mContext).inflate(R.layout.item_layout_new_music_head, parent, false));
        } else {
            return new NewMusicItemVH(LayoutInflater.from(mContext).inflate(R.layout.item_layout_new_music_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(NewMusicBaseVH holder, int position) {
        final BaseObject object = mDataList.get(position);

        if (holder instanceof NewMusicHeadVH) {
            NewMusicHeadVH headVH = (NewMusicHeadVH) holder;
            NewMusicHomeBean.HeadBean headBean = (NewMusicHomeBean.HeadBean) object;
            if (headBean.getType() == NewMusicHomeBean.ITEM_TYPE_SINGER) {
                headVH.getTvName().setText("歌手");
            } else if (headBean.getType() == NewMusicHomeBean.ITEM_TYPE_TOP_LIST) {
                headVH.getTvName().setText("榜单");
            }else if (headBean.getType() == NewMusicHomeBean.ITEM_TYPE_PLAY_LIST) {
                headVH.getTvName().setText("歌单");
            }

        } else if (holder instanceof NewMusicFootVH) {
            NewMusicFootVH footVH = (NewMusicFootVH) holder;
            footVH.itemView.setOnClickListener(new
                    FootClickListener(((NewMusicHomeBean.FootBean)mDataList.get(position)).getType()));
        } else {
            NewMusicItemVH itemVH = (NewMusicItemVH) holder;
            if (object instanceof NewMusicHomeBean.PlayListItemBean) {
                final NewMusicHomeBean.PlayListItemBean playListItemBean = (NewMusicHomeBean.PlayListItemBean) object;
                itemVH.getTv1().setText(playListItemBean.getPlaylistItemsList().get(0).getTitle());
                itemVH.getTv2().setText(playListItemBean.getPlaylistItemsList().get(1).getTitle());
                itemVH.getTv3().setText(playListItemBean.getPlaylistItemsList().get(2).getTitle());

                Glide.with(mContext).load(playListItemBean.getPlaylistItemsList().get(0).getPic_300())
                        .into(itemVH.getIv1());
                Glide.with(mContext).load(playListItemBean.getPlaylistItemsList().get(1).getPic_300())
                        .into(itemVH.getIv2());
                Glide.with(mContext).load(playListItemBean.getPlaylistItemsList().get(2).getPic_300())
                        .into(itemVH.getIv3());

                itemVH.getIv1().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onPlayListClick(playListItemBean.getPlaylistItemsList().get(0));
                        }
                    }
                });

                itemVH.getIv2().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onPlayListClick(playListItemBean.getPlaylistItemsList().get(1));
                        }
                    }
                });

                itemVH.getIv3().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onPlayListClick(playListItemBean.getPlaylistItemsList().get(2));
                        }
                    }
                });
            } else if (object instanceof NewMusicHomeBean.SingerItemBean) {
                final NewMusicHomeBean.SingerItemBean singerItemBean = (NewMusicHomeBean.SingerItemBean) object;
                itemVH.getTv1().setText(singerItemBean.getArtistList().get(0).mName);
                itemVH.getTv2().setText(singerItemBean.getArtistList().get(1).mName);
                itemVH.getTv3().setText(singerItemBean.getArtistList().get(2).mName);

                Glide.with(mContext).load(singerItemBean.getArtistList().get(0).mAvatarMiddle)
                        .into(itemVH.getIv1());
                Glide.with(mContext).load(singerItemBean.getArtistList().get(1).mAvatarMiddle)
                        .into(itemVH.getIv2());
                Glide.with(mContext).load(singerItemBean.getArtistList().get(2).mAvatarMiddle)
                        .into(itemVH.getIv3());

                itemVH.getIv1().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onSingerClick(singerItemBean.getArtistList().get(0));
                        }
                    }
                });

                itemVH.getIv2().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onSingerClick(singerItemBean.getArtistList().get(1));
                        }
                    }
                });

                itemVH.getIv3().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onSingerClick(singerItemBean.getArtistList().get(2));
                        }
                    }
                });

            } else if (object instanceof NewMusicHomeBean.TopListItemBean) {
                final NewMusicHomeBean.TopListItemBean topListItemBean = (NewMusicHomeBean.TopListItemBean) object;
                itemVH.getTv1().setText(topListItemBean.getTopListList().get(0).getTopList().mName);
                itemVH.getTv2().setText(topListItemBean.getTopListList().get(1).getTopList().mName);
                itemVH.getTv3().setText(topListItemBean.getTopListList().get(2).getTopList().mName);

                Glide.with(mContext).load(topListItemBean.getTopListList().get(0).getCoverUrl()).into(itemVH.getIv1());
                Glide.with(mContext).load(topListItemBean.getTopListList().get(1).getCoverUrl()).into(itemVH.getIv2());
                Glide.with(mContext).load(topListItemBean.getTopListList().get(2).getCoverUrl()).into(itemVH.getIv3());

                itemVH.getIv1().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onTopListClick(topListItemBean.getTopListList().get(0));
                        }
                    }
                });

                itemVH.getIv2().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onTopListClick(topListItemBean.getTopListList().get(1));
                        }
                    }
                });

                itemVH.getIv3().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mItemClickListener != null) {
                            mItemClickListener.onTopListClick(topListItemBean.getTopListList().get(2));
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }


    public void setFootClickListener(OnFootClickListener mFootClickListener) {
        this.mFootClickListener = mFootClickListener;
    }

    public void setItemClickListener(OnItemClickListenr mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private class FootClickListener implements View.OnClickListener{
        int type;

        public FootClickListener(int type) {
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            if (mFootClickListener != null) {
                mFootClickListener.onFootClick(type);
            }
        }
    }


    public interface OnFootClickListener {
        void onFootClick(int type);
    }

    public interface OnItemClickListenr {
        void onSingerClick(Artist artist);

        void onTopListClick(NewMusicMyTopListBean topListBean);

        void onPlayListClick(PlaylistItems playlist);
    }
}
