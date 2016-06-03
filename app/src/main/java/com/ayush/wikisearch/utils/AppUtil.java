package com.ayush.wikisearch.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by ayushkedia on 03/06/16.
 */
public class AppUtil {

    private static AppUtil appUtil;
    private static Toast mToast;

    private AppUtil() {
        //private Constructor
    }

    public static AppUtil getInstance() {
        if (appUtil == null) {
            appUtil = new AppUtil();
        }
        return appUtil;
    }

    /* method for loading image using Glide library */

    public void loadImageGlide(Context context, String url, ImageView imageView, int defaultBackground) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(defaultBackground)
                .animate(android.R.anim.slide_in_left)
                .into(imageView);
    }

    /* method to show long toast */

    public static void showLongToast(Context context, String msg) {
        showToast(context, msg, Toast.LENGTH_LONG);
    }

    /* method to show short toast */

    public static void showShortToast(Context context, String msg) {
        showToast(context, msg, Toast.LENGTH_SHORT);
    }

    private static void showToast(Context context, String msg, int duration) {
        if (mToast != null)
            mToast.cancel();
        mToast = Toast.makeText(context, msg, duration);
        mToast.show();
    }
}

