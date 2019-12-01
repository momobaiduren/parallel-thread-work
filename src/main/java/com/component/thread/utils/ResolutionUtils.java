package com.component.thread.utils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * created by zhanglong and since  2019/11/14  5:52 下午
 *
 * @description: 数据分割处理
 */
@Slf4j
public final class ResolutionUtils {
    private ResolutionUtils(){}
    /**
     * description 分片数从1开始执行，如果索引是0的要加1
     * 分片处理
     */
    public static Map<Integer, List<Integer>> sharding( int shardingNum, int count ) {
        Map<Integer, List<Integer>> shardingDataMap = new HashMap<>(shardingNum);
        for (int i = 1; i <= count; i++) {
            if (shardingDataMap.containsKey(i % shardingNum)) {
                shardingDataMap.get(i % shardingNum).add(i);
            } else {
                List<Integer> shardingData = new ArrayList<>(count / shardingNum + 1);
                shardingData.add(i);
                shardingDataMap.put(i % shardingNum, shardingData);
            }
        }
        return shardingDataMap;
    }
    /**
     * description cpu资源释放
     */
    public static void releaseCpuSource( long milliseconds ) {
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void releaseSource(Object... objs) {
        if (Objects.nonNull(objs)) {
            Arrays.fill(objs, null);
        }
    }


}
