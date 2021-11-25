package com.acg.exception;

/**
 * 串口读取数据异常
 */
public class ReadDataFromSerialPortFailure extends Exception {

	private static final long serialVersionUID = 1L;

	public ReadDataFromSerialPortFailure() {}

	@Override
	public String toString() {
		return "从串口读取数据时出错!";
	}
	
}
