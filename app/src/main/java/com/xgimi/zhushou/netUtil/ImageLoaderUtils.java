package com.xgimi.zhushou.netUtil;

import android.content.Context;
import android.os.Build;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xgimi.zhushou.R;

/**
 * Description : 图片加载工具类
 * Author : 霍长江
 */
public class ImageLoaderUtils {
    public static void display(Context context, ImageView imageView, String url, int placeholder, int error) {
        if(imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        if(Build.VERSION.SDK_INT>=23){
            Glide.with(context).load(url).placeholder(placeholder)
                    .error(error).crossFade().into(imageView);
        }else {
            Glide.with(context).load(url).placeholder(placeholder)
                    .error(error).crossFade().into(imageView);
        }
    }
    public static void display(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        if(Build.VERSION.SDK_INT>=23) {
            Glide.with(context).load(url).into(imageView);
        }else{
            Glide.with(context).load(url).placeholder(R.drawable.zhanweitu).dontAnimate()
                    .error(R.drawable.zhanweitu).crossFade().into(imageView);
        }
//        Glide.with(context).load(url).placeholder(R.drawable.image_loading).dontAnimate()
//               .crossFade().into(imageView);
    }


    public static void displaythree(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }

        Glide.with(context).load(url).into(imageView);

//        Glide.with(context).load(url).placeholder(R.drawable.image_loading).dontAnimate()
//               .crossFade().into(imageView);

    }
    public static void displayTwo(Context context, ImageView imageView, String url) {
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        if(Build.VERSION.SDK_INT>=23) {
            Glide.with(context).load(url).into(imageView);
        }else {
            Glide.with(context).load(url).placeholder(R.drawable.zhanweitu).transform(new GlideRoundTransform(context))
                    .error(R.drawable.zhanweitu).crossFade().into(imageView);
        }
//        Glide.with(context).load(url).placeholder(R.drawable.image_loading).dontAnimate()
//               .crossFade().into(imageView);

    }
}
