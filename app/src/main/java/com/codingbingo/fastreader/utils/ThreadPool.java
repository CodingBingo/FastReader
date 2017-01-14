package com.codingbingo.fastreader.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/1/5.
 */

public class ThreadPool {
    private static ThreadPool threadPool;

    public static ThreadPool getInstance() {
        if (threadPool == null) {
            threadPool = new ThreadPool();
        }
        return threadPool;
    }

    public static final int POOL_SIZE = 10;
    private ExecutorService executorService;

    private ThreadPool() {
        executorService = Executors.newFixedThreadPool(POOL_SIZE);
    }

    public void submitTask(Runnable runnable) {
        executorService.submit(runnable);
    }
}
