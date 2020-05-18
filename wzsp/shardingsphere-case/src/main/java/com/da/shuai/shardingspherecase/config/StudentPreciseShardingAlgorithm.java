package com.da.shuai.shardingspherecase.config;

import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.RangeShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import io.shardingsphere.api.algorithm.sharding.standard.RangeShardingAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

/**
 * 自定义的分片算法
 *
 * @Author: dashuai
 * @Date: 2020/5/8 16:26
 */
@Slf4j
public class StudentPreciseShardingAlgorithm implements PreciseShardingAlgorithm, RangeShardingAlgorithm {

    @Override
    public String doSharding(Collection collection, PreciseShardingValue preciseShardingValue) {
        log.info("collection:{}", collection);
        log.info("preciseShardingValue:{}", preciseShardingValue);

        int age = (int) preciseShardingValue.getValue();
        String index = String.valueOf(age % 2);
        String tableReal = preciseShardingValue.getLogicTableName().concat("_").concat(index);
        log.info("tableReal:", tableReal);
        return tableReal;

    }

    @Override
    public Collection<String> doSharding(Collection collection, RangeShardingValue rangeShardingValue) {

        return null;
    }
}
