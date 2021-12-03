package com.acg.service;

import com.acg.dao.SensorMapper;
import com.acg.pojo.Sensor;
import com.acg.pojo.Table;

/**
 * 业务逻辑层实现类
 */
public class SensorServiceImpl implements SensorService {

    //持有dao层类
    private SensorMapper sensorMapper;

    public void setSensorMapper(SensorMapper sensorMapper) {
        this.sensorMapper = sensorMapper;
    }

    /**
     * 创建表
     * @param table pojo类
     */
    @Override
    public void createTable(Table table) {

        sensorMapper.createTable(table);
    }

    /**
     * 插入数据
     * @param sensor pojo类
     */
    @Override
    public void insertTable(Sensor sensor, Table table) {

        sensorMapper.insertTable(sensor, table);
    }
}
