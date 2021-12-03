package com.acg.service;

import com.acg.pojo.Sensor;
import com.acg.pojo.Table;
import org.springframework.stereotype.Service;

/**
 * 业务逻辑层
 */
@Service
public interface SensorService {

    /**
     * 创建表
     * @param table pojo类
     */
    void createTable(Table table);

    /**
     * 插入数据
     * @param sensor pojo类
     */
    void insertTable(Sensor sensor, Table table);
}
