package com.codingbingo.fastreader.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.TextView;

import com.codingbingo.fastreader.R;
import com.codingbingo.fastreader.view.loadingview.CatLoadingView;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/4/19.
 */

public class LoadingDialog extends Dialog {

    private TextView loadingProgress;
    private String loadingText;

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    public LoadingDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public LoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    public void setLoadingText(String loadingText) {
        this.loadingText = loadingText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new CatLoadingView(getContext()));

        loadingProgress = (TextView) findViewById(R.id.loading_progress);
        loadingProgress.setVisibility(View.GONE);
    }
}
