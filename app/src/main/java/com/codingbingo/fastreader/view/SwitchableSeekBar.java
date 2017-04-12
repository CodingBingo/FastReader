package com.codingbingo.fastreader.view;

import android.content.Context;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/4/12.
 */

public class SwitchableSeekBar extends AppCompatSeekBar {

    private boolean enable = false;
    private Toast toast;

    public SwitchableSeekBar(Context context) {
        this(context, null);
    }

    public SwitchableSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.seekBarStyle);
    }

    public SwitchableSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (enable) {
            return super.onTouchEvent(event);
        } else {
            showToast("书籍未加载完成，暂时不可目录快进");
            return true;
        }
    }

    private void showToast(String content){
        if (toast == null){
            toast = Toast.makeText(getContext(), content, Toast.LENGTH_SHORT);
        }else{
            toast.setText(content);
        }

        toast.show();
    }
}
