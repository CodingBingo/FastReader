package com.codingbingo.fastreader.view.readview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.codingbingo.fastreader.Constants;
import com.codingbingo.fastreader.FRApplication;
import com.codingbingo.fastreader.dao.Book;
import com.codingbingo.fastreader.dao.BookDao;
import com.codingbingo.fastreader.dao.Chapter;
import com.codingbingo.fastreader.dao.ChapterDao;
import com.codingbingo.fastreader.dao.DaoSession;
import com.codingbingo.fastreader.utils.FileUtils;
import com.codingbingo.fastreader.utils.ThreadPool;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 阅读页面产生工厂
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/1/4.
 */

public class PageFactory {

    private int mWidth, mHeight;

    private int marginHeight;

    private int fontSize;
    //行间距
    private int mLineSpace;

    //文字样式

    //章节数
    private int mChapterSize;
    //当前的章节
    private int mCurrentChapter;

    private long mByteBufferLength;
    //当前页面开始的位置
    private int mCurrentPageStartPosition;

    //阅读背景
    private Bitmap bgBitmap;
    /**
     * 文件内存映射，高效读写
     */
    private MappedByteBuffer mMappedByteBuffer;
    //书籍的bookId
    private int bookId;
    private List<Chapter> chapterList;
    //时间显示
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

    //线程池
    private ThreadPool threadPool;

    //书籍对象
    private Book book;

    private BookDao mBookDao;
    private ChapterDao mChapterDao;

    private BookStatusChangedListener mBookStatusChangedListener;

    public PageFactory(Context context) {
        DaoSession daoSession = ((FRApplication) context.getApplicationContext()).getDaoSession();

        mBookDao = daoSession.getBookDao();
        mChapterDao = daoSession.getChapterDao();
        threadPool = ThreadPool.getInstance();

    }

    public void setmBookStatusChangedListener(BookStatusChangedListener mBookStatusChangedListener) {
        this.mBookStatusChangedListener = mBookStatusChangedListener;
    }

