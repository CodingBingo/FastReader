package com.codingbingo.fastreader.view.readview.interfaces;

import com.codingbingo.fastreader.dao.Book;
import com.codingbingo.fastreader.view.readview.BookStatus;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/2/14.
 */
public interface OnReadStateChangeListener {
    /**
     * 加载book完毕，完成编码方式、断章等等
     *
     * @param status
     * @param book
     */
    void onBookLoaded(BookStatus status, Book book);
}
