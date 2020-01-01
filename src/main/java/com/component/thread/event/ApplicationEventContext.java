package com.component.thread.event;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zhanglong
 * @since 2019/12/30 4:41 ����
 * @description: ������������
 */
public class ApplicationEventContext {
    /**
     * description
     */
    private Set<BaseEventListener> baseEventListeners = new HashSet<>();

    /**
     * ע�������
     * @param listener ������
     */
    public void registerEventListener( BaseEventListener listener) {
        this.baseEventListeners.add(listener);
    }

    /**
     * �����¼�
     * �ص����м������Ļص�����
     * @param eventSource �¼�
     */
    public void publishEvent( EventSource eventSource) {
        for (BaseEventListener baseEventListener : baseEventListeners) {
            // ����������¼��󶨼��������߼���
            baseEventListener.onEventListener(eventSource);
        }
    }
}
