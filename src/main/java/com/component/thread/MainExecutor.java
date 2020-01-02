package com.component.thread;

import com.component.thread.event.ApplicationEventContext;
import com.component.thread.eventDriven.DemoEventSource;
import com.component.thread.eventDriven.WorkEventListener;
import com.component.thread.worker.WorkTaskQueue;
import java.util.function.Consumer;

/**
 * created by zhanglong and since  2019/12/2  3:35 下午
 *
 * @description: 描述
 */
public class MainExecutor {
//    /**
//     * description 多线程任务窃取
//     */
//    public static void main( String[] args ) {
//        final long l = System.currentTimeMillis();
//        WorkTaskQueue workTaskQueue = new WorkTaskQueue();
//        for (int i = 0; i < 10000; i++) {
//            int finalI = i;
//            workTaskQueue.register(() -> {
//                System.out.println(finalI);
//            });
//        }
//        workTaskQueue.submit();
//        System.out.println(System.currentTimeMillis() - l);
//
//    }

    /**
     * description 多线程任务驱动
     */
    public static void main( String[] args ) {
        //第一步注册事件驱动容器
        final ApplicationEventContext<String, DemoEventSource<String>> applicationEventContext = new ApplicationEventContext<>();
        //第二步注册监听
        final WorkEventListener<String, DemoEventSource<String>> workEventListener = new WorkEventListener<>(
            System.out::println);
        applicationEventContext.registerEventListener(workEventListener);
        final WorkEventListener<String, DemoEventSource<String>> workEventListener1 = new WorkEventListener<>(
            System.out::println);
        applicationEventContext.registerEventListener(workEventListener1);
        //第三步发布事件
        applicationEventContext.publishEvent(new DemoEventSource<>("张龙--》事件驱动"));
    }
}

