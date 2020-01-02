package com.component.thread.event;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zhanglong
 * @description: ������������
 * @since 2019/12/30 4:41 ����
 */
public class ApplicationEventContext<T, S extends EventSource<T>> {

    /**
     * description
     */
    private Set<AbstractEventListener<T, S>> baseEventListeners = new HashSet<>();

    /**
     * ע�������
     *
     * @param listener ������
     */
    public void registerEventListener( AbstractEventListener<T, S> listener ) {
        this.baseEventListeners.add(listener);
    }

    /**
     * �����¼� �ص����м������Ļص�����
     *
     * @param eventSource �¼�
     */
    public void publishEvent( S eventSource ) {
        for (AbstractEventListener<T, S> baseEventListener : baseEventListeners) {
            // ����������¼��󶨼��������߼���
            baseEventListener.onEventListener(eventSource);
        }
    }
}
