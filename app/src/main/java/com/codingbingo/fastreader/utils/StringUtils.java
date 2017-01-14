package com.codingbingo.fastreader.utils;

/**
 * Created by bingo on 2017/1/4.
 */

public class StringUtils {

    public static boolean isBlank(String content) {
        if (content == null || content.length() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
