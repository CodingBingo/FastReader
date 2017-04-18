package com.codingbingo.fastreader.model.eventbus;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/4/17.
 */

public class BookStatusChangeEvent {
    private int progress;
    private int status;
    private long bookId;

    public BookStatusChangeEvent() {
    }

    public BookStatusChangeEvent( int status, int progress,long bookId) {
        this.progress = progress;
        this.status = status;
        this.bookId = bookId;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
