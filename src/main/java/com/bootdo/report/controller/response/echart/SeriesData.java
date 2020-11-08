package com.bootdo.report.controller.response.echart;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.util.List;

/**
* @Author: yogiCai
* @Date: 2018-02-01 10:43:43
*/
public class SeriesData {
    private String name;
    private String type;
    private List<Object> data = Lists.newArrayList();
    private MarkData markPoint = new MarkData();
    private MarkData markLine = new MarkData();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public MarkData getMarkPoint() {
        return markPoint;
    }

    public void setMarkPoint(MarkData markPoint) {
        this.markPoint = markPoint;
    }

    public MarkData getMarkLine() {
        return markLine;
    }

    public void setMarkLine(MarkData markLine) {
        this.markLine = markLine;
    }

    public class MarkData {
        private List<Object> data = Lists.newArrayList();


        public List<Object> getData() {
            return data;
        }

        public void setData(List<Object> data) {
            this.data = data;
        }

        public void addData(String name, String type) {
            this.data.add(ImmutableMap.of("name", name, "type", type));
        }
    }
}
