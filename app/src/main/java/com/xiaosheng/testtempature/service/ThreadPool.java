package com.xiaosheng.testtempature.service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
    private static final ConcurrentHashMap<String, ScheduledExecutorService> taskMap = new ConcurrentHashMap<>(10);
    public static ConcurrentHashMap<String, String> tempMsg = new ConcurrentHashMap<>(10);
    // 创建一个固定大小的线程池，这里设置为1
    public static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
    public static ExecutorService executor = new ThreadPoolExecutor(5, 10,
            60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue(10),new ThreadPoolExecutor.DiscardPolicy());


    public static void submitScheduleThreadPool(String taskName, Runnable task) {
        taskMap.computeIfAbsent(taskName, name -> {

            // 调度任务在初始延迟0秒之后，每20秒执行一次
            scheduledExecutorService.scheduleAtFixedRate(task, 0, 20, TimeUnit.SECONDS);

            // 添加一个钩子，当程序终止时，关闭线程池
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("关闭线程池: " + name);
                scheduledExecutorService.shutdown();
                try {
                    if (!scheduledExecutorService.awaitTermination(5, TimeUnit.SECONDS)) {
                        scheduledExecutorService.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    scheduledExecutorService.shutdownNow();
                }
                System.out.println("线程池已关闭: " + name);
            }));

            return scheduledExecutorService;
        });
    }



}
