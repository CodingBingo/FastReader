package com.codingbingo.fastreader.manager;

import com.codingbingo.fastreader.utils.SharedPreferenceUtils;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/2/14.
 */

public class SettingManager {

    private volatile static SettingManager instance;
    private SharedPreferenceUtils sharedPreferenceUtils;

    public static SettingManager getInstance() {
        if (instance == null) {
            synchronized (SettingManager.class) {
                if (instance == null) {
                    instance = new SettingManager();
                    instance.sharedPreferenceUtils = SharedPreferenceUtils.getInstance();
                }
            }
        }
        return instance;
    }

    public int getReadFontSize(){
        return instance.sharedPreferenceUtils.getInt("fontSize", 45);
    }

}
