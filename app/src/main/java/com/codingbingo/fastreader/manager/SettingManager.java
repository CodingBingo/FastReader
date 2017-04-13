package com.codingbingo.fastreader.manager;

import com.codingbingo.fastreader.Constants;
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
        return instance.sharedPreferenceUtils.getInt("fontSize", Constants.STYLE_NORMAL_FONT_SIZE);
    }

    public void setReadFontSize(int fontSize){
        instance.sharedPreferenceUtils.putInt("fontSize", fontSize);
    }

    public boolean getReadMode(){
        return instance.sharedPreferenceUtils.getBoolean("nightMode", false);
    }

    public void setReadMode(boolean value){
        instance.sharedPreferenceUtils.putBoolean("nightMode", value);
    }
}
