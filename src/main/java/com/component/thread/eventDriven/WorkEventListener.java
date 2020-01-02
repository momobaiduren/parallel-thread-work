package com.component.thread.eventDriven;

import com.component.thread.event.AbstractEventListener;
import com.component.thread.event.EventSource;
import com.component.thread.pool.ThreadPool;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * created by zhanglong and since  2020/1/1  10:18 ����
 *
 * @description: Ӧ�ó��������������ܼ��ʹ���һ���Զ������������ԱȽϴ�
 */
public class WorkEventListener<T, S extends EventSource<T>> extends AbstractEventListener<T, S> {

    private final ThreadPool threadPool;

    private Consumer<S> consumer;

    public WorkEventListener( Consumer<S> consumer ) {
        //���ﲻһ��Ҫʹ��Ĭ�ϵ��̳߳أ�����ҵ��������
        threadPool = ThreadPool.DEFAULT_THREAD_POOL;
        this.consumer = Objects.requireNonNull(consumer, "consumer is undefined");
    }

    @Override
    public void onEventListener( S eventSource ) {
        threadPool.submit(() -> consumer.accept(eventSource));
    }
}
