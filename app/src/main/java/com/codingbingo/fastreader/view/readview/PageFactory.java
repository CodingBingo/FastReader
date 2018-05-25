package com.codingbingo.fastreader.view.readview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.avos.avoscloud.LogUtil;
import com.codingbingo.fastreader.Constants;
import com.codingbingo.fastreader.FRApplication;
import com.codingbingo.fastreader.dao.Book;
import com.codingbingo.fastreader.dao.BookDao;
import com.codingbingo.fastreader.dao.Chapter;
import com.codingbingo.fastreader.dao.ChapterDao;
import com.codingbingo.fastreader.dao.DaoSession;
import com.codingbingo.fastreader.manager.SettingManager;
import com.codingbingo.fastreader.model.eventbus.BookStatusChangeEvent;
import com.codingbingo.fastreader.model.eventbus.RefreshBookListEvent;
import com.codingbingo.fastreader.utils.FileUtils;
import com.codingbingo.fastreader.utils.ScreenUtils;
import com.codingbingo.fastreader.utils.StringUtils;
import com.codingbingo.fastreader.utils.ThreadPool;

import org.greenrobot.eventbus.EventBus;

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
    private SettingManager settingManager;

    //数据库DAO
    private DaoSession mDaoSession;
    private BookDao mBookDao;
    private ChapterDao mChapterDao;

    private Book mBook;
    private List<Chapter> mChapterList;
    private List<String> mLines = new ArrayList<>();

    private MappedByteBuffer mMappedByteBuffer;
    private long mByteBufferLength;

    private Paint mTitlePaint;
    private Paint mPaint;
    private Paint mBottomPaint;

    //上一页或者下一页
    private int tempStartPos;

    //当前的章节以及位置
    private int totalWidth;
    private int totalHeight;
    private int currentChapter = 0;
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

    private String charSet = "UTF-8";

    private String backgroundColor = "#FFFFFF";

    public PageFactory(Context mContext) {
        this.mContext = mContext;
        settingManager = SettingManager.getInstance();

        mDaoSession = ((FRApplication) mContext.getApplicationContext()).getDaoSession();
        mBookDao = mDaoSession.getBookDao();
        mChapterDao = mDaoSession.getChapterDao();

        init();
    }


    public void init() {
        mTitleFontSize = ScreenUtils.dp2px(mContext, 17);
        mBottomFontSize = ScreenUtils.dp2px(mContext, 15);

        marginWidth = ScreenUtils.dp2px(mContext, 15);
        marginHeight = ScreenUtils.dp2px(mContext, 15);


        totalWidth = ScreenUtils.getScreenWidth(mContext);
        totalHeight = ScreenUtils.getScreenHeight(mContext);

        mVisibleWidth = totalWidth - 2 * marginWidth;
        mVisibleHeight = totalHeight - marginHeight - mTitleFontSize - mBottomFontSize;

        mFontSize = SettingManager.getInstance().getReadFontSize();
        mLineSpace = mFontSize / 5 * 2;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(mFontSize);
        if (settingManager.getReadMode()) {
            mPaint.setColor(Color.WHITE);
        } else{
            mPaint.setColor(Color.BLACK);
        }

        mTitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTitlePaint.setTextSize(mTitleFontSize);
        mTitlePaint.setColor(Color.GRAY);

        mBottomPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBottomPaint.setTextSize(mBottomFontSize);
        mBottomPaint.setColor(Color.GRAY);
    }

    public void clearParams(){
        mLines.clear();
        //更新一下book对象
        mBook = mBookDao.load(mBook.getId());
        currentChapter = mBook.getCurrentChapter();
        currentStartPosition = mBook.getCurrentPosition();
        currentEndPosition = 0;
    }

    /**
     * 打开已经加入数据库的书籍
     *
     * @param bookId
     */
    public void openBook(long bookId) {
        //先检查bookId是否存在
        mBook = mBookDao.load(bookId);
        if (mBook == null) {
            //书籍不存在
            //// TODO: 2017/3/11  书籍不存在，后续需要加载错误页
            return;
        }

        //处理状态
        int processStatus = mBook.getProcessStatus();

        switch (processStatus) {
            case Constants.BOOK_UNPROCESS:
                //书籍还没有处理，应该交给上面的函数处理
                openBook(mBook.getBookPath());
                break;
            case Constants.BOOK_PROCESSING:
                //书籍正在处理中，但是还没有处理完成，应该继续下一步处理
                List<Chapter> chapterList = mChapterDao.queryBuilder().where(ChapterDao.Properties.BookId.eq(mBook.getId())).list();

                try {

                    File file = new File(mBook.getBookPath());
                    mByteBufferLength = file.length();
                    mMappedByteBuffer = new RandomAccessFile(file, "r").getChannel().map(FileChannel.MapMode.READ_ONLY, 0, mByteBufferLength);
                    //此处应该根据整个页面的情况计算当前页面能显示多少字符

                    if (chapterList.size() > 0){
                        Chapter chapter = chapterList.get(chapterList.size() - 1);
                        //接着上一章节处理
                        ThreadPool.getInstance().submitTask(new OpenBookTask(chapter.getPosition() + chapter.getTitle().getBytes().length));
                    } else{
                        //一点都没处理
                        //开始处理章节
                        ThreadPool.getInstance().submitTask(new OpenBookTask(0));
                    }

                } catch (IOException e) {
                    //基本没可能了，上面已经确保了文件存在
                    Log.e(TAG, e.getMessage());
                }
                break;
            case Constants.BOOK_PROCESSED:
                //处理完成，可以开始阅读啦
                if (mBook.getCurrentChapter() != null) {
                    currentChapter = mBook.getCurrentChapter();
                } else {
                    currentChapter = 0;
                }
                if (mBook.getCurrentPosition() != null) {
                    currentStartPosition = mBook.getCurrentPosition();
                } else {
                    currentStartPosition = 0;
                }

                File bookFile = new File(mBook.getBookPath());
                mByteBufferLength = bookFile.length();
                charSet = mBook.getCharSet();

                if (bookFile.exists() == false) {
                    //文件可能已经删除
                    Log.e(TAG, "File doesn't exits");
                    return;
                }

                //获取所有章节
                mChapterList = mChapterDao.queryBuilder().where(ChapterDao.Properties.BookId.eq(mBook.getId())).orderAsc(ChapterDao.Properties.Id).list();
                try {
                    mMappedByteBuffer = new RandomAccessFile(bookFile, "r").getChannel().map(FileChannel.MapMode.READ_ONLY, 0, mByteBufferLength);
                    //此处应该根据整个页面的情况计算当前页面能显示多少字符
                } catch (IOException e) {
                    //基本没可能了，上面已经确保了文件存在
                    Log.e(TAG, e.getMessage());
                }

                break;
        }
    }

    public synchronized void onDraw(Canvas canvas) {
        if (mLines.size() == 0) {
            if (currentEndPosition > currentStartPosition) {
                currentStartPosition = currentEndPosition;
            } else {
                currentEndPosition = currentStartPosition;
            }
            mLines = pageDown();
        }

        if (mLines.size() > 0) {
            int y = marginHeight + mTitleFontSize / 2;
            //绘制背景，后面添加换背景功能
            if (settingManager.getReadMode()) {
                canvas.drawColor(Color.BLACK);
            }else{
                canvas.drawColor(Color.parseColor(SettingManager.getInstance().getReadBackground()));
            }
            //绘制章节名称
            canvas.drawText(mChapterList.get(currentChapter).getTitle(), marginWidth, y, mTitlePaint);
            y += mTitleFontSize;

            int bottomPositionY = totalHeight - mBottomFontSize / 2 - mLineSpace;
            //绘制内容
            for (String line : mLines) {
                y += mLineSpace;
                if (line.endsWith("@")) {
                    line = line.substring(0, line.length() - 1);
                    canvas.drawText(line, marginWidth, y, mPaint);
                    y += mLineSpace;
                } else {
                    canvas.drawText(line, marginWidth, y, mPaint);
                }
                y += mFontSize;
            }

            //绘制底部
            float progress = getReadProgress();
            String progressPercent = String.format("%.2f", progress * 100) + "%";
            int bottomPositionX = (int) (totalWidth / 2 - mBottomPaint.measureText(progressPercent) / 2);

            canvas.drawText(progressPercent, bottomPositionX, bottomPositionY, mBottomPaint);
        }
    }

    private float getReadProgress() {
        float progress = 0;
        if (mByteBufferLength != 0) {
            progress = currentStartPosition * 1.0f / mByteBufferLength;
            return progress;
        }

        return progress;
    }

    private List<String> pageUp() {
        String paragraphStr = "";
        List<String> lines = new ArrayList<>();

        mLineCount = mVisibleHeight / (mFontSize + mLineSpace);
        int paraSpace = 0;//段落中间会添加的空白
        while ((lines.size() < mLineCount) && currentStartPosition > 0) {
            List<String> paragraphLines = new ArrayList<>();
            byte[] paragraphBuffer = readParagraphBack(currentStartPosition);
            currentStartPosition -= paragraphBuffer.length;

            try {
                paragraphStr = new String(paragraphBuffer, charSet);
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, e.getMessage());
            }

            paragraphStr = paragraphStr.replaceAll("\r", " ").replaceAll("\n", " "); // 段落中的换行符去掉，绘制的时候再换行
            if (paragraphStr.replaceAll(" ", "").length() == 0){
                //说明当前段落无内容
                continue;
            }
            while (paragraphStr.length() > 0) {
                int paintSize = mPaint.breakText(paragraphStr, true, mVisibleWidth, null);
                paragraphLines.add(paragraphStr.substring(0, paintSize));
                paragraphStr = paragraphStr.substring(paintSize);
            }
            if (paragraphLines.size() > 0) {
                paragraphLines.set(paragraphLines.size() - 1, paragraphLines.get(paragraphLines.size() - 1) + "@");
                lines.addAll(0, paragraphLines);
            }

            while (lines.size() > mLineCount) { // 4.如果段落添加完，但是超出一页，则超出部分需删减
                try {
                    currentStartPosition += lines.get(0).getBytes(charSet).length; // 5.删减行数同时起始位置指针也要跟着偏移
                    lines.remove(0);
                } catch (UnsupportedEncodingException e) {
                    LogUtil.log.e("上翻页失败，文本解码失败", e);
                }
            }

            paraSpace += mLineSpace;
            mLineCount = (mVisibleHeight - paraSpace) / (mFontSize + mLineSpace); // 添加段落间距，实时更新容纳行数
        }

        return lines;
    }

    private List<String> pageDown() {
        String paragraphStr = "";
        List<String> lines = new ArrayList<>();
        int paraSpace = 0;
        //下一章节
        Chapter nextChapter = null;
        if (currentChapter < mChapterList.size() - 1) {
            nextChapter = mChapterList.get(currentChapter + 1);
        }

        mLineCount = mVisibleHeight / (mFontSize + mLineSpace);
        currentStartPosition = currentEndPosition;
        while ((lines.size() < mLineCount) && currentEndPosition < mByteBufferLength) {
            byte[] paragraphBuffer = readParagraphForward(currentEndPosition);
            currentEndPosition += paragraphBuffer.length;

            if (nextChapter != null && currentEndPosition >= nextChapter.getPosition()) {
                //已经到下一个章节了
                currentEndPosition = nextChapter.getPosition();
                return lines;
            }
            try {
                paragraphStr = new String(paragraphBuffer, charSet);
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage());
            }
            paragraphStr = paragraphStr.replaceAll("\r", " ").replaceAll("\n", " "); // 段落中的换行符去掉，绘制的时候再换行
            if (paragraphStr.replaceAll(" ", "").length() == 0){
                //说明当前段落无内容
                continue;
            }
            while (paragraphStr.length() > 0) {
                int paintSize = mPaint.breakText(paragraphStr, true, mVisibleWidth, null);
                lines.add(paragraphStr.substring(0, paintSize));
                paragraphStr = paragraphStr.substring(paintSize);
                if (lines.size() >= mLineCount) {
                    break;
                }
            }

            lines.set(lines.size() - 1, lines.get(lines.size() - 1) + "@");

            if (paragraphStr.length() != 0) {
                try {
                    currentEndPosition -= paragraphStr.getBytes(charSet).length;
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }

            paraSpace += mLineSpace;
            mLineCount = (mVisibleHeight - paraSpace) / (mFontSize + mLineSpace);
        }

        return lines;
    }


    /**
     * 打开没有处理过的文件
     *
     * @param filePath
     */
    public void openBook(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            //书籍路径为空
            Log.e(TAG, "FilePath is empty!");
            return;
        }

        //判断文件是否存在
        File bookFile = new File(filePath);
        if (bookFile.exists() == false) {
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
            mBook.setCurrentChapter(0); //章节从0开始
            mBook.setCurrentPosition(0);
            long bookId = mBookDao.insert(mBook);
            mBook.setId(bookId);

            //开始处理章节
            ThreadPool.getInstance().submitTask(new OpenBookTask(0));
        } catch (Exception e) {
            //文件已经判断是否存在了，理论上只有文件保存到数据库失败会出现问题
            Log.e(TAG, e.getMessage());
        }
    }

    public boolean hasNextPage() {
        return currentChapter + 1 < mChapterList.size() || currentEndPosition < mByteBufferLength;
    }

    public boolean hasPrePage() {
        return currentChapter > 0 || (currentChapter == 0 && currentStartPosition > 0);
    }

    /**
     * 根据新的样式刷新页面
     *
     * @param mCurrentPageCanvas
     */
    public void refreshAccordingToStyle(Canvas mCurrentPageCanvas) {
        tempStartPos = currentStartPosition;
        currentEndPosition = currentStartPosition;
        //刷新init中初始化得样式
        init();

        Chapter chapter = mChapterList.get(currentChapter);
        if (currentStartPosition == 0 || chapter.getPosition() == currentEndPosition) { //说明是章节的开始
            //直接读一遍就好
            mLines.clear();
            mLines.addAll(pageDown());
        } else {
            //这个时候需要从章节开头开始读，然后读到包含当前位置为止

            currentStartPosition = chapter.getPosition();
            currentEndPosition = currentStartPosition;

            while (!(currentStartPosition <= tempStartPos && currentEndPosition >= tempStartPos)) {
                mLines.clear();
                mLines.addAll(pageDown());
            }
        }
        onDraw(mCurrentPageCanvas);
    }

    public class OpenBookTask implements Runnable {
        private int processStartPosition;
        private String filePath;
        private boolean isFinished;

        public OpenBookTask(int processStartPosition) {
            this.processStartPosition = processStartPosition;
            filePath = mBook.getBookPath();
            isFinished = false;
        }

        public boolean isFinished() {
            return isFinished;
        }

        public String getFilePath() {
            return filePath;
        }

        @Override
        public void run() {
            if (mBook == null || StringUtils.isBlank(mBook.getBookPath())) {
                Log.e(TAG, "Error filePath");
                isFinished = true;
                return;
            }

            String charSet = FileUtils.getJavaEncode(mBook.getBookPath());//这个步骤需要消耗不少的时间

            mBook.setProcessStatus(Constants.BOOK_PROCESSING);
            mBook.setCharSet(charSet);
            mBookDao.update(mBook);

            processChapters(processStartPosition);//刷新页面

            mChapterList = mChapterDao.queryBuilder().where(ChapterDao.Properties.BookId.eq(mBook.getId())).list();
            if (mChapterList == null || mChapterList.size() == 0){
                Chapter chapter = new Chapter();
                chapter.setTitle(mBook.getBookName());
                chapter.setBook(mBook);
                chapter.setPosition(0);
                mChapterDao.insert(chapter);
            }

            mBook.setProcessStatus(Constants.BOOK_PROCESSED);
            mBookDao.update(mBook);

            //本次书籍处理完毕
            EventBus.getDefault().post(new RefreshBookListEvent());
            EventBus.getDefault().post(new BookStatusChangeEvent(Constants.BOOK_PROCESSED, 100, mBook.getId()));
            isFinished = true;
        }
    }


    /**
     * 读书籍目录，断章
     * 这个需要放到一个线程里面运行
     */
    public void processChapters(int startPosition) {
        int currentPosition = startPosition;
        int lastPosition = 0;
        //当前时间
        Long lastUpdateTime = null;

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
                            if (lastPosition == 0) {
                                chapter.setPosition(0);
                            } else {
                                chapter.setPosition(currentPosition);
                            }
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

            long currentTime = System.currentTimeMillis();
            //防止因为太快，导致卡顿
            if (lastUpdateTime == null || currentTime - lastUpdateTime >= 1000){
                lastUpdateTime = currentTime;
                BookStatusChangeEvent bookStatusChangeEvent = new BookStatusChangeEvent();
                bookStatusChangeEvent.setProgress((int)(currentPosition * 100.0 / mByteBufferLength));
                bookStatusChangeEvent.setStatus(Constants.BOOK_PROCESSING);
                bookStatusChangeEvent.setBookId(mBook.getId());
                EventBus.getDefault().post(bookStatusChangeEvent);
            }
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
     * 跳转下一页
     */
    public BookStatus nextPage() {
        if (!hasNextPage()) { // 最后一章的结束页
            return BookStatus.NO_NEXT_PAGE;
        } else {
            tempStartPos = currentStartPosition;
            //向下移
            currentStartPosition = currentEndPosition;
            if (currentChapter + 1 < mChapterList.size()) {
                Chapter nextChapter = mChapterList.get(currentChapter + 1);
                if (currentEndPosition == nextChapter.getPosition()) {
                    currentChapter++;
                }
            }

            mLines.clear();
            mLines.addAll(pageDown());

            //更新书籍
            updateBookInfo();
        }
        return BookStatus.LOAD_SUCCESS;
    }

    /**
     * 跳转上一页
     */
    public BookStatus prePage() {

        if (!hasPrePage()) { // 第一章第一页
            return BookStatus.NO_PRE_PAGE;
        } else {
            //向前移动
            currentEndPosition = currentStartPosition;

            Chapter chapter = mChapterList.get(currentChapter);
            if (chapter.getPosition() >= currentStartPosition) {
                //说明这是一章的开头，上一页就要到上一个章节了，这里要直接对上一章节的部分分页
                currentChapter--;

                Chapter preChapter = mChapterList.get(currentChapter);
                //上一章节开始的地方
                currentStartPosition = preChapter.getPosition();
                currentEndPosition = currentStartPosition;

                while (currentEndPosition != chapter.getPosition()) {

                    currentStartPosition = currentEndPosition;//调整开始位置

                    mLines.clear();
                    mLines.addAll(pageDown());
                }
            } else {
                //不是章节开头，直接往前读取
                mLines.clear();
                // 起始指针移到上一页开始处
                mLines.addAll(pageUp()); // 读取一页内容
            }

            //更新书籍
            updateBookInfo();
        }
        return BookStatus.LOAD_SUCCESS;
    }

    /**
     * 当前阅读章节，当前位置
     */
    private void updateBookInfo(){
        mBook.setCurrentChapter(currentChapter);
        mBook.setCurrentPosition(currentStartPosition);
        mBookDao.update(mBook);
    }
}
