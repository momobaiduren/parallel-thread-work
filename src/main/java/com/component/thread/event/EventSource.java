package com.component.thread.event;

import java.util.EventObject;

/**
 * @author zhanglong and since  2019/12/30  4:44 下午
 * @description: 事件源
 */
public class EventSource<T> extends EventObject {

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public EventSource( T source ) {
        super(source);
    }
}
