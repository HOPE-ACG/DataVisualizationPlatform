package com.acg.util;

import com.acg.bean.Sensor_HJ;

import java.util.Date;

public class DataProcessionUtils {

	public static Object arrayToObject(double num) {
		//定义当前时间
		Date curTime = new Date();
		//判断何种类型传感器数据
		Sensor_HJ sensor_hj = new Sensor_HJ();
		sensor_hj.setVoltage(num);
		sensor_hj.setTime(curTime);
		
		return sensor_hj;
	}
}
