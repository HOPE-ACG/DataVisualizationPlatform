package com.acg.app;

import com.acg.dao.SensorMapper;
import com.acg.exception.ReadDataFromSerialPortFailure;
import com.acg.exception.SerialPortInputStreamCloseFailure;
import com.acg.pojo.Sensor;
import com.acg.pojo.Table;
import com.acg.util.CommonTool;
import com.acg.util.MybatisTool;
import com.acg.util.SerialTool;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import org.apache.ibatis.session.SqlSession;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * 串口监听类
 */
public class SerialListener implements SerialPortEventListener {

    //打开的串口
    private final SerialPort serialPort;
    //标签
    private final HashMap<String, Label> labels;
    //数据库连接
    private final SqlSession sqlSession;

    public SerialListener(SerialPort serialPort, HashMap<String, Label> labels, SqlSession sqlSession) {
        this.serialPort = serialPort;
        this.labels = labels;
        this.sqlSession = sqlSession;
    }

    //处理监控到的串口事件
    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {

        SensorMapper mapper = sqlSession.getMapper(SensorMapper.class);

        switch (serialPortEvent.getEventType()) {

            // 通讯中断
            case SerialPortEvent.BI:
                JOptionPane.showMessageDialog(null, "与串口设备通讯中断",
                        "错误", JOptionPane.INFORMATION_MESSAGE);
                break;
            // 输出缓冲区已清空
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            // 串口存在可用数据
            case SerialPortEvent.DATA_AVAILABLE:
                byte[] bytes;
                try {
                    if (serialPort == null) {
                        JOptionPane.showMessageDialog(null,
                                "串口对象为空！监听失败！", "错误", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        //读取数据，存入字节数组
                        bytes = SerialTool.readFromPort(serialPort);
                        //数据清洗
                        if (bytes.length > 0) {
                            //将字节数组数据转换位为保存了原始数据的字符串
                            String data= new String(bytes);
                            String[] dataSplit = data.split(",");
                            //数据满足通讯格式
                            if(dataSplit.length > 2) {
                                Table table = new Table(dataSplit[0], dataSplit[1], "time");
                                //决定是否创建表
                                mapper.createTable(table);
                                //保存数据到数据库
                                Sensor sensor = new Sensor(Double.parseDouble(dataSplit[2]), CommonTool.getNow());
                                mapper.insertTable(sensor, table);
                                //更新界面Label值
                                labels.get(dataSplit[0]).setText(dataSplit[2] + "V");
                            }
                        }
                    }
                } catch (ReadDataFromSerialPortFailure | SerialPortInputStreamCloseFailure e) {
                    //先关闭数据库连接
                    MybatisTool.closeSession(sqlSession);
                    JOptionPane.showMessageDialog(null, e,
                            "错误", JOptionPane.INFORMATION_MESSAGE);
                    //发生读取错误时显示错误信息后退出系统
                    System.exit(0);
                }
                break;
        }
    }
}
