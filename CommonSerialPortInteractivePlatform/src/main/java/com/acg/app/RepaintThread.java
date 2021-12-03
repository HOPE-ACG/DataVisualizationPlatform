package com.acg.app;

import com.acg.util.SerialTool;

import java.awt.*;
import java.util.ArrayList;

/**
 * 扫描串口
 */
public class RepaintThread extends Frame implements Runnable {

    //保存可用端口号
    private ArrayList<String> commList;
    //串口选择（下拉框）
    private final Choice commChoice;

    public RepaintThread(ArrayList<String> commList, Choice commChoice) {
        this.commList = commList;
        this.commChoice = commChoice;
    }

    @Override
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
            } else {
                //如果扫描到的commList为空，则移除所有已有串口
                commChoice.removeAll();
            }
        }
    }
}
