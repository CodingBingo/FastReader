package com.codingbingo.fastreader.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingbingo.fastreader.R;
import com.codingbingo.fastreader.dao.Book;
import com.codingbingo.fastreader.utils.StringUtils;

import java.util.List;

/**
 * Created by bingo on 2016/12/21.
 */

public class BookListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ADD_BOOK = 1;
    private static final int VIEW_TYPE_NORMAL = 2;

    private Context mContext;
    private List<Book> bookList;
    private OnBookListItemClickListener onBookListItemClickListener;

    public BookListAdapter(Context mContext, List<Book> bookList) {
        this.mContext = mContext;
        this.bookList = bookList;
    }

    class BookViewHolder extends RecyclerView.ViewHolder {

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

    public void setOnBookListItemClickListener(OnBookListItemClickListener onBookListItemClickListener) {
        this.onBookListItemClickListener = onBookListItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.book_list_item, null);
        RecyclerView.ViewHolder viewHolder = new BookViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
            BookViewHolder bookViewHolder = (BookViewHolder) holder;

            Book book = bookList.get(position);

            String bookName;
            String bookTag;

            String fileName = book.getBookName();
            if (fileName.contains(".")) {
                String[] s = fileName.split("\\.");
                if (s.length == 0) {
                    bookName = fileName;
                    bookTag = "";
                } else {
                    bookName = s[0];
                    bookTag = s[1];
                }
            } else {
                bookName = fileName;
                bookTag = "";
            }
            bookViewHolder.novelTitle.setText(bookName);
            bookViewHolder.novelTag.setText(bookTag.toUpperCase());

            if (StringUtils.isBlank(book.getBookImagePath())) {
                bookViewHolder.novelImage.setBackgroundColor(mContext.getResources().getColor(R.color.default_book_bg));
            } else {
                //加载
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBookListItemClickListener != null) {
                    if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
                        onBookListItemClickListener.onBookItemClick(bookList.get(position), position);
                    } else if (getItemViewType(position) == VIEW_TYPE_ADD_BOOK) {
                        onBookListItemClickListener.onBookItemClick(null, position);
                    }
                }
            }
        });


    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() - 1 == position) {
            return VIEW_TYPE_ADD_BOOK;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return bookList.size() + 1;
    }

    /**
     *
     */
    public interface OnBookListItemClickListener {
        void onBookItemClick(Book book, int position);
    }
}
