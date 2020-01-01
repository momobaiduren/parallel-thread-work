package com.component.thread.sharding;

import com.component.thread.handler.ComputerHandler;
import com.component.thread.utils.ResolutionUtils;
import com.component.thread.worker.WorkTaskQueue;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * created by zhanglong and since  2019/11/18  3:49 下午
 *
 * @description: 多线程执行类
 */
public final class ShardingComputerOperation {

    /**
     * description 分片数量
     */
    private int shardingNum = Runtime.getRuntime().availableProcessors() * 5;
    /**
     * description 最小分片条件，超过之后才会归并
     */
    private Integer shardingDealMinCount = 1000;

    private WorkTaskQueue workTaskQueue;

    private ShardingComputerOperation() {
    }

    public static ShardingComputerOperation instance() {
        return new ShardingComputerOperation();
    }

    /**
     * created by zhanglong and since  2019/11/27 3:43 下午
     *
     * @param consumer jion结果集消费业务
     * @param workerContexts 业务上下文信息
     * @description 执行方法
     */
    @SafeVarargs
    public final <H extends ComputerHandler> void run(
        Consumer<Map<Class<H>, Map<Integer, Object>>> consumer,
        ShardingContext<H>... workerContexts ) {
        Objects.requireNonNull(workerContexts, "workerContexts could not be null");
        workTaskQueue = new WorkTaskQueue();
        Map<Class<H>, Map<Integer, Object>> mergeResult = new ConcurrentHashMap<>(
            workerContexts.length);
        try {
            for (ShardingContext<H> shardingContext : workerContexts) {
                mergeResult.put(shardingContext.getHandlerClass(), new ConcurrentHashMap<>());
                dealWith(shardingContext, mergeResult);
            }
            workTaskQueue.submit();
            if (Objects.nonNull(consumer)) {
                consumer.accept(mergeResult);
            }
        } finally {
            //释放资源
            ResolutionUtils.releaseSource(workTaskQueue, mergeResult, workerContexts);
        }
    }

    /**
     * description 处理
     */
    private <H extends ComputerHandler> void dealWith( ShardingContext<H> workerContext,
        Map<Class<H>, Map<Integer, Object>> joinResult ) {
        int count = workerContext.getShardingHander().count(workerContext.getConditions());
        if (Objects.nonNull(shardingDealMinCount) && shardingDealMinCount >= count) {
            singleDealWith(workerContext, joinResult);
        } else {
            parallelDealWith(workerContext, joinResult, count);
        }
    }

    /**
     * description 并行处理
     */
    private <H extends ComputerHandler> void parallelDealWith(
        ShardingContext<H> workerContext, Map<Class<H>, Map<Integer, Object>> joinResult,
        int count ) {
        Map<Integer, List<Integer>> shardingDataMap = ResolutionUtils.sharding(shardingNum, count);

        shardingDataMap.forEach(( sharding, shardingData ) -> workTaskQueue.register(
            () -> shardingData.forEach(
                shardingNum -> workerContext.getShardingHander().execut(shardingNum, result -> {
                    if (joinResult.containsKey(workerContext.getHandlerClass())) {
                        joinResult.get(workerContext.getHandlerClass()).put(shardingNum, result);
                    }
                }, workerContext.getConditions()))));

    }

    /**
     * description 单一处理
     */
    private <H extends ComputerHandler> void singleDealWith( ShardingContext<H> shardingContext,
        Map<Class<H>, Map<Integer, Object>> joinResult ) {
        workTaskQueue.register(() -> {
            shardingContext.getShardingHander().execut(result -> {
                if (joinResult.containsKey(shardingContext.getHandlerClass())) {
                    joinResult.get(shardingContext.getHandlerClass()).put(shardingNum, result);
                }
            }, shardingContext.getConditions());
        });
    }

    /**
     * description 分片数
     */
    public void setShardingNum( int shardingNum ) {
        if (shardingNum >= 0) {
            this.shardingNum = shardingNum;
        }
    }

    /**
     * description 最小分片数，大于该值才会进行并行执行任务
     */
    public void setShardingDealMinCount( Integer shardingDealMinCount ) {
        if (Objects.nonNull(shardingDealMinCount) && shardingDealMinCount > 0) {
            this.shardingDealMinCount = shardingDealMinCount;
        }
    }
}
