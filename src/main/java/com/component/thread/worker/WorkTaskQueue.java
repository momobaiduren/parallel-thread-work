package com.component.thread.worker;

import com.component.thread.CustomThreadFactory;
import com.component.thread.ThreadPoolProperties;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * created by zhanglong and since  2019/11/17  11:09 上午
 *
 * @description: 业务工作队列 本身线程不安全；单线程操作，不能用于多线程嵌套使用
 */
@Slf4j
public final class WorkTaskQueue {
    /**
     * description 最大等待时间
     */
    private long maxWait = 60000L;
    /**
     * description 线程队列
     */
    private Queue<Runnable> workTaskQueue = new ConcurrentLinkedQueue<>();

    private AtomicInteger taskCount = new AtomicInteger(0);

    /**
     * create by ZhangLong on 2019/11/30
     * description 每个任务超时时间默认值为60000ms
     */
    public WorkTaskQueue() {
    }

    /**
     * create by ZhangLong on 2019/11/30
     *
     * @param maxWait 小于0执行任务没有超时,轮训处理
     */
    public WorkTaskQueue(long maxWait) {
        if (maxWait <= 0) {
            this.maxWait = Long.MAX_VALUE;
        } else {
            this.maxWait = maxWait;
        }
    }

    /**
     * description 注册任务 线程安全同步
     */
    public void register(Runnable task) {
        taskCount.incrementAndGet();
        workTaskQueue.offer(task);
    }

    /**
     * description 提交任务
     */

    public void submit() {
        submit(new ThreadPoolProperties());
    }

    public void submit(ThreadPoolProperties threadPoolProperties) {
        CustomThreadFactory threadFactory = new CustomThreadFactory();
        threadFactory.bindingThreadGroup(new ThreadGroup(threadPoolProperties.getThreadGroupName()));
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                threadPoolProperties.getCorePoolSize(),
                threadPoolProperties.getMaximumPoolSize(),
                threadPoolProperties.getKeepAliveTime(),
                TimeUnit.SECONDS, threadPoolProperties.getWorkQuezue(), threadFactory);
        CountDownLatch countDownLatch = new CountDownLatch(taskCount.get());
        Queue<Pair<FutureTask<Object>, Long>> workingTaskQueue = new ArrayBlockingQueue<>(taskCount.get());
        int awaitTaskNum1 = taskCount.get();
        for (int i = 0; i < taskCount.get(); i++) {
            Runnable worker = workTaskQueue.poll();
            if (Objects.nonNull(worker)) {
                FutureTask<Object> futureTask = new FutureTask<>(worker, null);
                threadPoolExecutor.execute(threadFactory.newThread(futureTask));
                workingTaskQueue.offer(new Pair<>(futureTask, System.currentTimeMillis()));
                awaitTaskNum1--;
            }
        }
        int awaitTaskNum = awaitTaskNum1;
        //监控线程执行任务
        futureTaskExecutionListener(threadFactory, threadPoolExecutor,
                countDownLatch, workingTaskQueue, awaitTaskNum);
        threadPoolExecutor.shutdown();
    }

    private void futureTaskExecutionListener(CustomThreadFactory threadFactory, ThreadPoolExecutor threadPoolExecutor,
                                             CountDownLatch countDownLatch, Queue<Pair<FutureTask<Object>, Long>> workingTaskQueue, int awaitTaskNum) {
        while (countDownLatch.getCount() != 0) {
            Pair<FutureTask<Object>, Long> futureTaskLongPair = workingTaskQueue.poll();
            assert futureTaskLongPair != null;
            if (System.currentTimeMillis() - futureTaskLongPair.getValue() > maxWait) {
                //超时了
                futureTaskLongPair.getKey().cancel(true);
                System.err.println("线程执行超时,已取消");
                countDownLatch.countDown();
                continue;
            }
            if (Objects.requireNonNull(futureTaskLongPair.getKey()).isDone()) {
                countDownLatch.countDown();
                if (awaitTaskNum > 0) {
                    Runnable worker = workTaskQueue.poll();
                    if (Objects.nonNull(worker)) {
                        FutureTask<Object> futureTask = new FutureTask<>(worker, null);
                        threadPoolExecutor.execute(threadFactory.newThread(futureTask));
                        workingTaskQueue.offer(futureTaskLongPair);
                    }
                }
            } else {
                workingTaskQueue.offer(futureTaskLongPair);
            }
        }
    }
}
