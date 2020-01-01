package com.component.thread.event;
import java.util.EventListener;

/**
 * @author zhanglong and since  2019/12/30  4:42 ����
 * @description: �¼�������
 */
public abstract class BaseEventListener implements EventListener {
    /**
     * description �����¼�Դ
     * @param eventSource �¼�Դ
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