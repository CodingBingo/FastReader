package com.codingbingo.fastreader.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codingbingo.fastreader.Constants;
import com.codingbingo.fastreader.R;
import com.codingbingo.fastreader.base.BaseActivity;
import com.codingbingo.fastreader.model.LocalFile;
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

        Intent intent = getIntent();
        if (intent.hasExtra("type") == false) {
            finish();
            return;
        }

        pageFactory = new PageFactory(getApplicationContext());

        switch (intent.getIntExtra("type", Constants.TYPE_FROM_MAIN_ACTIVITY)){
            case Constants.TYPE_FROM_MAIN_ACTIVITY:
                pageFactory.openBook(intent.getIntExtra("bookId", 0), 0, 0);
                break;
            case Constants.TYPE_FROM_LOCAL_FILE_ACTIVITY:
                pageFactory.openBook(intent.getStringExtra("bookPath"));
                break;
        }
    }
}
