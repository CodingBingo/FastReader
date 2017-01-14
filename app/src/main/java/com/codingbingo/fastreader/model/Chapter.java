package com.codingbingo.fastreader.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/1/4.
 */
@Entity(generateGettersSetters = true)
public class Chapter{
    @Id
    private long id;
    @NotNull
    private long bookId;
    @NotNull
    private String title;
    private boolean isRead;
    private int pageCount;
    @NotNull
    private long position;
    @Generated(hash = 1618310764)
    public Chapter(long id, long bookId, @NotNull String title, boolean isRead,
            int pageCount, long position) {
        this.id = id;
        this.bookId = bookId;
        this.title = title;
        this.isRead = isRead;
        this.pageCount = pageCount;
        this.position = position;
    }
    @Generated(hash = 393170288)
    public Chapter() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getBookId() {
        return this.bookId;
    }
    public void setBookId(long bookId) {
        this.bookId = bookId;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public boolean getIsRead() {
        return this.isRead;
    }
    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
    public int getPageCount() {
        return this.pageCount;
    }
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
    public long getPosition() {
        return this.position;
    }
    public void setPosition(long position) {
        this.position = position;
    }
}
