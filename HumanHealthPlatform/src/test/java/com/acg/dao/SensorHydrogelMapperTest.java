package com.acg.dao;

import com.acg.util.MybatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

public class SensorHydrogelMapperTest {

    @Test
    public void obtainLatestData() {

        SqlSession sqlSession = MybatisUtil.getSqlSession();
        SensorHydrogelMapper mapper = sqlSession.getMapper(SensorHydrogelMapper.class);
        double v = mapper.obtainLatestData();
        System.out.println(v);
        sqlSession.close();
    }

    @Test
    public void obtainBiggestData() {

        SqlSession sqlSession = MybatisUtil.getSqlSession();
        SensorHydrogelMapper mapper = sqlSession.getMapper(SensorHydrogelMapper.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("start", "2021-09-18");
        map.put("end", "2021-09-19");
        double v = mapper.obtainBiggestData(map);
        System.out.println(v);
        sqlSession.close();
    }

    @Test
    public void obtainCertainData() {

        SqlSession sqlSession = MybatisUtil.getSqlSession();
        SensorHydrogelMapper mapper = sqlSession.getMapper(SensorHydrogelMapper.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("start", "2021-09-18");
        map.put("end", "2021-09-19");
        List<Double> v = mapper.obtainCertainData(map);
        System.out.println(v);
        sqlSession.close();
    }
}