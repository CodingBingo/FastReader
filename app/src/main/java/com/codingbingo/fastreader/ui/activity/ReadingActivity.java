package com.codingbingo.fastreader.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

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

    private LocalFile mLocalFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (intent.hasExtra("file") == false) {
            finish();
            return;
        }
        mLocalFile = (LocalFile) intent.getSerializableExtra("file");

        pageFactory = new PageFactory(getApplicationContext());
        pageFactory.openBook(mLocalFile.getFilePath());
    }
}
