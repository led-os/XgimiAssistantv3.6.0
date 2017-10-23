package com.xgimi.zhushou.activity;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sigma_rt.virtualdisplay.VirtualDisplayPicHandleH264;
import com.waxrain.droidsender.SenderService;
import com.waxrain.droidsender.delegate.Global;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.util.ToosUtil;
import com.xgimi.zhushou.widget.YaokongTitle;
import com.xgimi.zhushou.yaokongqi.BaiBaoFragment;
import com.xgimi.zhushou.yaokongqi.DirectionFragment;
import com.xgimi.zhushou.yaokongqi.YuYinFragment;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * 遥控器界面
 */
public class RemountActivity extends BaseActivity implements View.OnTouchListener{

    private ImageView iv;
    private YaokongTitle baibao;
    private YaokongTitle fangxiang;
    private YaokongTitle chumo;
    private YaokongTitle youxi;
    private YaokongTitle yuyin;
    private ImageView sanjiao;
    private BaiBaoFragment baiBaoFragment;
    private YuYinFragment yuYinFragment;
    public VirtualDisplayPicHandleH264 displayPicHandleH264;

    private ImageView back;
    List<YaokongTitle> titles = new ArrayList<YaokongTitle>();

    private static Context _context = null;
    private boolean activityRunning = true;
    private ImageButton playButton = null;
    private ImageButton stopButton = null;

