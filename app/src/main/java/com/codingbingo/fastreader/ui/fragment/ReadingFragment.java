package com.codingbingo.fastreader.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codingbingo.fastreader.R;
import com.codingbingo.fastreader.base.BaseActivity;
import com.codingbingo.fastreader.base.BaseFragment;
import com.codingbingo.fastreader.ui.activity.LocalFileListActivity;
import com.codingbingo.fastreader.utils.StringUtils;
import com.codingbingo.fastreader.view.readview.PageWidget;
import com.codingbingo.fastreader.view.readview.ReadController;
import com.codingbingo.fastreader.view.readview.interfaces.OnControllerStatusChangeListener;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/3/30.
 */

public class ReadingFragment extends BaseFragment implements OnControllerStatusChangeListener {

    private PageWidget readPageWidget;
    private ReadController readController;

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
        View view = inflater.inflate(R.layout.fragment_reading, null, false);

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
            readPageWidget.setBookPath(bookPath);
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
    }

    @Override
    public void onControllerStatusChange(boolean isShowing) {
        switchFullScreen(!isShowing);
    }
}
