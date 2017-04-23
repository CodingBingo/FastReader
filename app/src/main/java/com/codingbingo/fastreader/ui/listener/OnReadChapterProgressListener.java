package com.codingbingo.fastreader.ui.listener;

import android.widget.SeekBar;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/4/23.
 */

public interface OnReadChapterProgressListener {
    void onReadProgressChanged(SeekBar seekBar, int progress, boolean fromUser);
}
