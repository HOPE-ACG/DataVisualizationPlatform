package com.acg.dao;

import com.acg.pojo.Sensor;
import com.acg.pojo.Table;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 传感器通用映射方法
 */
@Repository
public interface SensorMapper {

    /**
     * 创建表
     * @param table pojo类
     */
    void createTable(Table table);

    /**
     * 插入数据
     * @param sensor pojo类
     */
    void insertTable(@Param("sensor") Sensor sensor, @Param("table") Table table);
}
