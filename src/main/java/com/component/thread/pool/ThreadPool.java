package com.component.thread.pool;

import com.component.thread.utils.ResolutionUtils;
import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * created by zhanglong and since  2020/1/1  7:45 ÏÂÎç
 *
 * @description: ÃèÊö
 */
public final class ThreadPool {

    private static final ThreadPool DEFAULT_THREAD_POOL = new ThreadPool(
        new ThreadPoolProperties());

    private static final String THREAD_GROUP_NAME = "default-worker-task-group";

    private ThreadFactory threadFactory;

    private ThreadPoolExecutor threadPoolExecutor;

    private ThreadPool( ThreadPoolProperties threadPoolProperties ) {
        initThreadPool(threadPoolProperties);
    }

    public static ThreadPool instance( ThreadPoolProperties threadPoolProperties ) {
        return new ThreadPool(threadPoolProperties);
    }

    public Future submit( Runnable runnable ) {
        return threadPoolExecutor.submit(threadFactory.newThread(runnable));
    }

    private void initThreadPool( ThreadPoolProperties threadPoolProperties ) {
        threadFactory = target -> {
            Objects.requireNonNull(target, "runnable could not be null");
            ThreadGroup threadGroup = threadPoolProperties.getThreadGroup();
            if (Objects.isNull(threadGroup)) {
                threadGroup = new ThreadGroup(THREAD_GROUP_NAME);
            }
            return new Thread(threadGroup, target);
        };
        threadPoolExecutor = new ThreadPoolExecutor(
            threadPoolProperties.getCorePoolSize(),
            threadPoolProperties.getMaxPoolSize(),
            threadPoolProperties.getKeepAliveTime(),
            TimeUnit.SECONDS, threadPoolProperties.getWorkQueue(), threadFactory,
            threadPoolProperties.getRejectedExecutionHandler());
        if (threadPoolProperties.isPerStartAllCoreThread()
            && threadPoolProperties.getCorePoolSize() > 0) {
            threadPoolExecutor.prestartAllCoreThreads();
        }
    }

    public void shutdown() {
        if (Objects.isNull(threadPoolExecutor)) {
            throw new NullPointerException("thread pool is not init,could not shutdown  ");
        }
        threadPoolExecutor.shutdown();
    }

    public void releaseSource() {
        if (threadPoolExecutor.isTerminated()) {
            ResolutionUtils.releaseSource(threadPoolExecutor, threadFactory);
        }
    }

}
