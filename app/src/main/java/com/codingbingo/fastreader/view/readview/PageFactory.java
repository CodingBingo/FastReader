package com.codingbingo.fastreader.view.readview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.codingbingo.fastreader.Constants;
import com.codingbingo.fastreader.FRApplication;
import com.codingbingo.fastreader.dao.Book;
import com.codingbingo.fastreader.dao.BookDao;
import com.codingbingo.fastreader.dao.Chapter;
import com.codingbingo.fastreader.dao.ChapterDao;
import com.codingbingo.fastreader.dao.DaoSession;
import com.codingbingo.fastreader.manager.SettingManager;
import com.codingbingo.fastreader.utils.FileUtils;
import com.codingbingo.fastreader.utils.ScreenUtils;
import com.codingbingo.fastreader.utils.StringUtils;
import com.codingbingo.fastreader.utils.ThreadPool;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/3/11.
 */

public class PageFactory {

    public static final String TAG = "PageFactory";

    private Context mContext;

    //数据库DAO
    private DaoSession mDaoSession;
    private BookDao mBookDao;
    private ChapterDao mChapterDao;

    private Book mBook;
    private List<Chapter> mChapterList;
    private long mByteBufferLength;

    private Paint mTitlePaint;
    private Paint mPaint;
    private Paint mBottomPaint;

    //当前的章节以及位置
    private int totalWidth;
    private int totleHeight;
    private int currentChapter = 1;
    private int currentStartPosition = 0;
    private int currentEndPosition = 0;
    private int mLineCount;
    private int mVisibleHeight;
    private int mVisibleWidth;
    private int mLineSpace;
    private int mFontSize;
    private int mTitleFontSize;
    private int mBottomFontSize;
    private int marginWidth;
    private int marginHeight;

    private MappedByteBuffer mMappedByteBuffer;
    private List<String> mLines = new ArrayList<>();

    private String charSet = "UTF-8";

    public PageFactory(Context mContext) {
        this.mContext = mContext;

        mDaoSession = ((FRApplication)mContext.getApplicationContext()).getDaoSession();
        mBookDao = mDaoSession.getBookDao();
        mChapterDao = mDaoSession.getChapterDao();


        init();
    }

    private void init(){
        mTitleFontSize = ScreenUtils.dp2px(mContext, 17);
        mBottomFontSize = ScreenUtils.dp2px(mContext, 15);

        marginWidth = ScreenUtils.dp2px(mContext, 15);
        marginHeight = ScreenUtils.dp2px(mContext, 15);


        totalWidth = ScreenUtils.getScreenWidth(mContext);
        totleHeight = ScreenUtils.getScreenHeight(mContext);

        mVisibleWidth = totalWidth - 2 * marginWidth;
        mVisibleHeight = totleHeight - marginHeight - mTitleFontSize - mBottomFontSize;

        mFontSize = SettingManager.getInstance().getReadFontSize();
        mLineSpace = mFontSize / 5 * 2;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mFontSize);
        mPaint.setColor(Color.BLACK);

        mTitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTitlePaint.setTextSize(mTitleFontSize);
        mTitlePaint.setColor(Color.GRAY);

        mBottomPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBottomPaint.setTextSize(mBottomFontSize);
        mBottomPaint.setColor(Color.GRAY);
    }

    /**
     * 打开已经加入数据库的书籍
     * @param bookId
     */
    public void openBook(long bookId){
        //先检查bookId是否存在
        mBook = mBookDao.load(bookId);
        if (mBook == null){
            //书籍不存在
            //// TODO: 2017/3/11  书籍不存在，后续需要加载错误页
            return;
        }

        //处理状态
        int processStatus = mBook.getProcessStatus();

        switch (processStatus){
            case Constants.BOOK_UNPROCESS:
                //书籍还没有处理，应该交给上面的函数处理
                openBook(mBook.getBookPath());
                break;
            case Constants.BOOK_PROCESSING:
                //书籍正在处理中，但是还没有处理完成，应该继续下一步处理
                break;
            case Constants.BOOK_PROCESSED:
                //处理完成，可以开始阅读啦
                if (mBook.getCurrentChapter() != null) {
                    currentChapter = mBook.getCurrentChapter();
                }else{
                    currentChapter = 1;
                }
                if (mBook.getCurrentPosition() != null) {
                    currentStartPosition = mBook.getCurrentPosition();
                }else{
                    currentStartPosition = 0;
                }

                File bookFile = new File(mBook.getBookPath());
                mByteBufferLength = bookFile.length();
                charSet = mBook.getCharSet();

                if (bookFile.exists() == false){
                    //文件可能已经删除
                    Log.e(TAG, "File doesn't exits");
                    return;
                }

                //获取所有章节
                mChapterList = mChapterDao.queryBuilder().where(ChapterDao.Properties.BookId.eq(mBook.getId())).orderAsc(ChapterDao.Properties.Id).list();
                //开始读取当前章节
                Chapter chapter = mChapterList.get(currentChapter);
                try {
                    mMappedByteBuffer = new RandomAccessFile(bookFile, "r").getChannel().map(FileChannel.MapMode.READ_ONLY, 0, mByteBufferLength);
                    //此处应该根据整个页面的情况计算当前页面能显示多少字符


                }catch(IOException e){
                    //基本没可能了，上面已经确保了文件存在
                    Log.e(TAG, e.getMessage());
                }

                break;
        }
    }

    public synchronized void onDraw(Canvas canvas){
        if (mLines.size() == 0){
            currentStartPosition = currentEndPosition;
            mLines = pageDown();
        }

        if (mLines.size() > 0){
            int y = marginHeight + mTitleFontSize / 2;
            //绘制背景，后面添加换背景功能
            canvas.drawColor(Color.WHITE);
            //绘制章节名称
            canvas.drawText(mChapterList.get(currentChapter - 1).getTitle(), marginWidth, y, mTitlePaint);
            y += mTitleFontSize;

            int bottomPositionY = totleHeight - mBottomFontSize / 2 - mLineSpace;
            //绘制内容
            for (String line : mLines) {
                y += mLineSpace;
                if (y >= bottomPositionY){
                    try {
                        currentEndPosition -= line.getBytes(charSet).length;
                        //// TODO: 2017/3/12  
                        //通知下一页状态需要改标
                    }catch (UnsupportedEncodingException e){
                        Log.e(TAG, e.getMessage());
                    }
                }else{
                    if (line.endsWith("@")){
                        line = line.substring(0, line.length() - 1);
                        canvas.drawText(line, marginWidth, y, mPaint);
                        y += mLineSpace;
                    }else{
                        canvas.drawText(line, marginWidth, y, mPaint);
                    }
                    y += mFontSize;
                }
            }

            //绘制底部
            float progress = getReadProgress();
            String progressPercent = String.format("%.2f", progress * 100) + "%";
            int bottomPositionX = (int) (totalWidth / 2 - mBottomPaint.measureText(progressPercent) / 2);

            canvas.drawText(progressPercent, bottomPositionX, bottomPositionY, mBottomPaint);
        }
    }

    private float getReadProgress(){
        float progress = 0;
        if (mByteBufferLength != 0){
            progress = currentStartPosition * 1.0f / mByteBufferLength;
            return progress;
        }

        return progress;
    }

    private List<String> pageDown() {
        String paragraphStr = "";
        List<String> lines = new ArrayList<>();

        mLineCount = mVisibleHeight / (mFontSize + mLineSpace);
        while((lines.size() < mLineCount) && currentEndPosition < mByteBufferLength){
            byte[] paragraphBuffer = readParagraphForward(currentEndPosition);
            currentEndPosition += paragraphBuffer.length;
            try{
                paragraphStr = new String(paragraphBuffer, charSet);
            }catch (Exception e){
                Log.e(TAG, e.getLocalizedMessage());
            }
            paragraphStr = paragraphStr.replaceAll("\r\n", "  ").replaceAll("\n", " "); // 段落中的换行符去掉，绘制的时候再换行
            while(paragraphStr.length() > 0){
                int paintSize = mPaint.breakText(paragraphStr, true, mVisibleWidth, null);
                lines.add(paragraphStr.substring(0, paintSize));
                paragraphStr = paragraphStr.substring(paintSize);
                if (lines.size() >= mLineCount){
                    break;
                }
            }

            lines.set(lines.size() - 1, lines.get(lines.size() - 1) + "@");
            if (paragraphStr.length() != 0){
                try{
                    currentEndPosition -= paragraphStr.getBytes(charSet).length;
                }catch (UnsupportedEncodingException e){
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }
        }

        return lines;
    }

    /**
     * 打开没有处理过的文件
     * @param filePath
     */
    public void openBook(String filePath){
        if (StringUtils.isBlank(filePath)){
            //书籍路径为空
            Log.e(TAG, "FilePath is empty!");
            return;
        }

        //判断文件是否存在
        File bookFile = new File(filePath);
        if (bookFile.exists() == false){
            Log.e(TAG, "File is not exits");
            return;
        }

        //文件存在，开始读取文件
        try {
            mByteBufferLength = bookFile.length();
            mMappedByteBuffer = new RandomAccessFile(bookFile, "r").getChannel().map(FileChannel.MapMode.READ_ONLY, 0, mByteBufferLength);

            if (mBook == null) {
                mBook = new Book();
            }
            mBook.setBookName(bookFile.getName());
            mBook.setBookPath(filePath);
            mBook.setCharSet("");//先插入数据库中，chatset只在后续需要处理
            mBook.setDescription("");
            mBook.setBookImagePath("");
            mBook.setProcessStatus(Constants.BOOK_UNPROCESS);
            mBook.setCurrentChapter(1); //章节从1开始
            mBook.setCurrentPosition(0);
            long bookId = mBookDao.insert(mBook);
            mBook.setId(bookId);

            //开始处理章节
            ThreadPool.getInstance().submitTask(new OpenBookTask());
        } catch (Exception e) {
            //文件已经判断是否存在了，理论上只有文件保存到数据库失败会出现问题
            Log.e(TAG, e.getMessage());
        }
    }

    private class OpenBookTask implements Runnable {
        @Override
        public void run() {
            if (mBook == null || StringUtils.isBlank(mBook.getBookPath())) {
                Log.e(TAG, "Error filePath");
                return;
            }
            String charSet = FileUtils.getJavaEncode(mBook.getBookPath());//这个步骤需要消耗不少的时间

            mBook.setProcessStatus(Constants.BOOK_PROCESSING);
            mBook.setCharSet(charSet);
            mBookDao.update(mBook);

            processChapters();//刷新页面

            mBook.setProcessStatus(Constants.BOOK_PROCESSED);
            mBookDao.update(mBook);
        }
    }



    /**
     * 读书籍目录，断章
     * 这个需要放到一个线程里面运行
     */
    public void processChapters() {
        int currentPosition = 0;
        int lastPosition = 0;

        Pattern pattern = Pattern.compile("第.{1,7}章.*\r\n");

        while (currentPosition < mByteBufferLength) {
            byte[] bytes = readParagraphForward(currentPosition);
            try {
                String paragraph = new String(bytes, mBook.getCharSet());
                Matcher matcher = pattern.matcher(paragraph);
                if (matcher.find()) {
                    //修正章节错
                    if (currentPosition - lastPosition > 200 && bytes.length < 50) {
                        if (mBook != null) {
                            Chapter chapter = new Chapter();
                            chapter.setTitle(matcher.group());
                            chapter.setPosition(currentPosition);
                            chapter.setIsRead(false);
                            chapter.setBook(mBook);
                            //插入数据库
                            mChapterDao.insert(chapter);

                            lastPosition = currentPosition;
                        }
                    }
                }
            } catch (UnsupportedEncodingException e) {
                //不支持编码
                Log.e(TAG, "Book encoding is not supported");
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
}
