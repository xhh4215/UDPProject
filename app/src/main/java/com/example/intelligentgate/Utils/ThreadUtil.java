package com.example.intelligentgate.Utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
 * Created by yxy on 2017/6/12.
 */
public class ThreadUtil {
    public static ThreadUtil threadUtil = null;
    private int MaxTheadSize = 8;
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(MaxTheadSize);

    public static ThreadUtil getInstance() {
        if (threadUtil == null) {
            Class var0 = ThreadUtil.class;
            synchronized (ThreadUtil.class) {
                if (threadUtil == null) {
                    threadUtil = new ThreadUtil();
                }
            }
        }
        return threadUtil;
    }

    public void execute(Runnable runnable) {
        fixedThreadPool.execute(runnable);
    }

    public void init(){
        fixedThreadPool = Executors.newFixedThreadPool(MaxTheadSize);
    }
    public void shutdown(){
        fixedThreadPool.shutdown();
    }
    public void shutdownNow(){
        fixedThreadPool.shutdownNow();
    }
}
