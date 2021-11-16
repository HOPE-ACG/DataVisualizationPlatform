package com.acg.service;

import java.util.List;
import java.util.Map;

/**
 * service model
 */
public interface SensorHydrogelService {

    /**
     * 获取最新数据
     */
    double obtainLatestData();

    /**
     * 获取过去某段时间最大数据值
     *
     * @param map 传入的参数集合
     */
    double obtainBiggestData(Map<String, String> map);

    /**
     * 获取过去某段时间的全部数据
     *
     * @param map 传入的参数集合
     */
    List<Double> obtainCertainData(Map<String, String> map);

}
