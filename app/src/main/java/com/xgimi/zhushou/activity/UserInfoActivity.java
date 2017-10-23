package com.xgimi.zhushou.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.xgimi.device.utils.FileUtils;
import com.xgimi.device.utils.StringUtils;
import com.xgimi.zhushou.App;
import com.xgimi.zhushou.BaseActivity;
import com.xgimi.zhushou.R;
import com.xgimi.zhushou.bean.HeadPhonto;
import com.xgimi.zhushou.inter.CommonCallBack;
import com.xgimi.zhushou.netUtil.HttpRequest;
import com.xgimi.zhushou.util.DisPlayRound;
import com.xgimi.zhushou.util.ToosUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

public class UserInfoActivity extends BaseActivity implements OnClickListener ,OnTouchListener{
	private File tempFile; // 拍照设置头像的零时file
	private final int CUTSIZE = 400; // 截取图片保存的分辨率

	private final String SaveName = "photo_icon"; // 需要拍照设置头像时，文件保存名

	private ImageView touxiang;

	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果
	public static final int MODIFYAVATARING = 99; // 修改头像请求开始
	public static final int MODIFYAVATAROK = 100; // 修改成功
	public static final int MODIFYAVATARFAIL = 101; // 修改失败
	private DisplayImageOptions options;

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MODIFYAVATAROK:
				Toast.makeText(UserInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
				FileUtils.deleteFile(App.getContext().avatarpath_bk);
				break;
			case MODIFYAVATARFAIL:
				if (StringUtils.isEmpty((String) msg.obj)) {
					Toast.makeText(UserInfoActivity.this,
							"更换头像失败，请重试", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(UserInfoActivity.this, (String) msg.obj, Toast.LENGTH_SHORT)
							.show();
				}
				FileUtils.reNamePath(App.getContext().avatarpath_bk, App.getContext().avatarpath);
				break;
			case MODIFYAVATARING:
				touxiang.setImageDrawable(Drawable
						.createFromPath(App.getContext().avatarpath));
				break;
			default:
				break;
			}
		}
	};
	private Uri uritempFile;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		initView();
		initData();
	}

	private void initView() {
		options = new DisplayImageOptions.Builder()
				// .showImageOnLoading(R.drawable.icon_default)
				.showImageOnLoading(R.drawable.zhanweitu)
				.showImageForEmptyUri(R.drawable.zhanweitu).showImageOnFail(R.drawable.zhanweitu)
				// .showImageOnLoading(LayoutToDrawable());
//				.showImageOnLoading(new BitmapDrawable(convertViewToBitmap()))
				.displayer(new DisPlayRound(0))
				.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisk(true)
				.cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY)
				// .cacheOnDisc(true)
				// .considerExifParams(true)
				.build();
		ImageView ivRemount = (ImageView) findViewById(R.id.include)
				.findViewById(R.id.iv_remount);
		ivRemount.setVisibility(View.GONE);
		TextView title = (TextView) findViewById(R.id.include).findViewById(
				R.id.tv_titile);
		title.setText("个人信息");
		back = (ImageView) findViewById(R.id.include).findViewById(R.id.back);
		back(back);
		back.setOnTouchListener(this);
		touxiang = (ImageView) findViewById(R.id.touxiang);
		TextView youxiag = (TextView) findViewById(R.id.youxiang);
		TextView shoujihao = (TextView) findViewById(R.id.shoujihao);
		TextView userName = (TextView) findViewById(R.id.tv_user);
		if(App.getContext().getLoginInfo()!=null&&App.getContext().getLoginInfo().data!=null) {
//			if (!App.getContext().getLoginInfo().data.email.equals("")) {
			if (!"".equals(App.getContext().getLoginInfo().data.email)) {
				youxiag.setText(App.getContext().getLoginInfo().data.email);
			}
			if (App.getContext().getLoginInfo().data.tel != null) {
				shoujihao.setText(App.getContext().getLoginInfo().data.tel);
			}
			if (App.getContext().getLoginInfo().data.avatar != null) {
				ImageLoader.getInstance().displayImage(
						App.getContext().getLoginInfo().data.avatar, touxiang,options);
			}
			if (!App.getContext().getLoginInfo().data.username.equals("")) {
				userName.setText(App.getContext().getLoginInfo().data.username);
			}
		}
	}

	private void initData() {
		touxiang.setOnClickListener(this);
		touxiang.setOnTouchListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.touxiang:
			// 在外部存储器上创建文件夹，并删除之前的文件(如果存在的话)
			tempFile = FileUtils.createFile(App.ExternalImageDir, SaveName);

			/**
			 * 没有外部存储器的情况下只调用系统相册进行头像设置
			 */
			if (FileUtils.checkSaveLocationExists() && tempFile != null) {
				showDialog();
			} else {
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
			}

			break;

		}
	}

	// 头像设置的提示对话框方法
	@SuppressLint("NewApi")
	private void showDialog() {

		View view = LayoutInflater.from(this).inflate(R.layout.dialog_avatar,
				null);

		final Dialog dialog = new Dialog(this,
				R.style.transparentFrameWindowStyle);
		dialog.setContentView(view);

		Button btn_camera = (Button) view
				.findViewById(R.id.dialog_avatar_camera);
		Button btn_photo = (Button) view.findViewById(R.id.dialog_avatar_photo);
		Button btn_cancel = (Button) view
				.findViewById(R.id.dialog_avatar_cancel);

		btn_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

				// 调用系统的拍照功能
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// 指定调用相机拍照后照片的储存路径
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
				startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);

			}
		});
		btn_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, PHOTO_REQUEST_GALLERY);

			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		Window window = dialog.getWindow();
		window.setDimAmount(0.2f);
		WindowManager.LayoutParams wl = window.getAttributes();

		wl.x = 0;
		wl.y = getWindowManager().getDefaultDisplay().getHeight();

		// 以下这两句是为了保证按钮可以水平满屏
		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

		// 设置显示位置
		dialog.onWindowAttributesChanged(wl);

		// 设置点击外围解散
		dialog.setCanceledOnTouchOutside(true);

		dialog.show();
	}

	Bitmap bitmap;
	private ImageView back;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PHOTO_REQUEST_TAKEPHOTO:// 当选择拍照时调用
			if (tempFile != null && tempFile.exists()) {
				startPhotoZoom(Uri.fromFile(tempFile), CUTSIZE);
			}
			break;
		case PHOTO_REQUEST_GALLERY:// 当选择从本地获取图片时
			// 做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
			if (data != null)
				startPhotoZoom(data.getData(), CUTSIZE);
			break;
		case PHOTO_REQUEST_CUT:// 返回的结果
			if (data != null)
				try {
					bitmap = BitmapFactory.decodeStream(getContentResolver()
							.openInputStream(uritempFile));
					setPicToView(bitmaptoString(bitmap, 100));
//					touxiang.setImageBitmap(bitmap);

					ImageLoader.getInstance().clearDiskCache();
					ImageLoader.getInstance().clearMemoryCache();
					Log.e("info", "aaaaa");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public String bitmaptoString(Bitmap bitmap, int bitmapQuality) {
	String string1;
			ByteArrayOutputStream bStream = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, bitmapQuality, bStream);
			byte[] bytes = bStream.toByteArray();
			 string1 = Base64.encodeToString(bytes, Base64.DEFAULT);
			return string1;
}

	/**
	 * 启动系统剪裁功能
	 * 
	 * @param uri
	 * @param size
	 */
	private void startPhotoZoom(Uri uri, int size) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");

		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		
		uritempFile = Uri.parse("file://" + "/"
				+ Environment.getExternalStorageDirectory().getPath() + "/"
				+ "small.jpg");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
		intent.putExtra("outputFormat", CompressFormat.JPEG.toString());

		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	// 将进行剪裁后的图片显示到UI界面上,并发出更换头像的网络请求
	private void setPicToView(String dizhi) {
			HttpRequest.getInstance(this).upLoadTouXiang(
					App.getContext().getLoginInfo().data.uid, dizhi,
					new CommonCallBack<HeadPhonto>() {

						@Override
						public void onSuccess(HeadPhonto data) {
							// TODO Auto-generated method stub
//							touxiang.setImageBitmap(bitmap);
							ImageLoader.getInstance().displayImage(data.data.avatar,touxiang,options);
							ToosUtil.getInstance().addEventUmeng(UserInfoActivity.this,"event_modify_header");
							ImageLoader.getInstance().clearDiskCache();
							ImageLoader.getInstance().clearMemoryCache();
						}

						@Override
						public void onStart() {
							// TODO Auto-generated method stub

						}

						@Override
						public void onFailed(String reason) {
							// TODO Auto-generated method stub
							Toast.makeText(UserInfoActivity.this, "头像修改失败", Toast.LENGTH_SHORT).show();
						}
					});
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				back.setAlpha(0.6f);
				break;
			case MotionEvent.ACTION_UP:
				back.setAlpha(1.0f);
				break;
			}
			break;
		case R.id.touxiang:
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touxiang.setAlpha(0.6f);
				break;
			case MotionEvent.ACTION_UP:
				touxiang.setAlpha(1.0f);
				break;
			}
			break;
		}
		return false;
	}



}
