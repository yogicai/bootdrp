package com.bootdo.report.controller.response.echart;

import com.google.common.collect.Lists;

import java.util.List;

/**
* @Author: yogiCai
* @Date: 2018-02-01 10:43:43
*/
public class XAxisData {

    private String type;
    private List<String> data = Lists.newArrayList();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
