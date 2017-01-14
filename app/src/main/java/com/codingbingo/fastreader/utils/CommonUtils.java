package com.codingbingo.fastreader.utils;

import android.content.Context;
import android.util.TypedValue;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by bingo on 2016/12/25.
 */

public class CommonUtils {
    private static Logger logger = Logger.getLogger(CommonUtils.class.getName());

    public static int dp2px(Context mContext, int dp) {
        return (int) (dp * mContext.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int getStatusBarHeight(Context mContext) {
        int height = 0;
        try {
            Class c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            height = mContext.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
        }
        return height;
    }

    /**
     * 获取titleBar的高度
     * @param mContext
     * @return
     */
    public static int getTitleBarHeight(Context mContext) {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (mContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, mContext.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }
}
