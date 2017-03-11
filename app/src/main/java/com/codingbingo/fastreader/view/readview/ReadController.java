package com.codingbingo.fastreader.view.readview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.codingbingo.fastreader.R;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/1/11.
 */

public class ReadController extends FrameLayout implements View.OnTouchListener{
    private Context mContext;
    private RelativeLayout controllerTopBar;
    private LinearLayout controllerBottomBar;

    private Animation topOutAnimation;
    private Animation topInAnimation;
    private Animation bottomOutAnimation;
    private Animation bottomInAnimation;

    private OnControllerStatusChangeListener onControllerStatusChangeListener;
    private boolean isShowing = false;

    public ReadController(Context context) {
        this(context, null, 0);
    }

    public ReadController(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReadController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        init();

        this.setOnTouchListener(this);
        isShowing = false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        int width = v.getWidth();
        int height = v.getHeight();

        if (isShowing){
            hideController();
        }else{
            showController();
        }

        //返回controller状态
        if (onControllerStatusChangeListener != null){
            onControllerStatusChangeListener.onControllerStatusChange(!isShowing);
        }
        isShowing = !isShowing;

        //onTouch事件只有在点击中间的时候才返回true
        return false;
    }

    private void init(){
        LayoutInflater.from(mContext).inflate(R.layout.read_controller, this);

        topOutAnimation = AnimationUtils.loadAnimation(mContext, R.anim.top_out_animation);
        topInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.top_in_animation);
        bottomInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.bottom_in_animation);
        bottomOutAnimation = AnimationUtils.loadAnimation(mContext, R.anim.bottom_out_animation);
        topInAnimation.setFillAfter(true);
        topOutAnimation.setFillAfter(true);
        bottomInAnimation.setFillAfter(true);
        bottomOutAnimation.setFillAfter(true);

        controllerTopBar = (RelativeLayout) findViewById(R.id.controllerTopBar);
        controllerBottomBar = (LinearLayout) findViewById(R.id.controllerBottomBar);

        //开始进来之后就要隐藏控制栏
        hideController();
    }

    private void hideController() {
        controllerTopBar.startAnimation(topOutAnimation);
        controllerBottomBar.startAnimation(bottomOutAnimation);
    }

    private void showController() {
        controllerTopBar.startAnimation(topInAnimation);
        controllerBottomBar.startAnimation(bottomInAnimation);
    }

    public void setOnControllerStatusChangeListener(OnControllerStatusChangeListener onControllerStatusChangeListener) {
        this.onControllerStatusChangeListener = onControllerStatusChangeListener;
    }

    public interface OnControllerStatusChangeListener{
        void onControllerStatusChange(boolean isShowing);
    }
}
