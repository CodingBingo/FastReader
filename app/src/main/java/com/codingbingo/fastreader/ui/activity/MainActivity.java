package com.codingbingo.fastreader.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codingbingo.fastreader.Constants;
import com.codingbingo.fastreader.FRApplication;
import com.codingbingo.fastreader.R;
import com.codingbingo.fastreader.base.BaseActivity;
import com.codingbingo.fastreader.dao.Book;
import com.codingbingo.fastreader.dao.BookDao;
import com.codingbingo.fastreader.ui.adapter.BookListAdapter;
import com.codingbingo.fastreader.utils.ScreenUtils;

import java.util.List;

public class MainActivity extends BaseActivity implements
        NestedScrollView.OnScrollChangeListener, View.OnClickListener,
        BookListAdapter.OnBookListItemClickListener {

    private int normalTopPanelHeight = 0;
    private int totalChangeAlphaArea = 0;
    //titleBar的颜色0 表示默认白色，1表示灰色，2表示黑色
    private int titleBarColor = 0;

    private CoordinatorLayout activityMainLayout;
    private ImageView showMenuBtn;
    private ImageView goSearchBtn;
    private TextView titleText;
    private RecyclerView bookListView;
    private RelativeLayout bookListTopPanel;

    private NestedScrollView bookIndexScrollview;
    private RelativeLayout titleBarLayout;

    private View menuView;
    private PopupWindow menuPopupWindow;
    private LinearLayout addLocalFile;
    private LinearLayout about;
    private LinearLayout feedback;

    private List<Book> bookList;

    private BookDao mBookDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        initView();
    }

    private void init() {
        mBookDao = ((FRApplication) getApplication()).getDaoSession().getBookDao();

        bookList = mBookDao.loadAll();
    }

    private void initView() {
        activityMainLayout = (CoordinatorLayout) findViewById(R.id.activity_main);
        showMenuBtn = (ImageView) findViewById(R.id.show_menu_btn);
        goSearchBtn = (ImageView) findViewById(R.id.go_search_btn);
        titleText = (TextView) findViewById(R.id.title_text_view);
        bookIndexScrollview = (NestedScrollView) findViewById(R.id.book_index_scrollview);
        bookListView = (RecyclerView) findViewById(R.id.book_list);
        titleBarLayout = (RelativeLayout) findViewById(R.id.title_bar_layout);
        bookListTopPanel = (RelativeLayout) findViewById(R.id.book_list_top_panel);

        /* menu */
        menuView = View.inflate(this, R.layout.main_menu, null);
        addLocalFile = (LinearLayout) menuView.findViewById(R.id.add_local_file);
        about = (LinearLayout) menuView.findViewById(R.id.about);
        feedback = (LinearLayout) menuView.findViewById(R.id.feedback);
        menuPopupWindow = new PopupWindow(menuView, CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        menuPopupWindow.setFocusable(true);
        menuPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(200, 81, 86, 88)));
        menuPopupWindow.setAnimationStyle(R.style.PopupAnimation);

        BookListAdapter bookListAdapter = new BookListAdapter(this, bookList);
        //图书列表点击事件
        bookListAdapter.setOnBookListItemClickListener(this);
        bookListView.setLayoutManager(new GridLayoutManager(this, 3));
        bookListView.setNestedScrollingEnabled(false);
        bookListView.setHasFixedSize(false);
        bookListView.setAdapter(bookListAdapter);

        float bookListViewHeight = getResources().getDisplayMetrics().heightPixels
                - ScreenUtils.getTitleBarHeight(this)
                - ScreenUtils.getStatusBarHeight(this);
        bookListView.setMinimumHeight((int) bookListViewHeight);
        //保证展示图书列表顶部区域
        bookIndexScrollview.smoothScrollTo(0, 0);

        //获取正常情况下，顶部区域的高度
        normalTopPanelHeight = ScreenUtils.dp2px(this, 200);
        totalChangeAlphaArea = normalTopPanelHeight - ScreenUtils.getTitleBarHeight(this);

        initViewListener();
    }

    private void initViewListener() {
        bookIndexScrollview.setOnScrollChangeListener(this);
        showMenuBtn.setOnClickListener(this);
        goSearchBtn.setOnClickListener(this);

        addLocalFile.setOnClickListener(this);
        feedback.setOnClickListener(this);
        about.setOnClickListener(this);
    }

    /**
     * 根据滑动动态调整颜色
     */
    private void changeTitleBarLayout(int scrollY) {
        float currentDis = totalChangeAlphaArea - scrollY;
        int value = (int) ((255 - currentDis * 255 * 1.0 / totalChangeAlphaArea));
        titleBarLayout.getBackground().setAlpha(value > 255 ? 255 : value);
        if (value < 160 && titleBarColor != 0) {
            showMenuBtn.setImageResource(R.drawable.menu_icon_white);
            goSearchBtn.setImageResource(R.drawable.search_icon_white);
            titleText.setTextColor(getResources().getColor(R.color.white));

            titleBarColor = 0;
        } else if (160 < value && value < 200 && titleBarColor != 1) {
            showMenuBtn.setImageResource(R.drawable.menu_icon_grey);
            goSearchBtn.setImageResource(R.drawable.search_icon_grey);
            titleText.setTextColor(getResources().getColor(R.color.grey));

            titleBarColor = 1;
        } else if (200 < value && titleBarColor != 2) {
            showMenuBtn.setImageResource(R.drawable.menu_icon_black);
            goSearchBtn.setImageResource(R.drawable.search_icon_black);
            titleText.setTextColor(getResources().getColor(R.color.black));

            titleBarColor = 2;
        }
    }

    @Override
    public void onBookItemClick(Book book, int position) {
        if (book == null) {
            //添加书籍
            Intent intent = new Intent(MainActivity.this, LocalFileListActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, ReadingActivity.class);
            intent.putExtra("type", Constants.TYPE_FROM_MAIN_ACTIVITY);
            intent.putExtra("bookId", book.getId());
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.show_menu_btn:
                menuPopupWindow.showAtLocation(activityMainLayout, Gravity.TOP, 0, 0);
                break;
            case R.id.go_search_btn:
                break;
            case R.id.add_local_file:
                Intent intent = new Intent(this, LocalFileListActivity.class);
                startActivity(intent);
                menuPopupWindow.dismiss();
                break;
            case R.id.about:
                break;
            case R.id.feedback:
                break;
        }
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        changeTitleBarLayout(scrollY);
    }
}
