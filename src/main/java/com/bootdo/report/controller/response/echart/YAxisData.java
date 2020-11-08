package com.bootdo.report.controller.response.echart;

/**
* @Author: yogiCai
* @Date: 2018-02-01 10:43:43
*/
public class YAxisData {

    private String type = "value";
    private String name = "";
    private int min = 0;
    private int max = 250;
    private int interval = 50;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