    public void openBook(long bookId, int chapter, int currentPosition) {
        List<Book> bookList = mBookDao.queryBuilder().where(BookDao.Properties.Id.eq(bookId)).list();
        if (bookList == null || bookList.isEmpty()) {
            //书籍为空
            return;
        }
        book = bookList.get(0);

        mCurrentChapter = chapter;
        if (chapterList != null) {
            mChapterSize = chapterList.size();
        }
        mCurrentPageStartPosition = currentPosition;
        try {
            File file = new File(book.getBookPath());
            long length = file.length();
            if (length > 10) {
                mByteBufferLength = length;

                mMappedByteBuffer = new RandomAccessFile(file, "r")
                        .getChannel()
                        .map(FileChannel.MapMode.READ_ONLY, 0, length);

                mCurrentPageStartPosition = currentPosition;
            }

            switch (book.getProcessStatus()){
                case Constants.BOOK_NORMAL:
                    book.setProcessStatus(Constants.BOOK_PROCESSING);//改为正在处理中
                    mBookDao.update(book);

                    processChapters();
                    break;
                case Constants.BOOK_PROCESSING:
                    List<Chapter> chapterList = mChapterDao.queryBuilder().where(ChapterDao.Properties.BookId.eq(book.getId())).list();
                    mChapterDao.deleteInTx(chapterList);

                    book.setProcessStatus(Constants.BOOK_PROCESSING);//改为正在处理中
                    mBookDao.update(book);

                    processChapters();
                    break;
                case Constants.BOOK_PROCESSED:
                    //可以开始处理书籍数据了

                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class OpenBookTask implements Runnable {
        private String bookPath;

        public OpenBookTask(String bookPath) {
            this.bookPath = bookPath;
        }

        @Override
        public void run() {
            int statusCode = BookStatusChangedListener.STATUS_OK;
            Book book = new Book();

            File file = new File(bookPath);
            if (file.exists() == true) {
                book.setBookName(file.getName());
                book.setBookPath(bookPath);
                String charSet = FileUtils.getJavaEncode(bookPath);
                book.setCharSet(charSet);
                book.setDescription("");
                book.setBookImagePath("");
                try {
                    long id = mBookDao.insert(book);
                    book.setId(id);
                    //开始读取书籍信息
                    openBook(id, 0, 0);
                }catch (Exception e){
                    //插入数据库的时候可能会失败
                    statusCode = BookStatusChangedListener.STATUS_DATABASE_ERROR;
                }
            } else {
                statusCode = BookStatusChangedListener.STATUS_FILE_NOT_FOUND_ERROR;
            }
            if (mBookStatusChangedListener != null) {
                mBookStatusChangedListener.onBookLoaded(statusCode, book);
            }
        }
    }

    /**
     * 打开书籍
     *
     * @param bookPath 书籍的位置
     */
    public void openBook(String bookPath) {
        threadPool.submitTask(new OpenBookTask(bookPath));
    }

    /**
     * 读书籍目录，断章
     * 这个需要放到一个线程里面运行
     */
    public void processChapters() {
        if (chapterList == null) {
            chapterList = new ArrayList<>();
        }
        int currentPosition = 0;
        int lastPosition = 0;

        Pattern pattern = Pattern.compile("第.{1,7}章.*\r\n");

        while (currentPosition < mByteBufferLength) {
            byte[] bytes = readParagraphForward(currentPosition);

            try {
                String paragraph = new String(bytes, book.getCharSet());

                Matcher matcher = pattern.matcher(paragraph);
                if (matcher.find()) {
                    //修正章节错
                    if (currentPosition - lastPosition > 200 && bytes.length < 50) {
                        if (book != null) {
                            Chapter chapter = new Chapter();
                            chapter.setId(null);
                            chapter.setTitle(matcher.group());
                            chapter.setPosition(currentPosition);
                            chapter.setIsRead(false);
                            chapter.setBook(book);
                            //插入数据库
                            mChapterDao.insert(chapter);
                            chapterList.add(chapter);

                            lastPosition = currentPosition;
                        }
                    }
                }

            } catch (UnsupportedEncodingException e) {
                //不支持编码
            }

            currentPosition += bytes.length;
        }
    }

    /**
     * 读取下一段落
     *
     * @param curEndPos 当前页结束位置指针
     * @return
     */
    private byte[] readParagraphForward(int curEndPos) {
        byte b0;
        int i = curEndPos;
        while (i < mByteBufferLength) {
            b0 = mMappedByteBuffer.get(i++);
            if (b0 == 0x0a) {
                break;
            }
        }
        int nParaSize = i - curEndPos;
        byte[] buf = new byte[nParaSize];
        for (i = 0; i < nParaSize; i++) {
            buf[i] = mMappedByteBuffer.get(curEndPos + i);
        }
        return buf;
    }

    /**
     * 读取上一段落
     *
     * @param curBeginPos 当前页起始位置指针
     * @return
     */
    private byte[] readParagraphBack(int curBeginPos) {
        byte b0;
        int i = curBeginPos - 1;
        while (i > 0) {
            b0 = mMappedByteBuffer.get(i);
            if (b0 == 0x0a && i != curBeginPos - 1) {
                i++;
                break;
            }
            i--;
        }
        int nParaSize = curBeginPos - i;
        byte[] buf = new byte[nParaSize];
        for (int j = 0; j < nParaSize; j++) {
            buf[j] = mMappedByteBuffer.get(i + j);
        }
        return buf;
    }

    /**
     * 绘制阅读页面
     *
     * @param canvas
     */
    public synchronized void onDraw(Canvas canvas) {
    }

    //读取一页的内容
    public void readPage() {
        
    }


    public interface BookStatusChangedListener {

        int STATUS_OK = 1;
        int STATUS_DATABASE_ERROR = 2;
        int STATUS_FILE_NOT_FOUND_ERROR = 3;

        /**
         * 加载book完毕，完成编码方式、断章等等
         *
         * @param status
         * @param book
         */
        void onBookLoaded(int status, Book book);
    }
}
