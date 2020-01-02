package com.component.thread.eventDriven;

import com.component.thread.event.AbstractEventListener;
import com.component.thread.event.EventSource;
import com.component.thread.pool.ThreadPool;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * created by zhanglong and since  2020/1/1  10:18 下午
 *
 * @description: 应用场景：基于任务密集型处理；一次性多个任务（任务相对比较大）
 */
public class WorkEventListener<T, S extends EventSource<T>> extends AbstractEventListener<T, S> {

    private final ThreadPool threadPool;

    private Consumer<S> consumer;

    public WorkEventListener( Consumer<S> consumer ) {
        //这里不一定要使用默认的线程池，根据业务需求定义
        threadPool = ThreadPool.DEFAULT_THREAD_POOL;
        this.consumer = Objects.requireNonNull(consumer, "consumer is undefined");
    }

    @Override
    public void onEventListener( S eventSource ) {
        threadPool.submit(() -> consumer.accept(eventSource));
    }
}
