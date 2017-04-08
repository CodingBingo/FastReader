package com.codingbingo.fastreader.view.readview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.codingbingo.fastreader.R;
import com.codingbingo.fastreader.utils.ScreenUtils;
import com.codingbingo.fastreader.view.readview.interfaces.OnControllerStatusChangeListener;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/1/11.
 */

public class ReadController extends FrameLayout implements View.OnTouchListener, Animation.AnimationListener {
    private Context mContext;
    private View statusBarBg;
    private LinearLayout controllerTopBar;
    private LinearLayout controllerBottomBar;
    //书籍目录
    private RelativeLayout mBookContents;
    //阅读模式：黑夜模式、白天
    private RelativeLayout mBookMode;
    //书籍样式
    private RelativeLayout mBookFonts;

    private ImageView backBtn;

    private Animation topOutAnimation;
    private Animation topInAnimation;
    private Animation bottomOutAnimation;
    private Animation bottomInAnimation;

    private OnControllerStatusChangeListener onControllerStatusChangeListener;
    private OnClickListener onClickListener;
    private boolean isShowing = false;

    private int statusBarHeight;

    public ReadController(Context context) {
        this(context, null, 0);
    }

    public ReadController(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReadController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        //获取对应的高度
        statusBarHeight = ScreenUtils.getStatusBarHeight(context);

        init();
        initView();

        this.setOnTouchListener(this);
        isShowing = false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int width = v.getWidth();
        int height = v.getHeight();

        if (isShowing) {
            hideController();
        } else {
            showController();
        }

        //返回controller状态
        isShowing = !isShowing;
        if (isShowing) {
            if (onControllerStatusChangeListener != null) {
                onControllerStatusChangeListener.onControllerStatusChange(isShowing);
            }
        }
        return false;
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.read_controller, this);

        topOutAnimation = AnimationUtils.loadAnimation(mContext, R.anim.top_out_animation);
        topInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.top_in_animation);
        bottomInAnimation = AnimationUtils.loadAnimation(mContext, R.anim.bottom_in_animation);
        bottomOutAnimation = AnimationUtils.loadAnimation(mContext, R.anim.bottom_out_animation);

        topOutAnimation.setFillAfter(true);
        topInAnimation.setFillAfter(true);
        bottomInAnimation.setFillAfter(true);
        bottomOutAnimation.setFillAfter(true);

        topOutAnimation.setAnimationListener(this);
        topInAnimation.setAnimationListener(this);
        bottomInAnimation.setAnimationListener(this);
        bottomOutAnimation.setAnimationListener(this);
    }

    private void initView() {
        statusBarBg = findViewById(R.id.status_bar_bg);
        statusBarBg.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight));//调整高度，让界面变得流畅点

        controllerTopBar = (LinearLayout) findViewById(R.id.controllerTopBar);
        controllerBottomBar = (LinearLayout) findViewById(R.id.controllerBottomBar);
        backBtn = (ImageView) findViewById(R.id.backBtn);
        mBookFonts = (RelativeLayout) findViewById(R.id.book_fonts);
        mBookContents = (RelativeLayout) findViewById(R.id.book_contents);
        mBookMode = (RelativeLayout) findViewById(R.id.book_mode);

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

    public void setOnViewClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;

        backBtn.setOnClickListener(onClickListener);
        mBookContents.setOnClickListener(onClickListener);
        mBookFonts.setOnClickListener(onClickListener);
        mBookMode.setOnClickListener(onClickListener);
    }

    private void invalidClickListener(){
        backBtn.setOnClickListener(null);
        mBookContents.setOnClickListener(null);
        mBookFonts.setOnClickListener(null);
        mBookMode.setOnClickListener(null);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (onControllerStatusChangeListener != null) {
            onControllerStatusChangeListener.onControllerStatusChange(isShowing);
        }

        if (isShowing){
            setOnViewClickListener(onClickListener);
        } else{
            invalidClickListener();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
