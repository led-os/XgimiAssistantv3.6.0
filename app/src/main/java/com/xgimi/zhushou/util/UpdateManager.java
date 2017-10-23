package com.xgimi.zhushou.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.Update;

import org.apache.http.Header;

import java.io.File;
import java.io.IOException;

/**
 * 应用程序更新工具包
 * <p>
 * 目前是检查到更新之后下载安装包，下载完成之后再提示用户升级
 */
public class UpdateManager {
    private String packgeName = "com.xgimi.zhushou";
    private String type = "all";
    private String downLoadUrl = "http://api.xgimi.com/apis/rest/VideoappService/updateappByPackagename?u_=1&v_=1&t_=1&p_=2348&package_name=" + packgeName + "&gimi_device=" + type;
    private Context mContext;

    private static UpdateManager updateManager;

    private static AsyncHttpClient httpClient;

    private static Gson gson;

    private Update mUpdate;

    private String savePath = "";
    private String apkFilePath = "";
    private static final int ANALYZEDONE = 1;

    public static UpdateManager getUpdateManager() {

        if (updateManager == null) {

            updateManager = new UpdateManager();
            httpClient = new AsyncHttpClient();
            gson = new Gson();

        }

        return updateManager;
    }


    public void checkAppUpdate(Context context) {

        this.mContext = context;

        final Handler handler = new Handler() {

            public void handleMessage(Message msg) {
                if (msg.what == ANALYZEDONE) {
                    mUpdate = (Update) msg.obj;
                    if (mUpdate != null && mUpdate.data != null && mUpdate.data.version_code != null) {
                        savePath = mContext.getDir("update", 0).getAbsolutePath();

                        if (DeviceUtils.getappVersionCode(mContext) < Integer.valueOf(mUpdate.data.version_code)) {

                            apkFilePath = savePath + File.separator + mUpdate.data.version_name + ".apk";

                            File file = new File(apkFilePath);

                            if (file.exists() && MD5Check(mUpdate.data.file_md5)) {
                                showUpdateDialog();
                            } else {
                                // 清空文件夹
                                FileUtils.deleteFolderFile(savePath, false);
                                Download(mUpdate.data.download_url, apkFilePath);
                            }

                        } else {
                            // 清空文件夹
                            FileUtils.deleteFolderFile(savePath, false);
                        }
                    } else {
                    }
                }
            }
        };

        httpClient.get(downLoadUrl, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                Message msg = new Message();
                msg.what = ANALYZEDONE;
                Update update = null;
                try {
                    String res = new String(arg2, "utf-8");
                    XGIMILOG.E("检测升级 : " + res);
                    update = gson.fromJson(res, Update.class);
                    msg.obj = update;
                } catch (Exception e) {
                    Log.e("update_error", e.toString());
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

            }
        });

    }

    /**
     * 下载资源
     *
     * @param download 下载地址
     * @param savepath 保存地址
     */
    private void Download(String download, String savepath) {
        XGIMILOG.E("开始下载新版本 : " + download + ", savePath = " + savepath);
        httpClient.get(download, new FileAsyncHttpResponseHandler(new File(savepath)) {
            @Override
            public void onSuccess(int arg0, Header[] arg1, File arg2) {
                // 下载成功之后提示用户升级
                XGIMILOG.E("下载更新成功");
                showUpdateDialog();
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, Throwable arg2, File arg3) {
                // 下载失败的情况下，清除apk文件
                XGIMILOG.E("下载更新失败");
                FileUtils.deleteFolderFile(savePath, false);
            }
        });
    }

    /**
     * 安装apk
     */
    private void installApk(String apk_path) {

        if (!new File(apk_path).exists()) {
            return;
        }

        // 安装之前先修改apk的权限，避免出现解析包错误的问题
        try {
            String command = "chmod 777 " + apk_path;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apk_path), "application/vnd.android.package-archive");
        mContext.startActivity(i);
    }

    public String ToSBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }

    /**
     * 安装提示对话框
     */
    private void showUpdateDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.welcomedilog, null);

        final Dialog dialog = new Dialog(mContext, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view);

        String rizhi = ToSBC(mUpdate.data.desc);
        TextView textView_version = (TextView) view.findViewById(R.id.text1);
        TextView tv_diary = (TextView) view.findViewById(R.id.tv_diary);
        tv_diary.setMovementMethod(ScrollingMovementMethod.getInstance());
        TextView tv_new_versions = (TextView) view.findViewById(R.id.banben);
        TextView textView_log = (TextView) view.findViewById(R.id.text2);
        ImageView iv_close = (ImageView) view.findViewById(R.id.close);
        iv_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        String[] strarray = mUpdate.data.desc.split(" ");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strarray.length; i++) {
            Log.e("info", strarray[i] + i);
            sb.append(strarray[i] + "\n");
        }
        tv_diary.setText(sb.toString());
        tv_new_versions.setText("发现新版本" + mUpdate.data.version_name);
        textView_log.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ToosUtil.getInstance().addEventUmeng(mContext, "event_app_update");
                installApk(apkFilePath);
                dialog.dismiss();
            }
        });
        textView_version.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ToosUtil.getInstance().addEventUmeng(mContext, "event_talk_later");
                dialog.dismiss();
            }
        });

        // 获取屏幕分辨率来控制宽度
        int width = ToosUtil.getInstance().getScreenWidth((Activity) mContext);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.width = width * 8 / 10;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);

        dialog.setCanceledOnTouchOutside(false);

        dialog.show();

    }

    /**
     * 校验下载的apk文件的md5值
     *
     * @param md5 期望的md5值
     * @return
     */
    private boolean MD5Check(String md5) {
        boolean b = false;

        String local_md5 = MD5Util.getFileMD5String(new File(apkFilePath));
        XGIMILOG.D("new md5 : " + md5 + ", local :" + local_md5);
        if (local_md5.equals(md5)) {
            b = true;
        }
        return b;
    }

    /**
     * 获取封面更新图片
     */
//	private void getUpdateImage() {
//		final String imagename = mUpdate.getImagename();
//		final String imagemd5 = mUpdate.getImagemd5();
//		final String imageUrl = mUpdate.getImageUrl();
//
//		final String dir_path = FileUtils.getAppCache(mContext, "xgimipage");
//		String image_path = dir_path + File.separator + imagename;
//		final File image_file = new File(image_path);
//
//		if (StringUtils.isEmpty(imagename) || StringUtils.isEmpty(imagemd5) || StringUtils.isEmpty(imageUrl)) {
//			FileUtils.deleteFolderFile(dir_path, false);
//			return;
//		}
//		if (image_file.exists() && MD5Util.getFileMD5String(image_file).equals(imagemd5)) {
//			return;
//		}
//		FileUtils.deleteFolderFile(dir_path, false);
//		httpClient.get(imageUrl, new FileAsyncHttpResponseHandler(image_file) {
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, File arg2) {
//				Log.e("httpClient", "getUpdateImage_onSuccess");
//				if (!MD5Util.getFileMD5String(image_file).equals(imagemd5)) {
//					FileUtils.deleteFolderFile(dir_path, false);
//				}
//			}
//			@Override
//			public void onFailure(int arg0, Header[] arg1, Throwable arg2, File arg3) {
//				Log.e("httpClient", "getUpdateImage_onFailure");
//				FileUtils.deleteFolderFile(dir_path, false);
//			}
//		});
//
//	}
}
