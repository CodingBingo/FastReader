package com.codingbingo.fastreader.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codingbingo.fastreader.Constants;
import com.codingbingo.fastreader.R;
import com.codingbingo.fastreader.base.BaseActivity;
import com.codingbingo.fastreader.base.BaseFragment;
import com.codingbingo.fastreader.dao.Book;
import com.codingbingo.fastreader.dao.BookDao;
import com.codingbingo.fastreader.model.eventbus.BookStatusChangeEvent;
import com.codingbingo.fastreader.view.loadingview.CatLoadingView;
import com.codingbingo.fastreader.view.readview.PageWidget;
import com.codingbingo.fastreader.view.readview.ReadController;
import com.codingbingo.fastreader.view.readview.interfaces.OnControllerStatusChangeListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/3/30.
 */

public class ReadingFragment extends BaseFragment implements OnControllerStatusChangeListener {

    private PageWidget readPageWidget;
    private ReadController readController;
    private CatLoadingView readLoadingView;

    private View.OnClickListener onClickListener;

    private long bookId;
    private String bookPath;

    @Override
    public String getFragmentName() {
        return ReadingFragment.class.getSimpleName();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reading, container, false);

        initView(view);
        return view;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private void initView(View view) {
        readController = (ReadController) view.findViewById(R.id.readController);
        readController.setOnControllerStatusChangeListener(this);
        if (onClickListener != null) {
            readController.setOnViewClickListener(onClickListener);
        }

        readPageWidget = (PageWidget) view.findViewById(R.id.readPageWidget);
        //bookId判断书籍状态种类
        if (BaseActivity.NO_BOOK_ID != bookId) {
            readPageWidget.setBookId(bookId);
        } else {
            List<Book> bookList = getDaoSession()
                    .getBookDao()
                    .queryBuilder()
                    .where(BookDao.Properties.BookPath.eq(bookPath))
                    .list();
            if (bookList.size() == 0) {
                readPageWidget.setBookPath(bookPath);
            } else{
                readPageWidget.setBookId(bookList.get(0).getId());
            }
        }

        readLoadingView = (CatLoadingView) view.findViewById(R.id.loading);
        Book book = getDaoSession().getBookDao().load(bookId);
        if (book == null || book.getProcessStatus() != Constants.BOOK_PROCESSED) {
            readLoadingView.setVisibility(View.VISIBLE);
        } else {
            readLoadingView.setVisibility(View.GONE);
        }
    }

    public void nextPage(){
        readPageWidget.nextPage();
    }

    public void prePage(){
        readPageWidget.prePage();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (EventBus.getDefault().isRegistered(this) == false) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (EventBus.getDefault().isRegistered(this) == true) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BookStatusChangeEvent bookStatusChangeEvent){
        switch (bookStatusChangeEvent.getStatus()){
            case Constants.BOOK_PROCESSED:
                readLoadingView.setVisibility(View.GONE);
                readPageWidget.setBookId(bookStatusChangeEvent.getBookId());
                bookId = bookStatusChangeEvent.getBookId();
                readPageWidget.postInvalidate();
                break;
            default:
                readLoadingView.setVisibility(View.VISIBLE);
                readLoadingView.setLoadingProgress(bookStatusChangeEvent.getProgress());
                break;
        }
    }

    @Override
    public void onControllerStatusChange(boolean isShowing) {
        switchFullScreen(!isShowing);
    }
}
