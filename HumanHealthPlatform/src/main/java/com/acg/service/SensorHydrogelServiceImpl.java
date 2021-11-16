package com.acg.service;

import com.acg.dao.SensorHydrogelMapper;

import java.util.List;
import java.util.Map;

/**
 * service model implement
 */
public class SensorHydrogelServiceImpl implements SensorHydrogelService {

    /**
     * dao层接口注入
      */
    private SensorHydrogelMapper mapper;

    /**
     * 交给spring调用set接口注入dao层接口类
     */
    public void setMapper(SensorHydrogelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public double obtainLatestData() {
        return mapper.obtainLatestData();
    }

    @Override
    public double obtainBiggestData(Map<String, String> map) {
        return mapper.obtainBiggestData(map);
    }

    @Override
    public List<Double> obtainCertainData(Map<String, String> map) {
        return mapper.obtainCertainData(map);
    }
}
