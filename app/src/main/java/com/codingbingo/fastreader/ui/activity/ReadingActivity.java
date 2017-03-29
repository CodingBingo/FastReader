package com.codingbingo.fastreader.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.codingbingo.fastreader.Constants;
import com.codingbingo.fastreader.R;
import com.codingbingo.fastreader.base.BaseActivity;
import com.codingbingo.fastreader.utils.StringUtils;
import com.codingbingo.fastreader.view.readview.PageWidget;
import com.codingbingo.fastreader.view.readview.ReadController;
import com.codingbingo.fastreader.view.readview.interfaces.OnControllerStatusChangeListener;


/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/1/11.
 */

public class ReadingActivity extends BaseActivity implements OnControllerStatusChangeListener, View.OnClickListener {

    private ReadController readController;
    private PageWidget readPageWidget;

    private long bookId;
    private String bookPath;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchFullScreen(true);
        setContentView(R.layout.activity_reading);


        init();
        initView();
    }

    private void init() {
        Intent intent = getIntent();
        if (intent.hasExtra("type") == false) {
            finish();
            return;
        }

        switch (intent.getIntExtra("type", Constants.TYPE_FROM_MAIN_ACTIVITY)) {
            case Constants.TYPE_FROM_MAIN_ACTIVITY:
                //从主页面进来的，说明本地数据已经插入数据库了
                bookId = intent.getLongExtra("bookId", 0);
                break;
            case Constants.TYPE_FROM_LOCAL_FILE_ACTIVITY:
                bookPath = intent.getStringExtra("bookPath");
                break;
        }
    }

    private void initView() {
        readController = (ReadController) findViewById(R.id.readController);
        readController.setOnControllerStatusChangeListener(this);
        readController.setOnViewClickListener(this);

        readPageWidget = (PageWidget) findViewById(R.id.readPageWidget);
        if (StringUtils.isBlank(bookPath)) {
            readPageWidget.setBookId(bookId);
        } else {
            readPageWidget.setBookPath(bookPath);
        }
    }

    private void switchFullScreen(boolean isFullScreen) {
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

    @Override
    public void onControllerStatusChange(boolean isShowing) {
        switchFullScreen(!isShowing);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.backBtn:
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        showToast("onKeyDown");
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            readPageWidget.nextPage();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            readPageWidget.prePage();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
