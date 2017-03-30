package com.codingbingo.fastreader.base;

import android.app.FragmentManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.codingbingo.fastreader.FRApplication;
import com.codingbingo.fastreader.dao.DaoSession;

/**
 * Created by bingo on 2016/12/23.
 */

public class BaseActivity extends AppCompatActivity{

    protected FragmentManager mFragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        init();
    }

    /**
     * 初始化操作
     */
    private void init() {
        mFragmentManager = getFragmentManager();
    }

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

    protected void showToast(String content){
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
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
