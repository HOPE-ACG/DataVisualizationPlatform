package com.acg.pojo;

/**
 * 通用传感器映射，用于创建表
 */
public class Table {

    //表名
    private String tableName;

    //数据名
    private String dataName;

    //时间名称
    private String timeName;

    public Table() {
    }

    public Table(String tableName, String dataName, String timeName) {
        this.tableName = tableName;
        this.dataName = dataName;
        this.timeName = timeName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getTimeName() {
        return timeName;
    }

    public void setTimeName(String timeName) {
        this.timeName = timeName;
    }
}
