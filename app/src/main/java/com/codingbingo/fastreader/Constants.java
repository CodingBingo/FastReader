package com.codingbingo.fastreader;

/**
 * Created by bingo on 2017/1/4.
 */

public class Constants {

    public static final String APP_ID = "OPzGifOOcII3IHvXEllk6SRS-gzGzoHsz";
    public static final String APP_KEY = "ABleRzk9C1SJRIQYRCuOWWV8";
    public static final String DB_NAME = "reader_db";

    public static final int TYPE_FROM_MAIN_ACTIVITY = 1;
    public static final int TYPE_FROM_LOCAL_FILE_ACTIVITY = 2;

    public static final int STYLE_MAX_FONT_SIZE = 95;
    public static final int STYLE_MIN_FONT_SIZE = 40;
    public static final int STYLE_NORMAL_FONT_SIZE = 50;

    /**
     * 只是插入数据，未做任何处理
     */
    public static final int BOOK_UNPROCESS = -1;

    /**
     * 已经读取编码方式
     */
    public static final int BOOK_CHARSETFINISH = 0;
    /**
     * 正在处理中
     */
    public static final int BOOK_PROCESSING = 1;
    /**
     *
     */
    public static final int BOOK_PROCESSED = 2;
}
