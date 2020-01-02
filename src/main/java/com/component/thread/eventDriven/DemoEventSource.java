package com.component.thread.eventDriven;

import com.component.thread.event.EventSource;

/**
 * created by zhanglong and since  2020/1/1  10:18 ÏÂÎç
 *
 * @description: ÃèÊö
 */
public class DemoEventSource<T> extends EventSource<T> {


    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public DemoEventSource( T source ) {
        super(source);
    }
}
