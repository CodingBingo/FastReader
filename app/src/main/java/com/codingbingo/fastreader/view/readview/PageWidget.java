package com.codingbingo.fastreader.view.readview;

import android.content.Context;
import android.graphics.Canvas;

import com.codingbingo.fastreader.base.view.BaseReadView;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/1/15.
 */

public class PageWidget extends BaseReadView {
    public PageWidget(Context context) {
        super(context);
    }

    @Override
    protected void drawCurrentPageArea(Canvas canvas) {
        canvas.save();
        canvas.drawBitmap(mCurrentPageBitmap, 0, 0, null);
        try {
            canvas.restore();
        } catch (Exception e) {

        }
    }
}
