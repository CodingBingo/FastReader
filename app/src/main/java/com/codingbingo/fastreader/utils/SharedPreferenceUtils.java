package com.codingbingo.fastreader.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/2/14.
 */

public class SharedPreferenceUtils {

    private volatile static SharedPreferenceUtils instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static SharedPreferenceUtils getInstance() {
        if (instance == null){
            synchronized (SharedPreferenceUtils.class){
                if (instance == null){
                    instance = new SharedPreferenceUtils();
                }
            }
        }
        return instance;
    }

    private SharedPreferenceUtils(){}

    /**
     * 在Application初始化的时候就要初始化
     * @param context
     * @param preferenceName
     */
    public static void init(Context context, String preferenceName){
        instance = new SharedPreferenceUtils();
        instance.sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        instance.editor = instance.sharedPreferences.edit();
    }

    public boolean getBoolean(String key, boolean defaultValue){
        return instance.sharedPreferences.getBoolean(key, defaultValue);
    }

    public int getInt(String key, int defaultValue){
        return instance.sharedPreferences.getInt(key, defaultValue);
    }

    public String getString(String key, String defaultValue){
        return instance.sharedPreferences.getString(key, defaultValue);
    }

    public void putString(String key, String value){
        instance.editor.putString(key, value);
        instance.editor.commit();
    }

    public void putBoolean(String key, boolean value){
        instance.editor.putBoolean(key, value);
        instance.editor.commit();
    }

    public void putInt(String key, int value){
        instance.editor.putInt(key, value);
        instance.editor.commit();
    }
}
