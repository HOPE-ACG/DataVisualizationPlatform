package com.acg.controller;

import com.acg.service.SensorHydrogelService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * sensor hydrogel control's layer
 */
@RestController
public class SensorHydrogelController {

    /**
     * 业务层注入
     *
     * @Autowired
     * @Qualifier("sensorHydrogelServiceImpl")
     * 使用配置文件注入替代了注解注入
     */
    private SensorHydrogelService sensorHydrogelService;

    /**
     * 交给spring调用set接口注入service层接口类
     */
    public void setSensorHydrogelService(SensorHydrogelService sensorHydrogelService) {
        this.sensorHydrogelService = sensorHydrogelService;
    }

    /**
     * 查询最新数据
     *
     */
    @RequestMapping("/latestData")
    public double queryLatestData() {
        return sensorHydrogelService.obtainLatestData();
    }

    /**
     * 获取某段时间内最大数据
     */
    @RequestMapping("/biggestData")
    public double queryBiggestData() {

        HashMap<String, String> map = new HashMap<>();
        map.put("start", "2021-09-18");
        map.put("end", "2021-09-19");
        return sensorHydrogelService.obtainBiggestData(map);
    }
    /**
     * 获取某段时间内全部数据
     */
    @RequestMapping("/certainData")
    public List<Double> queryCertainData() {

        HashMap<String, String> map = new HashMap<>();
        map.put("start", "2021-09-18");
        map.put("end", "2021-09-19");
        return sensorHydrogelService.obtainCertainData(map);
    }
}
