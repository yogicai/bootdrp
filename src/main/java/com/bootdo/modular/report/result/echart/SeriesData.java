package com.bootdo.modular.report.result.echart;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author yogiCai
 * @since 2018-02-01 10:43:43
 */
@Data
public class SeriesData {
    private String name;
    private String type;
    private int barGap;
    private List<Object> data = Lists.newArrayList();
    private MarkData markPoint = new MarkData();
    private MarkData markLine = new MarkData();


    @Data
    public class MarkData {
        private List<Object> data = Lists.newArrayList();

        public void addData(String name, String type) {
            this.data.add(ImmutableMap.of("name", name, "type", type));
        }
    }
}
