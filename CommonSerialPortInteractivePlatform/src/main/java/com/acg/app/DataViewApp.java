package com.acg.app;

import com.acg.exception.*;
import com.acg.util.CommonTool;
import com.acg.util.MybatisTool;
import com.acg.util.SerialTool;
import gnu.io.SerialPort;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 数据显示窗口
 */
@Component
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

    //保存串口对象
    private static SerialPort serialPort;

    //保存可用串口
    private static final ArrayList<String> commList = SerialTool.findPort();

    //重画时的画布
    private static Image offScreen;

    //获取数据库连接
    private static SqlSession sqlSession;

    /**
     * 类的构造方法
     */
    public DataViewApp() {

        init();
    }

    /**
     * 初始化gui界面
     */
    private void init() {

        this.setBounds(LOC_X, LOC_Y, WIDTH, HEIGHT);
        this.setBackground(Color.white);
        this.setLayout(null);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent arg0) {
                //程序退出时关闭串口释放资源
                SerialTool.closePort(serialPort);
                //关闭数据库连接
                MybatisTool.closeSession(sqlSession);
                System.exit(0);
            }
        });
    }

    /**
     * 主菜单窗口显示；
     * 添加Label、按钮、下拉条及相关事件监听；
     */
    public void dataFrame() {

        //保存标签对象
        HashMap<String, Label> labels = new HashMap<>();
        //添加标签
        Label mq9 = CommonTool.setLabel(140, 103, 225, 50);
        labels.put("mq9", mq9);
        add(mq9);
        Label mq135 = CommonTool.setLabel(530, 103, 225, 50);
        labels.put("mq135", mq135);
        add(mq135);

        //添加串口选择选项
        Choice commChoice = CommonTool.setChoice(160, 397, 200, 200, commList);
        add(commChoice);

        //添加波特率选项
        ArrayList<String> bpsList = new ArrayList<>();
        bpsList.add("9600");bpsList.add("115200");
        Choice bpsChoice = CommonTool.setChoice(526, 396, 200, 200, bpsList);
        add(bpsChoice);

        //添加打开串口按钮
        //打开串口按钮
        Button openSerialButton = CommonTool.setButton(50, 490, 300, 50, "打开端口");
        add(openSerialButton);
        //添加打开串口按钮的事件监听
        openSerialButton.addActionListener(e -> {
            //获取串口名称
            String commName = commChoice.getSelectedItem();
            //获取波特率
            String bpsStr = bpsChoice.getSelectedItem();
            if (commName == null || commName.equals("")) {  //检查串口名称是否获取正确
                JOptionPane.showMessageDialog(null, "没有搜索到有效串口！",
                        "警告", JOptionPane.INFORMATION_MESSAGE);
            } else if (bpsStr == null || bpsStr.equals("")) {   //检查波特率是否获取正确
                //检查波特率是否获取正确
                JOptionPane.showMessageDialog(null, "波特率获取错误！",
                        "错误", JOptionPane.INFORMATION_MESSAGE);
            }  else {
                //串口名、波特率均获取正确时
                int bps = Integer.parseInt(bpsStr);
                try {
                    //获取指定端口名及波特率的串口对象
                    serialPort = SerialTool.openPort(commName, bps);
                    //打开串口时获取数据库连接
                    sqlSession = MybatisTool.getSession();
                    //在该串口对象上添加监听器
                    SerialTool.addListener(serialPort, new SerialListener(serialPort, labels, sqlSession));
                    //监听成功进行提示
                    JOptionPane.showMessageDialog(null, "监听成功，稍后将显示监测数据！",
                            "提示", JOptionPane.INFORMATION_MESSAGE);
                } catch (SerialPortParameterFailure | NotASerialPort | NoSuchPort
                        | PortInUse | TooManyListeners e1) {
                    //关闭串口释放资源
                    SerialTool.closePort(serialPort);
                    //关闭数据库连接
                    MybatisTool.closeSession(sqlSession);
                    //发生错误时使用一个Dialog提示具体的错误信息
                    JOptionPane.showMessageDialog(null, e1,
                            "错误", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        //添加关闭串口按钮
        //关闭串口
        Button closeSerialButton = CommonTool.setButton(450, 490, 300, 50, "关闭端口");
        add(closeSerialButton);
        //添加关闭串口按钮的事件监听
        closeSerialButton.addActionListener(e -> {
            if(SerialTool.closePort(serialPort)) {
                //关闭串口后关闭数据库连接
                MybatisTool.closeSession(sqlSession);
                //关闭成功进行提示
                JOptionPane.showMessageDialog(null, "关闭串口成功!",
                        "提示", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "串口未打开!",
                        "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        this.setResizable(false);
        //启动重画线程
        new Thread(new RepaintThread(commList, commChoice)).start();
    }

    /**
     * 画出主界面组件元素
     */
    public void paint(Graphics g) {

        CommonTool.setGraphics(g, 45, 132, "MQ9:", 25);
        CommonTool.setGraphics(g, 411, 132, "MQ135:", 25);
        CommonTool.setGraphics(g, 45, 410, "端口选择", 20);
        CommonTool.setGraphics(g, 425, 410, "波特率", 20);

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
        //将新画好的画布贴在原画布上
        g.drawImage(offScreen, 0, 0, null);
    }

}
