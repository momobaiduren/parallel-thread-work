package com.component.thread;

import java.util.Objects;
import java.util.concurrent.ThreadFactory;

/**
 * @Author zhanglong
 * @Version V1.0.0
 * @Date 2019-09-13
 */
public class CustomThreadFactory implements ThreadFactory {

    public static final String DEFAULTTHREADGROUPNAME = "DEFAULT-COMPUTER-THREAD-GROUP";
    /**
     * description 线程组，用来排查线程问题，并且隔离线程数据
     */
    private ThreadGroup threadGroup;
    /**
     * description 线程名称
     */
    private String threadName;

    /**
     * description 自定义线程工厂
     */
    public CustomThreadFactory bindingThreadGroup(ThreadGroup threadGroup, String threadName){
        this.threadGroup = threadGroup;
        this.threadName = threadName;
        return this;
    }
    /**
     * description 线程工厂创建线程
     */
    @Override
    public Thread newThread(Runnable target) {
        Objects.requireNonNull(target, "runable could not be null");
        if (Objects.isNull(threadGroup)){
            threadGroup = new ThreadGroup(DEFAULTTHREADGROUPNAME);
        }
        Thread thread = new Thread(threadGroup, target, threadName);
        //设置线程的执行级别
        thread.setPriority(Thread.NORM_PRIORITY);
        thread.setUncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler());
        return thread;
    }
}
