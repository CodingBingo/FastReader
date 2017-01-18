package com.codingbingo.fastreader.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/1/4.
 */
@Entity
public class Chapter{
    @Id(autoincrement = true)
    private Long id;
    private Long bookId;
    @ToOne(joinProperty = "bookId")
    private Book book;
    @NotNull
    private String title;
    private boolean isRead;
    private int pageCount;
    @NotNull
    private long position;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1364227941)
    private transient ChapterDao myDao;
    @Generated(hash = 742933570)
    public Chapter(Long id, Long bookId, @NotNull String title, boolean isRead,
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
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getBookId() {
        return this.bookId;
    }
    public void setBookId(Long bookId) {
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
    @Generated(hash = 893611298)
    private transient Long book__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 577500745)
    public Book getBook() {
        Long __key = this.bookId;
        if (book__resolvedKey == null || !book__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            BookDao targetDao = daoSession.getBookDao();
            Book bookNew = targetDao.load(__key);
            synchronized (this) {
                book = bookNew;
                book__resolvedKey = __key;
            }
        }
        return book;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1131281780)
    public void setBook(Book book) {
        synchronized (this) {
            this.book = book;
            bookId = book == null ? null : book.getId();
            book__resolvedKey = bookId;
        }
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1600057230)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getChapterDao() : null;
    }
}
