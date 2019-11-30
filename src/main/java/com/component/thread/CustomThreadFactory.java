package com.component.thread;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.function.Supplier;

/**
 * @Author zhanglong
 * @Version V1.0.0
 * @Date 2019-09-13
 */
public class CustomThreadFactory implements ThreadFactory {

    private static final String THREAD_GROUP_NAME = "default-worker-task-group";
    /**
     * description 线程组，用来排查线程问题，并且隔离线程数据
     */
    private ThreadGroup threadGroup;


    /**
     * description 自定义线程工厂
     */
    public CustomThreadFactory bindingThreadGroup(ThreadGroup threadGroup) {
        this.threadGroup = threadGroup;
        return this;
    }

    /**
     * description 线程工厂创建线程
     */
    @Override
    public Thread newThread(Runnable target) {
        Objects.requireNonNull(target, "runable could not be null");
        if (Objects.isNull(threadGroup)) {
            threadGroup = new ThreadGroup(THREAD_GROUP_NAME);
        }
        Thread thread = new Thread(threadGroup, target);
        //设置线程的执行级别
        thread.setPriority(Thread.NORM_PRIORITY);
        thread.setUncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler());
        return thread;
    }
}
