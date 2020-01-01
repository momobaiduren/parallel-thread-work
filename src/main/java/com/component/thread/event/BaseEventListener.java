package com.component.thread.event;
import java.util.EventListener;

/**
 * @author zhanglong and since  2019/12/30  4:42 下午
 * @description: 事件监听器
 */
public abstract class BaseEventListener implements EventListener {
    /**
     * description 监听事件源
     * @param eventSource 事件源
     */
    public abstract void onEventListener( EventSource eventSource );

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals( Object obj ) {
        return super.equals(obj);
    }
}
