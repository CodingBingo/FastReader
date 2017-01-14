package com.codingbingo.fastreader.view.readview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

/**
 * Created by bingo on 2017/1/4.
 */

public class BaseReadView extends View {

    //屏幕属性
    protected int screenWidth;
    protected int screentHeight;

    //当前页
    protected Bitmap mCurrentPageBitmap;
    protected Canvas mCurrentPageCanvas;
    //下一页
    protected Bitmap mNextPageBitmap;
    protected Canvas mNextPageCanvas;


    public BaseReadView(Context context) {
        super(context);
    }

    private void init(){

        mCurrentPageBitmap = Bitmap.createBitmap(screenWidth, screentHeight, Bitmap.Config.ARGB_8888);
        mNextPageBitmap = Bitmap.createBitmap(screenWidth, screentHeight, Bitmap.Config.ARGB_8888);

        mCurrentPageCanvas = new Canvas(mCurrentPageBitmap);
        mNextPageCanvas = new Canvas(mNextPageBitmap);

    }
}
