package com.codingbingo.fastreader.view.readview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.codingbingo.fastreader.FRApplication;
import com.codingbingo.fastreader.model.Book;
import com.codingbingo.fastreader.model.BookDao;
import com.codingbingo.fastreader.model.Chapter;
import com.codingbingo.fastreader.model.ChapterDao;
import com.codingbingo.fastreader.model.DaoSession;
import com.codingbingo.fastreader.utils.FileUtils;

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
    //编码方式
    private String charSet = "UTF-8";
    //书籍的bookId
    private int bookId;
    private List<Chapter> chapterList;
    //时间显示
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");

    private BookDao mBookDao;
    private ChapterDao mChapterDao;

    public PageFactory(Context context) {
        DaoSession daoSession = ((FRApplication) context.getApplicationContext()).getDaoSession();

        mBookDao = daoSession.getBookDao();
        mChapterDao = daoSession.getChapterDao();
    }

    public File getBookFileById(int bookId, int chapter) {
        File file = null;
        return file;
    }

    public void openBook(long bookId, int chapter, int currentPosition) {
        mBookDao.queryBuilder().where()

        mCurrentChapter = chapter;
        if (chapterList != null) {
            mChapterSize = chapterList.size();
        }
        mCurrentPageStartPosition = currentPosition;
        //获取文件编码
        charSet = FileUtils.getJavaEncode(bookPath);
        try {
            File file = new File(bookPath);
            long length = file.length();
            if (length > 10) {
                mByteBufferLength = length;

                mMappedByteBuffer = new RandomAccessFile(file, "r")
                        .getChannel()
                        .map(FileChannel.MapMode.READ_ONLY, 0, length);

                mCurrentPageStartPosition = currentPosition;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开书籍
     *
     * @param bookPath        书籍的位置
     * @param chapter         章节
     * @param currentPosition 开始位置
     */
    public void openBook(String bookPath, int chapter, int currentPosition) {
        File file = new File(bookPath);
        if (file.exists() == false){
            //文件不存在
            return;
        }

        Book book = new Book();
        book.setBookName(file.getName());
        book.setBookPath(bookPath);
        book.setDescription("");
        book.setBookImagePath("");

        long id = mBookDao.insert(book);

        openBook(id, chapter, currentPosition);
    }

    /**
     * 读书籍目录，断章
     * 这个需要放到一个线程里面运行
     * @return
     * @throws UnsupportedEncodingException
     */
    public List<Chapter> processChapters() throws UnsupportedEncodingException {
        if (chapterList == null) {
            chapterList = new ArrayList<>();
        }
        int currentPosition = 0;
        int lastPosition = 0;

        Pattern pattern = Pattern.compile("第.{1,7}章.*\r\n");
        ChapterDao chapterDao = daoSession.getChapterDao();

        while (currentPosition < mByteBufferLength) {
            byte[] bytes = readParagraphForward(currentPosition);
            String paragraph = new String(bytes, charSet);

            Matcher matcher = pattern.matcher(paragraph);
            if (matcher.find()) {
                //修正章节错
                if (currentPosition - lastPosition > 200 && bytes.length < 50) {

                    Chapter chapter = new Chapter();
                    chapter.setTitle(matcher.group());
                    chapter.setPosition(currentPosition);
                    chapter.setIsRead(false);
                    chapter.setBookId(bookId);
                    //插入数据库
                    chapterDao.insert(chapter);

                    chapterList.add(chapter);
                }
            }
        }

        return chapterList;
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
    public void readPage(){

    }
}
