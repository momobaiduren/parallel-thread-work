package com.component.thread;

import com.component.thread.worker.WorkTaskQueue;

/**
 * created by zhanglong and since  2019/12/2  3:35 下午
 *
 * @description: 描述
 */
public class MainExecutor {

    public static void main( String[] args ) {
        final long l = System.currentTimeMillis();
        WorkTaskQueue workTaskQueue = new WorkTaskQueue();
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            workTaskQueue.register(() -> {
                System.out.println(finalI);
            });
        }
        workTaskQueue.submit();
        System.out.println(System.currentTimeMillis() - l);

    }
}
