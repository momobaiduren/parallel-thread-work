package com.component.thread.handler;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author zhanglong on 2019-09-13  12:08 上午
 * @version V1.0 数据处理器
 */
public interface ComputerHandler {

    /**
     * created by zhanglong and since  2019/11/15 4:17 下午 该方法是不进行分片处理的，而是直接处理
     */
    void execut( Integer sharding, Consumer<Object> resultConsumer, Object conditions );

    void execut( Consumer<Object> resultConsumer, Object conditions );


    /**
     * create by ZhangLong on 2019/11/14 description 获取处理数据的总量，根据索引或者主键查询
     */
    int count( Object conditions );

}

