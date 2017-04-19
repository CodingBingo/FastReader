package com.codingbingo.fastreader.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codingbingo.fastreader.FRApplication;
import com.codingbingo.fastreader.R;
import com.codingbingo.fastreader.dao.Book;
import com.codingbingo.fastreader.dao.BookDao;
import com.codingbingo.fastreader.dao.Chapter;
import com.codingbingo.fastreader.dao.ChapterDao;
import com.codingbingo.fastreader.dao.DaoSession;
import com.codingbingo.fastreader.model.eventbus.RefreshBookListEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/4/18.
 */

public class MainControllerBottomSheetFragment extends BottomSheetDialogFragment {

    private Book book;
    private DaoSession mDaoSession;

    public MainControllerBottomSheetFragment() {
        mDaoSession = FRApplication.getInstance().getDaoSession();
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_dialog_fragment, container, false);

        initView(view);

        return view;
    }

    private void initView(View view){
        TextView novelTitle = (TextView) view.findViewById(R.id.novel_title);
        novelTitle.setText(book.getBookName());

        RelativeLayout deleteLayout = (RelativeLayout) view.findViewById(R.id.delete);
        deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Chapter> chapterList = mDaoSession.getChapterDao().queryBuilder().where(ChapterDao.Properties.BookId.eq(book.getId())).list();
                mDaoSession.getChapterDao().deleteInTx(chapterList);
                //删除书籍
                mDaoSession.getBookDao().delete(book);

                EventBus.getDefault().post(new RefreshBookListEvent());

                dismiss();
            }
        });
    }
}
