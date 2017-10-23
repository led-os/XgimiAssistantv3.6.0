package com.xgimi.zhushou.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xgimi.device.device.GMDeviceController;
import com.xgimi.device.device.GMKeyCommand;
import com.xgimi.device.device.VcontrolCmd;
import com.xgimi.device.socket.UdpManager;
import com.xgimi.device.util.GMSdkCheck;
import com.xgimi.device.vcontrolcmd.VcontrolControl;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.ImageInfo;
import com.xgimi.zhushou.netUtil.HttpServer;
import com.xgimi.zhushou.util.Constant;
import com.xgimi.zhushou.util.GlobalConsts;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

public class PageViewImage {
    Dialog mDialog;
    Context contetn;
    private List<ImageInfo> childPathList;
    private TextView tv;
    private int totalSize;
    private boolean isTuiSong;
    private ImageView iv;
    private long lastSendTime; // 上一次推送时间
    private long currentSendTime; // 当前推送时间
    private GMKeyCommand gmKeyCommand;
    private PhonoViewPager viewpager;
    int mpos;

    public PageViewImage(Context context, String parentPath, int postion) {
        LayoutInflater inflater = LayoutInflater.from(context);
        this.contetn = context;
        View view = inflater.inflate(R.layout.activity_phono_view_pager, null);
        mDialog = new Dialog(context, R.style.dialog);


        mDialog.setContentView(view);
        //透明状态栏

        gmKeyCommand = new GMKeyCommand();

        mDialog.setCanceledOnTouchOutside(true);
        iv = (ImageView) view.findViewById(R.id.iv);
        childPathList = GlobalConsts.mImgMap.get(parentPath);
        totalSize = childPathList.size();
        mDialog.setContentView(view);
        mpos = postion;
        Window window = mDialog.getWindow();
        LayoutParams windowparams = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        windowparams.width = LayoutParams.FILL_PARENT;
        window.setAttributes(windowparams);
        Rect rect = new Rect();
        View view1 = window.getDecorView();
        view1.getWindowVisibleDisplayFrame(rect);
//		onSendBtn();
        initView(view, postion);
        if (Constant.netStatus) {
            lastSendTime = System.currentTimeMillis();
            iv.setImageResource(R.drawable.btn_photo_sending);
            onSendBtn();
        }
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (Constant.netStatus) {
                    if (!isTuiSong) {
                        onSendBtn();
                        iv.setImageResource(R.drawable.btn_photo_sending);
                    } else {
                        overSend();
                        iv.setImageResource(R.drawable.btn_photo_send);
                    }
                } else {
                    Toast.makeText(contetn, "请先连接设备", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    List<VcontrolCmd.CustomPlay.PlayList> mPlyLists = new ArrayList<>();

    // 传送文件命令
    public void onSendBtn() {
        mPlyLists.clear();
        isTuiSong = true;
        String id = GlobalConsts.IMAGE_PREFIX + childPathList.get(mpos).getId();
        String title = childPathList.get(mpos).getUrl();
        String sendCommand = "http://" + App.mPhoneIP + ":" + HttpServer.PORT + "/" + id + UdpManager.separator + title;
//			GMDeviceController.getInstance().SendJC(sendCommand);
//			VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(GMSdkCheck.appId,
//					"2",new VcontrolCmd.ControlCmd(5,results.get(0).toString()),20000)));
        VcontrolCmd.CustomPlay.PlayList playList = new VcontrolCmd.CustomPlay.PlayList(sendCommand);
        mPlyLists.add(playList);
        VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200,
                "2", GMSdkCheck.appId, null, new VcontrolCmd.CustomPlay(2, mPlyLists, 0, 0), null, null, null)));
        //GMDeviceController.getInstance().SendJC(sendCommand);
        //下面是长江写的
////			VcontrolCmd.CustomPlay.PlayList playList=new VcontrolCmd.CustomPlay.PlayList(null,null,null,sendCommand,null,null);
////			mPlyLists.add(playList);
////			VcontrolControl.getInstance().sendNewCommand(VcontrolControl.getInstance().getConnectControl(new VcontrolCmd(30200,
////					"2", GMSdkCheck.appId, null, new VcontrolCmd.CustomPlay(2, 0, null, mPlyLists, 0), null, null, null)));
//
//					"2", GMSdkCheck.appId, null, new VcontrolCmd.CustomPlay(2, 0, null, mPlyLists, 0), null, null, null)));
    }

    /**
     * 发送返回命令，结束服务端的activity
     */
    private void overSend() {
        isTuiSong = false;
//			GMDeviceController.getInstance().SendJC(GlobalConsts.PHOTOSTOP);
        gmKeyCommand.setKey(GMKeyCommand.GMKeyEventBack);
        GMDeviceController.getInstance().sendCommand(gmKeyCommand);
    }

    private void initView(View view, int postion) {
        tv = (TextView) view.findViewById(R.id.tv);
        tv.setText((postion + 1) + "/" + totalSize);

        viewpager = (PhonoViewPager) view.findViewById(R.id.viewpager);
        PhonoViewPager viewpager = (PhonoViewPager) view.findViewById(R.id.viewpager);
        viewpager.setAdapter(new SimpleAdapter1());
        viewpager.setCurrentItem(postion);
        viewpager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                currentSendTime = System.currentTimeMillis();
                tv.setText((arg0 + 1) + "/" + totalSize);
                mpos = arg0;

                if ((currentSendTime - lastSendTime) > 500) {
                    if (isTuiSong) {
                        onSendBtn();
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void show() {
        mDialog.show();
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        mDialog.setCanceledOnTouchOutside(cancel);
    }

    public void dismiss() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();

            // animationDrawable.stop();
        }
    }

    public boolean isShowing() {
        if (mDialog.isShowing()) {
            return true;
        }
        return false;
    }

    public class SimpleAdapter1 extends PagerAdapter {

//		private static final int[] sDrawables = { R.drawable.wallpaper,
//				R.drawable.wallpaper, R.drawable.wallpaper, R.drawable.wallpaper,
//				R.drawable.wallpaper, R.drawable.wallpaper };

        public SimpleAdapter1() {
        }


        @Override
        public int getCount() {
            return totalSize;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
//			photoView.setImageResource(sDrawables[position]);
            String imageUri = "file://" + childPathList.get(position).getUrl().toString();
            ImageLoader.getInstance().displayImage(imageUri, photoView);
//			PhotoViewAttacher attacher=new PhotoViewAttacher(photoView);
//			attacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
//				@Override
//				public void onPhotoTap(View view, float x, float y) {
//					// TODO Auto-generated method stub
//					overSend();
//					mDialog.dismiss();
//				}
//			});
//			attacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
//
//				@Override
//				public void onViewTap(View view, float x, float y) {
//					// TODO Auto-generated method stub
//					overSend();
//					mDialog.dismiss();
//
//
//				}
//			});
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


}
