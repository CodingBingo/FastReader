package com.codingbingo.fastreader.base;

import android.support.v7.app.AppCompatActivity;

import com.avos.avoscloud.AVAnalytics;
import com.codingbingo.fastreader.FRApplication;
import com.codingbingo.fastreader.model.DaoSession;

/**
 * Created by bingo on 2016/12/23.
 */

public class BaseActivity extends AppCompatActivity{
    @Override
    protected void onPause() {
        super.onPause();
        AVAnalytics.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AVAnalytics.onResume(this);
    }

    /**
     * 获取数据库session
     * @return
     */
    protected DaoSession getDaoSession(){
        return ((FRApplication) getApplication()).getDaoSession();
    }

    /**
     * 显示加载动画
     * @param message
     */
    protected void showLoadingDialog(String message){

    }

    /**
     * 关闭加载动画
     */
    protected void dismissLoadingDialog(){

    }
}
