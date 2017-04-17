package com.codingbingo.fastreader.view.loadingview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codingbingo.fastreader.R;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/4/17.
 */

public class CatLoadingView extends RelativeLayout {
    //动画
    Animation operatingAnim, eye_left_Anim, eye_right_Anim;

    View mouse, eye_left, eye_right;

    //眼睛
    EyelidView eyelid_left, eyelid_right;

    GraduallyTextView mGraduallyTextView;

    TextView mLoadingProgress;

    //loading文字
    String text;

    String progress;

    public CatLoadingView(Context context) {
        this(context, null, 0);
    }

    public CatLoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CatLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.cat_loading, this);
        init();
    }

    private void init() {
        operatingAnim = new RotateAnimation(360f, 0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        operatingAnim.setRepeatCount(Animation.INFINITE);
        operatingAnim.setDuration(2000);

        eye_left_Anim = new RotateAnimation(360f, 0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        eye_left_Anim.setRepeatCount(Animation.INFINITE);
        eye_left_Anim.setDuration(2000);

        eye_right_Anim = new RotateAnimation(360f, 0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        eye_right_Anim.setRepeatCount(Animation.INFINITE);
        eye_right_Anim.setDuration(2000);

        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        eye_left_Anim.setInterpolator(lin);
        eye_right_Anim.setInterpolator(lin);

        mouse = findViewById(R.id.mouse);

        mLoadingProgress = (TextView) findViewById(R.id.loading_progress);

        eye_left = findViewById(R.id.eye_left);

        eye_right = findViewById(R.id.eye_right);

        eyelid_left = (EyelidView) findViewById(R.id.eyelid_left);

        eyelid_left.setColor(Color.parseColor("#d0ced1"));

        eyelid_left.setFromFull(true);

        eyelid_right = (EyelidView) findViewById(R.id.eyelid_right);

        eyelid_right.setColor(Color.parseColor("#d0ced1"));

        eyelid_right.setFromFull(true);

        mGraduallyTextView = (GraduallyTextView) findViewById(R.id.graduallyTextView);

        if (!TextUtils.isEmpty(text)) {
            mGraduallyTextView.setText(text);
        }

        operatingAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {
            }

            @Override public void onAnimationEnd(Animation animation) {
            }

            @Override public void onAnimationRepeat(Animation animation) {
                eyelid_left.resetAnimator();
                eyelid_right.resetAnimator();
            }
        });
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        operatingAnim.reset();
        eye_left_Anim.reset();
        eye_right_Anim.reset();

        mouse.clearAnimation();
        eye_left.clearAnimation();
        eye_right.clearAnimation();

        eyelid_left.stopLoading();
        eyelid_right.stopLoading();
        mGraduallyTextView.stopLoading();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        mouse.setAnimation(operatingAnim);
        eye_left.setAnimation(eye_left_Anim);
        eye_right.setAnimation(eye_right_Anim);
        eyelid_left.startLoading();
        eyelid_right.startLoading();
        mGraduallyTextView.startLoading();
    }

    public void setText(String str) {
        text = str;
    }

    public void setLoadingProgress(int loadingProgress){
        progress = loadingProgress + " %";

        mLoadingProgress.setText(progress);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
