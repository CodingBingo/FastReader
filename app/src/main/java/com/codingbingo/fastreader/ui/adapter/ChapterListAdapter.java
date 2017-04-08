package com.codingbingo.fastreader.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.codingbingo.fastreader.dao.Chapter;

import java.util.List;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/4/8.
 */

public class ChapterListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Chapter> chapterList;
    private int mCurrentChapter;

    public ChapterListAdapter(Context mContext, List<Chapter> chapterList, int mCurrentChapter) {
        this.mContext = mContext;
        this.chapterList = chapterList;
        this.mCurrentChapter = mCurrentChapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
