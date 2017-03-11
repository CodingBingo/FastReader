package com.codingbingo.fastreader.view.readview;

import android.content.Context;
import android.graphics.Canvas;

import com.codingbingo.fastreader.base.view.BaseReadView;
import com.codingbingo.fastreader.view.readview.interfaces.OnReadStateChangeListener;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/1/15.
 */

public class PageWidget extends BaseReadView {

    public PageWidget(Context context, String bookId, OnReadStateChangeListener listener) {
        super(context, bookId, listener);
    }

    @Override
    protected void drawCurrentPageArea(Canvas canvas) {

    }

    @Override
    protected void drawNextPageAreaAndShadow(Canvas canvas) {

    }

    @Override
    protected void drawCurrentPageShadow(Canvas canvas) {

    }

    @Override
    protected void drawCurrentBackArea(Canvas canvas) {

    }

    @Override
    protected void calcPoints() {

    }
}
