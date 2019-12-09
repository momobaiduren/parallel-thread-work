package com.component.thread.worker;

import com.component.thread.ThreadPoolProperties;
import com.component.thread.utils.ResolutionUtils;
import javafx.util.Pair;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * created by zhanglong and since  2019/11/17  11:09 上午
 *
 * @description: 业务工作队列 本身线程不安全；单线程操作，不能用于多线程嵌套使用 在使用多线程时要考虑线程开销和正常业务开销的相对性，管理线程的开销比业务开销大，就不建议使用多线程
 */
public final class WorkTaskQueue {

    /**
     * description 最大等待时间ms
     */
    private long maxWait = 60000L;
    /**
     * description 线程队列, ConcurrentLinkedQueue 非阻塞无边界队列，线程安全的，执行效率比较高
     */
    private Queue<Runnable> workTaskQueue = new ConcurrentLinkedQueue<>();

    private static final String THREAD_GROUP_NAME = "default-worker-task-group";

    /**
     * create by ZhangLong on 2019/11/30 description 每个任务超时时间默认值为60000ms
     */
    public WorkTaskQueue() {
    }

    /**
     * create by ZhangLong on 2019/11/30
     *
     * @param maxWait 小于0执行任务没有超时,轮训处理
     */
    public WorkTaskQueue( long maxWait ) {
        if (maxWait <= 0) {
            this.maxWait = Long.MAX_VALUE;
        } else {
            this.maxWait = maxWait;
        }
    }

    /**
     * description 注册任务 线程安全同步
     */
    public void register( Runnable task ) {
        Objects.requireNonNull(task, "task could not be null");
        workTaskQueue.offer(task);
    }


    private ThreadGroup threadGroup;

    private ThreadFactory threadFactory;

    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * description 提交任务
     */
    public void submit() {
        submit(new ThreadPoolProperties());
    }

    /**
     * create by ZhangLong on 2019/12/1 description 提交任务
     */
    public void submit( ThreadPoolProperties threadPoolProperties ) {
        initThreadPool(threadPoolProperties);
        Queue<Pair<Future, Long>> workingTaskQueue = new ConcurrentLinkedQueue<>();
        try {
            futureTaskExecution(workingTaskQueue);
            //监控线程执行任务
            futureTaskExecutionListener(workingTaskQueue);
            threadPoolExecutor.shutdown();
        } finally {
            ResolutionUtils
                .releaseSource(threadPoolExecutor, threadFactory, threadGroup, workTaskQueue,
                    workingTaskQueue);
        }
    }

    /**
     * create by ZhangLong on 2019/12/1 description 执行任务
     */
    private void futureTaskExecution( Queue<Pair<Future, Long>> workingTaskQueue ) {
        Runnable worker;
        while (Objects.nonNull(worker = workTaskQueue.poll())) {
            final Future future = threadPoolExecutor
                .submit(threadFactory.newThread(worker));
            if (future.isCancelled()) {
                continue;
            }
            workingTaskQueue.offer(new Pair<>(future, System.currentTimeMillis()));
        }
    }

    /**
     * create by ZhangLong on 2019/12/1 description 初始化线程池
     */
    private void initThreadPool( ThreadPoolProperties threadPoolProperties ) {
        threadFactory = target -> {
            Objects.requireNonNull(target, "runable could not be null");
            if (Objects.isNull(threadGroup)) {
                threadGroup = new ThreadGroup(THREAD_GROUP_NAME);
            }
            return new Thread(threadGroup, target);
        };
        threadPoolExecutor = new ThreadPoolExecutor(
            threadPoolProperties.getCorePoolSize(),
            threadPoolProperties.getMaximumPoolSize(),
            threadPoolProperties.getKeepAliveTime(),
            TimeUnit.SECONDS, threadPoolProperties.getWorkQuezue(), threadFactory,
            ( target, executor ) -> {
                throw new UnsupportedOperationException(
                    "Thread pool is exhausted, or thread pool is too little! current task num is "
                        + executor.getTaskCount());
            });
        if (threadPoolProperties.getCorePoolSize() > 0 && threadPoolProperties
            .isPerStartAllCoreThread()) {
            threadPoolExecutor.prestartAllCoreThreads();
        }
    }

    /**
     * create by ZhangLong on 2019/12/1 description 任务执行监控执行结束或超时结束，用于阻塞等待结果集
     */
    private void futureTaskExecutionListener( Queue<Pair<Future, Long>> workingTaskQueue ) {
        Pair<Future, Long> futureTaskLongPair;
        //做阻塞等待结果集获取
        while (Objects.nonNull(futureTaskLongPair = workingTaskQueue.poll())) {
            if (System.currentTimeMillis() - futureTaskLongPair.getValue() > maxWait) {
                //超时处理
                futureTaskLongPair.getKey().cancel(true);
                System.err.println("线程执行超时,已取消");
                continue;
            }
            //调用isDone阻塞线程
            if (Objects.requireNonNull(futureTaskLongPair.getKey()).isDone()) {
                futureTaskExecution(workingTaskQueue);
            } else {
                workingTaskQueue.offer(futureTaskLongPair);
            }
        }

    }
}
