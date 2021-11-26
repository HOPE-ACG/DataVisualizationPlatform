package com.acg.app;

import com.acg.exception.*;
import com.acg.util.SerialTool;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * 数据显示窗口
 */
public class DataViewApp extends Frame {

    private static final long serialVersionUID = 1L;

    //程序界面宽度
    public static final int WIDTH = 800;
    //程序界面高度
    public static final int HEIGHT = 620;
    //程序界面出现位置（横坐标）
    public static final int LOC_X = 200;
    //程序界面出现位置（纵坐标）
    public static final int LOC_Y = 70;

    //保存可用端口号
    private ArrayList<String> commList;
    //保存串口对象
    private SerialPort serialPort = null;

    private final Font font = new Font("微软雅黑", Font.BOLD, 25);

    //电压
    private final Label vol = new Label("暂无数据", Label.CENTER);

    //串口选择（下拉框）
    private final Choice commChoice = new Choice();
    //波特率选择
    private final Choice bpsChoice = new Choice();
    //打开串口按钮
    private final Button openSerialButton = new Button("打开串口");
    //关闭串口
    private final Button closeSerialButton = new Button("关闭串口");

    //重画时的画布
    Image offScreen = null;

    /**
     * 类的构造方法
     */
    public DataViewApp() {
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        commList = SerialTool.findPort();
    }

