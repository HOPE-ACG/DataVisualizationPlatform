package com.acg.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * mybatis工具类
 */
public class MybatisUtil {

    /**
     * 创建sqlSession工厂类
     */
    private static SqlSessionFactory sessionFactory;

    static {
        try {
            InputStream resourceAsStream = Resources.getResourceAsStream("mybatis.xml");
            sessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取sqlSession
     * @return SqlSession
     */
    public static SqlSession getSqlSession() {

        return sessionFactory.openSession();
    }
}
