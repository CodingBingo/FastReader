package com.codingbingo.fastreader.view.readview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.codingbingo.fastreader.base.view.BaseReadView;
import com.codingbingo.fastreader.view.readview.interfaces.OnReadStateChangeListener;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/1/15.
 */

public class PageWidget extends BaseReadView {

    public PageWidget(Context context){
        this(context, null, 0);
    }

    public PageWidget(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void drawCurrentPageArea(Canvas canvas) {
        canvas.drawBitmap(mCurPageBitmap, 0, 0, null);
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

    @Override
    protected void calcCornerXY(float x, float y) {

    }
}
