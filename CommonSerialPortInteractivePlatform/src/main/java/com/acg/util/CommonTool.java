package com.acg.util;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 通用工具类
 */
public class CommonTool {

    /**
     * 获取当前格式化时间
     * @return now
     */
    public static String getNow() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return simpleDateFormat.format(new Date());
    }

    /**
     * 设置标签
     * @param x 横坐标
     * @param y 纵坐标
     * @param width 宽
     * @param height 高
     * @return 标签
     */
    public static Label setLabel(int x, int y, int width, int height) {

        Label label = new Label("暂无数据", Label.CENTER);
        label.setBounds(x, y, width, height);
        label.setBackground(Color.BLACK);
        label.setFont(new Font("微软雅黑", Font.BOLD, 25));
        label.setForeground(Color.WHITE);

        return label;
    }

    /**
     * 设置选择下拉框
     * @param x 横坐标
     * @param y 纵坐标
     * @param width 宽
     * @param height 高
     * @param list 可用元素集合
     * @return 下拉框
     */
    public static Choice setChoice(int x, int y, int width, int height, ArrayList<String> list) {

        Choice commChoice = new Choice();
        commChoice.setBounds(x, y, width, height);
        //遍历可用端口，添加到下拉框
        if (list==null || list.size()<1) {
            JOptionPane.showMessageDialog(null, "下拉框没有元素！",
                    "警告", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (String str : list) {
                commChoice.add(str);
            }
        }

        return commChoice;
    }

    /**
     * 设置按钮
     * @param x 横坐标
     * @param y 纵坐标
     * @param width 宽
     * @param height 高
     * @param name 名称
     * @return 按钮
     */
    public static Button setButton(int x, int y, int width, int height, String name) {

        Button button = new Button(name);
        button.setBounds(x, y, width, height);
        button.setBackground(Color.LIGHT_GRAY);
        button.setFont(new Font("微软雅黑", Font.BOLD, 20));
        button.setForeground(Color.DARK_GRAY);

        return button;
    }

    /**
     * 设置主界面组件元素
     * @param graphics 主界面组件
     * @param x 横坐标
     * @param y 纵坐标
     * @param name 名称
     * @param size 字体大小
     */
    public static void setGraphics(Graphics graphics, int x, int y, String name, int size) {

        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("微软雅黑", Font.BOLD, size));
        graphics.drawString(name, x, y);
    }

}
