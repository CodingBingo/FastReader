package com.codingbingo.fastreader.utils;

import com.avos.avoscloud.LogUtil;
import com.codingbingo.fastreader.view.readview.PageFactory;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: bingo
 * Email: codingbingo@gmail.com
 * By 2017/1/5.
 */

public class ThreadPool {
    private volatile static ThreadPool threadPool;

    public static ThreadPool getInstance() {
        if (threadPool == null) {
            synchronized (ThreadPool.class) {
                if (threadPool == null) {
                    threadPool = new ThreadPool();
                }
            }
        }
        return threadPool;
    }

    public static final int POOL_SIZE = 10;
    private ExecutorService executorService;
    private Map<String, PageFactory.OpenBookTask> processMap;

    private ThreadPool() {
        executorService = Executors.newFixedThreadPool(POOL_SIZE);
        processMap = new HashMap<>();
    }

    public synchronized void submitTask(Runnable runnable) {
        for (String key : processMap.keySet()) {
            if (processMap.get(key).isFinished() == true) {
                processMap.remove(key);
            }

            LogUtil.avlog.i("processMap: " + key);
        }

        if (runnable instanceof PageFactory.OpenBookTask) {
            PageFactory.OpenBookTask openBookTask = (PageFactory.OpenBookTask) runnable;

            if (processMap.get(openBookTask.getFilePath()) != null) {
                return;
            } else{
                processMap.put(openBookTask.getFilePath(), openBookTask);
            }
        }

        executorService.submit(runnable);
    }
}
