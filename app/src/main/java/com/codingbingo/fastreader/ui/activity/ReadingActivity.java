package com.codingbingo.fastreader.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.codingbingo.fastreader.Constants;
import com.codingbingo.fastreader.R;
import com.codingbingo.fastreader.base.BaseActivity;
import com.codingbingo.fastreader.view.readview.PageFactory;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/1/11.
 */

public class ReadingActivity extends BaseActivity {

    private PageFactory pageFactory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchFullScreen(true);

        setContentView(R.layout.activity_reading);

        init();
    }

    private void init() {
        Intent intent = getIntent();
        if (intent.hasExtra("type") == false) {
            finish();
            return;
        }

        pageFactory = new PageFactory(getApplicationContext());

        switch (intent.getIntExtra("type", Constants.TYPE_FROM_MAIN_ACTIVITY)){
            case Constants.TYPE_FROM_MAIN_ACTIVITY:
                //从主页面进来的，说明本地数据已经插入数据库了
                pageFactory.openBook(intent.getLongExtra("bookId", 0));
                break;
            case Constants.TYPE_FROM_LOCAL_FILE_ACTIVITY:
                pageFactory.openBook(intent.getStringExtra("bookPath"));
                break;
        }
    }

    private void switchFullScreen(boolean isFullScreen){
        if (isFullScreen) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(params);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(params);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        }
    }
}
