package com.codingbingo.fastreader.base.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.View;
import android.widget.Scroller;

import com.codingbingo.fastreader.utils.ScreenUtils;
import com.codingbingo.fastreader.view.readview.PageFactory;
import com.codingbingo.fastreader.view.readview.interfaces.OnReadStateChangeListener;

import java.util.List;

/**
 * Created by bingo on 2017/1/4.
 */

public abstract class BaseReadView extends View {

    protected int mScreenWidth;
    protected int mScreenHeight;

    protected PointF mTouch = new PointF();
    protected float actiondownX, actiondownY;
    protected float touch_down = 0; // 当前触摸点与按下时的点的差值

    protected Bitmap mCurPageBitmap, mNextPageBitmap;
    protected Canvas mCurrentPageCanvas, mNextPageCanvas;
    protected PageFactory pagefactory = null;

    protected OnReadStateChangeListener listener;
    protected String bookId;
    public boolean isPrepared = false;

    Scroller mScroller;

    public BaseReadView(Context context, String bookId, OnReadStateChangeListener listener) {
        super(context);
        this.listener = listener;
        this.bookId = bookId;

        mScreenWidth = ScreenUtils.getScreenWidth(context);
        mScreenHeight = ScreenUtils.getScreenHeight(context);

        mCurPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
        mNextPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
        mCurrentPageCanvas = new Canvas(mCurPageBitmap);
        mNextPageCanvas = new Canvas(mNextPageBitmap);

        mScroller = new Scroller(getContext());

        pagefactory = new PageFactory(context);
//        pagefactory.setOnReadStateChangeListener(listener);
    }

    protected void init(){
        try {
            postInvalidate();
        }catch (Exception e){

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calcPoints();
        drawCurrentPageArea(canvas);
        drawNextPageAreaAndShadow(canvas);
        drawCurrentPageShadow(canvas);
        drawCurrentBackArea(canvas);
    }

    protected abstract void drawNextPageAreaAndShadow(Canvas canvas);

    protected abstract void drawCurrentPageShadow(Canvas canvas);

    protected abstract void drawCurrentBackArea(Canvas canvas);

    protected abstract void drawCurrentPageArea(Canvas canvas);

    protected abstract void calcPoints();
}
