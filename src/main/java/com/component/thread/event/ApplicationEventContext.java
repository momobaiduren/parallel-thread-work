package com.component.thread.event;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zhanglong
 * @description: 监听的上下文
 * @since 2019/12/30 4:41 下午
 */
public class ApplicationEventContext<T, S extends EventSource<T>> {

    /**
     * description
     */
    private Set<AbstractEventListener<T, S>> baseEventListeners = new HashSet<>();

    /**
     * 注册监听器
     *
     * @param listener 监听器
     */
    public void registerEventListener( AbstractEventListener<T, S> listener ) {
        this.baseEventListeners.add(listener);
    }

    /**
     * 发布事件 回调所有监听器的回调方法
     *
     * @param eventSource 事件
     */
    public void publishEvent( S eventSource ) {
        for (AbstractEventListener<T, S> baseEventListener : baseEventListeners) {
            // 这里可以做事件绑定监听器的逻辑，
            baseEventListener.onEventListener(eventSource);
        }
    }
}
