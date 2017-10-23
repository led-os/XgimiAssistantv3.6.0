package com.xgimi.zhushou.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgimi.device.device.FeedbackInfo;
import com.xgimi.device.device.GMDeviceStorage;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.FileInfo;
import com.xgimi.zhushou.bean.UserBean;
import com.xgimi.zhushou.bean.YiJianBeen;
import com.xgimi.zhushou.util.SaveData;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.util.XGIMILOG;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2016/12/6.
 */
public class AlreadyTransferFileAdapter extends BaseAdapter {
    private List<FeedbackInfo.fileLists> info;
    private Activity context;
    private boolean isVISIBLE = false;
    public static Map<Integer, FeedbackInfo.fileLists> maps = new HashMap<>();
    public static Map<Integer, FeedbackInfo.fileLists> mapsbeen = new HashMap<>();

    public AlreadyTransferFileAdapter(Activity context, List<FeedbackInfo.fileLists> info, boolean isVISIBLE) {
        this.context = context;
        this.info = info;
        this.isVISIBLE = isVISIBLE;
    }

    @Override
    public int getCount() {
        return info != null && info.size() > 0 ? info.size() : 0;
//        return 10;
    }

    public void dataChange(Boolean isVISIBLE, List<FeedbackInfo.fileLists> info) {
        this.isVISIBLE = isVISIBLE;
        this.info = info;
        notifyDataSetChanged();
    }

    public void dataChange(List<FeedbackInfo.fileLists> info) {
        this.info = info;
        notifyDataSetChanged();
    }

    @Override
    public List<FeedbackInfo.fileLists> getItem(int i) {
        return info;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    //点击时候的回调
    getIds ids;

    public void setLisener(getIds id) {
        this.ids = id;
    }

    public interface getIds {
        void getMenmberIds(Map<Integer, FeedbackInfo.fileLists> beans, Map<Integer, FeedbackInfo.fileLists> cleanBeen, boolean isAdd);
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View myView = view;
        final ViewHolder vh;
        final int index = i;
        if (myView == null) {
            vh = new ViewHolder();
            myView = View.inflate(context, R.layout.already_transfer_file_item, null);
            vh.name = (TextView) myView.findViewById(R.id.name);
            vh.time = (TextView) myView.findViewById(R.id.time);
            vh.image = (ImageView) myView.findViewById(R.id.image);
            vh.select = (ImageView) myView.findViewById(R.id.select);
            vh.xian = myView.findViewById(R.id.xian);
            myView.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        //判断是不是显示可以打钩
        if (isVISIBLE) {
            vh.select.setVisibility(View.VISIBLE);
        } else {
            vh.select.setVisibility(View.GONE);
        }
        //记住之前打过沟沟的、
        if (info.get(index).type == UserBean.TYPE_CHECKED) {
            vh.select.setImageResource(R.drawable.xuanzhong);
        } else {
            vh.select.setImageResource(R.drawable.weixuanzhong);
        }
        String name = getFileName(info.get(i).Filepath) + "." + getHouZhui(info.get(i).Filepath);
        vh.name.setText(name);
        vh.time.setText(info.get(i).time);
        if (info.get(i).Imagepath != null) {
            String image_url;
            //将返回的url里的s%转换为自己的ip地址
            try {
                image_url = String.format(info.get(i).Imagepath, GMDeviceStorage.getInstance().getConnectedDevice().ip);
                ImageLoader.getInstance().displayImage(image_url, vh.image);
            } catch (Exception e) {
                XGIMILOG.E(e.getMessage());
            }
        } else if (info.get(i).Type.equals("mp4") || info.get(i).Type.equals("rmvb") || info.get(i).Type.equals("mkv") || info.get(i).Type.equals("mov")
                || info.get(i).Type.equals("flv")) {
            vh.image.setImageResource(R.drawable.morenshiping);
        } else if (info.get(i).Type.equals("mp3") || info.get(i).Type.equals("wav") || info.get(i).Type.equals("m4a")) {
            vh.image.setImageResource(R.drawable.morenyinyue);
        } else if (info.get(i).Type.equals("apk") || info.get(i).Type.equals("txt") || info.get(i).Type.equals("pptx") || info.get(i).Type.equals("ppt") || info.get(i).Type.equals("pdf")
                || info.get(i).Type.equals("xls") || info.get(i).Type.equals("xlsx") || info.get(i).Type.equals("doc") || info.get(i).Type.equals("docx")) {
            vh.image.setImageResource(R.drawable.morenwendang);
        }
        myView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!isVISIBLE) {
                    SaveData.getInstance().fileTransferPos = i;
                    EventBus.getDefault().post(new YiJianBeen());
                }
                return true;
            }
        });
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVISIBLE) {
                    //勾选
                    if (info.get(index).type == UserBean.TYPE_CHECKED) {
                        info.get(index).type = UserBean.TYPE_NOCHECKED;
                        if (ids != null) {
                            if (maps != null && maps.size() > 0) {
                                vh.select.setImageResource(R.drawable.weixuanzhong);
                                maps.remove(index);
                                mapsbeen.remove(index);
                                mapsbeen.put(index, info.get(index));
                                ids.getMenmberIds(maps, mapsbeen, false);
                                mapsbeen.remove(index);
                            }
                        }
                    } else {
                        info.get(index).type = UserBean.TYPE_CHECKED;
                        if (ids != null) {
                            vh.select.setImageResource(R.drawable.xuanzhong);
                            maps.put(index, info.get(index));
                            mapsbeen.put(index, info.get(index));
                            ids.getMenmberIds(maps, mapsbeen, true);
                        }
                    }
                } else {
                    SaveData.getInstance().fileTransferPos = i;
                    EventBus.getDefault().post(new FileInfo());
                }
            }
        });

        return myView;
    }

    public class ViewHolder {
        public TextView name, time;
        public ImageView image, select;
        public View xian;
    }

    //从url中获取图片的文件名
    public String getFileName(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return null;
        }
    }

    //从url中获取图片后缀名
    public String getHouZhui(String url) {
        String prefix = url.substring(url.lastIndexOf(".") + 1);
        return prefix;
    }
}