    private static final String STATE_MEDIAPROJECTION_RESULT_CODE = "result_mediaprojection_code";
    private static final String STATE_MEDIAPROJECTION_RESULT_DATA = "result_mediaprojection_data";
    private static final int REQUEST_MEDIA_PROJECTION = 1;
    private static int mMediaProjectionResultCode = 0;
    private static Intent mMediaProjectionResultData = null;
    public static int mImageWidth = 0;
    public static int mImageHeight = 0;
    public static int mScreenWidth = 0;
    public static int mScreenHeight = 0;
    public static int mDensityDPI = 0;
    public static Handler mainHandler = null;
    public static int pendingRequest = 0;
    public static Display _display = null;
    private static int mImageMaxBuffer = 1; // 1/2
    public static byte[] mImageBuffer = null;
    public static byte[] mImagePadding = null;
    private static boolean mImageAllocation = true;
    private static boolean mImageIgnorePadding = true;
    public static Object mImageReader = null;
    public static Object mMediaProjection = null; // MediaProjection
    public static Object mVirtualDisplay = null; // VirtualDisplay
    public static Object mMediaProjectionManager = null; // MediaProjectionManager
    private static boolean mMediaProjectionStopping = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yao_kong_list);
        initView();
        initData();

    }
    private void initView() {

        TextView title = (TextView) findViewById(R.id.local_yingyong_title)
                .findViewById(R.id.tv_titile);
        yaokong = (ImageView) findViewById(R.id.local_yingyong_title)
                .findViewById(R.id.iv_remount);
        yaokong.setImageResource(R.drawable.setting);
        title.setText("遥控器");
        back = (ImageView) findViewById(R.id.back);
        baibao = (YaokongTitle) findViewById(R.id.baobaoxiang);
        baibao.setDrable(R.drawable.baibao);
        baibao.setText("百宝箱");
        fangxiang = (YaokongTitle) findViewById(R.id.fangxiangjian);
        fangxiang.setDrable(R.drawable.fangxian);
        fangxiang.setText("方向键");
        chumo = (YaokongTitle) findViewById(R.id.chumo);
        chumo.setDrable(R.drawable.chumo);
        chumo.setText("触摸板");
        youxi = (YaokongTitle) findViewById(R.id.youxi);
        youxi.setDrable(R.drawable.youxi);
        youxi.setText("游戏");
        yuyin = (YaokongTitle) findViewById(R.id.yuyin);
        yuyin.setDrable(R.drawable.yuyin);
        yuyin.setText("语音");
        iv = (ImageView) findViewById(R.id.iv);
        baiBaoFragment = new BaiBaoFragment();
        showFragmentWithoutBackStackAndAnim(DirectionFragment
                .getInstance("direction"));
        ViewGroup.LayoutParams layoutParams = iv.getLayoutParams();
        layoutParams.width = width / 4;
        sanjiao = (ImageView) findViewById(R.id.sanjiao);
        transte(2, sanjiao);
        yuYinFragment = new YuYinFragment();
        transte(1);

        titles.add(baibao);
        titles.add(fangxiang);
        titles.add(chumo);
        // titles.add(youxi);
        titles.add(yuyin);
        titles.get(1).setTextColor();

        yaokong.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(RemountActivity.this,
                        RemountSetActivity.class);
                startActivity(intent);
            }
        });

        yaokong.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        yaokong.setAlpha(0.6f);
                        break;
                    case MotionEvent.ACTION_UP:
                        yaokong.setAlpha(1.0f);

                        break;

                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void initData() {
        baibao.setOnTouchListener(this);
        fangxiang.setOnTouchListener(this);
        chumo.setOnTouchListener(this);
        youxi.setOnTouchListener(this);
        yuyin.setOnTouchListener(this);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    private void transte(int a) {
        ObjectAnimator.ofFloat(iv, "x", (width / 4) * a).setDuration(0).start();
    }



	/*
	 * @Override public void onClick(View v) { // TODO Auto-generated method
	 * stub switch (v.getId()) { case R.id.baobaoxiang:
	 *
	 * transte(0); break; case R.id.fangxiangjian: transte(1); break;
	 *
	 * case R.id.chumo: transte(2); break;
	 *
	 * case R.id.youxi: transte(3);
	 *
	 * break;
	 *
	 * case R.id.yuyin: transte(4); break; case R.id.iv:
	 * Toast.makeText(YaoKongListActivity.this, "a", 0).show(); break; } }
	 */

    private int index;
    private ImageView yaokong;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            switch (v.getId()) {
                case R.id.baobaoxiang:
                    ToosUtil.getInstance().addEventUmeng(this,"event_device_tools");
                    transte(0);
                    transte(0, sanjiao);
                    showFragmentWithoutBackStackAndAnim(baiBaoFragment);
                    index = 0;
                    break;
                case R.id.fangxiangjian:
                    ToosUtil.getInstance().addEventUmeng(this,"event_device_keyboard");
                    transte(1);
                    showFragmentWithoutBackStackAndAnim(DirectionFragment
                            .getInstance("direction"));
                    transte(2, sanjiao);
                    index = 1;

                    break;
                case R.id.chumo:
                    transte(2);
                    ToosUtil.getInstance().addEventUmeng(this,"event_device_touch");
                    showFragmentWithoutBackStackAndAnim(DirectionFragment
                            .getInstance("chumo"));
                    transte(4, sanjiao);
                    index = 2;
                    break;

                case R.id.youxi:
                    // transte(3);
                    index = 4;

                    Intent intent = new Intent(this, GameActivity.class);
                    startActivity(intent);
                    // transte(6, sanjiao);
                    break;
                case R.id.yuyin:
                    index = 3;
                    showFragmentWithoutBackStackAndAnim(yuYinFragment);
                    transte(3);
                    transte(6, sanjiao);
                    break;
            }
            if (index != 4) {
                for (int i = 0; i < titles.size(); i++) {
                    if (index == i) {
                        titles.get(i).setTextColor();
                    } else {
                        titles.get(i).setNoColor();
                    }
                }
            }
        }
        return false;
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        int result=displayPicHandleH264.doActivityResult(requestCode,resultCode,data);
//    }


    public static int[] getRealScreenSize(Context ctx) {
        Display _display = ((Activity)ctx).getWindowManager().getDefaultDisplay();
        DisplayMetrics _metrics = new DisplayMetrics();
        _display.getMetrics(_metrics); // Include NAVIGATION BAR
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            _display.getRealMetrics(_metrics);
        } else if (android.os.Build.VERSION.SDK_INT >= 14) {
            try {
                Method method = _display.getClass().getMethod("getRealMetrics");
                method.invoke(_display, _metrics);
            } catch (Exception ex) {
            } catch (Throwable t) {
            }
        }
        int[] _ssize = new int[3];
        _ssize[0] = _metrics.widthPixels;
        _ssize[1] = _metrics.heightPixels;
        _ssize[2] = _metrics.densityDpi;
        return _ssize;
    }
