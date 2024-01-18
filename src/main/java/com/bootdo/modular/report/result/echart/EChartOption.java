package com.bootdo.modular.report.result.echart;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;


/**
 * @author yogiCai
 * @date 2018-02-01 10:43:43
 */
@Data
public class EChartOption {

    private TitleData title = new TitleData();
    private LegendData legend = new LegendData();
    @JsonProperty(namespace = "xAxis")
    private List<XAxisData> xAxis = Lists.newArrayList();
    @JsonProperty(namespace = "yAxis")
    private List<YAxisData> yAxis = Lists.newArrayList();
    private List<SeriesData> series = Lists.newArrayList();

    public EChartOption(int xAxisNum, int yAxisNum, int seriesNum) {
        for (int i = 0; i < xAxisNum; i++) {
            xAxis.add(new XAxisData());
        }
        for (int i = 0; i < yAxisNum; i++) {
            yAxis.add(new YAxisData());
        }
        for (int i = 0; i < seriesNum; i++) {
            series.add(new SeriesData());
        }
    }

}
