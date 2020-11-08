package com.bootdo.report.controller.response.echart;

import com.google.common.collect.Lists;

import java.util.List;


/**
* @Author: yogiCai
* @Date: 2018-02-01 10:43:43
*/
public class EChartOption {

    private LegendData legend = new LegendData();
    private List<XAxisData> xAxis = Lists.newArrayList();
    private List<YAxisData> yAxis = Lists.newArrayList();
    private List<SeriesData> series = Lists.newArrayList();

    public EChartOption() {
    }

    public EChartOption(int xAxisNum, int yAxisNum, int seriesNum) {
        for (int i=0; i<xAxisNum; i++) {
            xAxis.add(new XAxisData());
        }
        for (int i=0; i<yAxisNum; i++) {
            yAxis.add(new YAxisData());
        }
        for (int i=0; i<seriesNum; i++) {
            series.add(new SeriesData());
        }
    }

    public LegendData getLegend() {
        return legend;
    }

    public void setLegend(LegendData legend) {
        this.legend = legend;
    }

    public List<XAxisData> getxAxis() {
        return xAxis;
    }

    public void setxAxis(List<XAxisData> xAxis) {
        this.xAxis = xAxis;
    }

    public List<YAxisData> getyAxis() {
        return yAxis;
    }

    public void setyAxis(List<YAxisData> yAxis) {
        this.yAxis = yAxis;
    }

    public List<SeriesData> getSeries() {
        return series;
    }

    public void setSeries(List<SeriesData> series) {
        this.series = series;
    }
}
