package com.component.thread.pool;

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
    private int corePoolSize = 10;
    /**
     * description 最大线程数
     */
    private int maxPoolSize = Runtime.getRuntime().availableProcessors() * 10;
    /**
     * description 空闲线程存活时间
     */
    private long keepAliveTime = 60;
    /**
     * description 线程队列类型 初始化构造不设置队列长度默认65535
     * SynchronousQueue 不存储数据的阻塞队列每个put提供操作必须等待take消费操作否则不能继续添加
     */
    private BlockingQueue<Runnable> workQuezue = new SynchronousQueue<>();

    /**
     * description 自定义线程分组名称
     */
    private String threadGroupName = "default-thread-group";
    /**
     * description 是否默认启动所有基本线程（corePoolSize大于零的情况）
     */
    private boolean perStartAllCoreThread;
    /**
     * description 线程组标示线程资源的使用范围，也用于线程调试
     */
    private ThreadGroup threadGroup;
}