    /**
     * 主菜单窗口显示；
     * 添加Label、按钮、下拉条及相关事件监听；
     */
    public void dataFrame() {

        this.setBounds(LOC_X, LOC_Y, WIDTH, HEIGHT);
        this.setBackground(Color.white);
        this.setLayout(null);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent arg0) {
                if (serialPort != null) {
                    //程序退出时关闭串口释放资源
                    SerialTool.closePort(serialPort);
                }
                System.exit(0);
            }
        });

        //设置vol标签
        vol.setBounds(140, 103, 225, 50);
        vol.setBackground(Color.black);
        vol.setFont(font);
        vol.setForeground(Color.white);
        add(vol);

        //添加串口选择选项
        commChoice.setBounds(160, 397, 200, 200);
        //检查是否有可用串口，有则加入选项中
        if (commList==null || commList.size()<1) {
            JOptionPane.showMessageDialog(null, "没有搜索到有效串口！",
                    "错误", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (String s : commList) {
                commChoice.add(s);
            }
        }
        add(commChoice);

        //添加波特率选项
        bpsChoice.setBounds(526, 396, 200, 200);
        bpsChoice.add("1200");
        bpsChoice.add("2400");
        bpsChoice.add("4800");
        bpsChoice.add("9600");
        bpsChoice.add("14400");
        bpsChoice.add("19200");
        bpsChoice.add("115200");
        add(bpsChoice);

        //添加打开串口按钮
        openSerialButton.setBounds(50, 490, 300, 50);
        openSerialButton.setBackground(Color.lightGray);
        openSerialButton.setFont(new Font("微软雅黑", Font.BOLD, 20));
        openSerialButton.setForeground(Color.darkGray);
        add(openSerialButton);
        //添加打开串口按钮的事件监听
        openSerialButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //获取串口名称
                        String commName = commChoice.getSelectedItem();
                        //获取波特率
                        String bpsStr = bpsChoice.getSelectedItem();
                        //检查串口名称是否获取正确
                        if (commName == null || commName.equals("")) {
                            JOptionPane.showMessageDialog(null, "没有搜索到有效串口！",
                                    "错误", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            //检查波特率是否获取正确
                            if (bpsStr == null || bpsStr.equals("")) {
                                JOptionPane.showMessageDialog(null, "波特率获取错误！",
                                        "错误", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                //串口名、波特率均获取正确时
                                int bps = Integer.parseInt(bpsStr);
                                try {
                                    //获取指定端口名及波特率的串口对象
                                    serialPort = SerialTool.openPort(commName, bps);
                                    //在该串口对象上添加监听器
                                    SerialTool.addListener(serialPort, new SerialListener());
                                    //监听成功进行提示
                                    JOptionPane.showMessageDialog(null, "监听成功，稍后将显示监测数据！",
                                            "提示", JOptionPane.INFORMATION_MESSAGE);
                                } catch (SerialPortParameterFailure | NotASerialPort | NoSuchPort
                                        | PortInUse | TooManyListeners e1) {
                                    //发生错误时使用一个Dialog提示具体的错误信息
                                    JOptionPane.showMessageDialog(null, e1, "错误", JOptionPane.INFORMATION_MESSAGE);
                                }
                            }
                        }

                    }
                }
        );

        //添加关闭串口按钮
        closeSerialButton.setBounds(450, 490, 300, 50);
        closeSerialButton.setBackground(Color.lightGray);
        closeSerialButton.setFont(new Font("微软雅黑", Font.BOLD, 20));
        closeSerialButton.setForeground(Color.darkGray);
        add(closeSerialButton);
        //添加关闭串口按钮的事件监听
        closeSerialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SerialTool.closePort(serialPort);
            }
        });

        this.setResizable(false);
        //启动重画线程
        new Thread(new RepaintThread()).start();
    }

    /**
     * 画出主界面组件元素
     */
    public void paint(Graphics g) {

        g.setColor(Color.black);
        g.setFont(new Font("微软雅黑", Font.BOLD, 25));
        g.drawString(" 电压: ", 45, 130);

        g.setColor(Color.gray);
        g.setFont(new Font("微软雅黑", Font.BOLD, 20));
        g.drawString(" 串口选择： ", 45, 410);

        g.setColor(Color.gray);
        g.setFont(new Font("微软雅黑", Font.BOLD, 20));
        g.drawString(" 波特率： ", 425, 410);

    }

    /**
     * 双缓冲方式重画界面各元素组件
     */
    public void update(Graphics g) {

        if (offScreen == null)	offScreen = this.createImage(WIDTH, HEIGHT);
        Graphics gOffScreen = offScreen.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.white);
        //重画背景画布
        gOffScreen.fillRect(0, 0, WIDTH, HEIGHT);
        //重画界面元素
        this.paint(gOffScreen);
        gOffScreen.setColor(c);
        //将新画好的画布“贴”在原画布上
        g.drawImage(offScreen, 0, 0, null);
    }

    /*
     * 重画线程
     */
    private class RepaintThread implements Runnable {

        public void run() {
            while(true) {
                //调用重画方法
                repaint();
                //扫描可用串口
                commList = SerialTool.findPort();
                if (commList.size() > 0) {
                    //添加新扫描到的可用串口
                    for (String s : commList) {
                        //该串口名是否已存在，初始默认为不存在（在commList里存在但在commChoice里不存在，则新添加）
                        boolean commExist = false;
                        for (int i=0; i<commChoice.getItemCount(); i++) {
                            if (s.equals(commChoice.getItem(i))) {
                                //当前扫描到的串口名已经在初始扫描时存在
                                commExist = true;
                                break;
                            }
                        }
                        //若不存在则添加新串口名至可用串口下拉列表
                        if (!commExist) {
                            commChoice.add(s);
                        }
                    }
                    //移除已经不可用的串口
                    for (int i=0; i<commChoice.getItemCount(); i++) {
                        //该串口是否已失效，初始默认为已经失效（在commChoice里存在但在commList里不存在，则已经失效）
                        boolean commNotExist = true;
                        for (String s : commList) {
                            if (s.equals(commChoice.getItem(i))) {
                                commNotExist = false;
                                break;
                            }
                        }
                        if (commNotExist) {
                            commChoice.remove(i);
                        }
                    }
                }else {
                    //如果扫描到的commList为空，则移除所有已有串口
                    commChoice.removeAll();
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    String err = ExceptionWriter.getErrorInfoFromException(e);
                    JOptionPane.showMessageDialog(null, err,
                            "错误", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                }
            }
        }
    }

    /**
     * 以内部类形式创建一个串口监听类
     */
    private class SerialListener implements SerialPortEventListener {

        //处理监控到的串口事件
        public void serialEvent(SerialPortEvent serialPortEvent) {

            switch (serialPortEvent.getEventType()) {

                // 通讯中断
                case SerialPortEvent.BI:
                    JOptionPane.showMessageDialog(null, "与串口设备通讯中断",
                            "错误", JOptionPane.INFORMATION_MESSAGE);
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
                            if (bytes.length > 4) {
                                //将字节数组数据转换位为保存了原始数据的字符串
                                String data= new String(bytes);
                                String[] datas = data.split(",");

                                //解析数据


                                //数组转换成传感器对象


                                //保存数据到数据库




                                //更新界面Label值
                                if(datas.length > 0) {
                                    vol.setText(datas[1] + "V");
                                }
                            }
                        }
                    } catch (ReadDataFromSerialPortFailure | SerialPortInputStreamCloseFailure e) {
                        JOptionPane.showMessageDialog(null, e, "错误", JOptionPane.INFORMATION_MESSAGE);
                        //发生读取错误时显示错误信息后退出系统
                        System.exit(0);
                    }
                    break;
                //默认退出
                default:
                    break;
            }
        }
    }
}
