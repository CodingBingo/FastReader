package com.codingbingo.fastreader.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.codingbingo.fastreader.R;
import com.codingbingo.fastreader.base.BaseFragment;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/3/30.
 */

public class ChapterListFragment extends BaseFragment implements View.OnClickListener{

    private RecyclerView mChapterListView;
    private ImageView mBackBtn;

    private int mCurrentChapter;

    public ChapterListFragment() {
    }

    public void setmCurrentChapter(int mCurrentChapter) {
        this.mCurrentChapter = mCurrentChapter;
    }

    @Override
    public String getFragmentName() {
        return getClass().getSimpleName();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chapter_list, null);

        switchFullScreen(true);

        initView(view);

        return view;
    }

    private void initView(View view) {

        mChapterListView = (RecyclerView) view.findViewById(R.id.chapter_list);
        mBackBtn = (ImageView) view.findViewById(R.id.back_btn);

        mBackBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_btn:
                getFragmentManager().popBackStack();
                break;
        }
    }
}
