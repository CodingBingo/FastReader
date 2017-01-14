package com.codingbingo.fastreader.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingbingo.fastreader.R;
import com.codingbingo.fastreader.model.Book;

import java.util.List;

/**
 * Created by bingo on 2016/12/21.
 */

public class BookListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ADD_BOOK = 1;
    private static final int VIEW_TYPE_NORMAL = 2;

    private Context mContext;
    private List<Book> bookList;

    public BookListAdapter(Context mContext, List<Book> bookList) {
        this.mContext = mContext;
        this.bookList = bookList;
    }

    class BookViewHolder extends RecyclerView.ViewHolder{

        private ImageView novelImage;
        private TextView novelTag;
        private TextView novelTitle;

        public BookViewHolder(View itemView) {
            super(itemView);

            novelImage = (ImageView) itemView.findViewById(R.id.novel_image);
            novelTag = (TextView) itemView.findViewById(R.id.novel_tag);
            novelTitle = (TextView) itemView.findViewById(R.id.novel_title);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.book_list_item, null);
        RecyclerView.ViewHolder viewHolder = new BookViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
            BookViewHolder bookViewHolder = (BookViewHolder) holder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() == position){
            return VIEW_TYPE_ADD_BOOK;
        }else{
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return bookList.size() + 1;
    }
}
