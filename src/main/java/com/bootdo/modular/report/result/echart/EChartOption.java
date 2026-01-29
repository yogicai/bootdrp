package com.bootdo.modular.report.result.echart;

import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;


/**
 * @author yogiCai
 * @since 2018-02-01 10:43:43
 */
@Data
public class EChartOption {

    private TitleData title = new TitleData();
    private LegendData legend = new LegendData();
    @JsonProperty(namespace = "xAxis")
    private List<XAxisData> xAxis = CollUtil.newArrayList();
    @JsonProperty(namespace = "yAxis")
    private List<YAxisData> yAxis = CollUtil.newArrayList();
    private List<SeriesData> series = CollUtil.newArrayList();

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
