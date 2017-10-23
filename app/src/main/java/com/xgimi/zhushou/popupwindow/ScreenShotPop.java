package com.xgimi.zhushou.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xgimi.device.utils.FileUtils;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.R;

import java.io.File;

public class ScreenShotPop extends PopupWindow {

	private Context mContext;

	private WindowManager.LayoutParams params;

	private String image_path = null; // 记录分享的图片地址

	private TextView tip;
	private LinearLayout screenshot_layout;
	private Button btn_share, btn_ok;
	private int height;
	private int width;

	public ScreenShotPop(Context context, int h, int w) {
		super(context);
		this.mContext = context;
		this.height=h;
		this.width=w;
		init();
	}

	private void init() {

		params = ((Activity) mContext).getWindow().getAttributes();

		setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setAnimationStyle(R.style.popwin_anim_style);
		setWindowLayoutMode(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View root = inflater.inflate(R.layout.dialog_screenshot, null);

		setContentView(root);

		tip = (TextView) root.findViewById(R.id.screenshot_tip);
		screenshot_layout = (LinearLayout) root.findViewById(R.id.screenshot_layout);
		btn_share = (Button) root.findViewById(R.id.screenshot_share);
		btn_ok = (Button) root.findViewById(R.id.screenshot_ok);

		tip.setText(mContext.getResources().getString(R.string.screenshot_success_tip, App.ExternalImageDir));
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		btn_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				shareMsg(mContext, mContext.getString(R.string.screenshot_share_activitytip, FileUtils.getFileSize(image_path)),
						mContext.getString(R.string.screenshot_share_msgTitle), mContext.getString(R.string.screenshot_share_msgTitle), image_path);
			}
		});

	}

	/**
	 * 增加一张截屏预览图
	 * 
	 * @param drawable
	 *            图片资源
	 */
	public void addImage(Drawable drawable) {
		ImageView imageView = getImageView(drawable);
		screenshot_layout.addView(imageView);
	}

	/**
	 * 增加一张截屏预览图
	 * 
	 * @param imagepath
	 *            图片地址
	 */
	public void addImage(String imagepath) {
		image_path = imagepath;
		ImageView imageView = getImageView(Drawable.createFromPath(imagepath));
		screenshot_layout.addView(imageView);
	}

	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		params.alpha = 0.85f;
		((Activity) mContext).getWindow().setAttributes(params);
	}

	@Override
	public void dismiss() {
		super.dismiss();
		params.alpha = 1.0f;
		((Activity) mContext).getWindow().setAttributes(params);
		screenshot_layout.removeAllViews();
	}

	/**
	 * 获取图片控件
	 * 
	 * @param drawable
	 * @return
	 */
	private ImageView getImageView(Drawable drawable) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) (height/2.7), (int)(width/2.7));
		ImageView imageView = new ImageView(mContext);
		imageView.setLayoutParams(lp);
		imageView.setPadding(0, 10, 10, 0);
		imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setImageDrawable(drawable);
		return imageView;
	}
	/**
	 * 分享功能
	 * 
	 * @param context
	 *            上下文
	 * @param activityTitle
	 *            Activity的名字
	 * @param msgTitle
	 *            消息标题
	 * @param msgText
	 *            消息内容
	 * @param imgPath
	 *            图片路径，不分享图片则传null
	 */
	public static void shareMsg(Context context, String activityTitle, String msgTitle, String msgText, String imgPath) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		if (imgPath == null || imgPath.equals("")) {
			intent.setType("text/plain"); // 纯文本
		} else {
			File f = new File(imgPath);
			if (f != null && f.exists() && f.isFile()) {
				intent.setType("image/png");
				Uri u = Uri.fromFile(f);
				intent.putExtra(Intent.EXTRA_STREAM, u);
			}
		}
		intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
		intent.putExtra(Intent.EXTRA_TEXT, msgText);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(Intent.createChooser(intent, activityTitle));
	}
}
