package com.bootdo.report.controller.response.echart;

import com.google.common.collect.Lists;

import java.util.List;

/**
* @Author: yogiCai
* @Date: 2018-02-01 10:43:43
*/
public class LegendData {

    private List<Object> data = Lists.newArrayList();

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }
}
