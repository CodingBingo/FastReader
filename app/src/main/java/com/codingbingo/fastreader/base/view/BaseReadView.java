package com.codingbingo.fastreader.base.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import com.codingbingo.fastreader.view.readview.PageFactory;

/**
 * Created by bingo on 2017/1/4.
 */

public abstract class BaseReadView extends View {

    protected long bookId;

    //屏幕属性
    protected int screenWidth;
    protected int screenHeight;

    //当前页
    protected Bitmap mCurrentPageBitmap;
    protected Canvas mCurrentPageCanvas;
    //下一页
    protected Bitmap mNextPageBitmap;
    protected Canvas mNextPageCanvas;


    protected PageFactory pageFactory;

    public BaseReadView(Context context) {
        super(context);

        mCurrentPageBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        mNextPageBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);

        mCurrentPageCanvas = new Canvas(mCurrentPageBitmap);
        mNextPageCanvas = new Canvas(mNextPageBitmap);


        pageFactory = new PageFactory(context);

    }

    protected void init(){
        try {
            pageFactory.onDraw(mCurrentPageCanvas);
            postInvalidate();
        }catch (Exception e){

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        drawCurrentPageArea(canvas);
    }

    protected abstract void drawCurrentPageArea(Canvas canvas);
}