/**
 * 初始化镜像
 */
    private void initImage(Bundle savedInstanceState){
        if (SenderService.serviceExiting == true) { // Pending exiting
            finish();
            return ;
        }
        _context = this;
        DisplayMetrics dm = new DisplayMetrics();
        if (_display == null)
            _display = getWindowManager().getDefaultDisplay();
        int[] ssize = getRealScreenSize(this);
        mScreenWidth = ssize[0];
        mScreenHeight = ssize[1];
        mDensityDPI = ssize[2];
        int rotation = _display.getRotation();
        if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            int tmp = mScreenWidth;
            mScreenWidth = mScreenHeight;
            mScreenHeight = tmp;
        }
        mainHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (activityRunning == false) {
                    super.handleMessage(msg);
                    return ; // Avoid of pendding UI request
                }
                switch (msg.what) {
                    case 1:
                        start_ScreenCapture(msg.arg1, msg.arg2);
                        pendingRequest --;
                        break;
                    case 2:
                        stop_ScreenCapture();
                        break;
                    default:
                        break;
                }
            }
        };

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            try {
                mMediaProjectionManager = (Object)getSystemService(Context.MEDIA_PROJECTION_SERVICE);
                if (savedInstanceState != null) {
                    mMediaProjectionResultCode = savedInstanceState.getInt(STATE_MEDIAPROJECTION_RESULT_CODE);
                    mMediaProjectionResultData = savedInstanceState.getParcelable(STATE_MEDIAPROJECTION_RESULT_DATA);
                }
            } catch (Exception ex) {
            } catch (Throwable th) {
            }
        }

        Intent mIntent = new Intent();
        mIntent.setAction("com.waxrain.droidsender.SenderService");
        mIntent.setPackage(_context.getPackageName());
        _context.startService(mIntent);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            try {
                if (mMediaProjectionResultData != null) {
                    outState.putInt(STATE_MEDIAPROJECTION_RESULT_CODE, mMediaProjectionResultCode);
                    outState.putParcelable(STATE_MEDIAPROJECTION_RESULT_DATA, mMediaProjectionResultData);
                }
            } catch (Exception ex) {
            } catch (Throwable th) {
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                try {
                    if (resultCode != Activity.RESULT_OK) {
                        Log.i(Global.LOGTAG,"MP cancelled");
                        return;
                    }
                    Log.i(Global.LOGTAG,"MP confirmed");
                    mMediaProjectionResultCode = resultCode;
                    mMediaProjectionResultData = data;
                    setup_MediaProjection();
                    setup_VirtualDisplay();
                } catch (Exception ex) {
                } catch (Throwable th) {
                }
            }
        }
    }

    public static int start_ScreenCapture(int width, int height) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            int retVal = 0;
            if (width == mImageWidth && height == mImageHeight)
                return retVal;
            stop_ScreenCapture(); // For screen rotation
            mImageWidth = width;
            mImageHeight = height;
            try {
                if (mMediaProjection != null) {
                    setup_VirtualDisplay();
                } else if (mMediaProjectionResultCode != 0 && mMediaProjectionResultData != null) {
                    setup_MediaProjection();
                    setup_VirtualDisplay();
                } else {
                    Log.i(Global.LOGTAG,"MP Request for "+mImageWidth+"x"+mImageHeight+"...");
                    // This initiates a prompt dialog for the user to confirm screen projection
                    ((Activity)_context).startActivityForResult(
                            ((MediaProjectionManager)mMediaProjectionManager).createScreenCaptureIntent(),
                            REQUEST_MEDIA_PROJECTION);
                }
            } catch (Exception ex) {
                retVal = -1;
            } catch (Throwable th) {
                retVal = -1;
            }
            return retVal;
        }
        return -2;
    }

    public static int stop_ScreenCapture() {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            int retVal = 0;
            mMediaProjectionStopping = true;
            SenderService.ukm(1);
            mImageBuffer = null;
            mImagePadding = null;
            try {
                if (mVirtualDisplay != null) {
                    ((VirtualDisplay)mVirtualDisplay).release();
                    Log.i(Global.LOGTAG,"VD release...");
                }
                if (mImageReader != null)
                    ((ImageReader)mImageReader).close();
            } catch (Exception ex) {
                retVal = -1;
            } catch (Throwable th) {
                retVal = -1;
            }
            mVirtualDisplay = null;
            mImageReader = null;
            mImageWidth = 0;
            mImageHeight = 0;
            return retVal;
        }
        return -2;
    }

    public static int setup_MediaProjection() {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            int retVal = 0;
            try {
                mMediaProjection = (Object)((MediaProjectionManager)mMediaProjectionManager).
                        getMediaProjection(mMediaProjectionResultCode, mMediaProjectionResultData);
            } catch (Exception ex) {
                retVal = -1;
            } catch (Throwable th) {
                retVal = -1;
            }
            return retVal;
        }
        return -2;
    }

    @TargetApi(21)
    private static void setup_ImageCallback() {
        if (android.os.Build.VERSION.SDK_INT >= 21 && mImageReader != null) {
            ((ImageReader)mImageReader).setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    if (mMediaProjectionStopping == true) // Avoid entering dead lock
                        return ;
                    Image image = null;
                    try {
                        boolean surfaceChanged = false;
                        if (mImageReader != null) {
                            //long _startTs = System.currentTimeMillis();
                            image = ((ImageReader)mImageReader).acquireLatestImage();
                            Image.Plane[] planes = image.getPlanes();
                            int scr_pixelBytes = planes[0].getPixelStride();
                            int scr_linesize = planes[0].getRowStride();
                            final ByteBuffer buffer = planes[0].getBuffer();
                            if (buffer != null) {
                                int buffer_size = buffer.capacity();
                                if (buffer_size > 0) {
                                    if (mImageIgnorePadding == false) {
                                        if (mImageWidth != scr_linesize/scr_pixelBytes) {
                                            mImageWidth = scr_linesize/scr_pixelBytes;
                                            surfaceChanged = true;
                                        }
                                        if (mImageHeight != buffer_size/scr_linesize) {
                                            mImageHeight = buffer_size/scr_linesize;
                                            surfaceChanged = true;
                                        }
                                        if (surfaceChanged == true) {
                                            mImageReader = (Object)ImageReader.newInstance(mImageWidth, mImageHeight, PixelFormat.RGBA_8888, mImageMaxBuffer);
                                            ((VirtualDisplay)mVirtualDisplay).setSurface(((ImageReader)mImageReader).getSurface());
                                            setup_ImageCallback();
                                            return ;
                                        }
                                    }
                                    SenderService.lkm(1);
                                    if (mImageAllocation == false) {
                                        mImageBuffer = buffer.array();
                                    } else {
                                        if (mImageIgnorePadding == false) {
                                            if (mImageBuffer == null || mImageBuffer.length != buffer_size)
                                                mImageBuffer = new byte[buffer_size];
                                            buffer.get(mImageBuffer, 0, buffer_size);
                                        } else {
                                            int dst_offset = 0;
                                            int dst_linesize = mImageWidth*4;
                                            int linediff = scr_linesize - dst_linesize;
                                            int y = 0;
                                            if (mImageBuffer == null)
                                                mImageBuffer = new byte[dst_linesize*mImageHeight];
                                            if (linediff == 0) {
                                                buffer.get(mImageBuffer, 0, buffer_size);
                                            } else if (linediff > 0) {
                                                if (mImagePadding == null || mImagePadding.length != linediff)
                                                    mImagePadding = new byte[linediff];
                                                for (y = 0; y < mImageHeight; y++) {
                                                    buffer.get(mImageBuffer, dst_offset, dst_linesize);
                                                    buffer.get(mImagePadding, 0, linediff); // Skip padding size
                                                    dst_offset += dst_linesize;
                                                }
                                            } else if (linediff < 0) { // WILL THIS HAPPEN?
                                                for (y = 0; y < mImageHeight; y++) {
                                                    buffer.get(mImageBuffer, dst_offset, scr_linesize);
                                                    dst_offset += dst_linesize;
                                                }
                                            }
                                        }
                                    }
                                    SenderService.ukm(1);
                                    //Log.i(Global.LOGTAG,"onImageAvailable "+mImageWidth+"x"+mImageHeight+" in "+(System.currentTimeMillis()-_startTs)+"ms");
                                }
                            }
                        }
                    } catch (Exception ex) { // Maybe stop_ScreenCapture() called in onPause()
                    } catch (Throwable th) { // Maybe stop_ScreenCapture() called in onPause()
                    } finally {
                        if (image != null)
                            image.close();
                    }
                }
            }, null);
        }
    }

    public static int setup_VirtualDisplay() {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            int retVal = 0;
            mImageMaxBuffer = 1;
            if (mImageAllocation == false)
                mImageMaxBuffer = 2;
            try {
                mImageReader = (Object)ImageReader.newInstance(mImageWidth, mImageHeight, PixelFormat.RGBA_8888, mImageMaxBuffer);
                if (mImageReader == null)
                    return -1;
                mVirtualDisplay = (Object)((MediaProjection)mMediaProjection).createVirtualDisplay("ScreenCapture",
                        mImageWidth, mImageHeight, mDensityDPI,
                        DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                        ((ImageReader)mImageReader).getSurface(), new VirtualDisplay.Callback() {
                            @Override
                            public void onResumed() {
                                super.onResumed();
                            }
                            @Override
                            public void onPaused() {
                                super.onPaused();
                            }
                        }, null);
                mMediaProjectionStopping = false;
                SenderService.ukm(1);
                mImageBuffer = null;
                setup_ImageCallback();
                Log.i(Global.LOGTAG,"VD Changed to "+mImageWidth+"x"+mImageHeight+"...");
            } catch (Exception ex) {
                retVal = -1;
            } catch (Throwable th) {
                retVal = -1;
            }
            return retVal;
        }
        return -2;
    }

    public static int readDone_ScreenCapture() {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            SenderService.ukm(1);
            return 0;
        }
        return -1;
    }

    public static byte[] read_ScreenCapture() {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            int timeout = 5000;
            while (mImageBuffer == null && timeout > 0) {
                Global.do_sleep(2);
                timeout -= 2;
            }
            if (timeout > 0) {
                SenderService.lkm(1);
                return mImageBuffer;
            }
        }
        return null;
    }

    public static int get_ScreenCaptureWidth() {
        int timeout = 5000;
        while (mImageBuffer == null && timeout > 0) {
            Global.do_sleep(10);
            timeout -= 10;
        }
        return mImageWidth;
    }


    public static int get_ScreenCaptureHeight() {
        int timeout = 5000;
        while (mImageBuffer == null && timeout > 0) {
            Global.do_sleep(10);
            timeout -= 10;
        }
        return mImageHeight;
    }

    public static int teardown_MediaProjection() {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            int retVal = 0;
            try {
                if (mMediaProjection != null)
                    ((MediaProjection)mMediaProjection).stop();
            } catch (Exception ex) {
                retVal = -1;
            } catch (Throwable th) {
                retVal = -1;
            }
            mMediaProjection = null;
            return retVal;
        }
        return -2;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
//        SenderService.upnp.Stop();
//        stop_ScreenCapture();
//        teardown_MediaProjection();
//
//        stopService(new Intent(this, SenderService.class));
//        activityRunning = false;
//        SenderService.serviceExiting = false;
        super.onDestroy();
    }
}
