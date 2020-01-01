package com.component.thread.event;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zhanglong
 * @since 2019/12/30 4:41 下午
 * @description: 监听的上下文
 */
public class ApplicationEventContext {
    /**
     * description
     */
    private Set<BaseEventListener> baseEventListeners = new HashSet<>();

    /**
     * 注册监听器
     * @param listener 监听器
     */
    public void registerEventListener( BaseEventListener listener) {
        this.baseEventListeners.add(listener);
    }

    /**
     * 发布事件
     * 回调所有监听器的回调方法
     * @param eventSource 事件
     */
    public void publishEvent( EventSource eventSource) {
        for (BaseEventListener baseEventListener : baseEventListeners) {
            // 这里可以做事件绑定监听器的逻辑，
            baseEventListener.onEventListener(eventSource);
        }
    }
}
