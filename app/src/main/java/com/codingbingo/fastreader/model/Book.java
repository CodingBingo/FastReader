package com.codingbingo.fastreader.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Created by bingo on 2016/12/24.
 */

@Entity
public class Book {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String bookName;
    private String bookImagePath;
    private String description;
    private String tags;
    private String writer;
    private String charSet;
    @NotNull
    private String bookPath;
    @Generated(hash = 1476379689)
    public Book(Long id, @NotNull String bookName, String bookImagePath,
            String description, String tags, String writer, String charSet,
            @NotNull String bookPath) {
        this.id = id;
        this.bookName = bookName;
        this.bookImagePath = bookImagePath;
        this.description = description;
        this.tags = tags;
        this.writer = writer;
        this.charSet = charSet;
        this.bookPath = bookPath;
    }
    @Generated(hash = 1839243756)
    public Book() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getBookName() {
        return this.bookName;
    }
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
    public String getBookImagePath() {
        return this.bookImagePath;
    }
    public void setBookImagePath(String bookImagePath) {
        this.bookImagePath = bookImagePath;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getTags() {
        return this.tags;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }
    public String getWriter() {
        return this.writer;
    }
    public void setWriter(String writer) {
        this.writer = writer;
    }
    public String getCharSet() {
        return this.charSet;
    }
    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }
    public String getBookPath() {
        return this.bookPath;
    }
    public void setBookPath(String bookPath) {
        this.bookPath = bookPath;
    }
}
