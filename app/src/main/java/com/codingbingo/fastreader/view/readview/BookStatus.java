package com.codingbingo.fastreader.view.readview;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/2/14.
 */
public enum BookStatus {

    STATUS_OK,
    STATUS_DATABASE_ERROR,
    STATUS_FILE_NOT_FOUND_ERROR,

    NO_PRE_PAGE,
    NO_NEXT_PAGE,

    PRE_CHAPTER_LOAD_FAILURE,
    NEXT_CHAPTER_LOAD_FAILURE,

    LOAD_SUCCESS
}
