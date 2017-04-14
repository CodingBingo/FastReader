package com.codingbingo.fastreader.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codingbingo.fastreader.R;
import com.codingbingo.fastreader.dao.Chapter;

import java.util.List;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/4/8.
 */

public class ChapterListAdapter extends RecyclerView.Adapter<ChapterListAdapter.ViewHolder> {

    private Context mContext;
    private List<Chapter> chapterList;
    private int mCurrentChapter;

    public ChapterListAdapter(Context mContext, List<Chapter> chapterList, int mCurrentChapter) {
        this.mContext = mContext;
        this.chapterList = chapterList;
        this.mCurrentChapter = mCurrentChapter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.chapter_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Chapter chapter = chapterList.get(position);

        holder.chapterTitle.setText(chapter.getTitle());
        if (position == mCurrentChapter) {
            holder.chapterCurrent.setVisibility(View.VISIBLE);
            holder.chapterTitle.setTypeface(null, Typeface.BOLD);
        }else{
            holder.chapterCurrent.setVisibility(View.INVISIBLE);
            holder.chapterTitle.setTypeface(null, Typeface.NORMAL);
        }
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public View chapterCurrent;
        public TextView chapterTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            chapterCurrent = itemView.findViewById(R.id.chapter_current);
            chapterTitle = (TextView) itemView.findViewById(R.id.chapter_title);
        }
    }
}
