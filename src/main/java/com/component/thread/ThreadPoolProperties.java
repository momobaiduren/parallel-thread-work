package com.component.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * created by zhanglong and since  2019/11/14  5:43 下午
 *
 * @description: 线程池参数类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThreadPoolProperties {
    /**
     * description 核心线程数，太大会导致线程上下文切换的消耗
     */
    private int corePoolSize = 0;
    /**
     * description 最大线程数
     */
    private int maximumPoolSize = Runtime.getRuntime().availableProcessors() * 10;
    /**
     * description 空闲线程存活时间
     */
    private long keepAliveTime = 60;
    /**
     * description 线程队列类型 初始化构造不设置队列长度默认65535
     */
    private BlockingQueue<Runnable> workQuezue = new SynchronousQueue<>();

    /**
     * description 自定义线程分组名称
     */
    private String threadGroupName = "default-thread-group";
}
