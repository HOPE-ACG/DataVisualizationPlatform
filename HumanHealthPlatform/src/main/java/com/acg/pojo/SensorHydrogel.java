package com.acg.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * hydrogel sensor's mapping class
 */
public class SensorHydrogel implements Serializable {

    /**
     * key index
     */
    private int id;
    /**
     * voltage value
     */
    private double vol;
    /**
     * testing time
     */
    private Date time;

    public SensorHydrogel() {
    }

    public SensorHydrogel(int id, double vol, Date time) {
        this.id = id;
        this.vol = vol;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getVol() {
        return vol;
    }

    public void setVol(double vol) {
        this.vol = vol;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
