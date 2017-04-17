package com.codingbingo.fastreader.base.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import android.widget.Toast;

import com.codingbingo.fastreader.FRApplication;
import com.codingbingo.fastreader.dao.Book;
import com.codingbingo.fastreader.dao.BookDao;
import com.codingbingo.fastreader.dao.ChapterDao;
import com.codingbingo.fastreader.dao.DaoSession;
import com.codingbingo.fastreader.model.eventbus.StyleChangeEvent;
import com.codingbingo.fastreader.utils.ScreenUtils;
import com.codingbingo.fastreader.view.readview.BookStatus;
import com.codingbingo.fastreader.view.readview.PageFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by bingo on 2017/1/4.
 */

public abstract class BaseReadView extends View {
    protected Context mContext;

    protected int mScreenWidth;
    protected int mScreenHeight;

    protected PointF mTouch = new PointF();
    protected float actiondownX, actiondownY;
    protected float touch_down = 0; // 当前触摸点与按下时的点的差值

    protected Bitmap mCurPageBitmap, mNextPageBitmap;
    protected Canvas mCurrentPageCanvas, mNextPageCanvas;
    protected PageFactory pagefactory = null;

    protected long bookId;
    protected String bookPath;
    protected Book book;
    protected boolean isPrepared = true;

    private Toast toast;
    protected Scroller mScroller;

    protected DaoSession daoSession;
    protected BookDao bookDao;
    protected ChapterDao chapterDao;


    public BaseReadView(Context context){
        this(context, null, 0);
    }

    public BaseReadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public BaseReadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        mScreenWidth = ScreenUtils.getScreenWidth(context);
        mScreenHeight = ScreenUtils.getScreenHeight(context);

        mCurPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
        mNextPageBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.ARGB_8888);
        mCurrentPageCanvas = new Canvas(mCurPageBitmap);
        mNextPageCanvas = new Canvas(mNextPageBitmap);

        mScroller = new Scroller(getContext());
        pagefactory = new PageFactory(context);


        //数据库
        daoSession = ((FRApplication) context.getApplicationContext()).getDaoSession();
        bookDao = daoSession.getBookDao();
        chapterDao = daoSession.getChapterDao();
    }


    protected synchronized void init() {
        try {
            book = bookDao.load(bookId);

            pagefactory.onDraw(mCurrentPageCanvas);
            //主动刷新界面
            postInvalidate();
        } catch (Exception e) {
        }
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
        pagefactory.openBook(bookId);

        init();
    }

    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
        pagefactory.openBook(bookPath);

        init();
    }

    private int dx, dy;
    private long et = 0;
    private boolean cancel = false;
    private boolean center = false;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
//        switch (e.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (center)
//                    break;
//                int mx = (int) e.getX();
//                int my = (int) e.getY();
//                cancel = (actiondownX < mScreenWidth / 2 && mx < mTouch.x) || (actiondownX > mScreenWidth / 2 && mx > mTouch.x);
//                mTouch.x = mx;
//                mTouch.y = my;
//                touch_down = mTouch.x - actiondownX;
//                this.postInvalidate();
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//
//                break;
//            default:
//                break;
//        }
        return true;
    }

    /**
     * 复位触摸点位
     */
    protected void resetTouchPoint() {
        mTouch.x = 0.1f;
        mTouch.y = 0.1f;
        touch_down = 0;
        calcCornerXY(mTouch.x, mTouch.y);
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

    protected abstract void calcCornerXY(float x, float y);

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //取消订阅事件
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //订阅事件
        if (EventBus.getDefault().isRegistered(this) == false) {
            EventBus.getDefault().register(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveStyleChangeEvent(StyleChangeEvent styleChangeEvent){
        if (pagefactory != null) {
            pagefactory.refreshAccordingToStyle(mCurrentPageCanvas);
            invalidate();
        }
    }

    /**
     * 显示Toast提示
     * @param content
     */
    protected void showToast(String content){
        if (toast == null) {
            toast = Toast.makeText(mContext, content, Toast.LENGTH_SHORT);
        }else {
            toast.setText(content);
        }
        toast.show();
    }

    public void nextPage() {
        BookStatus status = pagefactory.nextPage();
        if (status == BookStatus.NO_NEXT_PAGE) {
            showToast("没有下一页啦");
            return;
        } else if (status == BookStatus.LOAD_SUCCESS) {
            if (isPrepared) {
                pagefactory.onDraw(mCurrentPageCanvas);
                postInvalidate();
            }
        } else {
            return;
        }

    }

    public void prePage() {
        BookStatus status = pagefactory.prePage();
        if (status == BookStatus.NO_PRE_PAGE) {
            showToast("没有上一页啦");
            return;
        } else if (status == BookStatus.LOAD_SUCCESS) {
            if (isPrepared) {
                pagefactory.onDraw(mCurrentPageCanvas);
                postInvalidate();
            }
        } else {
            return;
        }
    }
}
