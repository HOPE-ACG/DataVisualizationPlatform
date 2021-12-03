package com.acg.pojo;

/**
 * 通用传感器数据映射，用于插入数据
 */
public class Sensor {

    //传感器数据
    private double data;

    //日期
    private String time;

    public Sensor() {
    }

    public Sensor(double data, String time) {
        this.data = data;
        this.time = time;
    }

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
