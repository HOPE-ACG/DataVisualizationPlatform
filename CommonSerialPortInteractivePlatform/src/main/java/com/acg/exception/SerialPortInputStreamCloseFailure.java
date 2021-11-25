package com.acg.exception;

/**
 * 关闭串口对象输入流异常
 */
public class SerialPortInputStreamCloseFailure extends Exception {

	private static final long serialVersionUID = 1L;

	public SerialPortInputStreamCloseFailure() {}

	@Override
	public String toString() {
		return "关闭串口对象输入流时出错!";
	}

}
