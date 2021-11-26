package com.acg.util;

import com.acg.exception.*;
import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;

/**
 * 串口工具
 */
public class SerialTool {

    /**
     * 查找所有可用端口
     * @return 可用端口名称列表
     */
    public static ArrayList<String> findPort() {

        //获得当前所有可用串口
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();

        ArrayList<String> portNameList = new ArrayList<>();

        //将可用串口名添加到List并返回该List
        while (portList.hasMoreElements()) {
            CommPortIdentifier commPortIdentifier = (CommPortIdentifier) portList.nextElement();
            portNameList.add(commPortIdentifier.getName());
        }

        return portNameList;

    }

    /**
     * 打开串口
     * @param portName 端口名称
     * @param baudrate 波特率
     * @return 串口对象
     * @throws SerialPortParameterFailure 设置串口参数失败
     * @throws NotASerialPort 端口指向设备不是串口类型
     * @throws NoSuchPort 没有该端口对应的串口设备
     * @throws PortInUse 端口已被占用
     */
    public static SerialPort openPort(String portName, int baudrate) throws
            SerialPortParameterFailure, NotASerialPort, NoSuchPort, PortInUse {

        try {
            //通过端口名识别端口
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            //打开端口，并给端口名字和一个timeout（打开操作的超时时间）
            CommPort commPort = portIdentifier.open(portName, 2000);
            //判断是不是串口
            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                try {
                    //设置一下串口的波特率等参数
                    serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                } catch (UnsupportedCommOperationException e) {
                    throw new SerialPortParameterFailure();
                }
                return serialPort;

            }
            else {
                throw new NotASerialPort();
            }
        } catch (NoSuchPortException e1) {
            throw new NoSuchPort();
        } catch (PortInUseException e2) {
            throw new PortInUse();
        }
    }

    /**
     * 关闭串口
     * @param serialPort 待关闭的串口对象
     */
    public static void closePort(SerialPort serialPort) {

        if (serialPort != null) {
            serialPort.close();
        }
    }

    /**
     * 往串口发送数据
     * @param serialPort 串口对象
     * @param order	待发送数据
     * @throws SendDataToSerialPortFailure 向串口发送数据失败
     * @throws SerialPortOutputStreamCloseFailure 关闭串口对象的输出流出错
     */
    public static void sendToPort(SerialPort serialPort, byte[] order) throws
            SendDataToSerialPortFailure, SerialPortOutputStreamCloseFailure {

        OutputStream out;

        try {
            out = serialPort.getOutputStream();
            out.write(order);
            out.flush();
        } catch (IOException e) {
            throw new SendDataToSerialPortFailure();
        }

        try {
            out.close();
        } catch (IOException e) {
            throw new SerialPortOutputStreamCloseFailure();
        }
    }

    /**
     * 从串口读取数据
     * @param serialPort 当前已建立连接的SerialPort对象
     * @return 读取到的数据
     * @throws ReadDataFromSerialPortFailure 从串口读取数据时出错
     * @throws SerialPortInputStreamCloseFailure 关闭串口对象输入流出错
     */
    public static byte[] readFromPort(SerialPort serialPort) throws
            ReadDataFromSerialPortFailure, SerialPortInputStreamCloseFailure {

        InputStream in;
        byte[] bytes;

        try {
            in = serialPort.getInputStream();
            bytes = new byte[in.available()];  //获取buffer里的数据长度
            int read = 0;
            while (read != -1) {
                read = in.read(bytes);
            }
        } catch (IOException e) {
            throw new ReadDataFromSerialPortFailure();
        }

        try {
            in.close();
        } catch(IOException e) {
            throw new SerialPortInputStreamCloseFailure();
        }

        return bytes;
    }

    /**
     * 添加监听器
     * @param port     串口对象
     * @param listener 串口监听器
     * @throws TooManyListeners 监听类对象过多
     */
    public static void addListener(SerialPort port, SerialPortEventListener listener) throws
            TooManyListeners {

        try {
            //给串口添加监听器
            port.addEventListener(listener);
            //设置当有数据到达时唤醒监听接收线程
            port.notifyOnDataAvailable(true);
            //设置当通信中断时唤醒中断线程
            port.notifyOnBreakInterrupt(true);
        } catch (TooManyListenersException e) {
            throw new TooManyListeners();
        }
    }
}
