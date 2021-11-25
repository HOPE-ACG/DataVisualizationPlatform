package com.acg.bean;

import java.util.Date;

public class Sensor_HJ {

	private int num;
	private double voltage;
	private Date time;
	
	public Sensor_HJ() {}
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	
	public double getVoltage() {
		return voltage;
	}

	public void setVoltage(double voltage) {
		this.voltage = voltage;
	}

	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
}
