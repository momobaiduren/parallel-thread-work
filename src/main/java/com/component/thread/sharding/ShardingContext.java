package com.component.thread.sharding;

import com.component.thread.handler.ComputerHandler;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * created by zhanglong and since  2019/11/17  1:07 上午
 *
 * @description: 业务工作的上下文信息
 */
@Data
@AllArgsConstructor
public class ShardingContext<H extends ComputerHandler> {
    /**
     * description 执行器的class
     */
    private Class<H> handlerClass;
    /**
     * description 执行器
     */
    private H shardingHander;
    /**
     * description 条件
     */
    private Object conditions;

}
